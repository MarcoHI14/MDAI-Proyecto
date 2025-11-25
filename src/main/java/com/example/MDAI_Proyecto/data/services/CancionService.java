package com.example.MDAI_Proyecto.data.services;

import com.example.MDAI_Proyecto.data.model.Cancion;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para la entidad Cancion.
 * Proporciona métodos para realizar operaciones relacionadas con Cancion.
 */
public interface CancionService {

    /**
     * Busca una {@link Cancion} por su identificador.
     *
     * @param id identificador de la canción; puede ser {@code null}.
     * @return {@link Optional} con la canción si existe, o {@link Optional#empty()} en caso contrario.
     */
    Optional<Cancion> findById(Long id);

    /**
     * Recupera todas las canciones disponibles.
     *
     * @return un {@link Iterable} con todas las canciones; puede estar vacío.
     */
    Iterable<Cancion> getAll();

    /**
     * Busca una canción por título ignorando mayúsculas/minúsculas.
     *
     * @param titulo título de la canción; si es {@code null} la implementación puede devolver {@link Optional#empty()}.
     * @return {@link Optional} con la canción encontrada o {@link Optional#empty()} si no existe.
     */
    Optional<Cancion> findByTituloIgnoreCase (String titulo);

    /**
     * Busca canciones por género ignorando mayúsculas/minúsculas.
     *
     * @param genero nombre del género; si es {@code null} o vacío la implementación decide el comportamiento.
     * @return lista de canciones que pertenecen al género (posiblemente vacía).
     */
    List<Cancion> findByGeneroIgnoreCase (String genero);

    /**
     * Busca canciones por el nombre del artista.
     *
     * @param nombreArtista nombre del artista; si es {@code null} o vacío la implementación decide el comportamiento.
     * @return lista de canciones del artista (posiblemente vacía).
     */
    List<Cancion> findbyArtistaName (String nombreArtista);


    /**
     * Persiste o actualiza una {@link Cancion}.
     *
     * @param cancion entidad a guardar; no debe ser {@code null}.
     * @return la entidad persistida (puede incluir campos actualizados como el id).
     */
    Cancion save(Cancion cancion);

    /**
     * Elimina la canción con el identificador dado.
     *
     * @param id identificador de la canción a eliminar.
     */
    void deleteById(Long id);

    /**
     * Comprueba si existe una canción con el identificador dado.
     *
     * @param id identificador a comprobar; si es {@code null} devuelve {@code false}.
     * @return {@code true} si existe, {@code false} en caso contrario.
     */
    boolean existsById(Long id);

    //AÑADIR OTROS M�TODOS QUE SEAN NECESARIOS
}
