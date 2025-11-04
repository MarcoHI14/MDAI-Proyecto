package com.example.MDAI_Proyecto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InicioController {
    public InicioController() {
        System.out.println("\t InicioController");
    }

    @GetMapping("/")
    public String mostrarInicio() {
        System.out.println("\t Recojo la peticion");
        return "Inicio";
    }
}
