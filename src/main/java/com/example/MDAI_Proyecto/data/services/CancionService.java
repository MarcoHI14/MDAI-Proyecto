package com.example.MDAI_Proyecto.data.services;

import com.example.MDAI_Proyecto.data.model.Cancion;

import java.util.List;
import java.util.Optional;

public interface CancionService {

    public Optional<Cancion> findById(Long id);
    public Iterable<Cancion> getAll();
    public Optional<Cancion> findByTituloIgnoreCase (String titulo);
    public List<Cancion> findByGeneroIgnoreCase (String genero);
    public List<Cancion> findbyArtistaName (String nombreArtista);

    //AÑADIR OTROS M�TODOS QUE SEAN NECESARIOS
}
