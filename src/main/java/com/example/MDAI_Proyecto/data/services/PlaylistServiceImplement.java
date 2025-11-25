package com.example.MDAI_Proyecto.data.services;

import com.example.MDAI_Proyecto.data.model.Playlist;
import com.example.MDAI_Proyecto.data.repository.PlaylistRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Implementación de {@link PlaylistService} que delega operaciones en {@link PlaylistRepository}.
 * <p>
 * Se encarga de validaciones simples de entrada (null y valores no válidos) y de marcar
 * la transaccionalidad adecuada en cada método.
 * </p>
 */
@Service
public class PlaylistServiceImplement implements PlaylistService{

    PlaylistRepository playlistRepository;

    /**
     * Constructor de la implementación.
     *
     * @param playlistRepository repositorio usado para persistencia; no debe ser {@code null}.
     */
    public PlaylistServiceImplement(PlaylistRepository playlistRepository) {
        this.playlistRepository = playlistRepository;
    }

    /**
     * Busca una {@link Playlist} por su identificador.
     *
     * @param id identificador de la playlist; si es {@code null} o menor o igual a 0
     *           devuelve {@link Optional#empty()}.
     * @return {@link Optional} con la playlist si existe, o {@link Optional#empty()} si no.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Playlist> findById(Long id) {
        if (id == null || id <= 0) {
            return Optional.empty(); // o lanzar IllegalArgumentException si prefieres
        }
        return playlistRepository.findById(id);
    }

    /**
     * Recupera todas las playlists disponibles.
     *
     * @return un {@link Iterable} con todas las playlists; puede estar vacío.
     */
    @Override
    @Transactional(readOnly = true)
    public Iterable<Playlist> findAll() {
        return playlistRepository.findAll();
    }

    /**
     * Busca una playlist por su nombre.
     *
     * @param nombre nombre de la playlist.
     * @return {@link Optional} con la playlist si existe, o {@link Optional#empty()} si no.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Playlist> findByNombre(String nombre) {
        return playlistRepository.findByNombre(nombre);
    }

    /**
     * Busca una playlist por el nombre de usuario del propietario.
     *
     * @param nombre nombre de usuario del propietario.
     * @return {@link Optional} con la playlist si existe, o {@link Optional#empty()} si no.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Playlist> findByUsuario_Username(String nombre) {
        return playlistRepository.findByUsuario_Username(nombre);
    }

    /**
     * Persiste o actualiza una playlist.
     *
     * @param playlist entidad a guardar; si es {@code null} se devuelve {@code null}.
     * @return la entidad persistida (con id asignado si procede) o {@code null} si la entrada es {@code null}.
     */
    @Override
    @Transactional
    public Playlist save(Playlist playlist) {
        if (playlist == null) {
            return null;
        }
        return playlistRepository.save(playlist);
    }

    /**
     * Elimina la playlist identificada por {@code id}.
     *
     * @param id identificador de la playlist a eliminar.
     */
    @Override
    @Transactional
    public void deleteById(Long id) {
        playlistRepository.deleteById(id);
    }

    /**
     * Comprueba si existe una playlist con el identificador dado.
     *
     * @param id identificador a comprobar; si es {@code null} o menor o igual a 0 devuelve {@code false}.
     * @return {@code true} si existe, {@code false} en caso contrario.
     */
    @Override
    @Transactional
    public boolean existsById(Long id) {
        return playlistRepository.existsById(id);
    }

}
