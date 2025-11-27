package com.example.MDAI_Proyecto.data.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.MDAI_Proyecto.data.model.Cancion;
import com.example.MDAI_Proyecto.data.repository.CancionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * Implementación del servicio {@link CancionService} que delega en
 * {@link CancionRepository} las operaciones de persistencia.
 * <p>
 * Valida entradas simples (null, valores no válidos o cadenas vacías) y
 * aplica la transaccionalidad adecuada en cada método.
 * </p>
 */
@Service
public class CancionServiceImplement implements CancionService {

    private CancionRepository cancionRepository;

    /**
     * Constructor que inyecta el repositorio usado para la persistencia.
     *
     * @param cancionRepository repositorio no nulo que proporciona acceso a datos.
     */
    public CancionServiceImplement(CancionRepository cancionRepository) {
        this.cancionRepository = cancionRepository;
    }

    /**
     * Busca una canción por su identificador.
     *
     * @param id identificador de la canción; si es {@code null} o menor o igual a 0
     *           devuelve {@link Optional#empty()}.
     * @return {@link Optional} con la canción si existe, o {@link Optional#empty()} si no.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Cancion> findById(Long id) {
        if (id == null || id <= 0) {
            return Optional.empty(); // o lanzar IllegalArgumentException si prefieres
        }
        return cancionRepository.findById(id);
    }

    /**
     * Recupera todas las canciones disponibles.
     *
     * @return un {@link Iterable} con todas las canciones; puede estar vacío.
     */
    @Override
    @Transactional(readOnly = true)
    public Iterable<Cancion> getAll() {
        return cancionRepository.findAll();
    }

    /**
     * Busca una canción por título ignorando mayúsculas/minúsculas.
     *
     * @param titulo título de la canción; si no tiene texto devuelve {@link Optional#empty()}.
     * @return {@link Optional} con la canción encontrada o {@link Optional#empty()} si no existe.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Cancion> findByTituloIgnoreCase (String titulo) {
        if (!StringUtils.hasText(titulo)) {
            return Optional.empty(); // o lanzar IllegalArgumentException si prefieres
        }
        return cancionRepository.findByTituloIgnoreCase(titulo);
    }

    @Override
    public List<Cancion> findByTituloContainingIgnoreCase(String fragmento) {
        if (!StringUtils.hasText(fragmento)) {
            return new ArrayList<>();
        }
        return cancionRepository.findByTituloContainingIgnoreCase(fragmento);
    }

    /**
     * Busca canciones por género ignorando mayúsculas/minúsculas.
     *
     * @param genero nombre del género; si no tiene texto devuelve una lista vacía.
     * @return lista de canciones que pertenecen al género (posiblemente vacía).
     */
    @Override
    @Transactional(readOnly=true)
    public List<Cancion> findByGeneroIgnoreCase (String genero) {
        if (!StringUtils.hasText(genero)) {
            return new ArrayList<>(); // o lanzar IllegalArgumentException si prefieres
        }
        return cancionRepository.findByGeneroIgnoreCase(genero);
    }

    /**
     * Busca canciones por el nombre del artista.
     * <p>
     * Implementación simple que recorre todas las canciones y compara el
     * nombre de usuario del artista ignorando mayúsculas/minúsculas.
     * </p>
     *
     * @param nombreArtista nombre del artista; si no tiene texto devuelve lista vacía.
     * @return lista de canciones del artista (posiblemente vacía).
     */
    @Override
    @Transactional(readOnly = true)
    public List<Cancion> findbyArtistaName (String nombreArtista) {
        if (!StringUtils.hasText(nombreArtista)) {
            return new ArrayList<>(); // o lanzar IllegalArgumentException si prefieres
        }
        Iterable<Cancion> iterable = cancionRepository.findAll();
        List<Cancion> result = new ArrayList<>();
        for (Cancion cancion : iterable) {
            if (cancion.getArtista().getUsuario().getUsername().equalsIgnoreCase(nombreArtista)) {
                result.add(cancion);
            }
        }
        return result;
    }

    /**
     * Persiste o actualiza una canción.
     *
     * @param cancion entidad a guardar; si es {@code null} devuelve {@code null}.
     * @return la entidad persistida (puede incluir id y otros campos actualizados), o {@code null}.
     */
    @Override
    @Transactional
    public Cancion save(Cancion cancion) {
        if (cancion == null) {
            return null;
        }
        return cancionRepository.save(cancion);
    }

    /**
     * Elimina la canción con el identificador dado.
     *
     * @param id identificador de la canción a eliminar.
     */
    @Override
    @Transactional
    public void deleteById(Long id) {
        cancionRepository.deleteById(id);
    }

    /**
     * Comprueba si existe una canción con el identificador dado.
     *
     * @param id identificador a comprobar; si es {@code null} o menor o igual a 0 devuelve {@code false}.
     * @return {@code true} si existe, {@code false} en caso contrario.
     */
    @Override
    @Transactional
    public boolean existsById(Long id) {
        return cancionRepository.existsById(id);
    }


}
