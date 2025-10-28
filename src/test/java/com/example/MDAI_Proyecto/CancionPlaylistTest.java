package com.example.MDAI_Proyecto;

import com.example.MDAI_Proyecto.data.model.*;
import com.example.MDAI_Proyecto.data.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CancionPlaylistTest {

    @Autowired
    private CancionPlaylistRepository cancionPlaylistRepository;

    @Autowired
    private CancionRepository cancionRepository;

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ArtistaRepository artistaRepository;

    /** Test completo de CRUD para la entidad CancionPlaylist,
     * incluyendo la creación de las entidades relacionadas
     * Usuario, Artista, Cancion y Playlist.
     */
    @Test
    void CRUDCancionPlaylistTest() {
        Usuario usuario = new Usuario();
        usuario.setUsername("Cuarteto de Nos");
        usuario.setEmail("4denus@argmail.arg");
        usuario.setPassword("password123");

        // Guardar primero el usuario sin asignarle aún el artista
        usuario = usuarioRepository.save(usuario);

        Artista artista = new Artista();
        artista.setBiografia("Banda de rock alternativo.");
        artista.setUsuario(usuario); // relacionar artista con usuario
        artista = artistaRepository.save(artista); // persistir artista

        // ahora actualizar usuario para que apunte al artista persistido
        usuario.setArtista(artista);
        usuarioRepository.save(usuario);

        Cancion cancion = new Cancion();
        cancion.setTitulo("Lo Malo de Ser Bueno");
        cancion.setArtista(artista); // usa el artista ya persistido
        cancion.setGenero("Rock");
        cancion.setDuracion("3:45");
        cancion.setFechaSubida(java.time.LocalDateTime.now());
        cancion = cancionRepository.save(cancion);

        Playlist playlist = new Playlist();
        playlist.setNombre("Rock Alternativo");
        playlist.setDescripcion("Las mejores canciones de rock alternativo.");
        playlist.setUsuario(usuario);
        playlist.setFechaCreacion(java.time.LocalDateTime.now());
        playlist = playlistRepository.save(playlist);

        // relación bidireccional en memoria (si aplica)
        artista.addCancion(cancion);
        artistaRepository.save(artista);

        CancionPlaylist cancionPlaylist = new CancionPlaylist();
        cancionPlaylist.setCancion(cancion);
        cancionPlaylist.setPlaylist(playlist);
        cancionPlaylist.setOrden(1);
        cancionPlaylist = cancionPlaylistRepository.save(cancionPlaylist);

        // Leer
        CancionPlaylist cancionPlaylist1 = cancionPlaylistRepository.findById(cancionPlaylist.getId()).orElse(null);
        assertNotNull(cancionPlaylist1);
        assertEquals(1, cancionPlaylist1.getOrden());
        assertEquals("Lo Malo de Ser Bueno", cancionPlaylist1.getCancion().getTitulo());
        assertEquals("Rock Alternativo", cancionPlaylist1.getPlaylist().getNombre());
        // Actualizar
        cancionPlaylist1.setOrden(2);
        cancionPlaylistRepository.save(cancionPlaylist1);
        CancionPlaylist cancionPlaylist2 = cancionPlaylistRepository.findById(cancionPlaylist1.getId()).orElse(null);
        assertNotNull(cancionPlaylist2);
        assertEquals(2, cancionPlaylist2.getOrden());
        // Eliminar
        cancionPlaylistRepository.delete(cancionPlaylist2);
        CancionPlaylist cancionPlaylist3 = cancionPlaylistRepository.findById(cancionPlaylist2.getId()).orElse(null);
        assertNull(cancionPlaylist3);

        // borrar en orden inverso de dependencias evita problemas de flush
        playlistRepository.delete(playlist);
        cancionRepository.delete(cancion);
        artistaRepository.delete(artista);
        usuarioRepository.delete(usuario);
    }

    @Test
    void addCancionToPlaylistTest() {
        Usuario usuario = new Usuario();
        usuario.setUsername("Jhon Lennon");
        usuario.setEmail("jhlennon@beatle.uk");
        usuario.setPassword("imagine");
        usuario = usuarioRepository.save(usuario);

        Artista artista = new Artista();
        artista.setBiografia("Miembro de The Beatles con gafas redondas.");
        artista.setUsuario(usuario);
        artista = artistaRepository.save(artista);

        usuario.setArtista(artista);
        usuarioRepository.save(usuario);

        Cancion cancion = new Cancion();
        cancion.setTitulo("Imagine");
        cancion.setArtista(artista);
        cancion.setGenero("Pop");
        cancion.setDuracion("3:07");
        cancion.setFechaSubida(java.time.LocalDateTime.now());
        cancion = cancionRepository.save(cancion);

        Playlist playlist = new Playlist();
        playlist.setNombre("Clásicos del Jhon L.");
        playlist.setDescripcion("Las mejores canciones de Jhon Lennon.");
        playlist.setUsuario(usuario);
        playlist.setFechaCreacion(java.time.LocalDateTime.now());
        playlist = playlistRepository.save(playlist);

        // Añadir canción a la playlist
        CancionPlaylist cancionPlaylist = new CancionPlaylist();
        cancionPlaylist.setCancion(cancion);
        cancionPlaylist.setPlaylist(playlist);
        cancionPlaylist.setOrden(1);
        cancionPlaylist = cancionPlaylistRepository.save(cancionPlaylist);

        // Verificar que la canción fue añadida correctamente
        CancionPlaylist cancionPlaylist1 = cancionPlaylistRepository.findById(cancionPlaylist.getId()).orElse(null);
        assertNotNull(cancionPlaylist1);
        assertEquals("Imagine", cancionPlaylist1.getCancion().getTitulo());
        assertEquals("Clásicos del Jhon L.", cancionPlaylist1.getPlaylist().getNombre());

        // Add más canciones
        Cancion cancion2 = new Cancion();
        cancion2.setTitulo("Oh my Love");
        cancion2.setArtista(artista);
        cancion2.setGenero("Pop");
        cancion2.setDuracion("2:45");
        cancion2.setFechaSubida(java.time.LocalDateTime.now());
        cancion2 = cancionRepository.save(cancion2);

        CancionPlaylist cancionPlaylist2 = new CancionPlaylist();
        cancionPlaylist2.setCancion(cancion2);
        cancionPlaylist2.setPlaylist(playlist);
        cancionPlaylist2.setOrden(2);
        cancionPlaylist2 = cancionPlaylistRepository.save(cancionPlaylist2);

        Cancion cancion3 = new Cancion();
        cancion3.setTitulo("Jealous Guy");
        cancion3.setArtista(artista);
        cancion3.setGenero("Pop");
        cancion3.setDuracion("4:15");
        cancion3.setFechaSubida(java.time.LocalDateTime.now());
        cancion3 = cancionRepository.save(cancion3);

        CancionPlaylist cancionPlaylist3 = new CancionPlaylist();
        cancionPlaylist3.setCancion(cancion3);
        cancionPlaylist3.setPlaylist(playlist);
        cancionPlaylist3.setOrden(3);
        cancionPlaylist3 = cancionPlaylistRepository.save(cancionPlaylist3);

        // Verificar las canciones añadidas
        CancionPlaylist cp2 = cancionPlaylistRepository.findById(cancionPlaylist2.getId()).orElse(null);
        assertNotNull(cp2);
        assertEquals("Oh my Love", cp2.getCancion().getTitulo());
        CancionPlaylist cp3 = cancionPlaylistRepository.findById(cancionPlaylist3.getId()).orElse(null);
        assertNotNull(cp3);
        assertEquals("Jealous Guy", cp3.getCancion().getTitulo());

        // limpiar
        cancionPlaylistRepository.delete(cancionPlaylist3);
        cancionPlaylistRepository.delete(cancionPlaylist2);
        cancionPlaylistRepository.delete(cancionPlaylist1);
        playlistRepository.delete(playlist);
        cancionRepository.delete(cancion3);
        cancionRepository.delete(cancion2);
        cancionRepository.delete(cancion);
        artistaRepository.delete(artista);
        usuarioRepository.delete(usuario);
    }

    @Test
    void removeCancionFromPlaylistTest() {
        Usuario usuario = new Usuario();
        usuario.setUsername("The Verve");
        usuario.setEmail("theverve@oldmail.com");
        usuario.setPassword("bitterSweetSymphony");
        usuario = usuarioRepository.save(usuario);
        Artista artista = new Artista();
        artista.setBiografia("Banda británica de rock alternativo.");
        artista.setUsuario(usuario);
        artista = artistaRepository.save(artista);
        usuario.setArtista(artista);
        usuarioRepository.save(usuario);

        Cancion cancion = new Cancion();
        cancion.setTitulo("Bitter Sweet Symphony");
        cancion.setArtista(artista);
        cancion.setGenero("Rock");
        cancion.setDuracion("5:58");
        cancion.setFechaSubida(java.time.LocalDateTime.now());
        cancion = cancionRepository.save(cancion);

        Playlist playlist = new Playlist();
        playlist.setNombre("Rock Clásico");
        playlist.setDescripcion("Las mejores canciones de rock clásico.");
        playlist.setUsuario(usuario);
        playlist.setFechaCreacion(java.time.LocalDateTime.now());
        playlist = playlistRepository.save(playlist);

        CancionPlaylist cancionPlaylist = new CancionPlaylist();
        cancionPlaylist.setCancion(cancion);
        cancionPlaylist.setPlaylist(playlist);
        cancionPlaylist.setOrden(1);
        cancionPlaylist = cancionPlaylistRepository.save(cancionPlaylist);

        Cancion cancion2 = new Cancion();
        cancion2.setTitulo("Sonnet");
        cancion2.setArtista(artista);
        cancion2.setGenero("Rock");
        cancion2.setDuracion("4:20");
        cancion2.setFechaSubida(java.time.LocalDateTime.now());
        cancion2 = cancionRepository.save(cancion2);

        CancionPlaylist cancionPlaylist2 = new CancionPlaylist();
        cancionPlaylist2.setCancion(cancion2);
        cancionPlaylist2.setPlaylist(playlist);
        cancionPlaylist2.setOrden(2);
        cancionPlaylist2 = cancionPlaylistRepository.save(cancionPlaylist2);

        Cancion cancion3 = new Cancion();
        cancion3.setTitulo("The Drugs Don't Work");
        cancion3.setArtista(artista);
        cancion3.setGenero("Rock");
        cancion3.setDuracion("5:32");
        cancion3.setFechaSubida(java.time.LocalDateTime.now());
        cancion3 = cancionRepository.save(cancion3);

        CancionPlaylist cancionPlaylist3 = new CancionPlaylist();
        cancionPlaylist3.setCancion(cancion3);
        cancionPlaylist3.setPlaylist(playlist);
        cancionPlaylist3.setOrden(3);
        cancionPlaylist3 = cancionPlaylistRepository.save(cancionPlaylist3);

        // Eliminar la segunda canción de la playlist
        cancionPlaylistRepository.delete(cancionPlaylist2);
        CancionPlaylist cp2 = cancionPlaylistRepository.findById(cancionPlaylist2.getId()).orElse(null);
        assertNull(cp2);

        // Verificar que las otras canciones siguen en la playlist
        CancionPlaylist cp1 = cancionPlaylistRepository.findById(cancionPlaylist.getId()).orElse(null);
        assertNotNull(cp1);
        assertEquals("Bitter Sweet Symphony", cp1.getCancion().getTitulo());
        assertEquals(1, cp1.getOrden());
        CancionPlaylist cp3 = cancionPlaylistRepository.findById(cancionPlaylist3.getId()).orElse(null);
        assertNotNull(cp3);
        assertEquals("The Drugs Don't Work", cp3.getCancion().getTitulo());
        assertEquals(3, cp3.getOrden());

        // limpiar
        cancionPlaylistRepository.delete(cancionPlaylist3);
        cancionPlaylistRepository.delete(cancionPlaylist);
        playlistRepository.delete(playlist);
        cancionRepository.delete(cancion3);
        cancionRepository.delete(cancion2);
        cancionRepository.delete(cancion);
        artistaRepository.delete(artista);
        usuarioRepository.delete(usuario);
    }

}
