package package com.example.MDAI_Proyecto.data.repository;

import com.example.MDAI_PROYECTO.data.model.Cancion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CancionRepository extends CrudRepository<User, Long> {
}