package com.example.MDAI_Proyecto.service.embeddings.impl;

import com.example.MDAI_Proyecto.config.OllamaProperties;
import com.example.MDAI_Proyecto.service.embeddings.EmbeddingException;
import com.example.MDAI_Proyecto.service.embeddings.EmbeddingsService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class OllamaCliEmbeddingsService implements EmbeddingsService {

    private static final Logger log = LoggerFactory.getLogger(OllamaCliEmbeddingsService.class);

    private final OllamaProperties props;
    private final ObjectMapper mapper;

    public OllamaCliEmbeddingsService(OllamaProperties props, ObjectMapper mapper) {
        this.props = props;
        this.mapper = mapper;
    }

    @Override
    public List<List<Double>> embed(List<String> inputs) throws EmbeddingException {
        if (inputs == null || inputs.isEmpty()) return new ArrayList<>();
        if (inputs.size() > props.getMaxBatchSize()) throw new EmbeddingException("Batch too large: " + inputs.size());
        try {
            // Escribir inputs a un archivo temporal como JSON array
            File tmp = Files.createTempFile("ollama-embed-", ".json").toFile();
            mapper.writeValue(tmp, inputs);
            // Construir comando: ollama embed <model> --input-file tmp --json
            List<String> cmd = new ArrayList<>();
            cmd.add(props.getCliPath());
            cmd.add("embed");
            cmd.add(props.getModel());
            cmd.add("--input-file");
            cmd.add(tmp.getAbsolutePath());
            cmd.add("--json");

            if (log.isInfoEnabled()) log.info("Ollama CLI command: {} ... (input-file {})", cmd.subList(0, Math.min(cmd.size(), 4)), tmp.getAbsolutePath());

            ProcessBuilder pb = new ProcessBuilder(cmd);
            pb.redirectErrorStream(true);
            Process p = pb.start();
            boolean finished = p.waitFor(Duration.ofMillis(props.getHttpTimeoutMs()).toMillis(), java.util.concurrent.TimeUnit.MILLISECONDS);
            String out = new String(p.getInputStream().readAllBytes());
            tmp.delete();
            if (!finished) {
                p.destroyForcibly();
                throw new EmbeddingException("Ollama CLI timeout");
            }
            if (p.exitValue() != 0) {
                throw new EmbeddingException("Ollama CLI failed: " + out);
            }
            if (log.isDebugEnabled()) log.debug("Ollama CLI output length={}", out == null ? 0 : out.length());
            // parsear salida JSON: asumir { "embeddings": [[...],[...]] }
            JsonNode root = mapper.readTree(out);
            JsonNode embs = root.get("embeddings");
            if (embs == null || !embs.isArray()) throw new EmbeddingException("Invalid CLI response: missing embeddings");
            List<List<Double>> result = new ArrayList<>();
            for (JsonNode vec : embs) {
                List<Double> v = new ArrayList<>();
                for (JsonNode n : vec) v.add(n.asDouble());
                result.add(v);
            }
            if (log.isInfoEnabled()) log.info("Ollama CLI returned {} embeddings (first dim={})", result.size(), result.isEmpty() ? 0 : result.get(0).size());
            return result;
        } catch (IOException | InterruptedException e) {
            throw new EmbeddingException("Failed to get embeddings via CLI", e);
        }
    }
}
