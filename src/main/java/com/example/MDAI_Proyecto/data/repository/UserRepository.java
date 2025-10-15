package com.example.MDAI_Proyecto.data.repository;

import com.example.MDAI_Proyecto.data.model.Usuario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
}