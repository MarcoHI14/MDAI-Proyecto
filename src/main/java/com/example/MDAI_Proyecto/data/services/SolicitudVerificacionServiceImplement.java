package com.example.MDAI_Proyecto.data.services;

import com.example.MDAI_Proyecto.data.model.SolicitudVerificacion;
import com.example.MDAI_Proyecto.data.repository.SolicitudVerificacionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio para gestionar {@link SolicitudVerificacion}.
 * <p>
 * Esta clase delega en {@link SolicitudVerificacionRepository} las operaciones
 * de persistencia y aplica transaccionalidad mediante las anotaciones
 * {@link org.springframework.transaction.annotation.Transactional}.
 * </p>
 */
@Service
public class SolicitudVerificacionServiceImplement implements SolicitudVerificacionService {

    /**
     * Repositorio usado para operaciones CRUD sobre solicitudes de verificación.
     * Se marca como {@code private final} para reforzar inmutabilidad.
     */
    private final SolicitudVerificacionRepository solicitudVerificacionRepository;

    /**
     * Constructor por inyección de dependencias.
     *
     * @param solicitudVerificacionRepository repositorio para persistencia
     */
    public SolicitudVerificacionServiceImplement(SolicitudVerificacionRepository solicitudVerificacionRepository) {
        this.solicitudVerificacionRepository = solicitudVerificacionRepository;
    }

    /**
     * Busca una lista de solicitudes por su estado.
     *
     * @param estado estado por el que filtrar
     * @return Optional con la lista de {@link SolicitudVerificacion} que cumplen el estado,
     *         o {@link Optional#empty()} si el repositorio devuelve {@code null}
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<List<SolicitudVerificacion>> findByEstado(String estado) {
        return solicitudVerificacionRepository.findByEstado(estado);
    }

    /**
     * Busca una solicitud por su identificador.
     *
     * @param id identificador de la solicitud
     * @return la {@link SolicitudVerificacion} encontrada o {@code null} si no existe.
     *         Considerar cambiar la firma a {@link Optional} en la interfaz para
     *         evitar valores nulos.
     */
    @Override
    @Transactional(readOnly = true)
    public SolicitudVerificacion findById(Long id) {
        return solicitudVerificacionRepository.findById(id).orElse(null);
    }

    /**
     * Recupera todas las solicitudes de verificación.
     *
     * @return Optional con la lista de todas las solicitudes (posible lista vacía),
     *         nunca devuelve {@code null}
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<List<SolicitudVerificacion>> findAll() {
        Iterable<SolicitudVerificacion> iterable = solicitudVerificacionRepository.findAll();
        List<SolicitudVerificacion> list = new ArrayList<>();
        iterable.forEach(list::add);
        return Optional.of(list);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<List<SolicitudVerificacion>> findByUsuarioId(Long usuarioId) {
        return solicitudVerificacionRepository.findByUsuarioId(usuarioId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<List<SolicitudVerificacion>> findByUsuarioIdAndEstadoIn(Long usuarioId, List<String> estados) {
        return solicitudVerificacionRepository.findByUsuarioIdAndEstadoIn(usuarioId, estados);
    }

    /**
     * Guarda una solicitud de verificación.
     *
     * @param solicitudVerificacion entidad a guardar; si es {@code null} no se guarda y se devuelve {@code null}
     * @return la entidad persistida o {@code null} si el parámetro era {@code null}
     */
    @Override
    @Transactional
    public SolicitudVerificacion save(SolicitudVerificacion solicitudVerificacion) {
        if (solicitudVerificacion == null) {
            return null;
        }
        return solicitudVerificacionRepository.save(solicitudVerificacion);
    }

    /**
     * Elimina una solicitud por su identificador.
     *
     * @param id identificador de la solicitud a eliminar
     */
    @Override
    @Transactional
    public void deleteById(Long id) {
        solicitudVerificacionRepository.deleteById(id);
    }

    /**
     * Comprueba si existe una solicitud con el identificador indicado.
     *
     * @param id identificador a comprobar
     * @return {@code true} si existe, {@code false} en caso contrario
     */
    @Override
    @Transactional
    public boolean existsById(Long id) {
        return solicitudVerificacionRepository.existsById(id);
    }

}
