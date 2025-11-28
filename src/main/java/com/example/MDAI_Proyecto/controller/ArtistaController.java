package com.example.MDAI_Proyecto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ArtistaController {

    public ArtistaController() {
        System.out.println("\t ControllerArtista inicializado");
    }

    @GetMapping({"/Artista", "/Artista.html"})
    public String mostrarBienvenidaArtista() {
        System.out.println("\t Recojo la petición de /Artista");
        return "BienvenidaArtista";
    }

}

