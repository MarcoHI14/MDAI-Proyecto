package com.example.MDAI_Proyecto.data.services;

import com.example.MDAI_Proyecto.data.model.Playlist;
import com.example.MDAI_Proyecto.data.repository.PlaylistRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PlaylistServiceImplement implements PlaylistService{

    PlaylistRepository playlistRepository;

    public PlaylistServiceImplement(PlaylistRepository playlistRepository) {
        this.playlistRepository = playlistRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Playlist> findById(Long id) {
        if (id == null || id <= 0) {
            return Optional.empty(); // o lanzar IllegalArgumentException si prefieres
        }
        return playlistRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Iterable<Playlist> findAll() {
        return playlistRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Playlist> findByNombre(String nombre) {
        return playlistRepository.findByNombre(nombre);
    }

    @Transactional(readOnly = true)
    public Optional<Playlist> findByUsuario_Username(String nombre) {
        return playlistRepository.findByUsuario_Username(nombre);
    }

}
