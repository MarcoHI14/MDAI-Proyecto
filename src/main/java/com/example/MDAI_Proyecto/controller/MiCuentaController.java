package com.example.MDAI_Proyecto.controller;


import com.example.MDAI_Proyecto.data.model.SolicitudVerificacion;
import com.example.MDAI_Proyecto.data.model.Usuario;
import com.example.MDAI_Proyecto.data.services.SolicitudVerificacionService;
import com.example.MDAI_Proyecto.data.services.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Controlador para manejar las solicitudes relacionadas con la cuenta del usuario.
 */
@Controller
@RequestMapping("/Micuenta")
public class MiCuentaController {

    private final UsuarioService usuarioService;
    private final SolicitudVerificacionService solicitudVerificacionService;

    /** Inyectar servicios necesarios para operaciones relacionadas con usuarios y solicitudes de verificación */
    public MiCuentaController(UsuarioService usuarioService, SolicitudVerificacionService solicitudVerificacionService) {
        this.usuarioService = usuarioService;
        this.solicitudVerificacionService = solicitudVerificacionService;
        System.out.println("\t UsuarioController inicializado");
    }

    /**
     * Página de "Mi Cuenta"
     * @param model
     * @return
     */
    @GetMapping
    public String mostrarMiCuenta(Model model) {
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            return "redirect:/InicioSesion";
        }
        Object idObj = attrs.getAttribute("id_usuario", RequestAttributes.SCOPE_SESSION);
        if (!(idObj instanceof Long)) {
            return "redirect:/InicioSesion";
        }

        Usuario usuario = usuarioService.obtenerUsuarioPorId((Long) idObj);
        if (usuario == null) {
            return "redirect:/InicioSesion";
        }

        // Determinar el tipo de usuario según si tiene un Artista asignado
        String tipoUsuario = (usuario.getArtista() != null) ? "Artista" : "Oyente";

        // Obtener la última solicitud de verificación
        List<SolicitudVerificacion> solicitudes = usuario.getSolicitudes();
        String estadoVerificacion = "Sin verificar"; // Por defecto

        if (!solicitudes.isEmpty()) {
            // Obtener la última solicitud (asumiendo que están ordenadas cronológicamente)
            SolicitudVerificacion ultimaSolicitud = solicitudes.getLast();
            estadoVerificacion = ultimaSolicitud.getEstado();
        }

        // Pasar datos al modelo
        model.addAttribute("usuario", usuario);
        model.addAttribute("tipoUsuario", tipoUsuario);
        model.addAttribute("estadoVerificacion", estadoVerificacion);

