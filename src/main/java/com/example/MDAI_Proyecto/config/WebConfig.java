package com.example.MDAI_Proyecto.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.http.CacheControl;

import java.time.Duration;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    /**
     * Configura los manejadores de recursos estáticos,
     * sirve archivos desde la carpeta uploads/ en el sistema de archivos
     * bajo la ruta /uploads/** sin almacenamiento en caché.
     */
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/")
                .setCacheControl(CacheControl.noStore())
                .resourceChain(false);
    }
}

