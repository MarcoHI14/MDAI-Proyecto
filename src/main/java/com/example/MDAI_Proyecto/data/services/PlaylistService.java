package com.example.MDAI_Proyecto.data.services;

import com.example.MDAI_Proyecto.data.model.Playlist;
import java.util.Optional;

public interface PlaylistService {

    public Optional<Playlist> findById(Long id);
    public Iterable<Playlist> findAll();
    public Optional<Playlist> findByNombre(String nombre);
    public Optional<Playlist> findByUsuario_Username(String nombre);

}
