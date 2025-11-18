package com.example.MDAI_Proyecto.data.services;

import com.example.MDAI_Proyecto.data.model.Artista;
import com.example.MDAI_Proyecto.data.model.Usuario;
import com.example.MDAI_Proyecto.data.repository.ArtistaRepository;
import com.example.MDAI_Proyecto.data.repository.UsuarioRepository;

import java.util.List;

public class ArtistaServiceImplement implements ArtistaService{

    private final ArtistaRepository artistaRepository;

    /**
     * Constructor para inyección de dependencias.
     *
     * @param artistaRepository El repositorio de artistas.
     */
    public ArtistaServiceImplement(ArtistaRepository artistaRepository) {
        this.artistaRepository = artistaRepository;
    }

    /**
     * Obtiene todos los artistas del repositorio.
     *
     * @return Una lista de todos los artistas.
     */
    public  List<Artista> findAll(String nome) { return (List<Artista>) artistaRepository.findAll();}

    /**
     * Obtiene un artista por su ID.
     *
     * @param id El ID del artista a obtener.
     * @return El artista si se encuentra, o null si no.
     */
    public Artista findById(Long id) { return (Artista) artistaRepository.findById(id).get(); }

    /**
     * Obtiene un artista por el nombre de usuario del usuario asociado.
     *
     * @param username El nombre de usuario del usuario asociado.
     * @return El artista si se encuentra, o null si no.
     */
    public Artista findByUsuarioUsername(String username) { return (Artista) artistaRepository.findByUsuarioUsername(username).get(); }

    /**
     * Obtiene un artista por el email del usuario asociado.
     *
     * @param email El email del usuario asociado.
     * @return El artista si se encuentra, o null si no.
     */
    public Artista findByUsuarioEmail(String email) { return (Artista) artistaRepository.findByUsuarioEmail(email).get(); }

    /**
     * Guarda un artista en el repositorio.
     *
     * @param artista El artista a guardar.
     * @return El artista guardado.
     */
    public Artista saveArtista(Artista artista) {
        return artistaRepository.save(artista);
    }

    /**
     * Elimina un artista del repositorio.
     *
     * @param artista El artista a eliminar.
     */
    public void deleteArtista(Artista artista) {
        artistaRepository.delete(artista);
    }

}
