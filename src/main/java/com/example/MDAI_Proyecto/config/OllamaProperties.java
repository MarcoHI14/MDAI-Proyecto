package com.example.MDAI_Proyecto.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "ollama")
public class OllamaProperties {
    private String mode = "http"; // http or cli
    private String httpUrl = "http://127.0.0.1:11434";
    private String model = "nomic-embed-text";
    private String cliPath = "ollama";
    private int maxBatchSize = 32;
    private int maxInputLength = 10000;
    private int httpTimeoutMs = 30000;

    public String getMode() { return mode; }
    public void setMode(String mode) { this.mode = mode; }
    public String getHttpUrl() { return httpUrl; }
    public void setHttpUrl(String httpUrl) { this.httpUrl = httpUrl; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public String getCliPath() { return cliPath; }
    public void setCliPath(String cliPath) { this.cliPath = cliPath; }
    public int getMaxBatchSize() { return maxBatchSize; }
    public void setMaxBatchSize(int maxBatchSize) { this.maxBatchSize = maxBatchSize; }
    public int getMaxInputLength() { return maxInputLength; }
    public void setMaxInputLength(int maxInputLength) { this.maxInputLength = maxInputLength; }
    public int getHttpTimeoutMs() { return httpTimeoutMs; }
    public void setHttpTimeoutMs(int httpTimeoutMs) { this.httpTimeoutMs = httpTimeoutMs; }
}

