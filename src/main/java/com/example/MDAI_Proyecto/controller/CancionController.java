package com.example.MDAI_Proyecto.controller;

import com.example.MDAI_Proyecto.data.model.Cancion;
import com.example.MDAI_Proyecto.data.services.CancionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class CancionController {

    private final CancionService cancionService;

    public CancionController(CancionService cancionService) {
        this.cancionService = cancionService;
        System.out.println("\t CancionController inicializado");
    }

    @GetMapping("/Buscar")
    public String buscar(
            @RequestParam(required = false) String filtroTipo,
            @RequestParam(required = false) String query,
            Model model) {

        Iterable<Cancion> canciones;
        System.out.println("\t Recojo la petición de /Buscar en CancionController");
        if (filtroTipo == null || filtroTipo.isBlank()) {
            canciones = cancionService.getAll();
        } else {
            String q = (query == null) ? "" : query;
            switch (filtroTipo) {
                case "nombre":
                    canciones = cancionService.findByTituloContainingIgnoreCase(q);
                    break;
                case "genero":
                    canciones = cancionService.findByGeneroIgnoreCase(q);
                    break;
                case "artista":
                    canciones = cancionService.findbyArtistaName(q);
                    break;
                default:
                    canciones = cancionService.getAll();
            }
        }

        model.addAttribute("canciones", canciones);
        return "BuscarCanciones";
    }
}
