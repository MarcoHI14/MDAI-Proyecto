package com.example.MDAI_Proyecto.controller;

import com.example.MDAI_Proyecto.data.model.SolicitudVerificacion;
import com.example.MDAI_Proyecto.data.services.ArtistaService;
import com.example.MDAI_Proyecto.data.services.CancionService;
import com.example.MDAI_Proyecto.data.services.PlaylistService;
import com.example.MDAI_Proyecto.data.services.SolicitudVerificacionService;
import com.example.MDAI_Proyecto.data.services.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import com.example.MDAI_Proyecto.data.model.Usuario;

import com.example.MDAI_Proyecto.data.model.Artista;
import com.example.MDAI_Proyecto.data.model.Cancion;
import com.example.MDAI_Proyecto.data.services.CancionService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UsuarioService usuarioService;
    private final ArtistaService artistaService;
    private final CancionService cancionService;
    private final PlaylistService playlistService;
    private final SolicitudVerificacionService solicitudService;

    public AdminController(UsuarioService usuarioService,
                           ArtistaService artistaService,
                           CancionService cancionService,
                           PlaylistService playlistService,
                           SolicitudVerificacionService solicitudService) {
        this.usuarioService = usuarioService;
        this.artistaService = artistaService;
        this.cancionService = cancionService;
        this.playlistService = playlistService;
        this.solicitudService = solicitudService;
    }

    // GET /admin  -> panel de control (redirige a bienvenida admin o muestra resumen)
    @GetMapping({"/",""})
    public String panelControl() {
        // devolver la vista de bienvenida del administrador
        return "BienvenidaAdmin";
    }

    // GET /admin/AdminSolicitudes -> listar solicitudes para admin
    @GetMapping({"/AdminSolicitudes","/AdminSolicitudes.html"})
    public String listarSolicitudesAdmin(@RequestParam(name = "estado", required = false) String estado, Model model) {
        System.out.println("\t Recojo la petición de /admin/AdminSolicitudes (AdminController) estado=" + estado);
        String estadoSeleccionado = (estado == null || estado.trim().isEmpty()) ? "PENDIENTE" : estado.trim().toUpperCase();
        List<SolicitudVerificacion> lista;
        if ("ALL".equals(estadoSeleccionado) || "TODAS".equals(estadoSeleccionado)) {
            Optional<List<SolicitudVerificacion>> optAll = solicitudService.findAll();
            lista = optAll.orElse(Collections.emptyList());
        } else {
            Optional<List<SolicitudVerificacion>> opt = solicitudService.findByEstado(estadoSeleccionado);
            lista = opt.orElse(Collections.emptyList());
        }
        System.out.println("\t Se han obtenido " + (lista == null ? 0 : lista.size()) + " solicitudes para estado=" + estadoSeleccionado);
        if (lista != null && !lista.isEmpty()) {
            // imprimir hasta 5 ids para depuración
            StringBuilder ids = new StringBuilder();
            int count = Math.min(5, lista.size());
            for (int i = 0; i < count; i++) {
                ids.append(lista.get(i).getIdSolicitud());
                if (i < count - 1) ids.append(",");
            }
            System.out.println("\t IDs ejemplo: " + ids.toString());
        }
        model.addAttribute("solicitudes", lista);
        model.addAttribute("selectedEstado", estadoSeleccionado);
        return "AdminSolicitudes";
    }

    // GET /admin/estadisticas -> página de estadísticas
    @GetMapping({"/estadisticas","/estadisticas.html"})
    public String mostrarEstadisticas(Model model) {
        // Calcular estadísticas básicas
        int totalUsuarios = 0;
        try {
            var ulist = usuarioService.findAll();
            if (ulist != null) totalUsuarios = ulist.size();
        } catch (Exception ignored) {}

        int totalArtistas = 0;
        try {
            var alist = artistaService.findAll("");
            if (alist != null) totalArtistas = alist.size();
        } catch (Exception ignored) {}

        int totalCanciones = 0;
        try {
            var clist = cancionService.getAll();
            if (clist != null) {
                int c = 0;
                for (var x : clist) c++;
                totalCanciones = c;
            }
        } catch (Exception ignored) {}

        int totalPlaylists = 0;
        try {
            var plist = playlistService.findAll();
            if (plist != null) {
                int p = 0;
                for (var x : plist) p++;
                totalPlaylists = p;
            }
        } catch (Exception ignored) {}

        int pendientes = solicitudService.findByEstado("PENDIENTE").map(java.util.List::size).orElse(0);
        int aprobadas = solicitudService.findByEstado("APROBADA").map(java.util.List::size).orElse(0);
        int rechazadas = solicitudService.findByEstado("RECHAZADA").map(java.util.List::size).orElse(0);

        model.addAttribute("totalUsuarios", totalUsuarios);
        model.addAttribute("totalArtistas", totalArtistas);
        model.addAttribute("totalCanciones", totalCanciones);
        model.addAttribute("totalPlaylists", totalPlaylists);
        model.addAttribute("pendientes", pendientes);
        model.addAttribute("aprobadas", aprobadas);
        model.addAttribute("rechazadas", rechazadas);

        return "AdminEstadisticas";
    }

    // --- Gestión de Usuarios integrada en AdminController ---
    @GetMapping({"/AdminUsuarios","/AdminUsuarios.html"})
    public String listarUsuarios(Model model) {
        System.out.println("\t Recojo la petición de /admin/AdminUsuarios");
        List<Usuario> usuarios = usuarioService.findAll();
        // Map userId -> isArtista (true/false) consultando por username
        Map<Long, Boolean> isArtistaByUserId = new HashMap<>();
        if (usuarios != null) {
            for (Usuario u : usuarios) {
                boolean isArtista = false;
                try {
                    if (u != null && u.getId() != null) {
                        // Comprobar por id de usuario: si existe un Artista cuyo id coincide con el id del usuario
                        // usar la nueva búsqueda por usuario id
                        isArtista = (artistaService.findByUsuarioId(u.getId()) != null);
                    }
                } catch (Exception ex) {
                    System.err.println("Error comprobando artista para usuario " + (u != null ? u.getId() : "?") + ": " + ex.getMessage());
                }
                isArtistaByUserId.put(u != null && u.getId() != null ? u.getId() : -1L, isArtista);
            }
        }
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("isArtistaByUserId", isArtistaByUserId);
        return "AdminUsuarios";
    }

    // API: eliminar usuario por id (DELETE)
    @DeleteMapping("/api/users/{id}")
    @ResponseBody
    public ResponseEntity<?> apiEliminarUsuario(@PathVariable("id") Long id) {
        try {
            Usuario u = usuarioService.obtenerUsuarioPorId(id);
            if (u == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Usuario no encontrado"));
            }
            usuarioService.eliminarUsuario(u);
            return ResponseEntity.ok(Map.of("status", "deleted", "id", id));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", ex.getMessage()));
        }
    }

    // API: eliminar artista por id (DELETE)
    @DeleteMapping("/api/artistas/{id}")
    @ResponseBody
    public ResponseEntity<?> apiEliminarArtista(@PathVariable("id") Long id) {
        try {
            Artista a = artistaService.findById(id);
            if (a == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Artista no encontrado"));
            }
            artistaService.deleteArtista(a);
            return ResponseEntity.ok(Map.of("status", "deleted", "id", id));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", ex.getMessage()));
        }
    }

    // API: eliminar canción por id (DELETE)
    @DeleteMapping("/api/canciones/{id}")
    @ResponseBody
    public ResponseEntity<?> apiEliminarCancion(@PathVariable("id") Long id) {
        try {
            if (!cancionService.findById(id).isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Canción no encontrada"));
            }
            cancionService.deleteById(id);
            return ResponseEntity.ok(Map.of("status", "deleted", "id", id));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", ex.getMessage()));
        }
    }

    // API: actualizar username
    @PutMapping("/api/users/{id}/username")
    @ResponseBody
    public ResponseEntity<?> apiActualizarUsername(@PathVariable("id") Long id, @RequestBody Map<String, String> body) {
        try {
            String nuevo = body.getOrDefault("username", "").trim();
            if (nuevo.isEmpty()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "username vacío"));
            Usuario u = usuarioService.obtenerUsuarioPorId(id);
            if (u == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Usuario no encontrado"));
            Usuario existe = usuarioService.obtenerUsuarioByUsername(nuevo);
            if (existe != null && !existe.getId().equals(id)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "username ya en uso"));
            }
            u.setUsername(nuevo);
            usuarioService.guardarUsuario(u);
            return ResponseEntity.ok(Map.of("status", "ok", "username", nuevo));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", ex.getMessage()));
        }
    }

    // API: actualizar email
    @PutMapping("/api/users/{id}/email")
    @ResponseBody
    public ResponseEntity<?> apiActualizarEmail(@PathVariable("id") Long id, @RequestBody Map<String, String> body) {
        try {
            String nuevo = body.getOrDefault("email", "").trim();
            if (nuevo.isEmpty()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "email vacío"));
            Usuario u = usuarioService.obtenerUsuarioPorId(id);
            if (u == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Usuario no encontrado"));
            Usuario existe = usuarioService.obtenerUsuarioByEmail(nuevo);
            if (existe != null && !existe.getId().equals(id)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "email ya en uso"));
            }
            u.setEmail(nuevo);
            usuarioService.guardarUsuario(u);
            return ResponseEntity.ok(Map.of("status", "ok", "email", nuevo));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", ex.getMessage()));
        }
    }

    // API: actualizar contraseña
    @PutMapping("/api/users/{id}/password")
    @ResponseBody
    public ResponseEntity<?> apiActualizarPassword(@PathVariable("id") Long id, @RequestBody Map<String, String> body) {
        try {
            String nuevo = body.getOrDefault("password", "");
            String confirm = body.getOrDefault("confirm", "");
            if (nuevo == null || nuevo.isEmpty()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "password vacío"));
            if (!nuevo.equals(confirm)) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "passwords no coinciden"));
            Usuario u = usuarioService.obtenerUsuarioPorId(id);
            if (u == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Usuario no encontrado"));
            u.setPassword(nuevo);
            usuarioService.guardarUsuario(u);
            return ResponseEntity.ok(Map.of("status", "ok"));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", ex.getMessage()));
        }
    }

    // GET /admin/AdminArtistas -> listar artistas para administración (URL canonical)
    @GetMapping({"/AdminArtistas","/AdminArtistas.html"})
    public String listarArtistas(Model model) {
        System.out.println("\t Recojo la petición de /admin/AdminArtistas");
        List<Artista> artistas = Collections.emptyList();
        try {
            artistas = artistaService.findAll("");
        } catch (Exception ex) {
            System.err.println("Error obteniendo artistas: " + ex.getMessage());
        }
        model.addAttribute("artistas", artistas);
        // Devolver la vista cuyo fichero es src/main/resources/templates/AdminArtistas.html
        return "AdminArtistas";
    }

    // Redirigir rutas antiguas /AdministrarArtistas a la URL canonical /AdminArtistas
    @GetMapping({"/AdministrarArtistas","/AdministrarArtistas.html"})
    public String redirectAdministrarArtistas() {
        return "redirect:/admin/AdminArtistas";
    }

    // GET /admin/AdminCanciones -> listar canciones para administración
    @GetMapping({"/AdminCanciones","/AdminCanciones.html"})
    public String listarCanciones(Model model) {
        System.out.println("\t Recojo la petición de /admin/AdminCanciones");
        Iterable<Cancion> canciones = cancionService.getAll();
        model.addAttribute("canciones", canciones);
        return "AdminCanciones";
    }
}
