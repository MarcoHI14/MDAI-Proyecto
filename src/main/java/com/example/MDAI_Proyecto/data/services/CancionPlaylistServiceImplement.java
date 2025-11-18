package com.example.MDAI_Proyecto.data.services;

import com.example.MDAI_Proyecto.data.model.CancionPlaylist;
import com.example.MDAI_Proyecto.data.repository.CancionPlaylistRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio {@link CancionPlaylistService} que delega
 * operaciones en el repositorio {@link CancionPlaylistRepository}.
 * <p>
 * Se encarga de las validaciones simples de entrada (null y valores no válidos)
 * y de marcar la transaccionalidad donde procede.
 * </p>
 */
@Service
public class CancionPlaylistServiceImplement implements CancionPlaylistService{

    private CancionPlaylistRepository cancionPlaylistRepository;

    /**
     * Constructor de la implementación.
     *
     * @param cancionPlaylistRepository repositorio usado para persistencia; no debe ser {@code null}.
     */
    public CancionPlaylistServiceImplement(CancionPlaylistRepository cancionPlaylistRepository) {
        this.cancionPlaylistRepository = cancionPlaylistRepository;
    }

    /**
     * Busca todas las asociaciones {@link CancionPlaylist} para una playlist dada.
     *
     * @param playlistId identificador de la playlist; si es {@code null} o menor o igual a 0
     *                   se devuelve {@link Optional#empty()}.
     * @return {@link Optional} con la lista de resultados o {@link Optional#empty()} si la entrada no es válida.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<List<CancionPlaylist>> findByPlaylistIdPlaylist (Long playlistId) {
        if (playlistId == null || playlistId <= 0) {
            return Optional.empty();
        }
        return cancionPlaylistRepository.findByPlaylistIdPlaylist(playlistId);
    }

    /**
     * Busca la asociación {@link CancionPlaylist} por el identificador de la canción.
     *
     * @param cancionId identificador de la canción; si es {@code null} o inválido devuelve {@link Optional#empty()}.
     * @return {@link Optional} con la asociación si existe, o {@link Optional#empty()}.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CancionPlaylist> findByCancionId(Long cancionId) {
        if (cancionId == null || cancionId <= 0) {
            return Optional.empty();
        }
        return cancionPlaylistRepository.findByCancionId(cancionId);
    }

    /**
     * Busca las asociaciones de una playlist ordenadas por {@code orden} ascendente.
     *
     * @param playlistId identificador de la playlist; si es inválido devuelve {@link Optional#empty()}.
     * @return {@link Optional} con la lista ordenada o {@link Optional#empty()}.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<List<CancionPlaylist>> findByPlaylistIdPlaylistOrderByOrdenAsc(Long playlistId) {
        if (playlistId == null || playlistId <= 0) {
            return Optional.empty();
        }
        return cancionPlaylistRepository.findByPlaylistIdPlaylistOrderByOrdenAsc(playlistId);
    }

    /**
     * Busca las asociaciones de una playlist ordenadas por {@code orden} descendente.
     *
     * @param playlistId identificador de la playlist; si es inválido devuelve {@link Optional#empty()}.
     * @return {@link Optional} con la lista ordenada o {@link Optional#empty()}.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<List<CancionPlaylist>> findByPlaylistIdPlaylistOrderByOrdenDesc(Long playlistId) {
        if (playlistId == null || playlistId <= 0) {
            return Optional.empty();
        }
        return cancionPlaylistRepository.findByPlaylistIdPlaylistOrderByOrdenDesc(playlistId);
    }

    /**
     * Busca las asociaciones de una playlist filtrando por géneros y ordenando por {@code orden} ascendente.
     *
     * @param playlistId identificador de la playlist; si es inválido devuelve {@link Optional#empty()}.
     * @param generos    lista de nombres de género a filtrar; puede ser {@code null} según la lógica del repositorio.
     * @return {@link Optional} con la lista filtrada y ordenada o {@link Optional#empty()}.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<List<CancionPlaylist>> findByPlaylistIdPlaylistAndCancion_Genero_NombreInOrderByOrdenAsc(Long playlistId, List<String> generos) {
        if (playlistId == null || playlistId <= 0) {
            return Optional.empty();
        }
        return cancionPlaylistRepository.findByPlaylistIdAndCancionGeneroInOrderByOrdenAsc(playlistId, generos);
    }

    /**
     * Busca las asociaciones de una playlist filtrando por títulos de canción y ordenando por {@code orden} ascendente.
     *
     * @param playlistId identificador de la playlist; si es inválido devuelve {@link Optional#empty()}.
     * @param titulos    lista de títulos a filtrar.
     * @return {@link Optional} con la lista filtrada y ordenada o {@link Optional#empty()}.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<List<CancionPlaylist>> findByPlaylistIdAndCancionTituloInOrderByOrdenAsc (Long playlistId, List<String> titulos) {
        if (playlistId == null || playlistId <= 0) {
            return Optional.empty();
        }
        return cancionPlaylistRepository.findByPlaylistIdAndCancionTituloInOrderByOrdenAsc(playlistId, titulos);
    }

    /**
     * Busca las asociaciones de una playlist por el nombre del artista y ordena por {@code orden} ascendente.
     *
     * @param playlistId    identificador de la playlist; si es inválido devuelve {@link Optional#empty()}.
     * @param artistaNombre nombre del artista a filtrar.
     * @return {@link Optional} con la lista filtrada y ordenada o {@link Optional#empty()}.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<List<CancionPlaylist>> findByPlaylistIdAndCancionArtistaNombreOrderByOrdenAsc(Long playlistId, String artistaNombre) {
        if (playlistId == null || playlistId <= 0) {
            return Optional.empty();
        }
        return cancionPlaylistRepository.findByPlaylistIdAndCancionArtistaNombreOrderByOrdenAsc(playlistId, artistaNombre);
    }

    /**
     * Persiste una asociación {@link CancionPlaylist}.
     *
     * @param cancionPlaylist entidad a guardar; si es {@code null} se devuelve {@code null}.
     * @return la entidad persistida (con id asignado si procede) o {@code null} si la entrada es {@code null}.
     */
    @Override
    @Transactional
    public CancionPlaylist save(CancionPlaylist cancionPlaylist) {
        if (cancionPlaylist == null) {
            return null;
        }
        return cancionPlaylistRepository.save(cancionPlaylist);
    }

    /**
     * Elimina la asociación identificada por {@code id}.
     * <p>
     * No lanza excepciones de negocio; deja que Spring propague excepciones de acceso a datos
     * si ocurren problemas en la capa de persistencia.
     * </p>
     *
     * @param id identificador de la asociación a eliminar.
     */
    @Override
    @Transactional
    public void deleteById(Long id) {
        cancionPlaylistRepository.deleteById(id);
    }

    /**
     * Comprueba si existe una asociación con el identificador dado.
     *
     * @param id identificador a comprobar; si es {@code null} o inválido devuelve {@code false}.
     * @return {@code true} si existe, {@code false} en caso contrario.
     */
    @Override
    @Transactional
    public boolean existsById(Long id) {
        if (id == null || id <= 0) {
            return false;
        }
        return cancionPlaylistRepository.existsById(id);
    }
}
