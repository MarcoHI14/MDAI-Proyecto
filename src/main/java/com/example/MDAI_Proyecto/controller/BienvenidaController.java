package com.example.MDAI_Proyecto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BienvenidaController {

    public BienvenidaController() {
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
        System.out.println("\t Recojo la petición de /Artistas");
        return "BuscarArtistas";
    }

    @GetMapping("/Micuenta")
    public String mostrarMiCuenta() {
        System.out.println("\t Recojo la petición de /Micuenta");
        return "MiCuenta";
    }
}
