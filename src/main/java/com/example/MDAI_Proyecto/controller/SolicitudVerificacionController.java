package com.example.MDAI_Proyecto.controller;

import com.example.MDAI_Proyecto.data.model.SolicitudVerificacion;
import com.example.MDAI_Proyecto.data.model.Usuario;
import com.example.MDAI_Proyecto.data.services.SolicitudVerificacionService;
import com.example.MDAI_Proyecto.data.services.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
public class SolicitudVerificacionController {

    private final SolicitudVerificacionService solicitudService;
    private final UsuarioService usuarioService;

    public SolicitudVerificacionController(SolicitudVerificacionService solicitudService, UsuarioService usuarioService) {
        this.solicitudService = solicitudService;
        this.usuarioService = usuarioService;
        System.out.println("\t SolicitudVerificacionController inicializado");
    }

    // Listar solicitudes pendientes para la vista de admin
    @GetMapping({"/AdminSolicitudes","/AdminSolicitudes.html"})
    public String listarSolicitudesAdmin(@RequestParam(name = "estado", required = false) String estado, Model model) {
        System.out.println("\t Recojo la petición de /AdminSolicitudes (SolicitudVerificacionController) estado=" + estado);

        String estadoSeleccionado = (estado == null || estado.trim().isEmpty()) ? "PENDIENTE" : estado.trim().toUpperCase();

        List<SolicitudVerificacion> lista;
        if ("ALL".equals(estadoSeleccionado) || "TODAS".equals(estadoSeleccionado)) {
            Optional<List<SolicitudVerificacion>> optAll = solicitudService.findAll();
            lista = optAll.orElse(Collections.emptyList());
            System.out.println("\t Se han cargado " + lista.size() + " solicitudes (todas)");
        } else {
            Optional<List<SolicitudVerificacion>> opt = solicitudService.findByEstado(estadoSeleccionado);
            lista = opt.orElse(Collections.emptyList());
            System.out.println("\t Se han cargado " + lista.size() + " solicitudes con estado=" + estadoSeleccionado);
        }

        model.addAttribute("solicitudes", lista);
        model.addAttribute("selectedEstado", estadoSeleccionado);
        return "AdminSolicitudes";
    }

    // Aceptar solicitud (simplificado)
    @PostMapping("/solicitudes/aceptar")
    public String aceptarSolicitud(@RequestParam Long id,
                                   @RequestParam(name = "estado", required = false) String estado) {
        System.out.println("\t Petición aceptar solicitud id=" + id + " estado=" + estado);
        SolicitudVerificacion s = solicitudService.findById(id);
        if (s != null) {
            s.setEstado("APROBADA");
            solicitudService.save(s);
        }
        String redirectEstado = (estado == null || estado.trim().isEmpty()) ? "PENDIENTE" : estado.trim();
        return "redirect:/AdminSolicitudes?estado=" + redirectEstado;
    }

    // Rechazar solicitud (simplificado)
    @PostMapping("/solicitudes/rechazar")
    public String rechazarSolicitud(@RequestParam Long id,
                                    @RequestParam(name = "estado", required = false) String estado) {
        System.out.println("\t Petición rechazar solicitud id=" + id + " estado=" + estado);
        SolicitudVerificacion s = solicitudService.findById(id);
        if (s != null) {
            s.setEstado("RECHAZADA");
            solicitudService.save(s);
        }
        String redirectEstado = (estado == null || estado.trim().isEmpty()) ? "PENDIENTE" : estado.trim();
        return "redirect:/AdminSolicitudes?estado=" + redirectEstado;
    }

    // Crear nueva solicitud desde BienvenidaUsuario
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
