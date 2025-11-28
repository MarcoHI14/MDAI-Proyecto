package com.example.MDAI_Proyecto.controller;

import com.example.MDAI_Proyecto.data.model.Cancion;
import com.example.MDAI_Proyecto.data.model.Artista;
import com.example.MDAI_Proyecto.data.services.CancionService;
import com.example.MDAI_Proyecto.data.services.ArtistaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.UUID;

@Controller
public class CancionController {

    private final CancionService cancionService;
    private final ArtistaService artistaService;

    public CancionController(CancionService cancionService, ArtistaService artistaService) {
        this.cancionService = cancionService;
        this.artistaService = artistaService;
        System.out.println("\t CancionController inicializado");
    }

    @GetMapping("/BuscarCanciones")
    public String buscar(
            @RequestParam(required = false) String filtroTipo,
            @RequestParam(required = false) String query,
            Model model) {

        Iterable<Cancion> canciones;
        System.out.println("\t Recojo la petición de /Buscar en CancionController");
        if (filtroTipo == null || filtroTipo.isBlank()) {
            canciones = cancionService.getAll();
        } else {
            String q = (query == null) ? "" : query;
            switch (filtroTipo) {
                case "nombre":
                    canciones = cancionService.findByTituloContainingIgnoreCase(q);
                    break;
                case "genero":
                    canciones = cancionService.findByGeneroIgnoreCase(q);
                    break;
                case "artista":
                    canciones = cancionService.findbyArtistaName(q);
                    break;
                default:
                    canciones = cancionService.getAll();
            }
        }

        model.addAttribute("canciones", canciones);
        return "BuscarCanciones";
    }

    // Nuevo: manejar la subida/registro de canciones desde el formulario de artista
    @PostMapping("/canciones/registrar")
    public String registrarCancion(@RequestParam String titulo,
                                   @RequestParam(required = false) String genero,
                                   @RequestParam(required = false) String descripcion,
                                   @RequestParam("archivo") MultipartFile archivo,
                                   @RequestParam(required = false) String duracion,
                                   RedirectAttributes redirectAttributes) {

        // Comprobar sesión
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            redirectAttributes.addFlashAttribute("error", "No hay sesión activa. Por favor inicia sesión.");
            return "redirect:/Inicio";
        }
        Object idObj = attrs.getAttribute("id_usuario", RequestAttributes.SCOPE_SESSION);
        Long idUsuario = null;
        if (idObj instanceof Long) idUsuario = (Long) idObj;
        else if (idObj instanceof Integer) idUsuario = ((Integer) idObj).longValue();
        else if (idObj != null) {
            try { idUsuario = Long.parseLong(idObj.toString()); } catch (NumberFormatException ignored) {}
        }
        if (idUsuario == null) {
            redirectAttributes.addFlashAttribute("error", "No hay sesión activa. Inicia sesión para subir canciones.");
            return "redirect:/Inicio";
        }

        // Comprobar que el usuario es artista
        Artista artista = artistaService.findByUsuarioId(idUsuario);
        if (artista == null) {
            redirectAttributes.addFlashAttribute("error", "Sólo los artistas pueden subir canciones.");
            return "redirect:/Bienvenida";
        }

        // Validaciones básicas
        if (titulo == null || titulo.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Introduce un título para la canción.");
            return "redirect:/Artista";
        }
        if (archivo == null || archivo.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Selecciona un archivo de audio.");
            return "redirect:/Artista";
        }

        // Registrar la descripción en logs para uso futuro
        if (descripcion != null && !descripcion.isBlank()) {
            System.out.println("\t Descripción proporcionada: " + descripcion);
        }

        // NOTE: no se impone límite de tamaño en el controlador (gestionado por configuración del servidor).
        // El usuario pidió no tener límite de subidas, por lo que no validamos aquí el tamaño del archivo.

        // Comprobar extensión y preparar nombre
        String origName = archivo.getOriginalFilename();
        String original = (origName == null) ? "upload" : StringUtils.cleanPath(origName);
        String lower = original.toLowerCase();
        if (!(lower.endsWith(".mp3") || lower.endsWith(".wav") || lower.endsWith(".ogg") || lower.endsWith(".oga"))) {
            redirectAttributes.addFlashAttribute("error", "Formato no soportado. Usa mp3, wav u ogg.");
            return "redirect:/Artista";
        }

        // Guardar fichero en carpeta static/uploads
        try {
            // Ubicación relativa al proyecto para desarrollo
            String uploadsDir = "src/main/resources/static/uploads";
            File uploadRoot = new File(uploadsDir);
            if (!uploadRoot.exists()) {
                boolean created = uploadRoot.mkdirs();
                if (!created) {
                    System.err.println("No se pudo crear el directorio de uploads: " + uploadRoot.getAbsolutePath());
                }
            }

            String uid = UUID.randomUUID().toString();
            String filename = System.currentTimeMillis() + "_" + uid + "_" + original.replaceAll("[^a-zA-Z0-9._-]","_");
            Path target = uploadRoot.toPath().resolve(filename).normalize();

            Files.copy(archivo.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

            // Crear entidad Cancion y persistir
            Cancion cancion = new Cancion();
            cancion.setTitulo(titulo.trim());
            cancion.setGenero(genero != null ? genero.trim() : null);
            // descripcion no tiene campo en Cancion; se ignora o podría mapearse a otra entidad
            cancion.setArchivoAudio("/uploads/" + filename);
            cancion.setFechaSubida(LocalDateTime.now());
            cancion.setArtista(artista);

            // Si el cliente nos pasó la duración (form), usarla; si no, dejar null
            if (duracion != null && !duracion.isBlank()) {
                cancion.setDuracion(duracion.trim());
                System.out.println("\t Duración recibida desde cliente: " + duracion);
            } else {
                System.out.println("\t No se recibió duración desde el cliente para: " + filename);
            }

            Cancion saved = cancionService.save(cancion);
             System.out.println("\t Canción guardada con id: " + (saved != null ? saved.getIdCancion() : "(nulo)"));
             redirectAttributes.addFlashAttribute("success", "Canción subida correctamente.");
             return "redirect:/Artista";

        } catch (IOException ioex) {
            System.err.println("Error guardando archivo: " + ioex.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error guardando el archivo.");
            return "redirect:/Artista";
        } catch (Exception ex) {
            System.err.println("Error registrando canción: " + ex.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error registrando la canción.");
            return "redirect:/Artista";
        }
    }

}
