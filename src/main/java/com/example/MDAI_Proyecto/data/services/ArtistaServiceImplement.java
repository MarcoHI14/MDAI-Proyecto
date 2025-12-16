package com.example.MDAI_Proyecto.data.services;

import com.example.MDAI_Proyecto.data.model.Artista;
import com.example.MDAI_Proyecto.data.model.Usuario;
import com.example.MDAI_Proyecto.data.repository.ArtistaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
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
    public Artista findById(Long id) { return artistaRepository.findById(id).orElse(null); }

    /**
     * Obtiene un artista por el nombre de usuario del usuario asociado.
     *
     * @param username El nombre de usuario del usuario asociado.
     * @return El artista si se encuentra, o null si no.
     */
    public Artista findByUsuarioUsername(String username) { return artistaRepository.findByUsuarioUsername(username).orElse(null); }

    /**
     * Obtiene un artista por el email del usuario asociado.
     *
     * @param email El email del usuario asociado.
     * @return El artista si se encuentra, o null si no.
     */
    public Artista findByUsuarioEmail(String email) { return artistaRepository.findByUsuarioEmail(email).orElse(null); }

    public Artista findByUsuarioId(Long usuarioId) { return artistaRepository.findByUsuarioId(usuarioId).orElse(null); }

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
     * Se realiza dentro de una transacción, se recupera la entidad gestionada,
     * se desvincula del usuario y se limpian las canciones (orphanRemoval) antes de borrar.
     * Esto evita errores de Hibernate relacionados con instancias transitorias.
     *
     * @param artista El artista a eliminar.
     */
    @Transactional
    public void deleteArtista(Artista artista) {
        if (artista == null) return;
        // Recuperar la entidad gestionada por id si es posible
        Artista managed = null;
        if (artista.getIdArtista() != null) {
            managed = artistaRepository.findById(artista.getIdArtista()).orElse(null);
        }
        // Si no tenemos id, intentar buscar por usuario id
        if (managed == null && artista.getUsuario() != null && artista.getUsuario().getId() != null) {
            managed = artistaRepository.findByUsuarioId(artista.getUsuario().getId()).orElse(null);
        }
        // Si sigue sin gestionada, no hacemos nada
        if (managed == null) return;

        // Desvincular del usuario (lado inverso) para evitar referencias inconsistentes
        Usuario u = managed.getUsuario();
        if (u != null) {
            // el setter en Usuario mantiene la bidireccionalidad
            u.setArtista(null);
            managed.setUsuario(null);
        }

        // Limpiar lista de canciones para que orphanRemoval las borre correctamente
        if (managed.getCanciones() != null && !managed.getCanciones().isEmpty()) {
            // Asegurarse de actualizar el owning side (Cancion.artista) antes de borrar
            var lista = List.copyOf(managed.getCanciones());
            for (var c : lista) {
                try {
                    c.setArtista(null);
                } catch (Exception ignore) {}
            }
            managed.getCanciones().clear();
        }

        // Finalmente borrar la entidad gestionada
        artistaRepository.delete(managed);
    }

    /**
     * Borrar artista por id (transaccional).
     * @param id id del artista
     */
    @Transactional
    public void deleteArtistaById(Long id) {
        if (id == null) return;
        Artista managed = artistaRepository.findById(id).orElse(null);
        if (managed == null) return;
        deleteArtista(managed);
    }

}
