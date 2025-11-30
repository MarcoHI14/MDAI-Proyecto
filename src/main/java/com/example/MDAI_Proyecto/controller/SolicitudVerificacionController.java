package com.example.MDAI_Proyecto.controller;

import com.example.MDAI_Proyecto.data.model.SolicitudVerificacion;
import com.example.MDAI_Proyecto.data.model.Usuario;
import com.example.MDAI_Proyecto.data.services.SolicitudVerificacionService;
import com.example.MDAI_Proyecto.data.services.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.time.LocalDateTime;
import java.util.Optional;
/**
 * Controlador para manejar las solicitudes de verificación de usuarios.
 */
@Controller
public class SolicitudVerificacionController {

    private final SolicitudVerificacionService solicitudService;
    private final UsuarioService usuarioService;
    /** Inyectar los servicios necesarios */
    public SolicitudVerificacionController(SolicitudVerificacionService solicitudService, UsuarioService usuarioService) {
        this.solicitudService = solicitudService;
        this.usuarioService = usuarioService;
        System.out.println("\t SolicitudVerificacionController inicializado");
    }

    /**
     * Redirigir rutas antiguas de AdminSolicitudes a la nueva ruta agrupada.
      * @return
     */
    @org.springframework.web.bind.annotation.GetMapping({"/AdminSolicitudes","/AdminSolicitudes.html"})
    public String redirectOldAdminSolicitudes() {
        return "redirect:/admin/AdminSolicitudes";
    }

    /**
     * Aprobar solicitud y crear artista si procede
      * @param id
     * @param estado
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/solicitudes/aceptar")
    public String aceptarSolicitud(@RequestParam Long id,
                                   @RequestParam(name = "estado", required = false) String estado,
                                   RedirectAttributes redirectAttributes) {
        System.out.println("\t Petición aceptar solicitud id=" + id + " estado=" + estado);
        try {
            // Verificar que la solicitud está en estado PENDIENTE
            com.example.MDAI_Proyecto.data.model.SolicitudVerificacion s = solicitudService.findById(id);
            if (s == null) {
                redirectAttributes.addFlashAttribute("error", "Solicitud no encontrada.");
            } else if (!"PENDIENTE".equalsIgnoreCase(s.getEstado())) {
                redirectAttributes.addFlashAttribute("error", "Solo se pueden aceptar solicitudes pendientes.");
                System.out.println("\t Intento de aceptar solicitud con estado=" + s.getEstado());
            } else {
                solicitudService.aprobarSolicitudYCrearArtista(id);
                System.out.println("\t Solicitud aprobada y se intentó crear artista (si procedía)");
                redirectAttributes.addFlashAttribute("success", "Solicitud aprobada correctamente.");
            }
        } catch (Exception ex) {
            System.err.println("Error al aprobar solicitud y crear artista: " + ex.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error al procesar la solicitud: " + ex.getMessage());
        }
        String redirectEstado = (estado == null || estado.trim().isEmpty()) ? "PENDIENTE" : estado.trim();
        return "redirect:/admin/AdminSolicitudes?estado=" + redirectEstado; // ir a la ruta agrupada
    }

    /**
     * Rechazar solicitud
      * @param id
     * @param estado
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/solicitudes/rechazar")
    public String rechazarSolicitud(@RequestParam Long id,
                                     @RequestParam(name = "estado", required = false) String estado,
                                     RedirectAttributes redirectAttributes) {
        System.out.println("\t Petición rechazar solicitud id=" + id + " estado=" + estado);
        com.example.MDAI_Proyecto.data.model.SolicitudVerificacion s = solicitudService.findById(id);
        if (s == null) {
            redirectAttributes.addFlashAttribute("error", "Solicitud no encontrada.");
        } else if (!"PENDIENTE".equalsIgnoreCase(s.getEstado())) {
            redirectAttributes.addFlashAttribute("error", "Solo se pueden rechazar solicitudes pendientes.");
            System.out.println("\t Intento de rechazar solicitud con estado=" + s.getEstado());
        } else {
            s.setEstado("RECHAZADA");
            solicitudService.save(s);
            redirectAttributes.addFlashAttribute("success", "Solicitud rechazada correctamente.");
        }
        String redirectEstado = (estado == null || estado.trim().isEmpty()) ? "PENDIENTE" : estado.trim();
        return "redirect:/admin/AdminSolicitudes?estado=" + redirectEstado; // ir a la ruta agrupada
    }

    /**
     * Crear nueva solicitud de verificación
      * @param descripcion
     * @param tipo
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/solicitudes/crear")
    public String crearSolicitud(@RequestParam(name = "descripcion", required = false) String descripcion,
                                 @RequestParam(name = "tipo", required = false) String tipo,
                                 RedirectAttributes redirectAttributes) {
        System.out.println("\t Petición crear solicitud tipo=" + tipo + " descripcion=" + (descripcion != null ? descripcion.substring(0, Math.min(40, descripcion.length())) : "(vacía)"));

        // Obtener id de usuario de la sesión
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión para solicitar verificación.");
            return "redirect:/InicioSesion";
        }
        Object idObj = attrs.getAttribute("id_usuario", RequestAttributes.SCOPE_SESSION);
        if (!(idObj instanceof Long)) {
            redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión para solicitar verificación.");
            return "redirect:/InicioSesion";
        }
        Long idUsuario = (Long) idObj;

        Usuario usuario = usuarioService.obtenerUsuarioPorId(idUsuario);
        if (usuario == null) {
            redirectAttributes.addFlashAttribute("error", "Usuario no encontrado en sesión.");
            return "redirect:/InicioSesion";
        }

        // Comprobar si el usuario ya tiene solicitudes en estado PENDIENTE o APROBADA
        try {
            java.util.List<String> estadosProhibidos = java.util.Arrays.asList("PENDIENTE", "APROBADA");
            Optional<java.util.List<com.example.MDAI_Proyecto.data.model.SolicitudVerificacion>> ya = solicitudService.findByUsuarioIdAndEstadoIn(idUsuario, estadosProhibidos);
            if (ya.isPresent() && !ya.get().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Ya existe una solicitud activa (pendiente o aprobada). No puedes enviar otra.");
                return "redirect:/Bienvenida";
            }
        } catch (Exception ex) {
            System.err.println("Error comprobando solicitudes previas: " + ex.getMessage());
            // si hay un fallo en la comprobación, proceder con precaución y evitar crear duplicados
            redirectAttributes.addFlashAttribute("error", "No se ha podido comprobar solicitudes previas. Inténtalo más tarde.");
            return "redirect:/Bienvenida";
        }

        if (descripcion == null || descripcion.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Rellena la descripción antes de enviar.");
            return "redirect:/Bienvenida";
        }

        SolicitudVerificacion s = new SolicitudVerificacion();
        s.setUsuario(usuario);
        s.setMensajeVerificacion(descripcion.trim());
        s.setFechaSolicitud(LocalDateTime.now());
        s.setEstado("PENDIENTE");

        SolicitudVerificacion saved = solicitudService.save(s);
        if (saved != null && saved.getIdSolicitud() != null) {
            redirectAttributes.addFlashAttribute("success", "Solicitud enviada correctamente.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Error al crear la solicitud. Inténtalo de nuevo.");
        }
        return "redirect:/Bienvenida";
    }

}
