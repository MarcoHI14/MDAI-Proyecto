package com.example.MDAI_Proyecto.data.repository;

import com.example.MDAI_Proyecto.data.model.SolicitudVerificacion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SolicitudVerificacionRepository extends CrudRepository<SolicitudVerificacion, Long> {

    Optional<List<SolicitudVerificacion>> findByEstado(String estado);

}
