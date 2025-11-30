package com.example.MDAI_Proyecto.controller;

import com.example.MDAI_Proyecto.data.services.ArtistaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador para manejar las solicitudes relacionadas con artistas.
 */
@Controller
public class ArtistaController {

    private final ArtistaService artistaService;

    /** Inyectar ArtistaService para operaciones relacionadas con artistas */
    public ArtistaController(ArtistaService artistaService) {
        this.artistaService = artistaService;
        System.out.println("\t ControllerArtista inicializado");
    }

    /** Página de bienvenida para artistas */
    @GetMapping({"/Artista", "/Artista.html"})
    public String mostrarBienvenidaArtista() {
        System.out.println("\t Recojo la petición de /Artista");
        return "BienvenidaArtista";
    }

    /** Página para buscar artistas */
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

