package com.example.MDAI_Proyecto.data.services;

import com.example.MDAI_Proyecto.data.model.CancionPlaylist;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para la entidad CancionPlaylist.
 * Proporciona métodos para realizar operaciones relacionadas con CancionPlaylist.
 */
public interface CancionPlaylistService {

    /**
     * Busca todas las asociaciones {@link CancionPlaylist} para una playlist dada.
     *
     * @param playlistId identificador de la playlist; si es {@code null} o inválido,
     *                   la implementación puede devolver {@link Optional#empty()}.
     * @return {@link Optional} con la lista de resultados (posiblemente vacía) o
     *         {@link Optional#empty()} si no procede realizar la búsqueda.
     */

    Optional<List<CancionPlaylist>> findByPlaylistIdPlaylist (Long playlistId);

    /**
     * Busca la asociación {@link CancionPlaylist} por el identificador de la canción.
     *
     * @param cancionId identificador de la canción.
     * @return {@link Optional} con la asociación si existe, o {@link Optional#empty()} en caso contrario.
     */
    Optional<CancionPlaylist> findByCancionId(Long cancionId);

    /**
     * Busca las asociaciones de una playlist ordenadas por el campo {@code orden} en
     * orden ascendente.
     *
     * @param playlistId identificador de la playlist.
     * @return {@link Optional} con la lista ordenada o {@link Optional#empty()} si no procede.
     */
    Optional<List<CancionPlaylist>> findByPlaylistIdPlaylistOrderByOrdenAsc(Long playlistId);

    /**
     * Busca las asociaciones de una playlist ordenadas por el campo {@code orden} en
     * orden descendente.
     *
     * @param playlistId identificador de la playlist.
     * @return {@link Optional} con la lista ordenada o {@link Optional#empty()} si no procede.
     */
    Optional<List<CancionPlaylist>> findByPlaylistIdPlaylistOrderByOrdenDesc(Long playlistId);

    /**
     * Busca las asociaciones de una playlist filtrando por una lista de nombres de género
     * y ordenando por {@code orden} ascendente.
     *
     * @param playlistId identificador de la playlist.
     * @param generos    lista de nombres de género a filtrar; si es {@code null} o vacía,
     *                   la implementación puede devolver todos o ninguno según la lógica de negocio.
     * @return {@link Optional} con la lista filtrada y ordenada o {@link Optional#empty()} si no procede.
     */
    Optional<List<CancionPlaylist>> findByPlaylistIdPlaylistAndCancion_Genero_NombreInOrderByOrdenAsc(Long playlistId, List<String> generos);

    /**
     * Busca las asociaciones de una playlist filtrando por una lista de títulos de canción
     * y ordenando por {@code orden} ascendente.
     *
     * @param playlistId identificador de la playlist.
     * @param titulos    lista de títulos de canción a filtrar.
     * @return {@link Optional} con la lista filtrada y ordenada o {@link Optional#empty()} si no procede.
     */
    Optional<List<CancionPlaylist>> findByPlaylistIdAndCancionTituloInOrderByOrdenAsc (Long playlistId, List<String> titulos);

    /**
     * Busca las asociaciones de una playlist por el nombre del artista de la canción,
     * ordenadas por {@code orden} ascendente.
     *
     * @param playlistId    identificador de la playlist.
     * @param artistaNombre nombre del artista a filtrar.
     * @return {@link Optional} con la lista filtrada y ordenada o {@link Optional#empty()} si no procede.
     */
    Optional<List<CancionPlaylist>> findByPlaylistIdAndCancionArtistaNombreOrderByOrdenAsc(Long playlistId, String artistaNombre);

    /**
     * Persiste una asociación {@link CancionPlaylist}.
     * <p>
     * La implementación debe validar la entidad y gestionar transacciones. En caso de
     * entrada inválida puede lanzar una excepción unchecked o devolver {@code null}
     * según la política del servicio.
     * </p>
     *
     * @param cancionPlaylist entidad a guardar.
     * @return la entidad persistida (con id asignado si procede).
     */
    CancionPlaylist save(CancionPlaylist cancionPlaylist);

    /**
     * Elimina la asociación identificada por {@code id}.
     *
     * @param id identificador de la asociación a eliminar.
     */
    void deleteById(Long id);

    /**
     * Comprueba si existe una asociación con el identificador dado.
     *
     * @param id identificador a comprobar.
     * @return {@code true} si existe, {@code false} en caso contrario.
     */
    boolean existsById(Long id);
}
