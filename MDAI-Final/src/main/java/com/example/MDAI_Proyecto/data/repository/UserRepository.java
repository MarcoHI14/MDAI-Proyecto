package com.example.MDAI_Proyecto.data.repository;

import com.example.MDAI_Proyecto.data.model.Usuario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<Usuario, Long> {
    boolean findByUsername(String testeador2);
}