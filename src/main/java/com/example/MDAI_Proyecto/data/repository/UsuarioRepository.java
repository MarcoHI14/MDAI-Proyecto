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

import com.example.MDAI_Proyecto.data.model.Usuario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repositorio para la entidad Usuario.
 * Proporciona métodos para realizar operaciones CRUD y consultas personalizadas.
 */
@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Long> {
    /**
     * Busca un usuario por su nombre de usuario.
     *
     * @param username El nombre de usuario a buscar.
     * @return Un Optional que contiene el usuario si se encuentra, o vacío si no.
     */
    Optional<Usuario> findByUsername(String username);

    /**
     * Busca un usuario por su correo electrónico.
     *
     * @param email El correo electrónico a buscar.
     * @return Un Optional que contiene el usuario si se encuentra, o vacío si no.
     */
    Optional<Usuario> findByEmail(String email);

    /**
     * Busca un usuario por su ID.
     *
     * @param id El ID del usuario a buscar.
     * @return Un Optional que contiene el usuario si se encuentra, o vacío si no.
     */
    Optional<Usuario> findById(Long id);


}