        return "MiCuenta";
    }

    /**
     * Maneja la solicitud para cambiar el nombre de usuario.
     * @param nuevoUsername El nuevo nombre de usuario proporcionado por el usuario.
     * @param redirectAttributes Atributos para pasar mensajes flash en redirecciones.
     * @return Redirige a la página de "Mi Cuenta" con un mensaje de éxito o error.
     */
    @PostMapping("/cambiarUsername")
    public String cambiarUsername(@RequestParam("nuevoUsername") String nuevoUsername,
                                  RedirectAttributes redirectAttributes) {
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión.");
            return "redirect:/InicioSesion";
        }
        Object idObj = attrs.getAttribute("id_usuario", RequestAttributes.SCOPE_SESSION);
        if (!(idObj instanceof Long)) {
            redirectAttributes.addFlashAttribute("error", "Sesión inválida.");
            return "redirect:/InicioSesion";
        }
        Long usuarioId = (Long) idObj;

        try {
            Usuario u = usuarioService.obtenerUsuarioPorId(usuarioId);
            u.setUsername(nuevoUsername);
            usuarioService.guardarUsuario(u);
            redirectAttributes.addFlashAttribute("mensaje", "Username actualizado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al cambiar username: " + e.getMessage());
        }
        return "redirect:/Micuenta";
    }

    /**
     * Maneja la solicitud para cambiar la contraseña del usuario.
     * @param passwordActual La contraseña actual del usuario.
     * @param nuevaPassword La nueva contraseña que el usuario desea establecer.
     * @param confirmarPassword La confirmación de la nueva contraseña.
     * @param redirectAttributes Atributos para pasar mensajes flash en redirecciones.
     * @return Redirige a la página de "Mi Cuenta" con un mensaje de éxito o error.
     */
    @PostMapping("/cambiarPassword")
    public String cambiarPassword(@RequestParam("passwordActual") String passwordActual,
                                  @RequestParam("nuevaPassword") String nuevaPassword,
                                  @RequestParam("confirmarPassword") String confirmarPassword,
                                  RedirectAttributes redirectAttributes) {
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión.");
            return "redirect:/InicioSesion";
        }
        Object idObj = attrs.getAttribute("id_usuario", RequestAttributes.SCOPE_SESSION);
        if (!(idObj instanceof Long)) {
            redirectAttributes.addFlashAttribute("error", "Sesión inválida.");
            return "redirect:/InicioSesion";
        }
        Long usuarioId = (Long) idObj;

        if (!nuevaPassword.equals(confirmarPassword)) {
            redirectAttributes.addFlashAttribute("error", "Las contraseñas no coinciden");
            return "redirect:/Micuenta";
        }

        try {
            Usuario u = usuarioService.obtenerUsuarioPorId(usuarioId);
            if (!u.getPassword().equals(passwordActual)) {
                redirectAttributes.addFlashAttribute("error", "La contraseña actual es incorrecta");
                return "redirect:/Micuenta";
            }
            u.setPassword(nuevaPassword);
            usuarioService.guardarUsuario(u);
            redirectAttributes.addFlashAttribute("mensaje", "Contraseña actualizada correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al cambiar contraseña: " + e.getMessage());
        }
        return "redirect:/Micuenta";
    }

    /**
     * Maneja la solicitud para enviar una solicitud de verificación de artista.
     * @param nombreArtistico El nombre artístico proporcionado por el usuario.
     * @param nacionalidad La nacionalidad del usuario.
     * @param biografia La biografía del usuario.
     * @param redirectAttributes Atributos para pasar mensajes flash en redirecciones.
     * @return Redirige a la página de "Mi Cuenta" con un mensaje de éxito o error.
     */
    @PostMapping("/solicitarVerificacion")
    public String solicitarVerificacion(@RequestParam("nombreArtistico") String nombreArtistico,
                                        @RequestParam("nacionalidad") String nacionalidad,
                                        @RequestParam("biografia") String biografia,
                                        RedirectAttributes redirectAttributes) {
        System.out.println("\t Petición solicitar verificación desde Mi Cuenta");

        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión.");
            return "redirect:/InicioSesion";
        }
        Object idObj = attrs.getAttribute("id_usuario", RequestAttributes.SCOPE_SESSION);
        if (!(idObj instanceof Long)) {
            redirectAttributes.addFlashAttribute("error", "Sesión inválida.");
            return "redirect:/InicioSesion";
        }
        Long idUsuario = (Long) idObj;

        Usuario usuario = usuarioService.obtenerUsuarioPorId(idUsuario);
        if (usuario == null) {
            redirectAttributes.addFlashAttribute("error", "Usuario no encontrado en sesión.");
            return "redirect:/InicioSesion";
        }

        try {
            List<String> estadosProhibidos = Arrays.asList("PENDIENTE", "APROBADA");
            Optional<List<SolicitudVerificacion>> ya = solicitudVerificacionService.findByUsuarioIdAndEstadoIn(idUsuario, estadosProhibidos);
            if (ya.isPresent() && !ya.get().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Ya existe una solicitud activa. No puedes enviar otra.");
                return "redirect:/Micuenta";
            }
        } catch (Exception ex) {
            System.err.println("Error comprobando solicitudes previas: " + ex.getMessage());
            redirectAttributes.addFlashAttribute("error", "No se ha podido comprobar solicitudes previas.");
            return "redirect:/Micuenta";
        }

        if (biografia == null || biografia.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Rellena todos los campos antes de enviar.");
            return "redirect:/Micuenta";
        }

        SolicitudVerificacion s = new SolicitudVerificacion();
        s.setUsuario(usuario);
        s.setMensajeVerificacion(String.format("Nombre: %s | Nacionalidad: %s | Bio: %s",
                nombreArtistico, nacionalidad, biografia.trim()));
        s.setFechaSolicitud(LocalDateTime.now());
        s.setEstado("PENDIENTE");

        SolicitudVerificacion saved = solicitudVerificacionService.save(s);
        if (saved != null && saved.getIdSolicitud() != null) {
            redirectAttributes.addFlashAttribute("success", "Solicitud enviada correctamente.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Error al crear la solicitud.");
        }
        return "redirect:/Micuenta";
    }

}
