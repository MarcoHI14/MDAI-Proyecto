package com.example.MDAI_Proyecto.data.services;

import com.example.MDAI_Proyecto.data.model.Artista;
import com.example.MDAI_Proyecto.data.model.SolicitudVerificacion;
import com.example.MDAI_Proyecto.data.model.Usuario;
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

    private final ArtistaService artistaService;
    private final UsuarioService usuarioService;

    /**
     * Constructor por inyección de dependencias.
     *
     * @param solicitudVerificacionRepository repositorio para persistencia
     * @param artistaService servicio de artista
     * @param usuarioService servicio de usuario
     */
    public SolicitudVerificacionServiceImplement(SolicitudVerificacionRepository solicitudVerificacionRepository,
                                                 ArtistaService artistaService,
                                                 UsuarioService usuarioService) {
        this.solicitudVerificacionRepository = solicitudVerificacionRepository;
        this.artistaService = artistaService;
        this.usuarioService = usuarioService;
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

    @Override
    @Transactional
    public void aprobarSolicitudYCrearArtista(Long idSolicitud) {
        SolicitudVerificacion s = solicitudVerificacionRepository.findById(idSolicitud).orElse(null);
        if (s == null) {
            throw new IllegalArgumentException("Solicitud con id " + idSolicitud + " no encontrada.");
        }
        // Solo proceder si está en PENDIENTE
        if (!"PENDIENTE".equalsIgnoreCase(s.getEstado())) {
            throw new IllegalStateException("La solicitud con id " + idSolicitud + " no está pendiente (estado=" + s.getEstado() + ")");
        }

        // Cambiar estado a APROBADA y persistir
        s.setEstado("APROBADA");
        solicitudVerificacionRepository.save(s);

        // Crear artista asociado si procede, dentro de la misma transacción
        try {
            Usuario u = s.getUsuario();
            if (u != null && artistaService != null) {
                Artista existing = artistaService.findById(u.getId());
                if (existing == null) {
                    Artista a = new Artista();
                    a.setUsuario(u);
                    a.setBiografia("");
                    artistaService.saveArtista(a);
                }
            }
        } catch (Exception ex) {
            // Lanzar runtime para que la transacción pueda decidir rollback si se desea
            throw new RuntimeException("Error al crear artista al aprobar solicitud: " + ex.getMessage(), ex);
        }
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
