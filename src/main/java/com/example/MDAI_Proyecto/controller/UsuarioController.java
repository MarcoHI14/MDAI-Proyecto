package com.example.MDAI_Proyecto.controller;

import com.example.MDAI_Proyecto.data.model.Usuario;
import com.example.MDAI_Proyecto.data.services.ArtistaService;
import com.example.MDAI_Proyecto.data.services.UsuarioService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
/**
 * Controlador para manejar las solicitudes relacionadas con usuarios, incluyendo login, registro y administración.
 */
@Controller
@RequestMapping("/users")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final ArtistaService artistaService;

    /**
     * Constructor que inyecta los servicios necesarios.
      * @param usuarioService
     * @param artistaService
     */
    public UsuarioController(UsuarioService usuarioService, ArtistaService artistaService) {
        this.usuarioService = usuarioService;
        this.artistaService = artistaService;
        System.out.println("\t UsuarioController inicializado");
    }

    /**
     * Muestra la página de inicio de sesión.
      * @return Nombre de la vista "InicioSesion".
     */
    @GetMapping("/login")
    public String mostrarLogin() {
        System.out.println("\t Recojo la petición de /users/login");
        return "InicioSesion";
    }

    /**
     * Procesa la solicitud de inicio de sesión.
      * @param email Correo electrónico del usuario.
     * @param password Contraseña del usuario.
     * @param redirectAttributes Atributos para redirección con mensajes flash.
     * @return Redirección a la página correspondiente según el rol del usuario.
     */
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
            // Comprobar si el usuario es artista y redirigir a la vista adecuada
            try {
                boolean esArtista = false;
                if (usuario.getId() != null) {
                    esArtista = (artistaService.findByUsuarioId(usuario.getId()) != null);
                }
                if (esArtista) {
                    return "redirect:/Artista"; // ControllerArtista mapea a BienvenidaArtista
                } else {
                    return "redirect:/Bienvenida"; // InicioController mostrará BienvenidaUsuario
                }
            } catch (Exception ex) {
                System.err.println("Error comprobando rol de artista: " + ex.getMessage());
                // llevar a la bienvenida genérica
                return "redirect:/Bienvenida";
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Credenciales inválidas");
            return "redirect:/users/login";
        }
    }

    /**
     * Muestra la página de administración para el usuario admin.
      * @return Redirección a la ruta centralizada de administración.
     */
    @GetMapping("/admin")
    public String BienvenidaAdmin() {
        System.out.println("\t Recojo la petición de /users/admin");
        // Redirigir a la ruta centralizada de admin (/admin) para agrupar rutas
        return "redirect:/admin";
    }

    // Nota: el listado de solicitudes (ruta /AdminSolicitudes) se gestiona desde
    // `SolicitudVerificacionController` (mapping en root). Se evita duplicar el mapping
    // aquí para prevenir confusiones. Si quieres que la ruta sea `/users/AdminSolicitudes`
    // podemos mover o añadir un mapping específico, pero por ahora se delega al controlador
    // dedicado a solicitudes.

    /**
     * Muestra la página de registro de usuarios.
      * @return Nombre de la vista "Registro".
     */
    @GetMapping("/register")
    public String mostrarRegistro() {
        return "Registro";
    }

    /**
     * Procesa la solicitud de registro de un nuevo usuario.
      * @param username Nombre de usuario.
     * @param email Correo electrónico.
     * @param password Contraseña.
     * @param password2 Confirmación de contraseña.
     * @param model Modelo para pasar atributos a la vista.
     * @param redirectAttributes Atributos para redirección con mensajes flash.
     * @return Redirección a la página de bienvenida o vuelta al registro con errores.
     */
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

    /**
     * Cierra la sesión del usuario eliminando el atributo de sesión correspondiente.
      * @param redirectAttributes
     * @return
     */
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

    /**
     * Verifica si un nombre de usuario ya existe.
      * @param username Nombre de usuario a verificar.
     * @return "true" si existe, "false" en caso contrario.
     */
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

    /**
     * Verifica si un correo electrónico ya existe.
      * @param email Correo electrónico a verificar.
     * @return "true" si existe, "false" en caso contrario.
     */
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

    /**
     * Devuelve el nombre de usuario del usuario actualmente autenticado en la sesión.
      * @return Nombre de usuario o "ANONYMOUS" si no hay usuario autenticado.
     */
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
