package com.example.MDAI_Proyecto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.MDAI_Proyecto.data.services.ArtistaService;

@Controller
public class BienvenidaController {

    private final ArtistaService artistaService;

    public BienvenidaController(ArtistaService artistaService) {
        this.artistaService = artistaService;
        System.out.println("\t BienvenidaController inicializado");
    }

    @GetMapping("/Buscar")
    public String mostrarBuscar() {
        System.out.println("\t Recojo la petición de /Buscar -> redirigiendo a /BuscarCanciones");
        return "redirect:/BuscarCanciones";
    }

    @GetMapping("/Playlists")
    public String mostrarPlaylists() {
        System.out.println("\t Recojo la petición de /Playlists");
        return "GestorPlaylists";
    }

    @GetMapping("/Artistas")
    public String mostrarArtistas() {
        System.out.println("\t Recojo la petición de /Artistas -> redirigiendo a /BuscarArtistas");
        return "redirect:/BuscarArtistas";
    }
}
