package com.example.MDAI_Proyecto.controller.dto;

import java.util.List;

public class EmbeddingsRequest {
    private List<String> inputs;

    public EmbeddingsRequest() {}
    public EmbeddingsRequest(List<String> inputs) { this.inputs = inputs; }
    public List<String> getInputs() { return inputs; }
    public void setInputs(List<String> inputs) { this.inputs = inputs; }
}

