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

import java.util.Optional;

@Repository
public interface CancionRepository extends CrudRepository<Cancion, Long> {
    Optional<Cancion> findByTitulo(String titulo);
    Optional<Cancion> findByGenero(String genero);

    //Artista.getUsuario().getUsername()
    Optional<Cancion> findByArtista_Usuario_Username(String artista);
}