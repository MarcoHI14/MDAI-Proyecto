package com.example.MDAI_Proyecto.controller;

import com.example.MDAI_Proyecto.config.OllamaProperties;
import com.example.MDAI_Proyecto.controller.dto.EmbeddingsRequest;
import com.example.MDAI_Proyecto.controller.dto.EmbeddingsResponse;
import com.example.MDAI_Proyecto.service.embeddings.EmbeddingException;
import com.example.MDAI_Proyecto.service.embeddings.EmbeddingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EmbeddingsController {

    private static final Logger log = LoggerFactory.getLogger(EmbeddingsController.class);

    private final EmbeddingsService svc;
    private final OllamaProperties props;

    public EmbeddingsController(@Qualifier("embeddingsService") EmbeddingsService svc, OllamaProperties props) {
        this.svc = svc;
        this.props = props;
    }

    @PostMapping("/embeddings")
    public ResponseEntity<EmbeddingsResponse> embeddings(@RequestBody EmbeddingsRequest req) throws EmbeddingException {
        if (req == null || req.getInputs() == null) return ResponseEntity.badRequest().build();
        List<String> inputs = req.getInputs();
        if (inputs.isEmpty() || inputs.size() > props.getMaxBatchSize()) return ResponseEntity.badRequest().build();
        for (String s : inputs) if (s != null && s.length() > props.getMaxInputLength()) return ResponseEntity.badRequest().build();

        log.info("/api/embeddings called: inputs={} mode={}", inputs.size(), props.getMode());
        List<List<Double>> embs = svc.embed(inputs);
        return ResponseEntity.ok(new EmbeddingsResponse(embs));
    }
}
