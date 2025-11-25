package com.example.MDAI_Proyecto.controller;

import com.example.MDAI_Proyecto.data.model.Usuario;
import com.example.MDAI_Proyecto.data.services.UsuarioService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

@Controller
@RequestMapping("/users")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
        System.out.println("\t UsuarioController inicializado");
    }

    @GetMapping("/login")
    public String mostrarLogin() {
        System.out.println("\t Recojo la petición de /users/login");
        return "InicioSesion";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        RedirectAttributes redirectAttributes) {
        System.out.println("\t Recojo la petición de login (UsuarioController)");

        String e = email == null ? "" : email.trim();
        String p = password == null ? "" : password.trim();

        System.out.println("Email: " + e);

        if ("admin@admin".equals(e) && "admin".equals(p)) {
            return "redirect:/users/admin";
        }

        Usuario usuario = usuarioService.obtenerUsuarioByEmail(e);
        if (usuario != null && usuario.comprobarPassword(p)) {
            // Guardamos solo el id del usuario en la sesión (Long)
            RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
            if (attrs != null) {
                Long idUsuario = usuario.getId();
                attrs.setAttribute("id_usuario", idUsuario, RequestAttributes.SCOPE_SESSION);
                System.out.println("\t [SESSION] id_usuario guardado en sesión: " + idUsuario);
            }
            redirectAttributes.addFlashAttribute("username", usuario.getUsername());
            return "redirect:/Bienvenida";
        } else {
            redirectAttributes.addFlashAttribute("error", "Credenciales inválidas");
            return "redirect:/users/login";
        }
    }

    @GetMapping("/admin")
    public String mostrarAdminSolicitudes() {
        System.out.println("\t Recojo la petición de /users/admin");
        return "AdminSolicitudes";
    }

    @GetMapping("/register")
    public String mostrarRegistro() {
        return "Registro";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String email,
                           @RequestParam String password,
                           @RequestParam(required = false) String password2,
                           org.springframework.ui.Model model,
                           RedirectAttributes redirectAttributes) {

        java.util.List<String> errors = new java.util.ArrayList<>();

        String u = username == null ? "" : username.trim();
        String e = email == null ? "" : email.trim();
        String p = password == null ? "" : password;
        String p2 = password2 == null ? "" : password2;

        if (u.isEmpty()) errors.add("Introduce un nombre de usuario.");
        if (e.isEmpty()) errors.add("Introduce un correo electrónico.");
        if (p.isEmpty()) errors.add("Introduce una contraseña.");
        if (!p.isEmpty() && !p.equals(p2)) errors.add("Las contraseñas no coinciden.");

        if (!u.isEmpty() && usuarioService.obtenerUsuarioByUsername(u) != null) {
            errors.add("El nombre de usuario ya existe.");
        }
        if (!e.isEmpty() && usuarioService.obtenerUsuarioByEmail(e) != null) {
            errors.add("El correo ya está registrado.");
        }

        if (!errors.isEmpty()) {
            model.addAttribute("validationErrors", errors);
            model.addAttribute("username", u);
            model.addAttribute("email", e);
            return "Registro";
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(u);
        usuario.setEmail(e);
        usuario.setPassword(p);

        // Guardar y usar el usuario devuelto por el servicio para obtener el id asignado
        Usuario saved = usuarioService.guardarUsuario(usuario);

        // Guardamos en la sesión usando la abstracción de Spring el id del usuario recién creado
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            Long idUsuario = (saved != null && saved.getId() != null) ? saved.getId() : usuario.getId();
            attrs.setAttribute("id_usuario", idUsuario, RequestAttributes.SCOPE_SESSION);
            System.out.println("\t [SESSION] id_usuario guardado en sesión: " + idUsuario);
        }

        redirectAttributes.addFlashAttribute("username", saved != null ? saved.getUsername() : usuario.getUsername());
        redirectAttributes.addFlashAttribute("success", "Registro completado. ¡Bienvenido!");
        return "redirect:/Bienvenida";
    }

    // Logout que elimina el atributo de sesión (sin usar jakarta)
    @GetMapping("/logout")
    public String logout(RedirectAttributes redirectAttributes) {
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            Object idUsuarioObj = attrs.getAttribute("id_usuario", RequestAttributes.SCOPE_SESSION);
            if (idUsuarioObj instanceof Long idUsuario) {
                System.out.println("\t [SESSION] Eliminando usuario con id " + idUsuario + " de sesión");
            } else {
                System.out.println("\t [SESSION] No había usuario en sesión para eliminar");
            }
            attrs.removeAttribute("id_usuario", RequestAttributes.SCOPE_SESSION);
        }
        redirectAttributes.addFlashAttribute("info", "Sesión cerrada correctamente.");
        return "redirect:/";
    }

    @GetMapping(value = "/exists/username", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public ResponseEntity<String> usernameExists(@RequestParam String username) {
        try {
            boolean exists = usuarioService.obtenerUsuarioByUsername(username) != null;
            System.out.println("[usernameExists] username='" + username + "' -> " + exists);
            return ResponseEntity.ok(Boolean.toString(exists));
        } catch (Exception ex) {
            System.err.println("Error comprobando username: " + ex.getMessage());
            return ResponseEntity.ok("false");
        }
    }

    @GetMapping(value = "/exists/email", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public ResponseEntity<String> emailExists(@RequestParam String email) {
        try {
            boolean exists = usuarioService.obtenerUsuarioByEmail(email) != null;
            System.out.println("[emailExists] email='" + email + "' -> " + exists);
            return ResponseEntity.ok(Boolean.toString(exists));
        } catch (Exception ex) {
            System.err.println("Error comprobando email: " + ex.getMessage());
            return ResponseEntity.ok("false");
        }
    }

    @GetMapping(value = "/whoami", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public ResponseEntity<String> whoami() {
        try {
            RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
            if (attrs != null) {
                Object idUsuarioObj = attrs.getAttribute("id_usuario", RequestAttributes.SCOPE_SESSION);
                if (idUsuarioObj instanceof Long idUsuario) {
                    // Resolver el usuario a partir del id
                    Usuario usuario = usuarioService.obtenerUsuarioPorId(idUsuario);
                    if (usuario != null) {
                        return ResponseEntity.ok(usuario.getUsername());
                    } else {
                        return ResponseEntity.ok("ANONYMOUS");
                    }
                } else if (idUsuarioObj != null) {
                    return ResponseEntity.ok(idUsuarioObj.toString());
                }
            }
            return ResponseEntity.ok("ANONYMOUS");
        } catch (Exception ex) {
            System.err.println("Error en whoami: " + ex.getMessage());
            return ResponseEntity.status(500).body("ERROR");
        }
    }
}
