package com.example.MDAI_Proyecto.controller;

import com.example.MDAI_Proyecto.data.model.Playlist;
import com.example.MDAI_Proyecto.data.model.Usuario;
import com.example.MDAI_Proyecto.data.services.CancionPlaylistService;
import com.example.MDAI_Proyecto.data.services.CancionService;
import com.example.MDAI_Proyecto.data.services.PlaylistService;
import com.example.MDAI_Proyecto.data.services.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class PlaylistsController {

    private final UsuarioService usuarioService;
    private final PlaylistService playlistService;
    private final CancionService cancionService;
    private final CancionPlaylistService cancionPlaylistService;

    public PlaylistsController(UsuarioService usuarioService, PlaylistService playlistService, CancionService cancionService, CancionPlaylistService cancionPlaylistService) {
        this.usuarioService = usuarioService;
        this.playlistService = playlistService;
        this.cancionService = cancionService;
        this.cancionPlaylistService = cancionPlaylistService;
        System.out.println("\t PlaylistsController inicializado");
    }

    @GetMapping("/Playlists")
    public String mostrarPlaylists(Model model) {
        System.out.println("\t Recojo la petición de /Playlists");
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

        List<Playlist> playlists = (List<Playlist>) playlistService.findAll();
        // Filtrar las playlists para que solo queden las del usuario actual
        playlists.removeIf(p -> !p.getUsuario().getUsername().equals(usuario.getUsername()));

        model.addAttribute("id_usuario", usuario.getId());
        model.addAttribute("playlists", playlists);
        return "GestorPlaylists";
    }

    @PostMapping("/CrearPlaylist")
    public String crearPlaylist(@RequestParam("id_usuario") Long idUsuario,
                                @RequestParam("nombre") String nombre,
                                @RequestParam("descripcion") String descripcion) {
        // Lógica para crear la playlist
        Playlist playlist = new Playlist();
        playlist.setUsuario(usuarioService.obtenerUsuarioPorId(idUsuario));
        playlist.setNombre(nombre);
        playlist.setDescripcion(descripcion);
        playlist.setFechaCreacion(LocalDateTime.now());

        playlistService.save(playlist);
        return "GestorPlaylists"; // Nombre de tu template de creación
    }

    @GetMapping("/BuscarPlaylist")
    public String buscarPlaylist(@RequestParam(name = "nombre", required = false) String nombre, Model model) {
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

        List<Playlist> playlists = (List<Playlist>) playlistService.findAll();
        // Mantener solo las del usuario actual y evitar NPEs
        playlists.removeIf(p -> p.getUsuario() == null || !usuario.getUsername().equals(p.getUsuario().getUsername()));

        if (nombre != null && !nombre.trim().isEmpty()) {
            String busc = nombre.trim().toLowerCase();
            playlists.removeIf(p -> p.getNombre() == null || !p.getNombre().toLowerCase().contains(busc));
            model.addAttribute("nombreBusqueda", nombre);
        }

        model.addAttribute("id_usuario", usuario.getId());
        model.addAttribute("playlists", playlists);
        return "GestorPlaylists";
    }

    @GetMapping("/BuscarAlbum")
    public String buscarAlbum(Model model) {
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

        model.addAttribute("id_usuario", usuario.getId());
        // Aquí puedes añadir lógica para buscar álbumes
        return "GestorPlaylists"; // Nombre de tu template de búsqueda de álbumes
    }
}
