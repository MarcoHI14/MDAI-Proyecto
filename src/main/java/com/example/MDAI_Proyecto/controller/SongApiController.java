package com.example.MDAI_Proyecto.controller;

import com.example.MDAI_Proyecto.config.OllamaProperties;
import com.example.MDAI_Proyecto.data.model.Cancion;
import com.example.MDAI_Proyecto.data.services.CancionService;
import com.example.MDAI_Proyecto.service.embeddings.EmbeddingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/songs")
public class SongApiController {

    private final CancionService cancionService;
    private final Optional<EmbeddingsService> embeddingsService;
    private final OllamaProperties ollamaProps;
    private static final Logger log = LoggerFactory.getLogger(SongApiController.class);

    public SongApiController(CancionService cancionService, Optional<EmbeddingsService> embeddingsService, OllamaProperties ollamaProps) {
        this.cancionService = cancionService;
        this.embeddingsService = embeddingsService;
        this.ollamaProps = ollamaProps;
    }

    // Autocomplete / búsqueda simple: título, artista o género
    @GetMapping("/search")
    public ResponseEntity<List<Map<String,Object>>> search(@RequestParam("q") String q){
        if (q == null || q.trim().isEmpty()) return ResponseEntity.ok(Collections.emptyList());
        // buscar por título que contenga
        List<Cancion> byTitle = cancionService.findByTituloContainingIgnoreCase(q);
        // buscar por artista (implementación simple)
        List<Cancion> byArtist = cancionService.findbyArtistaName(q);
        // buscar por genero
        List<Cancion> byGenre = cancionService.findByGeneroIgnoreCase(q);

        // combinar resultados en orden de relevancia (título > artista > género), evitando duplicados
        LinkedHashMap<Long, Cancion> map = new LinkedHashMap<>();
        for (Cancion c: byTitle) map.put(c.getIdCancion(), c);
        for (Cancion c: byArtist) map.putIfAbsent(c.getIdCancion(), c);
        for (Cancion c: byGenre) map.putIfAbsent(c.getIdCancion(), c);

        List<Map<String,Object>> out = map.values().stream().map(c -> {
            Map<String,Object> m = new HashMap<>();
            m.put("id", c.getIdCancion());
            m.put("titulo", c.getTitulo());
            m.put("artista", safeArtistName(c));
            m.put("genero", c.getGenero());
            // usar duración como campo descriptivo en lugar de archivoAudio (URL)
            m.put("descripcion", c.getDuracion() != null ? c.getDuracion() : "");
            return m;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(out);
    }

    private String safeArtistName(Cancion c){
        try{
            if (c == null) return "";
            if (c.getArtista() == null) return "";
            if (c.getArtista().getUsuario() == null) return "";
            String u = c.getArtista().getUsuario().getUsername();
            return u == null ? "" : u;
        }catch(Exception e){ return ""; }
    }

    // Endpoint simple /api/recommend - delega a embeddings si está disponible
    @PostMapping("/recommend")
    public ResponseEntity<List<Map<String,Object>>> recommend(@RequestBody Map<String,Object> body){
        try{
            // convertir historyIds entrantes a Long de forma segura
            List<Object> rawHistory = (List<Object>) body.getOrDefault("historyIds", Collections.emptyList());
            List<Long> historyIds = rawHistory.stream().map(o -> {
                try{ return Long.valueOf(String.valueOf(o)); }catch(Exception ex){ return null; }
            }).filter(Objects::nonNull).collect(Collectors.toList());
            int topN = 5;
            Object topNraw = body.get("topN");
            if (topNraw instanceof Number) topN = ((Number) topNraw).intValue();
            else if (topNraw instanceof String) try{ topN = Integer.parseInt((String)topNraw); }catch(Exception ignored){}

            // cargar catálogo completo
            List<Cancion> all = new ArrayList<>();
            cancionService.getAll().forEach(all::add);
            // transformar a lista simple (incluye durationSeconds para scoring por duración)
            List<Map<String,Object>> cat = all.stream().map(c->{
                Map<String,Object> m = new HashMap<>();
                m.put("id", c.getIdCancion());
                m.put("titulo", c.getTitulo());
                m.put("artista", safeArtistName(c));
                m.put("genero", c.getGenero());
                String durStr = c.getDuracion();
                Integer durSec = null;
                if (durStr != null) durSec = parseDurationToSeconds(durStr);
                m.put("descripcion", durStr != null ? durStr : "");
                m.put("durationSeconds", durSec);
                return m; }).collect(Collectors.toList());

            // Pre-calcular topGenre/topWords y duración media del historial (usado para explicaciones y scoring)
            Map<String,Object> histStats = computeHistoryStats(cat, historyIds);
            String topGenre = (String) histStats.getOrDefault("topGenre", null);
            @SuppressWarnings("unchecked")
            List<String> topWords = (List<String>) histStats.getOrDefault("topWords", Collections.emptyList());
            Double avgDuration = (Double) histStats.getOrDefault("avgDurationSeconds", null); // segundos
            @SuppressWarnings("unchecked")
            List<String> historyArtistsList = (List<String>) histStats.getOrDefault("historyArtists", Collections.emptyList());
            Set<String> historyArtists = historyArtistsList.stream().filter(Objects::nonNull).map(String::toLowerCase).collect(Collectors.toSet());

            // comprobación rápida de conectividad a Ollama si está configurado en modo http
            boolean canUseEmbeddings = true;
            try {
                if (ollamaProps != null && "http".equalsIgnoreCase(ollamaProps.getMode())){
                    URI u = URI.create(ollamaProps.getHttpUrl());
                    String host = u.getHost();
                    int port = u.getPort() == -1 ? ("https".equalsIgnoreCase(u.getScheme())?443:80) : u.getPort();
                    try (Socket s = new Socket()){
                        s.connect(new InetSocketAddress(host, port), Math.max(1000, ollamaProps.getHttpTimeoutMs()/2));
                    }catch(IOException ex){
                        log.warn("Ollama HTTP not reachable at {}:{} - skipping embeddings", host, port);
                        canUseEmbeddings = false;
                    }
                }
            } catch(Exception e){ log.warn("Failed to check Ollama connectivity - will try embed and fallback on error", e); }

            // Si embeddingsService está presente y la comprobación pasó, podemos usarlo para ranking
            if (embeddingsService.isPresent() && canUseEmbeddings){
                try{
                    // construir inputs (titulo | artista | genero | descripcion)
                    List<String> inputs = cat.stream().map(m -> String.format("%s | %s | %s | %s", m.get("titulo"), m.get("artista"), m.get("genero"), m.get("descripcion"))).collect(Collectors.toList());
                    List<List<Double>> embs = embeddingsService.get().embed(inputs);
                    if (embs == null || embs.size() != cat.size()) throw new RuntimeException("Embeddings size mismatch");
                    // map id->embedding
                    Map<String, List<Double>> embMap = new HashMap<>();
                    for (int i=0;i<cat.size();i++) embMap.put(String.valueOf(cat.get(i).get("id")), embs.get(i));
                    // user vector: average of history embeddings
                    List<List<Double>> histEmb = historyIds.stream().map(id->embMap.get(String.valueOf(id))).filter(Objects::nonNull).collect(Collectors.toList());
                    if (histEmb.isEmpty()) throw new RuntimeException("No embeddings for history");
                    final int D = histEmb.get(0).size();
                    double[] user = new double[D];
                    for (List<Double> v: histEmb){ for (int j=0;j<D;j++) user[j]+=v.get(j); }
                    for (int j=0;j<D;j++) user[j]/=histEmb.size();

                    // calcular similitud (IA) y similitud por duración, combinarlas para ranking
                    final double EMBEDDING_WEIGHT = 0.85; // peso de la similitud IA
                    final double DURATION_WEIGHT = 0.15;  // peso de la similitud por duración
                    final double MAX_DURATION_RANGE = 300.0; // 5 minutos para normalizar distancia

                    List<Map<String,Object>> scored = new ArrayList<>();
                    for (Map<String,Object> m : cat){
                        if (historyIds.contains(Long.valueOf(String.valueOf(m.get("id"))))) continue;
                        List<Double> v = embMap.get(String.valueOf(m.get("id")));
                        double embSim = v == null ? 0.0 : cosine(user, v);
                        // duration similarity
                        Double durSec = m.get("durationSeconds") instanceof Number ? ((Number)m.get("durationSeconds")).doubleValue() : null;
                        double durSim = 0.0;
                        if (avgDuration != null && durSec != null){
                            double diff = Math.abs(durSec - avgDuration);
                            durSim = Math.max(0.0, 1.0 - (diff / MAX_DURATION_RANGE));
                        }
                        double combined = EMBEDDING_WEIGHT * embSim + DURATION_WEIGHT * durSim;
                        m.put("similarity", embSim);
                        m.put("durationSimilarity", durSim);
                        m.put("score", combined);
                        scored.add(m);
                    }
                    scored.sort((a,b)-> Double.compare(((Number)b.getOrDefault("score",0.0)).doubleValue(), ((Number)a.getOrDefault("score",0.0)).doubleValue()));
                    List<Map<String,Object>> candidates = scored.stream().limit(topN).collect(Collectors.toList());

                    // añadir explicación basada en topGenre/topWords, similitud IA y similitud duración
                    for (Map<String,Object> cand : candidates){ String expl = buildExplanationForCandidateWithDuration(cand, topGenre, topWords, historyArtists, avgDuration); cand.put("explanation", expl); }

                    return ResponseEntity.ok(candidates);
                }catch(Exception ex){
                    // registrar aviso sin stacktrace para no spamear logs cuando Ollama no responde
                    log.warn("Embeddings failed, falling back to heuristic: {}", ex.getMessage());
                    log.debug("Embeddings exception details", ex);
                }
            }

            // fallback: heurístico local (similar al clientRecommend), pero ahora con explicación y con scoring por duración
            List<Map<String,Object>> rec = localFallbackRecommendWithExplanation(cat, historyIds.stream().map(Object::toString).collect(Collectors.toList()), topN, topGenre, topWords, historyArtists, avgDuration);
            return ResponseEntity.ok(rec);
        }catch(Exception e){ log.error("Recommend error", e); return ResponseEntity.status(500).body(Collections.emptyList()); }
    }

    // --- helpers for duration parsing and enhanced explanation ---
    private Integer parseDurationToSeconds(String s){
        if (s == null) return null;
        String t = s.trim();
        try{
            // accept format mm:ss or m:ss or seconds numeric
            if (t.matches("^\\d+:\\d{1,2}$")){
                String[] parts = t.split(":");
                int mm = Integer.parseInt(parts[0]);
                int ss = Integer.parseInt(parts[1]);
                return mm*60 + ss;
            }
            // just seconds
            if (t.matches("^\\d+$")) return Integer.parseInt(t);
        }catch(Exception e){ /* ignore */ }
        return null;
    }

    private String formatSeconds(Integer secs){ if (secs == null) return ""; int m = secs/60; int s = secs%60; return String.format("%d:%02d", m, s); }

    private String buildExplanationForCandidateWithDuration(Map<String,Object> cand, String topGenre, List<String> topWords, Set<String> historyArtists, Double avgDuration){
        // reuse previous explanation builders but include duration similarity
        String base = buildExplanationForCandidate(cand, topGenre, topWords, historyArtists);
        String durPart = "";
        // If we have durationSeconds and avgDuration, check exact ±10s match
        Integer dsec = cand.get("durationSeconds") instanceof Number ? ((Number)cand.get("durationSeconds")).intValue() : null;
        if (avgDuration != null && dsec != null){
            double diff = Math.abs(dsec - avgDuration);
            if (diff <= 10.0){
                // exact phrasing requested
                return base + ". Recomendado por duración similar (±10s)";
            } else {
                // include a softer duration info if somewhat similar
                double ds = cand.containsKey("durationSimilarity") ? ((Number)cand.get("durationSimilarity")).doubleValue() : Math.max(0.0, 1.0 - (diff / 300.0));
                durPart = String.format("Duración: %s (sim: %.2f)", formatSeconds(dsec), ds);
            }
        }
        if (!durPart.isEmpty()) return base + ". " + durPart; else return base;
    }

    private Map<String,Object> computeHistoryStats(List<Map<String,Object>> cat, List<Long> historyIds){
        Map<String,Object> out = new HashMap<>();
        Map<String,Integer> genreCount = new HashMap<>();
        Map<String,Integer> wordCount = new HashMap<>();
        Set<String> histSet = historyIds.stream().map(String::valueOf).collect(Collectors.toSet());
        for (Map<String,Object> m : cat){ if (!histSet.contains(String.valueOf(m.get("id")))) continue; String g = String.valueOf(m.getOrDefault("genero","" )).toLowerCase(); if (!g.isEmpty()) genreCount.put(g, genreCount.getOrDefault(g,0)+1); String txt = String.valueOf(m.getOrDefault("titulo","")) + " " + String.valueOf(m.getOrDefault("descripcion","")); for (String w : txt.toLowerCase().split("[^\\p{L}0-9]+")){ if (w.length()<=2) continue; wordCount.put(w, wordCount.getOrDefault(w,0)+1); } }
        String topGenre = genreCount.entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse(null);
        List<String> topWords = wordCount.entrySet().stream().sorted((a,b)->b.getValue()-a.getValue()).limit(10).map(Map.Entry::getKey).collect(Collectors.toList());
        double avgDuration = historyIds.stream().mapToDouble(id -> {
            for (Map<String,Object> m : cat) if (String.valueOf(m.get("id")).equals(String.valueOf(id))) return m.get("durationSeconds") instanceof Number ? ((Number)m.get("durationSeconds")).doubleValue() : 0;
            return 0;
        }).average().orElse(0);
        // --- nuevo: recolectar artistas del historial (leer campo 'artista' del mapa) ---
        List<String> historyArtists = historyIds.stream().map(id -> {
            for (Map<String,Object> m : cat) if (String.valueOf(m.get("id")).equals(String.valueOf(id))) return String.valueOf(m.getOrDefault("artista",""));
            return null;
        }).filter(s->s!=null && !s.isEmpty()).collect(Collectors.toList());
        // ---
        out.put("topGenre", topGenre);
        out.put("topWords", topWords);
        out.put("avgDurationSeconds", avgDuration);
        out.put("historyArtists", historyArtists);
        return out;
    }

    private String buildExplanationForCandidate(Map<String,Object> cand, String topGenre, List<String> topWords, Set<String> historyArtists){
        List<String> parts = new ArrayList<>();
        String g = String.valueOf(cand.getOrDefault("genero","" )).toLowerCase();
        if (topGenre != null && topGenre.equals(g)) parts.add(String.format("Coincide con tu interés por el género '%s'", capitalize(topGenre)));
        String txt = String.valueOf(cand.getOrDefault("titulo","")) + " " + String.valueOf(cand.getOrDefault("descripcion",""));
        Set<String> candWords = Arrays.stream(txt.toLowerCase().split("[^\\p{L}0-9]+")).filter(s->s.length()>2).collect(Collectors.toSet());
        List<String> common = topWords.stream().filter(candWords::contains).limit(3).collect(Collectors.toList());
        if (!common.isEmpty()) parts.add("Comparte palabras clave con tu historial: " + common.stream().map(this::capitalize).collect(Collectors.joining(", ")) + ".");
        if (cand.containsKey("similarity")){
            double sim = ((Number)cand.get("similarity")).doubleValue();
            parts.add(String.format("Similaridad (IA): %.3f", sim));
        }
        // mismo artista
        String artist = String.valueOf(cand.getOrDefault("artista","" )).toLowerCase();
        if (historyArtists != null && !artist.isEmpty() && historyArtists.contains(artist)){
            parts.add("Recomendado por mismo artista");
        }
        if (parts.isEmpty()) parts.add("Recomendado según tu historial");
        return String.join(". ", parts);
    }

    private List<Map<String,Object>> localFallbackRecommendWithExplanation(List<Map<String,Object>> cat, List<String> historyIds, int topN, String topGenre, List<String> topWords, Set<String> historyArtists, Double avgDuration){
        Set<String> hist = historyIds.stream().collect(Collectors.toSet());
        List<Map<String,Object>> candidates = cat.stream().filter(m -> !hist.contains(String.valueOf(m.get("id")))).collect(Collectors.toList());
        // scoring basic + duration
        final double DURATION_BOOST = 10.0; // scaling factor for duration
        final int CLOSE_SEC = 10; // within 10s considered very close
        List<Map<String,Object>> scored = new ArrayList<>();
        for (Map<String,Object> m: candidates){
            double score=0;
            String g = String.valueOf(m.getOrDefault("genero","" )).toLowerCase();
            if (topGenre!=null && topGenre.equals(g)) score += 20;
            String txt = String.valueOf(m.getOrDefault("titulo","")) + " " + String.valueOf(m.getOrDefault("descripcion",""));
            for (String w: topWords) if (txt.toLowerCase().contains(w)) score += 5;
            // duration bonus
            Double avgDur = avgDuration;
            Integer durSec = m.get("durationSeconds") instanceof Number ? ((Number)m.get("durationSeconds")).intValue() : null;
            double durSim = 0.0;
            if (avgDur != null && durSec != null){
                double diff = Math.abs(durSec - avgDur);
                if (diff <= CLOSE_SEC) durSim = 1.0;
                else if (diff <= 30) durSim = 0.7;
                else if (diff <= 60) durSim = 0.4;
                else durSim = Math.max(0, 1.0 - (diff / 300.0));
                score += durSim * DURATION_BOOST;
                m.put("durationSimilarity", durSim);
            }
            m.put("_score", (int)score);
            scored.add(m);
        }
        scored.sort((a,b)-> Double.compare(((Number)b.getOrDefault("_score",0)).doubleValue(), ((Number)a.getOrDefault("_score",0)).doubleValue()));
        List<Map<String,Object>> top = scored.stream().limit(topN).collect(Collectors.toList());
        for (Map<String,Object> m: top){ m.put("explanation", buildExplanationForCandidate(m, topGenre, topWords, historyArtists)); }
        return top;
    }

    private String capitalize(String s){ if (s==null||s.isEmpty()) return s; return s.substring(0,1).toUpperCase()+s.substring(1); }

    private double cosine(double[] u, List<Double> v){ if (v==null) return 0; double dot=0, na=0, nb=0; for (int i=0;i<u.length;i++){ double a=u[i], b=v.get(i); dot += a*b; na += a*a; nb += b*b; } if (na==0||nb==0) return 0; return dot / (Math.sqrt(na)*Math.sqrt(nb)); }}
