package com.example.MDAI_Proyecto.controller;

import com.example.MDAI_Proyecto.data.model.Playlist;
import com.example.MDAI_Proyecto.data.model.Usuario;
import com.example.MDAI_Proyecto.data.model.Cancion;
import com.example.MDAI_Proyecto.data.model.CancionPlaylist;
import com.example.MDAI_Proyecto.data.services.PlaylistService;
import com.example.MDAI_Proyecto.data.services.UsuarioService;
import com.example.MDAI_Proyecto.data.services.CancionService;
import com.example.MDAI_Proyecto.data.services.CancionPlaylistService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

@Controller
public class PlaylistsController {

    private final UsuarioService usuarioService;
    private final PlaylistService playlistService;
    private final CancionService cancionService;
    private final CancionPlaylistService cancionPlaylistService;

    public PlaylistsController(UsuarioService usuarioService, PlaylistService playlistService,
                               CancionService cancionService, CancionPlaylistService cancionPlaylistService) {
        this.usuarioService = usuarioService;
        this.playlistService = playlistService;
        this.cancionService = cancionService;
        this.cancionPlaylistService = cancionPlaylistService;
        System.out.println("\t PlaylistsController inicializado");
    }

    @GetMapping("/Playlists")
    public String mostrarPlaylists(Model model) {
        System.out.println("\t Recojo la petición de /Playlists");
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            return "redirect:/InicioSesion";
        }
        Object idObj = attrs.getAttribute("id_usuario", RequestAttributes.SCOPE_SESSION);
        if (!(idObj instanceof Long)) {
            return "redirect:/InicioSesion";
        }

        Usuario usuario = usuarioService.obtenerUsuarioPorId((Long) idObj);
        if (usuario == null) {
            return "redirect:/InicioSesion";
        }

        List<Playlist> playlists = (List<Playlist>) playlistService.findAll();
        // Filtrar las playlists para que solo queden las del usuario actual y evitar NPEs
        playlists.removeIf(p -> p.getUsuario() == null || !usuario.getUsername().equals(p.getUsuario().getUsername()));

