package com.example.MDAI_Proyecto.controller;

import com.example.MDAI_Proyecto.data.services.ArtistaService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * Controlador para manejar las solicitudes de las páginas principales como Inicio, Registro, Información, etc.
 */
@Controller
public class InicioController {

    private final ArtistaService artistaService;

    /**
     * Constructor que inyecta el servicio de artista.
     * @param artistaService Servicio para operaciones relacionadas con artistas.
     */
    public InicioController(ArtistaService artistaService) {
        this.artistaService = artistaService;
        System.out.println("\t InicioController inicializado");
    }

    /**
     * Página de inicio (raíz "/")
      * @return
     */
    @GetMapping("/")
    public String mostrarInicio() {
        System.out.println("\t Recojo la petición de inicio");
        return "Inicio"; // Thymeleaf buscará Inicio.html en /templates
    }

    /**
     * Mapea las solicitudes GET para "/Inicio" y "/Inicio.html".
     * @return Nombre de la vista "Inicio".
     */
    @GetMapping({"/Inicio", "/Inicio.html"})
    public String mostrarInicioHtml() {
        System.out.println("\t Recojo la petición de /Inicio");
        return "Inicio";
    }

    /**
     * Página de registro
      * @return
     */
    @GetMapping({"/Registro","/Registro.html"})
    public String mostrarRegistroHtml() {
        System.out.println("\t Recojo la petición de /Registro");
        return "Registro";
    }

    /**
     * Página de información
      * @return
     */
    @GetMapping({"/Información", "/Información.html"})
    public String mostrarInformacionHtml() {
        System.out.println("\t Recojo la petición de /Informacion");
        return "Información";
    }

    /** Página de inicio de sesión
      * @return
     */
    @GetMapping({"/InicioSesion", "/InicioSesion.html"})
    public String mostrarInicioSesionHtml() {
        System.out.println("\t Recojo la petición de /InicioSesion");
        return "InicioSesion";
    }

    /**
     * Página de bienvenida para usuarios
      * @return
     */
    @GetMapping({"/Bienvenida", "/Bienvenida.html"})
    public String mostrarBienvenida() {
        System.out.println("\t Recojo la petición de /Bienvenida");
        return "BienvenidaUsuario";
    }

    /**
     * Página de logo/home que redirige según el estado de sesión
      * @return
     */
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
