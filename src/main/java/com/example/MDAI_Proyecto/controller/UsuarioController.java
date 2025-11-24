package com.example.MDAI_Proyecto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users") // 👈 todas las rutas de este controller empiezan con /users
public class UsuarioController {

    public UsuarioController() {
        System.out.println("\t UsuarioController inicializado");
    }

    // Página de inicio de sesión
    @GetMapping("/login")
    public String mostrarLogin() {
        System.out.println("\t Recojo la petición de /users/login");
        return "InicioSesion"; // Thymeleaf buscará InicioSesion.html
    }

    // Procesar login
    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        RedirectAttributes redirectAttributes) {
        System.out.println("\t Recojo la petición de login (UsuarioController)");

        String e = email == null ? "" : email.trim();
        String p = password == null ? "" : password.trim();

        System.out.println("Email: " + e);
        System.out.println("Password: " + p);

        if ("admin@admin".equals(e) && "admin".equals(p)) {
            return "redirect:/users/admin"; // redirige a /users/admin
        } else {
            redirectAttributes.addFlashAttribute("error", "Credenciales inválidas");
            return "redirect:/users/login"; // vuelve al formulario
        }
    }

    // Página de administración
    @GetMapping("/admin")
    public String mostrarAdminSolicitudes() {
        System.out.println("\t Recojo la petición de /users/admin");
        return "AdminSolicitudes"; // AdminSolicitudes.html
    }
}
