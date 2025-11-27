package com.example.MDAI_Proyecto.data.services;

import com.example.MDAI_Proyecto.data.model.SolicitudVerificacion;

import java.util.List;
import java.util.Optional;

/**
 * Servicio encargado de gestionar las operaciones relacionadas con las
 * {@link SolicitudVerificacion}. Proporciona métodos para consultar,
 * almacenar, actualizar y eliminar solicitudes de verificación.
 */
public interface SolicitudVerificacionService {

    /**
     * Busca todas las solicitudes de verificación que coincidan con el estado proporcionado.
     *
     * @param estado Estado de la solicitud (por ejemplo: "PENDIENTE", "APROBADA", "RECHAZADA").
     * @return Un {@link Optional} que contiene la lista de solicitudes encontradas,
     *         o un Optional vacío si no hay resultados.
     */
    Optional<List<SolicitudVerificacion>> findByEstado(String estado);

    /**
     * Busca todas las solicitudes asociadas a un usuario por su id.
     */
    Optional<List<SolicitudVerificacion>> findByUsuarioId(Long usuarioId);

    /**
     * Busca solicitudes de un usuario que estén en alguno de los estados indicados.
     */
    Optional<List<SolicitudVerificacion>> findByUsuarioIdAndEstadoIn(Long usuarioId, List<String> estados);

    /**
     * Aprueba una solicitud y crea el artista asociado si procede, todo en una transacción.
     * @param idSolicitud id de la solicitud a aprobar
     */
    void aprobarSolicitudYCrearArtista(Long idSolicitud);

    /**
     * Obtiene todas las solicitudes de verificación registradas.
     *
     * @return Un {@link Optional} que contiene una lista con todas las solicitudes,
     *         o un Optional vacío si no existen registros.
     */
    Optional<List<SolicitudVerificacion>> findAll();

    /**
     * Guarda o actualiza una solicitud de verificación.
     *
     * @param solicitudVerificacion Objeto que representa la solicitud a almacenar.
     * @return La solicitud almacenada o actualizada.
     */
    SolicitudVerificacion save(SolicitudVerificacion solicitudVerificacion);

    /**
     * Busca una solicitud de verificación por su identificador.
     *
     * @param id Identificador único de la solicitud.
     * @return La solicitud encontrada.
     * @throws org.springframework.dao.EmptyResultDataAccessException si no existe una solicitud con el ID dado.
     */
    SolicitudVerificacion findById(Long id);

    /**
     * Elimina una solicitud de verificación utilizando su identificador.
     *
     * @param id Identificador de la solicitud a eliminar.
     */
    void deleteById(Long id);

    /**
     * Verifica si existe una solicitud de verificación con el ID especificado.
     *
     * @param id Identificador a comprobar.
     * @return true si existe, false en caso contrario.
     */
    boolean existsById(Long id);
}
