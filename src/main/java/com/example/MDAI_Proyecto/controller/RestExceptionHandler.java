package com.example.MDAI_Proyecto.controller;

import com.example.MDAI_Proyecto.service.embeddings.EmbeddingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(EmbeddingException.class)
    public ResponseEntity<Map<String,String>> handleEmbedding(EmbeddingException ex){
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(Map.of("error", "Embedding service error", "details", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String,String>> handleGeneric(Exception ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error","Internal error","details", ex.getMessage()));
    }
}

