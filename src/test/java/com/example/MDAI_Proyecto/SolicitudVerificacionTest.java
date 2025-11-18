package com.example.MDAI_Proyecto;
import com.example.MDAI_Proyecto.data.model.SolicitudVerificacion;
import com.example.MDAI_Proyecto.data.model.Usuario;
import com.example.MDAI_Proyecto.data.repository.SolicitudVerificacionRepository;
import com.example.MDAI_Proyecto.data.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
public class SolicitudVerificacionTest {

    @Autowired
    private SolicitudVerificacionRepository solicitudVerificacionRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    /** Prueba para verificar que el repositorio de SolicitudVerificacion no es nulo y funciona correctamente */
    @Test
    public void testSolicitudVerificacionRepositoryIsNotNull() {
        Usuario usuario = new Usuario();
        usuario.setUsername("LaDamaSeEsconde");
        usuario.setPassword("password123");
        usuario.setEmail("ladamaseesconde@gmail.es");
        usuarioRepository.save(usuario);

        SolicitudVerificacion solicitudVerificacion = new SolicitudVerificacion();
        solicitudVerificacion.setUsuario(usuario);
        solicitudVerificacion.setMensajeVerificacion("Quiero verificar mi cuenta porque soy un artista reconocido.");
        solicitudVerificacion.setEstado("pendiente");
        solicitudVerificacionRepository.save(solicitudVerificacion);
        // Verificar que el repositorio no es nulo y que la solicitud se ha guardado correctamente
        assertNotNull(solicitudVerificacionRepository);
        assertTrue(solicitudVerificacionRepository.existsById(solicitudVerificacion.getIdSolicitud()));
        assertEquals("pendiente", solicitudVerificacion.getEstado());
        assertEquals("Quiero verificar mi cuenta porque soy un artista reconocido.", solicitudVerificacion.getMensajeVerificacion());

        //Limpiar repositorios
        solicitudVerificacionRepository.deleteAll();
        usuarioRepository.deleteAll();
    }

    /** Prueba CRUD completa para la entidad SolicitudVerificacion */
    @Test
    public void testSolicitudVerificacionCRUD() {
        // Crear y guardar un usuario
        Usuario usuario = new Usuario();
        usuario.setUsername("El sueño de Morfeo");
        usuario.setPassword("morfeo123");
        usuario.setEmail("elmorfe@gmai.com");
        usuarioRepository.save(usuario);

        // Crear y guardar una solicitud de verificación
        SolicitudVerificacion solicitud = new SolicitudVerificacion();
        solicitud.setUsuario(usuario);
        solicitud.setMensajeVerificacion("Soy un artista famoso y quiero verificar mi cuenta.");
        solicitud.setEstado("pendiente");
        solicitudVerificacionRepository.save(solicitud);

        // Leer la solicitud guardada
        SolicitudVerificacion solicitudLeida = solicitudVerificacionRepository.findById(solicitud.getIdSolicitud()).orElse(null);
        assertNotNull(solicitudLeida);
        assertEquals("pendiente", solicitudLeida.getEstado());
        assertEquals("Soy un artista famoso y quiero verificar mi cuenta.", solicitudLeida.getMensajeVerificacion());
        assertEquals("El sueño de Morfeo", solicitudLeida.getUsuario().getUsername());

        // Actualizar el estado de la solicitud
        solicitudLeida.setEstado("aprobada");
        solicitudVerificacionRepository.save(solicitudLeida);
        SolicitudVerificacion solicitudActualizada = solicitudVerificacionRepository.findById(solicitud.getIdSolicitud()).orElse(null);
        assertNotNull(solicitudActualizada);
        assertEquals("aprobada", solicitudActualizada.getEstado());

        // Eliminar la solicitud
        solicitudVerificacionRepository.delete(solicitudActualizada);
        SolicitudVerificacion solicitudEliminada = solicitudVerificacionRepository.findById(solicitud.getIdSolicitud()).orElse(null);
        assertNull(solicitudEliminada);

        //Limpiar repositorios
        usuarioRepository.deleteAll();

    }

    /** Prueba para verificar que el repositorio de SolicitudVerificacion está vacío al inicio */
    @Test
    public void testSolicitudVerificacionRepositoryIsEmpty() {
        assertNotNull(solicitudVerificacionRepository);
        assertEquals(0, solicitudVerificacionRepository.count());
    }

