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

import com.example.MDAI_Proyecto.data.model.Playlist;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para la entidad Playlist.
 * Proporciona métodos para realizar operaciones CRUD y consultas personalizadas.
 */
@Repository
public interface PlaylistRepository extends CrudRepository<Playlist, Long> {

    /**
     * Busca una playlist por su nombre.
     *
     * @param nombre el nombre de la playlist
     * @return Optional con la Playlist si existe, vacío en caso contrario
     */
    Optional<Playlist> findByNombre(String nombre);
    /**
     * Busca una playlist por el nombre de usuario del usuario asociado.
     * Utiliza la relación con Usuario (campo `usuario`) para derivar la consulta
     * por la propiedad anidada `usuario.username`.
     *
     * @param nombre el nombre de usuario del usuario asociado
     * @return Optional con la Playlist si existe, vacío en caso contrario
     */
    Optional<Playlist> findByUsuario_Username(String nombre);
}