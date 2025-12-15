package com.example.MDAI_Proyecto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RecommendViewController {

    @GetMapping("/recomendar")
    public String recommendView() {
        return "recommend"; // Thymeleaf template name
    }
}

