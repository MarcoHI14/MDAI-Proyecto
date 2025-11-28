package com.example.MDAI_Proyecto.controller;

import com.example.MDAI_Proyecto.data.model.Artista;
import com.example.MDAI_Proyecto.data.model.Cancion;
import com.example.MDAI_Proyecto.data.services.ArtistaService;
import com.example.MDAI_Proyecto.data.services.CancionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;

@Controller
public class ArtistaController {

    private final ArtistaService artistaService;
    private final CancionService cancionService;

    public ArtistaController(ArtistaService artistaService, CancionService cancionService) {
        this.artistaService = artistaService;
        this.cancionService = cancionService;
        System.out.println("\t ArtistaController inicializado");
    }

    @GetMapping("/BuscarArtistas")
    public String buscarArtistas(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Long artistId,
            Model model) {

        System.out.println("\t Recojo la petición de /BuscarArtistas");

        List<Artista> artistas;

        if (query != null && !query.trim().isEmpty()) {
            artistas = Collections.singletonList(artistaService.findByUsuarioUsername(query));
        } else {
            artistas = artistaService.findAll("");
        }

        model.addAttribute("artistas", artistas);

        // Si se seleccionó un artista, cargar sus canciones
        if (artistId != null) {
            Artista artistaSeleccionado = artistaService.findById(artistId);
            List<Cancion> canciones = cancionService.findbyArtistaName(artistaSeleccionado.getUsuario().getUsername());

            model.addAttribute("artistaSeleccionado", artistaSeleccionado);
            model.addAttribute("canciones", canciones);
        }

        return "BuscarArtistas";
    }
}
