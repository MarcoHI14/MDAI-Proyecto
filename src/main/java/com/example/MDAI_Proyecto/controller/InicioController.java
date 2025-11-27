package com.example.MDAI_Proyecto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

@Controller
public class InicioController {

    public InicioController() {
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
    @GetMapping({"/AdminSolicitudes", "/AdminSolicitudes.html"})
    public String mostrarAdminSolicitudesHtml() {
        System.out.println("\t Recojo la petición de /AdminSolicitudes");
        return "AdminSolicitudes";
    }

    // Página de bienvenida tras registro/inicio de sesión
    @GetMapping({"/Bienvenida", "/Bienvenida.html"})
    public String mostrarBienvenida() {
        System.out.println("\t Recojo la petición de /Bienvenida");
        return "BienvenidaUsuario";
    }

    // Link del logo: comprueba si hay sesión y redirige a Bienvenida o Inicio
    @GetMapping({"/home","/home.html","/logo"})
    public String logoHome() {
        System.out.println("\t Recojo la petición de /home (logo)");
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            Object idObj = attrs.getAttribute("id_usuario", RequestAttributes.SCOPE_SESSION);
            if (idObj != null) {
                System.out.println("\t [SESSION] id_usuario encontrado en logoHome: " + idObj);
                return "redirect:/Bienvenida";
            }
        }
        System.out.println("\t [SESSION] no hay sesión en logoHome, redirigiendo a Inicio");
        return "redirect:/Inicio";
    }


}
