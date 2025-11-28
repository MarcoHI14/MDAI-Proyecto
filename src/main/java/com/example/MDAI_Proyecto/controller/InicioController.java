package com.example.MDAI_Proyecto.controller;

import com.example.MDAI_Proyecto.data.services.ArtistaService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

@Controller
public class InicioController {

    private final ArtistaService artistaService;

    // Inyectar ArtistaService para comprobar si el usuario es artista
    public InicioController(ArtistaService artistaService) {
        this.artistaService = artistaService;
        System.out.println("\t InicioController inicializado");
    }

    // Página principal
    @GetMapping("/")
    public String mostrarInicio() {
        System.out.println("\t Recojo la petición de inicio");
        return "Inicio"; // Thymeleaf buscará Inicio.html en /templates
    }

    // Página de inicio explícita
    @GetMapping({"/Inicio", "/Inicio.html"})
    public String mostrarInicioHtml() {
        System.out.println("\t Recojo la petición de /Inicio");
        return "Inicio";
    }

    // Página de registro
    @GetMapping({"/Registro","/Registro.html"})
    public String mostrarRegistroHtml() {
        System.out.println("\t Recojo la petición de /Registro");
        return "Registro";
    }

    // Página de información
    @GetMapping({"/Información", "/Información.html"})
    public String mostrarInformacionHtml() {
        System.out.println("\t Recojo la petición de /Informacion");
        return "Información";
    }

    // Página de inicio de sesión
    @GetMapping({"/InicioSesion", "/InicioSesion.html"})
    public String mostrarInicioSesionHtml() {
        System.out.println("\t Recojo la petición de /InicioSesion");
        return "InicioSesion";
    }

    // Página de administración
    // Nota: el mapeo de AdminSolicitudes se gestiona en SolicitudVerificacionController
    // para permitir cargar las solicitudes pendientes en el modelo.
    // (Se dejó el comportamiento en un controlador dedicado).

    // Página de bienvenida tras registro/inicio de sesión
    @GetMapping({"/Bienvenida", "/Bienvenida.html"})
    public String mostrarBienvenida() {
        System.out.println("\t Recojo la petición de /Bienvenida");
        return "BienvenidaUsuario";
    }

    // Link del logo: comprueba si hay sesión y redirige a Inicio, BienvenidaUsuario o BienvenidaArtista
    @GetMapping({"/home","/home.html","/logo"})
    public String logoHome() {
        System.out.println("\t Recojo la petición de /home (logo)");
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
                System.out.println("\t [SESSION] id_usuario encontrado en logoHome: " + idUsuario);
                try {
                    if (artistaService.findByUsuarioId(idUsuario) != null) {
                        return "redirect:/Artista";
                    } else {
                        return "redirect:/Bienvenida";
                    }
                } catch (Exception ex) {
                    System.err.println("Error comprobando artista en logoHome: " + ex.getMessage());
                    return "redirect:/Bienvenida";
                }
            }
        }
        System.out.println("\t [SESSION] no hay sesión en logoHome, redirigiendo a Inicio");
        return "redirect:/Inicio";
    }


}
