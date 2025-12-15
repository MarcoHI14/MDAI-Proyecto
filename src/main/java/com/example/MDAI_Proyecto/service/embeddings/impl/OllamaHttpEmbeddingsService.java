package com.example.MDAI_Proyecto.service.embeddings.impl;

import com.example.MDAI_Proyecto.config.OllamaProperties;
import com.example.MDAI_Proyecto.service.embeddings.EmbeddingException;
import com.example.MDAI_Proyecto.service.embeddings.EmbeddingsService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OllamaHttpEmbeddingsService implements EmbeddingsService {

    private static final Logger log = LoggerFactory.getLogger(OllamaHttpEmbeddingsService.class);

    private final OllamaProperties props;
    private final ObjectMapper mapper;
    private final HttpClient client;

    public OllamaHttpEmbeddingsService(OllamaProperties props, ObjectMapper mapper) {
        this.props = props;
        this.mapper = mapper;
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(props.getHttpTimeoutMs()))
                .build();
    }

    @Override
    public List<List<Double>> embed(List<String> inputs) throws EmbeddingException {
        try {
            if (inputs == null || inputs.isEmpty()) return new ArrayList<>();
            if (inputs.size() > props.getMaxBatchSize()) throw new EmbeddingException("Batch too large: " + inputs.size());
            // Construir body: { "input": [ ... ] }
            String body = mapper.writeValueAsString(new Payload(inputs));
            String url = props.getHttpUrl();
            if (!url.endsWith("/")) url += "/";
            url += "embed?model=" + props.getModel();

            // logging (no volcar todo el texto en info por privacidad)
            if (log.isInfoEnabled()) {
                String preview = inputs.stream().limit(3)
                        .map(s -> s == null ? "" : (s.length() > 60 ? s.substring(0, 60) + "..." : s))
                        .collect(Collectors.joining(" | "));
                log.info("Ollama HTTP request to {} model={} inputs={} preview=[{}]", url, props.getModel(), inputs.size(), preview);
            }

            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofMillis(props.getHttpTimeoutMs()))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
            if (log.isDebugEnabled()) log.debug("Ollama HTTP response status={} bodyLength={}", resp.statusCode(), resp.body() == null ? 0 : resp.body().length());
            if (resp.statusCode() != 200) {
                throw new EmbeddingException("Ollama HTTP returned status " + resp.statusCode() + ": " + resp.body());
            }
            JsonNode root = mapper.readTree(resp.body());
            // Esperamos { "embeddings": [[...],[...]] }
            JsonNode embs = root.get("embeddings");
            if (embs == null || !embs.isArray()) throw new EmbeddingException("Invalid response from Ollama: missing embeddings");
            List<List<Double>> out = new ArrayList<>();
            for (JsonNode vec : embs) {
                List<Double> v = new ArrayList<>();
                for (JsonNode n : vec) v.add(n.asDouble());
                out.add(v);
            }
            if (log.isInfoEnabled()) log.info("Ollama returned {} embeddings (first dim={})", out.size(), out.isEmpty() ? 0 : out.get(0).size());
            return out;
        } catch (EmbeddingException e) { throw e; }
        catch (Exception e) { throw new EmbeddingException("Failed to get embeddings via HTTP", e); }
    }

    // payload helper
    static class Payload {
        public List<String> input;
        public Payload(List<String> input) { this.input = input; }
    }
}
