package com.example.MDAI_Proyecto.data.repository;

import com.example.MDAI_Proyecto.data.model.CancionPlaylist;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad CancionPlaylist.
 * Proporciona métodos para realizar operaciones CRUD y consultas personalizadas.
 */
@Repository
public interface CancionPlaylistRepository extends CrudRepository<CancionPlaylist, Long> {

    /**
     * Busca todas las entradas de CancionPlaylist asociadas a una playlist específica.
     *
     * @param playlistId El ID de la playlist.
     * @return Una lista opcional de CancionPlaylist asociadas a la playlist.
     */
    @Query("SELECT cp FROM CancionPlaylist cp WHERE cp.playlist.idPlaylist = :playlistId")
    Optional<List<CancionPlaylist>> findByPlaylistIdPlaylist(@Param("playlistId") Long playlistId);

    /**
     * Busca una entrada de CancionPlaylist por el ID de la canción. Solo ha de haber
     * una entrada por canción, ya que no se puede añadir dos veces la misma a la playlist.
     * @param cancionId
     * @return
     */
    @Query("SELECT cp FROM CancionPlaylist cp WHERE cp.cancion.idCancion = :cancionId")
    Optional<CancionPlaylist> findByCancionId(@Param("cancionId") Long cancionId);

    /**
     * Busca todas las entradas de CancionPlaylist asociadas a una playlist específica,
     * ordenadas por el campo 'orden' en orden ascendente.
     *
     * @param playlistId El ID de la playlist.
     * @return Una lista opcional de CancionPlaylist asociadas a la playlist, ordenadas por 'orden'.
     */
    @Query("SELECT cp FROM CancionPlaylist cp WHERE cp.playlist.idPlaylist = :playlistId ORDER BY cp.orden ASC")
    Optional<List<CancionPlaylist>> findByPlaylistIdPlaylistOrderByOrdenAsc(@Param("playlistId") Long playlistId);

    /**
     * Busca todas las entradas de CancionPlaylist asociadas a una playlist específica,
     * ordenadas por el campo 'orden' en orden descendente.
     *
     * @param playlistId El ID de la playlist.
     * @return Una lista opcional de CancionPlaylist asociadas a la playlist, ordenadas por 'orden' en orden descendente.
     */
    @Query("SELECT cp FROM CancionPlaylist cp WHERE cp.playlist.idPlaylist = :playlistId ORDER BY cp.orden DESC")
    Optional<List<CancionPlaylist>> findByPlaylistIdPlaylistOrderByOrdenDesc(@Param("playlistId") Long playlistId);

    /**
     * Busca todas las entradas de CancionPlaylist asociadas a una playlist específica y
     * a una lista de géneros de canción, ordenadas por el campo 'orden' en orden ascendente.
     *
     * @param playlistId El ID de la playlist.
     * @param genres     Una lista de géneros de canción.
     * @return Una lista opcional de CancionPlaylist asociadas a la playlist y géneros dados,
     * ordenadas por 'orden'.
     */
    @Query("SELECT cp FROM CancionPlaylist cp WHERE cp.playlist.idPlaylist = :playlistId AND cp.cancion.genero IN :genres ORDER BY cp.orden ASC")
    Optional<List<CancionPlaylist>> findByPlaylistIdAndCancionGeneroInOrderByOrdenAsc(@Param("playlistId") Long playlistId, @Param("genres") List<String> genres);

    /**
     * Busca todas las entradas de CancionPlaylist asociadas a una playlist específica y
     * a una lista de títulos de canción, ordenadas por el campo 'orden' en orden ascendente.
     *
     * @param playlistId El ID de la playlist.
     * @param titulos    Una lista de títulos de canción.
     * @return Una lista opcional de CancionPlaylist asociadas a la playlist y títulos dados,
     * ordenadas por 'orden'.
     */
    @Query("SELECT cp FROM CancionPlaylist cp WHERE cp.playlist.idPlaylist = :playlistId AND cp.cancion.titulo IN :titulos ORDER BY cp.orden ASC")
    Optional<List<CancionPlaylist>> findByPlaylistIdAndCancionTituloInOrderByOrdenAsc (@Param("playlistId") Long playlistId, @Param("titulos") List<String> titulos);

    /**
     * Busca todas las entradas de CancionPlaylist asociadas a una playlist específica y
     * al nombre de un artista (parcial, case insensitive), ordenadas por el campo 'orden' en orden ascendente.
     *
     * @param playlistId   El ID de la playlist.
     * @param artistaNombre El nombre (o parte del nombre) del artista.
     * @return Una lista opcional de CancionPlaylist asociadas a la playlist y al artista dado,
     * ordenadas por 'orden'.
     */
    @Query("SELECT cp FROM CancionPlaylist cp WHERE cp.playlist.idPlaylist = :playlistId AND LOWER(cp.cancion.artista.usuario.username) LIKE LOWER(CONCAT('%', :artistaNombre, '%')) ORDER BY cp.orden ASC")
    Optional<List<CancionPlaylist>> findByPlaylistIdAndCancionArtistaNombreOrderByOrdenAsc(@Param("playlistId") Long playlistId, @Param("artistaNombre") String artistaNombre);
}

