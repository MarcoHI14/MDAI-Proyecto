package com.example.MDAI_Proyecto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.MDAI_Proyecto.data.services.ArtistaService;

/**
 * Controlador para manejar las solicitudes de la página de bienvenida.
 */
@Controller
public class BienvenidaController {

    private final ArtistaService artistaService;

    /** Inyectar ArtistaService para posibles operaciones relacionadas con artistas */
    public BienvenidaController(ArtistaService artistaService) {
        this.artistaService = artistaService;
        System.out.println("\t BienvenidaController inicializado");
    }

    /** Página de bienvenida */
    @GetMapping("/Buscar")
    public String mostrarBuscar() {
        System.out.println("\t Recojo la petición de /Buscar -> redirigiendo a /BuscarCanciones");
        return "redirect:/BuscarCanciones";
    }

    /** Redirigir a la página de búsqueda de artistas */
    @GetMapping("/Artistas")
    public String mostrarArtistas() {
        System.out.println("\t Recojo la petición de /Artistas -> redirigiendo a /BuscarArtistas");
        return "redirect:/BuscarArtistas";
    }
}
