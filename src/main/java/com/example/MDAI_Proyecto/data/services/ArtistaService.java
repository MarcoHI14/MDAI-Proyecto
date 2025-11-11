package com.example.MDAI_Proyecto.data.services;

import com.example.MDAI_Proyecto.data.model.Artista;
import com.example.MDAI_Proyecto.data.repository.ArtistaRepository;

import java.util.List;

/**
 * Servicio para la entidad Artista.
 * Proporciona métodos para realizar operaciones relacionadas con Artista.
 */
public interface ArtistaService  {

    List<Artista> findAll(String nome);
    Artista findById(Long id);
    Artista findByUsuarioUsername(String username);
    Artista findByUsuarioEmail(String email);
    Artista saveArtista(Artista artista);
    void deleteArtista(Artista artista);


}
