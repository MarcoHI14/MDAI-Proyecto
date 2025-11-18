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

import com.example.MDAI_Proyecto.data.model.Artista;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repositorio para la entidad Artista.
 * Proporciona métodos para realizar operaciones CRUD y consultas personalizadas.
 */
@Repository
public interface ArtistaRepository extends CrudRepository<Artista, Long> {

    /**
     * Busca un atista por el email del usuario asociado.
     * Ya que en la entidad Artista hay una relación @OneToOne con Usuario (campo `usuario`),
     * Spring Data JPA permite derivar la consulta por la propiedad anidada `usuario.email`.
     *
     * @param email el email del usuario asociado
     * @return Optional con el Artista si existe, vacío en caso contrario
     */
    Optional<Artista> findByUsuarioEmail(String email);

    /**
     * Busca un artista por el nombre de usuario del usuario asociado.
     * Utiliza la relación @OneToOne con Usuario (campo `usuario`) para derivar la consulta
     * por la propiedad anidada `usuario.username`.
     *
     * @param username el nombre de usuario del usuario asociado
     * @return Optional con el Artista si existe, vacío en caso contrario
     */
    Optional<Artista> findByUsuarioUsername(String username);
}