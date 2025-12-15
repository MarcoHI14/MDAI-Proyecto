package com.example.MDAI_Proyecto.service.embeddings;

import java.util.List;

public interface EmbeddingsService {
    // Devuelve una lista de vectores (uno por cada input)
    List<List<Double>> embed(List<String> inputs) throws EmbeddingException;
}

