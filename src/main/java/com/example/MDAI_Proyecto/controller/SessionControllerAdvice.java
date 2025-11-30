package com.example.MDAI_Proyecto.controller;

import com.example.MDAI_Proyecto.data.model.Usuario;
import com.example.MDAI_Proyecto.data.services.UsuarioService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * Clase de asesoramiento de controlador para manejar atributos de sesión relacionados con el usuario.
 */
@ControllerAdvice
public class SessionControllerAdvice {

    private final UsuarioService usuarioService;

    /** Inyectar UsuarioService para operaciones relacionadas con usuarios */
    public SessionControllerAdvice(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Proporciona el atributo "sessionUsuario" para los controladores.
     * Obtiene el usuario actual de la sesión utilizando el ID almacenado en la sesión.
     * @return El objeto Usuario correspondiente al ID de la sesión, o null si no se encuentra.
     */
    @ModelAttribute("sessionUsuario")
    public Usuario sessionUsuario() {
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            Object idObj = attrs.getAttribute("id_usuario", RequestAttributes.SCOPE_SESSION);
            Long idUsuario = null;
            if (idObj instanceof Long l) {
                idUsuario = l;
            } else if (idObj instanceof Integer i) {
                idUsuario = i.longValue();
            } else if (idObj != null) {
                try {
                    idUsuario = Long.parseLong(idObj.toString());
                } catch (NumberFormatException ignored) {
                }
            }
            if (idUsuario != null) {
                try {
                    return usuarioService.obtenerUsuarioPorId(idUsuario);
                } catch (Exception ex) {
                    System.err.println("Error obteniendo usuario por id en SessionControllerAdvice: " + ex.getMessage());
                }
            }
        }
        return null;
    }
}
