package com.example.MDAI_Proyecto.config;

import com.example.MDAI_Proyecto.service.embeddings.EmbeddingsService;
import com.example.MDAI_Proyecto.service.embeddings.impl.OllamaCliEmbeddingsService;
import com.example.MDAI_Proyecto.service.embeddings.impl.OllamaHttpEmbeddingsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OllamaAutoConfiguration {

    @Bean
    public EmbeddingsService embeddingsService(OllamaProperties props, ObjectMapper mapper) {
        // crear instancias directamente para evitar registros múltiples por component-scan
        OllamaHttpEmbeddingsService httpService = new OllamaHttpEmbeddingsService(props, mapper);
        OllamaCliEmbeddingsService cliService = new OllamaCliEmbeddingsService(props, mapper);
        if ("cli".equalsIgnoreCase(props.getMode())) return cliService;
        return httpService;
    }
}
