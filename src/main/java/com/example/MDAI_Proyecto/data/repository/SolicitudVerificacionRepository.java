package com.example.MDAI_Proyecto.data.repository;

import com.example.MDAI_Proyecto.data.model.SolicitudVerificacion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad SolicitudVerificacion.
 * Proporciona métodos para realizar operaciones CRUD y consultas personalizadas.
 */
@Repository
public interface SolicitudVerificacionRepository extends CrudRepository<SolicitudVerificacion, Long> {

    /**
     * Busca todas las solicitudes de verificación por su estado.
     *
     * @param estado el estado de la solicitud de verificación
     * @return Optional con la lista de SolicitudVerificacion si existen, vacío en caso contrario
     */
    Optional<List<SolicitudVerificacion>> findByEstado(String estado);

}
