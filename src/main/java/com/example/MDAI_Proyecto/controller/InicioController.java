package com.example.MDAI_Proyecto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InicioController {
    public InicioController() {
        System.out.println("\t InicioController");
    }

    @GetMapping("/")
    public String mostrarHtml() {
        System.out.println("\t Recojo la peticion");
        return "Inicio";
    }
    @GetMapping("/Inicio.html")
    public String mostrarInicioHtml() {
        System.out.println("\t Recojo la peticion");
        return "Inicio";
    }

    @GetMapping("/InicioSesion.html")
    public String mostrarInicioSesionHtml() {
        System.out.println("\t Recojo la peticion");
        return "InicioSesion";
    }

    @GetMapping("/Registro.html")
    public String mostrarRegistroHtml() {
        System.out.println("\t Recojo la peticion");
        return "Registro";
    }

    @GetMapping("/Información.html")
    public String mostrarInformacionHtml() {
        System.out.println("\t Recojo la peticion");
        return "Información";
    }
}
