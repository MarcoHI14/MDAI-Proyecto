package com.example.MDAI_Proyecto.data.repository;

import com.example.MDAI_Proyecto.data.model.Artista;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends CrudRepository<Artista, Long> {
}