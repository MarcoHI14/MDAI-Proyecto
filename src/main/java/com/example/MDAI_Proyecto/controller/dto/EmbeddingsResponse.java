package com.example.MDAI_Proyecto.controller.dto;

import java.util.List;

public class EmbeddingsResponse {
    private List<List<Double>> embeddings;

    public EmbeddingsResponse() {}
    public EmbeddingsResponse(List<List<Double>> embeddings) { this.embeddings = embeddings; }
    public List<List<Double>> getEmbeddings() { return embeddings; }
    public void setEmbeddings(List<List<Double>> embeddings) { this.embeddings = embeddings; }
}

