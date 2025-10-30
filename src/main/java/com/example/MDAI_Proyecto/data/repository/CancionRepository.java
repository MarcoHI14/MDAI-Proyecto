package com.example.MDAI_Proyecto.data.repository;

/*PALABRAS CLAVE JPA
* findBy...

  existsBy...

  countBy...

  deleteBy...

  readBy...

  getBy...
  *
  *
  LOS METODOS QUE NO USEN DICHAS PALABRAS DEBERÁN IR ACOMPAÑADAS DE UNA ANOTACIÓN CON SU QUERY*/

import com.example.MDAI_Proyecto.data.model.Cancion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad {@link Cancion}.
 *
 * Proporciona métodos de consulta basados en las convenciones de nombres de
 * Spring Data JPA para operaciones comunes sobre canciones.
 *
 */
@Repository
public interface CancionRepository extends CrudRepository<Cancion, Long> {

    /**
     * Busca una {@link Cancion} por su identificador.
     *
     * @param id Identificador de la canción.
     * @return Optional que contiene la canción si existe, o vacío en caso contrario.
     */
    Optional<Cancion> findById(Long id);

    /**
     * Busca una {@link Cancion} por su título exacto.
     *
     * @param titulo Título de la canción a buscar.
     * @return Optional con la canción encontrada o vacío si no existe.
     */
    Optional<Cancion> findByTitulo(String titulo);

    /**
     * Recupera todas las canciones que coincidan con el género indicado.
     *
     * @param genero Género de las canciones a recuperar.
     * @return Lista (posiblemente vacía) de canciones con el género solicitado.
     */
    List<Cancion> findByGenero(String genero);

    /**
     * Recupera las canciones cuyo artista esté asociado al usuario con el
     * nombre de usuario indicado.
     * <p>
     * Navega la relación: {@code Cancion -> Artista -> Usuario -> username}.
     * </p>
     *
     * @param artista Nombre de usuario del artista (username).
     * @return Lista (posiblemente vacía) de canciones del artista indicado.
     */
    List<Cancion> findByArtista_Usuario_Username(String artista);

    /**
     * Recupera las canciones cuya duración coincida exactamente con el valor
     * proporcionado.
     *
     * @param duracion Duración en formato utilizado en la entidad (p. ej. "03:05").
     * @return Lista (posiblemente vacía) de canciones con la duración especificada.
     */
    List<Cancion> findByDuracion(String duracion);

    /**
     * Recupera las canciones cuya fecha de subida sea anterior a la fecha indicada.
     *
     * @param fechaSubida Límite superior (exclusive) de la fecha de subida.
     * @return Lista (posiblemente vacía) de canciones subidas antes de {@code fechaSubida}.
     */
    List<Cancion> findByFechaSubidaBefore(LocalDateTime fechaSubida);
}