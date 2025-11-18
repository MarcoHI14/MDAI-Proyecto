package com.example.MDAI_Proyecto.data.services;

import com.example.MDAI_Proyecto.data.model.Playlist;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Servicio para la entidad Playlist.
 * Proporciona métodos para realizar operaciones relacionadas con Playlist.
 */
public interface PlaylistService {

    /**
     * Busca una {@link Playlist} por su identificador.
     *
     * @param id identificador de la playlist; puede ser {@code null}.
     * @return {@link Optional} con la playlist si existe, o {@link Optional#empty()} en caso contrario.
     */
    Optional<Playlist> findById(Long id);

    /**
     * Recupera todas las playlists disponibles.
     *
     * @return un {@link Iterable} con todas las playlists; puede estar vacío.
     */
    Iterable<Playlist> findAll();

    /**
     * Busca una playlist por su nombre.
     *
     * @param nombre nombre de la playlist; puede ser {@code null}.
     * @return {@link Optional} con la playlist si existe, o {@link Optional#empty()} en caso contrario.
     */
    Optional<Playlist> findByNombre(String nombre);

    /**
     * Busca una playlist por el nombre de usuario del propietario.
     *
     * @param nombre nombre de usuario del propietario; puede ser {@code null}.
     * @return {@link Optional} con la playlist si existe, o {@link Optional#empty()} en caso contrario.
     */
    Optional<Playlist> findByUsuario_Username(String nombre);

    /**
     * Persiste o actualiza una {@link Playlist}.
     *
     * @param playlist entidad a guardar; no debe ser {@code null}.
     * @return la entidad persistida (puede incluir campos actualizados como el id).
     */
    Playlist save(Playlist playlist);

    /**
     * Elimina la playlist con el identificador dado.
     *
     * @param id identificador de la playlist a eliminar.
     */
    void deleteById(Long id);

    /**
     * Comprueba si existe una playlist con el identificador dado.
     *
     * @param id identificador a comprobar; si es {@code null} devuelve {@code false}.
     * @return {@code true} si existe, {@code false} en caso contrario.
     */
    boolean existsById(Long id);
}