    /** Prueba para verificar la eliminación en cascada de SolicitudVerificacion al eliminar un Usuario */
    @Test
    public void testCascadeDeleteUsuarioAndSolicitudVerificacion() {
        Usuario usuario = new Usuario();
        usuario.setUsername("Amaral");
        usuario.setPassword("amaral123");
        usuario.setEmail("amar@gmail.com");
        usuarioRepository.save(usuario);

        SolicitudVerificacion solicitud = new SolicitudVerificacion();
        solicitud.setUsuario(usuario);
        solicitud.setMensajeVerificacion("Quiero verificar mi cuenta porque soy un artista reconocido.");
        solicitud.setEstado("pendiente");
        // Mantener la relación bidireccional: añadir la solicitud a la colección del usuario
        usuario.addSolicitud(solicitud);


        solicitudVerificacionRepository.save(solicitud);
        Long solicitudId = solicitud.getIdSolicitud();
        Long usuarioId = usuario.getId();
        assertTrue(solicitudVerificacionRepository.existsById(solicitudId));

        // Eliminar el usuario y verificar que la solicitud también se elimina
        usuarioRepository.delete(usuario);
        assertFalse(solicitudVerificacionRepository.existsById(solicitudId));
        assertFalse(usuarioRepository.existsById(usuarioId));

        //Limpiar repositorios
        solicitudVerificacionRepository.deleteAll();
        usuarioRepository.deleteAll();
    }

    /** Prueba para eliminar una solicitud de verificación con estado "denegada" */
    @Test
    public void deleteDeniedSolicitudVerificacion() {
        Usuario usuario = new Usuario();
        usuario.setUsername("Vetusta Morla");
        usuario.setPassword("vetusta123");
        usuario.setEmail("vetu@morla.com");
        usuarioRepository.save(usuario);
        SolicitudVerificacion solicitud = new SolicitudVerificacion();
        solicitud.setUsuario(usuario);
        solicitud.setMensajeVerificacion("Quiero verificar mi cuenta porque soy un artista reconocido.");
        solicitud.setEstado("denegada");
        solicitudVerificacionRepository.save(solicitud);
        Long solicitudId = solicitud.getIdSolicitud();
        // Buscar solicitudes de estado "denegada"
        List<SolicitudVerificacion> denegadas = solicitudVerificacionRepository.findByEstado("denegada").orElse(Collections.emptyList());
        assertFalse(denegadas.isEmpty());
        SolicitudVerificacion solicitudDenegada = denegadas.get(0);
        assertNotNull(solicitudDenegada);
        assertEquals(solicitudId, solicitudDenegada.getIdSolicitud());
        //Eliminar la solicitud denegada
        solicitudVerificacionRepository.delete(solicitudDenegada);
        assertFalse(solicitudVerificacionRepository.existsById(solicitudId));
        //Limpiar repositorios
        solicitudVerificacionRepository.deleteAll();
        usuarioRepository.deleteAll();
    }

    /** Prueba para buscar una solicitud de verificación por su estado */
    @Test
    public  void findByEstadoTest() {
        Usuario usuario = new Usuario();
        usuario.setUsername("Izal");
        usuario.setPassword("izal123");
        usuario.setEmail("iz@mail.com");
        usuarioRepository.save(usuario);

        SolicitudVerificacion solicitud = new SolicitudVerificacion();
        solicitud.setUsuario(usuario);
        solicitud.setMensajeVerificacion("Quiero verificar mi cuenta porque soy un artista reconocido.");
        solicitud.setEstado("aprobada");
        solicitudVerificacionRepository.save(solicitud);

        SolicitudVerificacion solicitud2 = new SolicitudVerificacion();
        solicitud2.setUsuario(usuario);
        solicitud2.setMensajeVerificacion("Otra solicitud de verificación.");
        solicitud2.setEstado("pendiente");
        solicitudVerificacionRepository.save(solicitud2);

        Usuario usuario2 = new Usuario();
        usuario2.setUsername("Supersubmarina");
        usuario2.setPassword("super123");
        usuario2.setEmail("sup@gmail.com");
        usuarioRepository.save(usuario2);

        SolicitudVerificacion solicitud3 = new SolicitudVerificacion();
        solicitud3.setUsuario(usuario2);
        solicitud3.setMensajeVerificacion("Quiero verificar mi cuenta porque soy un artista reconocido.");
        solicitud3.setEstado("aprobada");
        solicitudVerificacionRepository.save(solicitud3);

        // Buscar solicitudes de estado "aprobada"
        List<SolicitudVerificacion> aprobadas = solicitudVerificacionRepository.findByEstado("aprobada").orElse(Collections.emptyList());
        assertEquals(2, aprobadas.size());
        //Limpiar repositorios
        solicitudVerificacionRepository.deleteAll();
        usuarioRepository.deleteAll();
    }
}
