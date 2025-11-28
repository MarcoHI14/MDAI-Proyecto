package com.example.MDAI_Proyecto.controller;

import com.example.MDAI_Proyecto.data.services.ArtistaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ArtistaController {

    private final ArtistaService artistaService;

    public ArtistaController(ArtistaService artistaService) {
        this.artistaService = artistaService;
        System.out.println("\t ControllerArtista inicializado");
    }

    @GetMapping({"/Artista", "/Artista.html"})
    public String mostrarBienvenidaArtista() {
        System.out.println("\t Recojo la petición de /Artista");
        return "BienvenidaArtista";
    }

    // Nuevo: servir la página pública de búsqueda de artistas y poblar el modelo
    @GetMapping({"/BuscarArtistas", "/BuscarArtistas.html"})
    public String mostrarBuscarArtistas(Model model) {
        System.out.println("\t Recojo la petición de /BuscarArtistas");
        try {
            model.addAttribute("artistas", artistaService.findAll(""));
        } catch (Exception ex) {
            System.err.println("Error cargando lista de artistas en ArtistaController: " + ex.getMessage());
            model.addAttribute("artistas", null);
        }
        return "BuscarArtistas";
    }

}