        model.addAttribute("id_usuario", usuario.getId());
        model.addAttribute("playlists", playlists);
        // Indicar a la vista que no estamos en modo 'álbumes'
        model.addAttribute("mostrarAlbumes", false);
        return "GestorPlaylists";
    }

    @PostMapping("/CrearPlaylist")
    public String crearPlaylist(@RequestParam("id_usuario") Long idUsuario,
                                @RequestParam("nombre") String nombre,
                                @RequestParam("descripcion") String descripcion) {
        // Lógica para crear la playlist
        Playlist playlist = new Playlist();
        playlist.setUsuario(usuarioService.obtenerUsuarioPorId(idUsuario));
        playlist.setNombre(nombre);
        playlist.setDescripcion(descripcion);
        playlist.setFechaCreacion(LocalDateTime.now());

        playlistService.save(playlist);
        // Asegurar que la sesión contiene id_usuario para que /Playlists pueda leerla
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            attrs.setAttribute("id_usuario", idUsuario, RequestAttributes.SCOPE_SESSION);
        }
        // Redirigimos a la lista de playlists para recargar datos desde el controlador
        return "redirect:/Playlists";
    }

    @GetMapping("/BuscarPlaylist")
    public String buscarPlaylist(@RequestParam(name = "nombre", required = false) String nombre, Model model) {
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            return "redirect:/InicioSesion";
        }
        Object idObj = attrs.getAttribute("id_usuario", RequestAttributes.SCOPE_SESSION);
        if (!(idObj instanceof Long)) {
            return "redirect:/InicioSesion";
        }

        Usuario usuario = usuarioService.obtenerUsuarioPorId((Long) idObj);
        if (usuario == null) {
            return "redirect:/InicioSesion";
        }

        List<Playlist> playlists = (List<Playlist>) playlistService.findAll();
        // Mantener solo las del usuario actual y evitar NPEs
        playlists.removeIf(p -> p.getUsuario() == null || !usuario.getUsername().equals(p.getUsuario().getUsername()));

        if (nombre != null && !nombre.trim().isEmpty()) {
            String busc = nombre.trim().toLowerCase();
            playlists.removeIf(p -> p.getNombre() == null || !p.getNombre().toLowerCase().contains(busc));
            model.addAttribute("nombreBusqueda", nombre);
        }

        model.addAttribute("id_usuario", usuario.getId());
        model.addAttribute("playlists", playlists);
        // No estamos mostrando álbumes aquí
        model.addAttribute("mostrarAlbumes", false);
        return "GestorPlaylists";
    }

    @GetMapping("/BuscarAlbum")
    public String buscarAlbum(@RequestParam(name = "nombre", required = false) String nombre, Model model) {
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            return "redirect:/InicioSesion";
        }
        Object idObj = attrs.getAttribute("id_usuario", RequestAttributes.SCOPE_SESSION);
        if (!(idObj instanceof Long)) {
            return "redirect:/InicioSesion";
        }

        Usuario usuario = usuarioService.obtenerUsuarioPorId((Long) idObj);
        if (usuario == null) {
            return "redirect:/InicioSesion";
        }

        // Recuperar todas las playlists y quedarnos sólo con las creadas por usuarios-artistas
        List<Playlist> playlists = (List<Playlist>) playlistService.findAll();
        playlists.removeIf(p -> p.getUsuario() == null || p.getUsuario().getArtista() == null);
        // Si se proporcionó un nombre, filtrar por él (como en BuscarPlaylist)
        if (nombre != null && !nombre.trim().isEmpty()) {
            String busc = nombre.trim().toLowerCase();
            playlists.removeIf(p -> p.getNombre() == null || !p.getNombre().toLowerCase().contains(busc));
            model.addAttribute("nombreBusqueda", nombre);
        }

        model.addAttribute("id_usuario", usuario.getId());
        model.addAttribute("playlists", playlists);
        // Indicador para la vista: estamos mostrando álbumes de artistas
        model.addAttribute("mostrarAlbumes", true);
        return "GestorPlaylists";
    }

    // --- API handlers movidos aquí para controlar desde PlaylistsController ---
    @GetMapping("/api/misplaylists")
    @ResponseBody
    public List<Map<String,Object>> misPlaylistsApi() {
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        if (attrs == null) return List.of();
        Object idObj = attrs.getAttribute("id_usuario", RequestAttributes.SCOPE_SESSION);
        if (!(idObj instanceof Long)) return List.of();
        Usuario usuario = usuarioService.obtenerUsuarioPorId((Long) idObj);
        if (usuario == null) return List.of();
        List<Map<String,Object>> out = new ArrayList<>();
        Iterable<Playlist> pls = playlistService.findAll();
        for (Playlist p : pls) {
            if (p.getUsuario() != null && p.getUsuario().getId() != null && p.getUsuario().getId().equals(usuario.getId())) {
                Map<String,Object> m = new HashMap<>();
                m.put("idPlaylist", p.getIdPlaylist());
                m.put("nombre", p.getNombre());
                out.add(m);
            }
        }
        return out;
    }

    @PostMapping("/api/playlists/{playlistId}/add")
    @ResponseBody
    public ResponseEntity<Map<String,Object>> addSongToPlaylistApi(@PathVariable("playlistId") Long playlistId,
                                                                   @RequestParam("cancionId") Long cancionId) {
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        if (attrs == null) return ResponseEntity.badRequest().body(Map.of("error","No session"));
        Object idObj = attrs.getAttribute("id_usuario", RequestAttributes.SCOPE_SESSION);
        if (!(idObj instanceof Long)) return ResponseEntity.status(403).body(Map.of("error","Not authenticated"));
        Usuario usuario = usuarioService.obtenerUsuarioPorId((Long) idObj);
        if (usuario == null) return ResponseEntity.status(403).body(Map.of("error","Not authenticated"));

        // Comprobar existencia de playlist y que pertenece al usuario
        Optional<Playlist> playlistOpt = playlistService.findById(playlistId);
        if (playlistOpt.isEmpty()) return ResponseEntity.badRequest().body(Map.of("error","Playlist not found"));
        Playlist playlist = playlistOpt.get();
        if (playlist.getUsuario() == null || !playlist.getUsuario().getId().equals(usuario.getId())) {
            return ResponseEntity.status(403).body(Map.of("error","Playlist does not belong to user"));
        }

        Optional<Cancion> cancionOpt = cancionService.findById(cancionId);
        if (cancionOpt.isEmpty()) return ResponseEntity.badRequest().body(Map.of("error","Song not found"));
        Cancion cancion = cancionOpt.get();

        // Comprobar si la canción ya está en esta playlist
        var listOpt = cancionPlaylistService.findByPlaylistIdPlaylistOrderByOrdenAsc(playlistId);
        if (listOpt.isPresent()) {
            for (var cp : listOpt.get()) {
                if (cp.getCancion() != null && cp.getCancion().getIdCancion().equals(cancionId)) {
                    return ResponseEntity.badRequest().body(Map.of("error","La canción ya está en la playlist"));
                }
            }
        }

        // Crear asociación y asignar orden al final
        CancionPlaylist cp = new CancionPlaylist();
        cp.setCancion(cancion);
        cp.setPlaylist(playlist);
        int orden = 1;
        if (listOpt.isPresent()) orden = listOpt.get().size() + 1;
        cp.setOrden(orden);
        CancionPlaylist saved = cancionPlaylistService.save(cp);
        if (saved == null) return ResponseEntity.status(500).body(Map.of("error","failed to save"));
        return ResponseEntity.ok(Map.of("ok",true));
    }

    // Endpoint para editar una playlist (nombre/descripcion) vía AJAX
    @PostMapping("/api/playlists/{playlistId}/edit")
    @ResponseBody
    public ResponseEntity<Map<String,Object>> editPlaylistApi(@PathVariable("playlistId") Long playlistId,
                                                               @RequestParam(name = "nombre", required = false) String nombre,
                                                               @RequestParam(name = "descripcion", required = false) String descripcion) {
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        if (attrs == null) return ResponseEntity.badRequest().body(Map.of("error","No session"));
        Object idObj = attrs.getAttribute("id_usuario", RequestAttributes.SCOPE_SESSION);
        if (!(idObj instanceof Long)) return ResponseEntity.status(403).body(Map.of("error","Not authenticated"));
        Usuario usuario = usuarioService.obtenerUsuarioPorId((Long) idObj);
        if (usuario == null) return ResponseEntity.status(403).body(Map.of("error","Not authenticated"));

        Optional<Playlist> playlistOpt = playlistService.findById(playlistId);
        if (playlistOpt.isEmpty()) return ResponseEntity.badRequest().body(Map.of("error","Playlist not found"));
        Playlist playlist = playlistOpt.get();
        if (playlist.getUsuario() == null || !playlist.getUsuario().getId().equals(usuario.getId())) {
            return ResponseEntity.status(403).body(Map.of("error","Playlist does not belong to user"));
        }

        boolean changed = false;
        if (nombre != null) {
            String n = nombre.trim();
            if (!n.equals(playlist.getNombre())) { playlist.setNombre(n); changed = true; }
        }
        if (descripcion != null) {
            String d = descripcion.trim();
            if (playlist.getDescripcion() == null || !playlist.getDescripcion().equals(d)) { playlist.setDescripcion(d); changed = true; }
        }

        if (changed) {
            playlistService.save(playlist);
        }
        return ResponseEntity.ok(Map.of("ok",true));
    }

    // Endpoint para obtener detalles de una playlist incluyendo sus canciones
    @GetMapping("/api/playlists/{playlistId}")
    @ResponseBody
    public ResponseEntity<Map<String,Object>> getPlaylistApi(@PathVariable("playlistId") Long playlistId) {
        Optional<Playlist> playlistOpt = playlistService.findById(playlistId);
        if (playlistOpt.isEmpty()) return ResponseEntity.badRequest().body(Map.of("error","Playlist not found"));
        Playlist p = playlistOpt.get();
        Map<String,Object> out = new HashMap<>();
        out.put("idPlaylist", p.getIdPlaylist());
        out.put("nombre", p.getNombre());
        out.put("descripcion", p.getDescripcion() != null ? p.getDescripcion() : "");
        if (p.getUsuario() != null) {
            Map<String,Object> autor = new HashMap<>();
            autor.put("id", p.getUsuario().getId());
            autor.put("username", p.getUsuario().getUsername());
            out.put("autor", autor);
        } else {
            out.put("autor", Map.of());
        }
        // canciones asociadas (ordenadas)
        List<Map<String,Object>> cancionesOut = new ArrayList<>();
        var cpsOpt = cancionPlaylistService.findByPlaylistIdPlaylistOrderByOrdenAsc(playlistId);
        if (cpsOpt.isPresent()) {
            for (var cp : cpsOpt.get()) {
                if (cp.getCancion() == null) continue;
                Cancion c = cp.getCancion();
                Map<String,Object> cm = new HashMap<>();
                cm.put("idCancion", c.getIdCancion());
                cm.put("titulo", c.getTitulo());
                cm.put("genero", c.getGenero());
                cm.put("duracion", c.getDuracion());
                cm.put("archivoAudio", c.getArchivoAudio());
                // artista asociado
                if (c.getArtista() != null) {
                    if (c.getArtista().getUsuario() != null) cm.put("artista", c.getArtista().getUsuario().getUsername());
                    else cm.put("artista", "");
                } else {
                    cm.put("artista", "");
                }
                cm.put("orden", cp.getOrden());
                cancionesOut.add(cm);
            }
        }
        out.put("canciones", cancionesOut);
        out.put("numCanciones", cancionesOut.size());
        return ResponseEntity.ok(out);
    }
}
