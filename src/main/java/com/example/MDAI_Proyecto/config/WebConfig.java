package com.example.MDAI_Proyecto.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.http.CacheControl;

import java.time.Duration;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Servir archivos desde la carpeta uploads/ en filesystem en la ruta /uploads/**
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/")
                .setCacheControl(CacheControl.noStore())
                .resourceChain(false);
    }
}

