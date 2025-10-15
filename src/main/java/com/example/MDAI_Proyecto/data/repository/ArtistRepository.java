package com.example.MDAI_Proyecto.data.repository;

import com.example.MDAI_Proyecto.data.model.Artista;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface ArtistRepository extends CrudRepository<Artista, Long> {
    Optional<Artista>findArtistByName(String name);
    boolean existsByUsername(String username);
    Optional<List<Artista>> findAllByOrderByUsernameAsc();
    Optional<List<Artista>> findAllByOrderByUsernameDesc();
    Optional<List<Artista>> filterByUsername(String username);
}