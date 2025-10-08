package com.example.MDAI_Proyecto.data.repository;

import com.example.MDAI_Proyecto.data.model.Cancion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CancionRepository extends CrudRepository<Cancion, Long> {
}