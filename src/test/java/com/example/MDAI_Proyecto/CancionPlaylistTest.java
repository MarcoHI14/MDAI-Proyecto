package com.example.MDAI_Proyecto;

import com.example.MDAI_Proyecto.data.model.*;
import com.example.MDAI_Proyecto.data.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;
import java.util.Collections;
import java.util.Optional;

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

    /** Test específico para añadir múltiples canciones a una playlist
     * y verificar que se han añadido correctamente.
     */
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

    /** Test específico para eliminar una canción de una playlist
     * y verificar que se ha eliminado correctamente sin afectar
     * a las otras canciones en la playlist.
     */
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

    /** Test específico para actualizar el orden de las canciones
     * en una playlist y verificar que el orden se ha actualizado correctamente.
     */
    @Test
    void actualizarOrdenCancionInPlaylistTest() {
        Usuario usuario = new Usuario();
        usuario.setUsername("Nirvana");
        usuario.setEmail("nirvana@gmail.com");
        usuario.setPassword("grunge");
        usuario = usuarioRepository.save(usuario);

        Artista artista = new Artista();
        artista.setBiografia("Banda icónica de grunge.");
        artista.setUsuario(usuario);
        artista = artistaRepository.save(artista);
        usuario.setArtista(artista);
        usuarioRepository.save(usuario);

        Playlist playlist = new Playlist();
        playlist.setNombre("Grunge Hits");
        playlist.setDescripcion("Las mejores canciones de grunge.");
        playlist.setUsuario(usuario);
        playlist.setFechaCreacion(java.time.LocalDateTime.now());
        playlist = playlistRepository.save(playlist);

        Cancion cancion = new Cancion();
        cancion.setTitulo("Smells Like Teen Spirit");
        cancion.setArtista(artista);
        cancion.setGenero("Grunge");
        cancion.setDuracion("5:01");
        cancion.setFechaSubida(java.time.LocalDateTime.now());
        cancion = cancionRepository.save(cancion);

        CancionPlaylist cancionPlaylist = new CancionPlaylist();
        cancionPlaylist.setCancion(cancion);
        cancionPlaylist.setPlaylist(playlist);
        cancionPlaylist.setOrden(1);
        cancionPlaylist = cancionPlaylistRepository.save(cancionPlaylist);

        Cancion cancion2 = new Cancion();
        cancion2.setTitulo("Come As You Are");
        cancion2.setArtista(artista);
        cancion2.setGenero("Grunge");
        cancion2.setDuracion("3:39");
        cancion2.setFechaSubida(java.time.LocalDateTime.now());
        cancion2 = cancionRepository.save(cancion2);

        CancionPlaylist cancionPlaylist2 = new CancionPlaylist();
        cancionPlaylist2.setCancion(cancion2);
        cancionPlaylist2.setPlaylist(playlist);
        cancionPlaylist2.setOrden(2);
        cancionPlaylist2 = cancionPlaylistRepository.save(cancionPlaylist2);

        Cancion cancion3 = new Cancion();
        cancion3.setTitulo("Lithium");
        cancion3.setArtista(artista);
        cancion3.setGenero("Grunge");
        cancion3.setDuracion("4:17");
        cancion3.setFechaSubida(java.time.LocalDateTime.now());
        cancion3 = cancionRepository.save(cancion3);

        CancionPlaylist cancionPlaylist3 = new CancionPlaylist();
        cancionPlaylist3.setCancion(cancion3);
        cancionPlaylist3.setPlaylist(playlist);
        cancionPlaylist3.setOrden(3);
        cancionPlaylist3 = cancionPlaylistRepository.save(cancionPlaylist3);

        // Actualizar el orden de la segunda canción
        cancionPlaylist2.setOrden(1);
        cancionPlaylist3.setOrden(2);
        cancionPlaylist.setOrden(3);
        cancionPlaylistRepository.save(cancionPlaylist2);
        cancionPlaylistRepository.save(cancionPlaylist3);
        cancionPlaylistRepository.save(cancionPlaylist);

        CancionPlaylist cp2 = cancionPlaylistRepository.findById(cancionPlaylist2.getId()).orElse(null);
        assertNotNull(cp2);
        assertEquals(1, cp2.getOrden());

        CancionPlaylist cp3 = cancionPlaylistRepository.findById(cancionPlaylist3.getId()).orElse(null);
        assertNotNull(cp3);
        assertEquals(2, cp3.getOrden());

        CancionPlaylist cp1 = cancionPlaylistRepository.findById(cancionPlaylist.getId()).orElse(null);
        assertNotNull(cp1);
        assertEquals(3, cp1.getOrden());

        // limpiar
        cancionPlaylistRepository.delete(cancionPlaylist3);
        cancionPlaylistRepository.delete(cancionPlaylist2);
        cancionPlaylistRepository.delete(cancionPlaylist);
        playlistRepository.delete(playlist);
        cancionRepository.delete(cancion3);
        cancionRepository.delete(cancion2);
        cancionRepository.delete(cancion);
        artistaRepository.delete(artista);
        usuarioRepository.delete(usuario);

    }

    /** Test para verificar que la restricción de unicidad
     * en la tabla CancionPlaylist funciona correctamente,
     * impidiendo añadir la misma canción a la misma playlist más de una vez.
     */
    @Test
    void uniqueConstraintTest() {
        Usuario usuario = new Usuario();
        usuario.setUsername("Pink Floyd");
        usuario.setEmail("pinkfloyd@yahoo.com");
        usuario.setPassword("darkSide");
        usuario = usuarioRepository.save(usuario);

        Artista artista = new Artista();
        artista.setBiografia("Banda legendaria de rock progresivo.");
        artista.setUsuario(usuario);
        artista = artistaRepository.save(artista);
        usuario.setArtista(artista);
        usuarioRepository.save(usuario);

        Playlist playlist = new Playlist();
        playlist.setNombre("Rock Progresivo");
        playlist.setDescripcion("Las mejores canciones de rock progresivo.");
        playlist.setUsuario(usuario);
        playlist.setFechaCreacion(java.time.LocalDateTime.now());
        playlist = playlistRepository.save(playlist);

        Cancion cancion = new Cancion();
        cancion.setTitulo("Comfortably Numb");
        cancion.setArtista(artista);
        cancion.setGenero("Rock Progresivo");
        cancion.setDuracion("6:22");
        cancion.setFechaSubida(java.time.LocalDateTime.now());
        cancion = cancionRepository.save(cancion);

        CancionPlaylist cancionPlaylist = new CancionPlaylist();
        cancionPlaylist.setCancion(cancion);
        cancionPlaylist.setPlaylist(playlist);
        cancionPlaylist.setOrden(1);
        cancionPlaylist = cancionPlaylistRepository.save(cancionPlaylist);

        // Intentar añadir la misma canción a la misma playlist
        CancionPlaylist cancionPlaylistDuplicada = new CancionPlaylist();
        cancionPlaylistDuplicada.setCancion(cancion);
        cancionPlaylistDuplicada.setPlaylist(playlist);
        cancionPlaylistDuplicada.setOrden(2);
        Exception exception = assertThrows(Exception.class, () -> {
            cancionPlaylistRepository.save(cancionPlaylistDuplicada);
        });


        // limpiar
        cancionPlaylistRepository.delete(cancionPlaylist);
        playlistRepository.delete(playlist);
        cancionRepository.delete(cancion);
        artistaRepository.delete(artista);
        usuarioRepository.delete(usuario);

    }

    /** Test para verificar que no se puede añadir
     * una CancionPlaylist sin asignar una Cancion
     * ni una Playlist, ya que ambas son obligatorias.
     */
    @Test
    void addSinCancionToPlaylist() {
        CancionPlaylist cancionPlaylist = new CancionPlaylist();
        cancionPlaylist.setOrden(1);

        // sin cancion ni playlist -> debe fallar al hacer flush
        assertThrows(Exception.class, () -> {
            cancionPlaylistRepository.save(cancionPlaylist);
        });
    }

    /** Test para verificar que no se puede añadir
     * una CancionPlaylist sin asignar una Playlist,
     * ya que es obligatoria.
     */
    @Test
    void addCancionSinPlaylist() {
        Usuario usuario = new Usuario();
        usuario.setUsername("Adele");
        usuario.setEmail("adele@hotmail.com");
        usuario.setPassword("hello");
        usuario = usuarioRepository.save(usuario);

        Artista artista = new Artista();
        artista.setBiografia("Cantante y compositora británica.");
        artista.setUsuario(usuario);
        artista = artistaRepository.save(artista);
        usuario.setArtista(artista);
        usuarioRepository.save(usuario);

        Cancion cancion = new Cancion();
        cancion.setTitulo("Rolling in the Deep");
        cancion.setArtista(artista);
        cancion.setGenero("Pop");
        cancion.setDuracion("3:48");
        cancion.setFechaSubida(java.time.LocalDateTime.now());
        cancion = cancionRepository.save(cancion);

        CancionPlaylist cancionPlaylist = new CancionPlaylist();
        cancionPlaylist.setCancion(cancion);
        cancionPlaylist.setOrden(1);

        // sin playlist -> debe fallar
        assertThrows(Exception.class, () -> {
            cancionPlaylistRepository.save(cancionPlaylist);
        });

        // limpiar
        cancionRepository.delete(cancion);
        artistaRepository.delete(artista);
        usuarioRepository.delete(usuario);
    }

    /** Test para verificar la correcta recuperación
     * de las canciones asociadas a una playlist específica.
     */
    @Test
    void findByCancionPlaylistIdTest() {
        Usuario usuario = new Usuario();
        usuario.setUsername("Metallica");
        usuario.setEmail("mentallica@iron.com");
        usuario.setPassword("masterOfPuppets");
        usuario = usuarioRepository.save(usuario);
        Artista artista = new Artista();
        artista.setBiografia("Banda legendaria de heavy metal.");
        artista.setUsuario(usuario);
        artista = artistaRepository.save(artista);
        usuario.setArtista(artista);
        usuarioRepository.save(usuario);

        Playlist playlist = new Playlist();
        playlist.setNombre("Heavy Metal Classics");
        playlist.setDescripcion("Las mejores canciones de heavy metal.");
        playlist.setUsuario(usuario);
        playlist.setFechaCreacion(java.time.LocalDateTime.now());
        playlist = playlistRepository.save(playlist);

        Cancion cancion = new Cancion();
        cancion.setTitulo("Enter Sandman");
        cancion.setArtista(artista);
        cancion.setGenero("Heavy Metal");
        cancion.setDuracion("5:32");
        cancion.setFechaSubida(java.time.LocalDateTime.now());
        cancion = cancionRepository.save(cancion);

        CancionPlaylist cancionPlaylist = new CancionPlaylist();
        cancionPlaylist.setCancion(cancion);
        cancionPlaylist.setPlaylist(playlist);
        cancionPlaylist.setOrden(1);
        cancionPlaylist = cancionPlaylistRepository.save(cancionPlaylist);

        Cancion cancion2 = new Cancion();
        cancion2.setTitulo("Nothing Else Matters");
        cancion2.setArtista(artista);
        cancion2.setGenero("Heavy Metal");
        cancion2.setDuracion("6:28");
        cancion2.setFechaSubida(java.time.LocalDateTime.now());
        cancion2 = cancionRepository.save(cancion2);

        CancionPlaylist cancionPlaylist2 = new CancionPlaylist();
        cancionPlaylist2.setCancion(cancion2);
        cancionPlaylist2.setPlaylist(playlist);
        cancionPlaylist2.setOrden(2);
        cancionPlaylist2 = cancionPlaylistRepository.save(cancionPlaylist2);

        CancionPlaylist cp = cancionPlaylistRepository.findById(cancionPlaylist.getId()).orElse(null);
        assertNotNull(cp);
        assertEquals("Enter Sandman", cp.getCancion().getTitulo());
        assertEquals("Heavy Metal Classics", cp.getPlaylist().getNombre());
        CancionPlaylist cp2 = cancionPlaylistRepository.findById(cancionPlaylist2.getId()).orElse(null);
        assertNotNull(cp2);
        assertEquals("Nothing Else Matters", cp2.getCancion().getTitulo());
        assertEquals("Heavy Metal Classics", cp2.getPlaylist().getNombre());


        // limpiar
        cancionPlaylistRepository.delete(cancionPlaylist);
        cancionPlaylistRepository.delete(cancionPlaylist2);
        playlistRepository.delete(playlist);
        cancionRepository.delete(cancion2);
        cancionRepository.delete(cancion);
        artistaRepository.delete(artista);
        usuarioRepository.delete(usuario);
    }

    /** Test para verificar la correcta recuperación
     * de todas las canciones asociadas a una playlist
     * utilizando el metodo findByPlaylistIdPlaylist.
     */
    @Test
    void findByPlaylistId() {
        Usuario usuario = new Usuario();
        usuario.setUsername("Linkin Park");
        usuario.setEmail("linkinthepark@lookmail.com");
        usuario.setPassword("inTheEnd");
        usuario = usuarioRepository.save(usuario);
        Artista artista = new Artista();
        artista.setBiografia("Banda de rock alternativo y nu metal.");
        artista.setUsuario(usuario);
        artista = artistaRepository.save(artista);
        usuario.setArtista(artista);
        usuarioRepository.save(usuario);

        Playlist playlist = new Playlist();
        playlist.setNombre("Nu Metal Hits");
        playlist.setDescripcion("Las mejores canciones de nu metal.");
        playlist.setUsuario(usuario);
        playlist.setFechaCreacion(java.time.LocalDateTime.now());
        playlist = playlistRepository.save(playlist);

        Cancion cancion1 = new Cancion();
        cancion1.setTitulo("In The End");
        cancion1.setArtista(artista);
        cancion1.setGenero("Nu Metal");
        cancion1.setDuracion("3:36");
        cancion1.setFechaSubida(java.time.LocalDateTime.now());
        cancion1 = cancionRepository.save(cancion1);

        CancionPlaylist cancionPlaylist1 = new CancionPlaylist();
        cancionPlaylist1.setCancion(cancion1);
        cancionPlaylist1.setPlaylist(playlist);
        cancionPlaylist1.setOrden(1);
        cancionPlaylist1 = cancionPlaylistRepository.save(cancionPlaylist1);

        Cancion cancion2 = new Cancion();
        cancion2.setTitulo("Numb");
        cancion2.setArtista(artista);
        cancion2.setGenero("Nu Metal");
        cancion2.setDuracion("3:07");
        cancion2.setFechaSubida(java.time.LocalDateTime.now());
        cancion2 = cancionRepository.save(cancion2);

        CancionPlaylist cancionPlaylist2 = new CancionPlaylist();
        cancionPlaylist2.setCancion(cancion2);
        cancionPlaylist2.setPlaylist(playlist);
        cancionPlaylist2.setOrden(2);
        cancionPlaylist2 = cancionPlaylistRepository.save(cancionPlaylist2);

        // Verificar que ambas canciones están asociadas a la playlist
        var cancionesEnPlaylistOpt = cancionPlaylistRepository.findByPlaylistIdPlaylist(playlist.getIdPlaylist());
        var cancionesEnPlaylist = cancionesEnPlaylistOpt.orElse(Collections.emptyList());
        assertEquals(2, cancionesEnPlaylist.size());
        for (CancionPlaylist cp : cancionesEnPlaylist) {
            assertEquals(playlist.getIdPlaylist(), cp.getPlaylist().getIdPlaylist());
        }
        // limpiar
        cancionPlaylistRepository.delete(cancionPlaylist1);
        cancionPlaylistRepository.delete(cancionPlaylist2);
        playlistRepository.delete(playlist);
        cancionRepository.delete(cancion2);
        cancionRepository.delete(cancion1);
        artistaRepository.delete(artista);
        usuarioRepository.delete(usuario);

    }

    /** Test para verificar que findByPlaylistIdPlaylist
     * devuelve una lista vacía cuando no hay canciones
     * asociadas a la playlist.
     */
    @Test
    void findByPlaylistId_EmptyTest() {
        Usuario usuario = new Usuario();
        usuario.setUsername("Nacho Vegas");
        usuario.setEmail("nachoenlasvegas@gmail.com");
        usuario.setPassword("cancionesTristes");
        usuario = usuarioRepository.save(usuario);

        Artista artista = new Artista();
        artista.setBiografia("Cantautor español de indie y rock alternativo.");
        artista.setUsuario(usuario);
        artista = artistaRepository.save(artista);
        usuario.setArtista(artista);
        usuarioRepository.save(usuario);

        Playlist playlist = new Playlist();
        playlist.setNombre("Canciones Tristes");
        playlist.setDescripcion("Las mejores canciones tristes de Nacho Vegas.");
        playlist.setUsuario(usuario);
        playlist.setFechaCreacion(java.time.LocalDateTime.now());
        playlist = playlistRepository.save(playlist);

        // Verificar que no hay canciones asociadas a la playlist
        var cancionesEnPlaylistOpt = cancionPlaylistRepository.findByPlaylistIdPlaylist(playlist.getIdPlaylist());
        var cancionesEnPlaylist = cancionesEnPlaylistOpt.orElse(Collections.emptyList());
        assertEquals(0, cancionesEnPlaylist.size());

        // limpiar
        playlistRepository.delete(playlist);
        artistaRepository.delete(artista);
        usuarioRepository.delete(usuario);

    }

    /** Test para verificar la correcta recuperación
     * de una CancionPlaylist específica utilizando
     * el metodo findByCancionId.
     */
    @Test
    void findByCancionIdTest() {
        // Configuración inicial: crear usuario, artista, canción, playlist y cancionPlaylist
        Usuario usuario = new Usuario();
        usuario.setUsername("Michael Jackson");
        usuario.setEmail("mjackson@gmail.com");
        usuario.setPassword("thriller");
        usuario = usuarioRepository.save(usuario);

        Artista artista = new Artista();
        artista.setBiografia("El Rey del Pop.");
        artista.setUsuario(usuario);
        artista = artistaRepository.save(artista);
        usuario.setArtista(artista);
        usuarioRepository.save(usuario);

        Playlist playlist = new Playlist();
        playlist.setNombre("Pop Hits");
        playlist.setDescripcion("Las mejores canciones de pop.");
        playlist.setUsuario(usuario);
        playlist.setFechaCreacion(java.time.LocalDateTime.now());
        playlist = playlistRepository.save(playlist);

        Cancion cancion = new Cancion();
        cancion.setTitulo("Billie Jean");
        cancion.setArtista(artista);
        cancion.setGenero("Pop");
        cancion.setDuracion("4:54");
        cancion.setFechaSubida(java.time.LocalDateTime.now());
        cancion = cancionRepository.save(cancion);
        CancionPlaylist cancionPlaylist = new CancionPlaylist();
        cancionPlaylist.setCancion(cancion);
        cancionPlaylist.setPlaylist(playlist);
        cancionPlaylist.setOrden(1);
        cancionPlaylist = cancionPlaylistRepository.save(cancionPlaylist);

        Cancion cancion2 = new Cancion();
        cancion2.setTitulo("Thriller");
        cancion2.setArtista(artista);
        cancion2.setGenero("Pop");
        cancion2.setDuracion("5:57");
        cancion2.setFechaSubida(java.time.LocalDateTime.now());
        cancion2 = cancionRepository.save(cancion2);
        CancionPlaylist cancionPlaylist2 = new CancionPlaylist();
        cancionPlaylist2.setCancion(cancion2);
        cancionPlaylist2.setPlaylist(playlist);
        cancionPlaylist2.setOrden(2);
        cancionPlaylist2 = cancionPlaylistRepository.save(cancionPlaylist2);

        Optional<CancionPlaylist> resultadoOpt = cancionPlaylistRepository.findByCancionId(cancion.getIdCancion());
        assertTrue(resultadoOpt.isPresent());
        CancionPlaylist resultado =  resultadoOpt.get();
        assertEquals("Billie Jean", resultado.getCancion().getTitulo());
        assertEquals("Pop Hits", resultado.getPlaylist().getNombre());

        Optional<CancionPlaylist> resultadoOpt2 = cancionPlaylistRepository.findByCancionId(cancion2.getIdCancion());
        assertTrue(resultadoOpt2.isPresent());
        CancionPlaylist resultado2 =  resultadoOpt2.get();
        assertEquals("Thriller", resultado2.getCancion().getTitulo());
        assertEquals("Pop Hits", resultado2.getPlaylist().getNombre());

        // limpieza de datos
        cancionPlaylistRepository.delete(cancionPlaylist2);
        cancionPlaylistRepository.delete(cancionPlaylist);

        Optional<CancionPlaylist> deleted1 = cancionPlaylistRepository.findById(cancionPlaylist.getId());
        assertFalse(deleted1.isPresent());
        Optional<CancionPlaylist> deleted2 = cancionPlaylistRepository.findById(cancionPlaylist2.getId());
        assertFalse(deleted2.isPresent());

        playlistRepository.delete(playlist);
        cancionRepository.delete(cancion2);
        cancionRepository.delete(cancion);
        artistaRepository.delete(artista);
        usuarioRepository.delete(usuario);
    }

    /** Test para verificar que findByCancionId
     * devuelve un Optional vacío cuando no existe
     * una CancionPlaylist asociada a la canción dada.
     */
    @Test
    void findByCancionId_NonExistenteTest() {
        Optional<CancionPlaylist> resultadoOpt = cancionPlaylistRepository.findByCancionId(9999L);
        assertFalse(resultadoOpt.isPresent());
    }

    /** Test para verificar que findByPlaylistIdPlaylistOrderByOrdenAsc
     * devuelve las canciones en el orden correcto (ascendente) según
     * el campo 'orden' en la entidad CancionPlaylist.
     */
    @Test
    void findByPlaylistIdPlaylistOrderByOrdenAsc_Test() {
        Usuario usuario = new Usuario();
        usuario.setUsername("Coldplay");
        usuario.setEmail("jugadafria@campofrio.com");
        usuario.setPassword("yellow");
        usuario = usuarioRepository.save(usuario);
        Artista artista = new Artista();
        artista.setBiografia("Banda británica de rock alternativo.");
        artista.setUsuario(usuario);
        artista = artistaRepository.save(artista);
        usuario.setArtista(artista);
        usuarioRepository.save(usuario);

        Playlist playlist = new Playlist();
        playlist.setNombre("Rock Alternativo");
        playlist.setDescripcion("Las mejores canciones de rock alternativo.");
        playlist.setUsuario(usuario);
        playlist.setFechaCreacion(java.time.LocalDateTime.now());
        playlist = playlistRepository.save(playlist);

        Cancion cancion1 = new Cancion();
        cancion1.setTitulo("Yellow");
        cancion1.setArtista(artista);
        cancion1.setGenero("Rock Alternativo");
        cancion1.setDuracion("4:26");
        cancion1.setFechaSubida(java.time.LocalDateTime.now());
        cancion1 = cancionRepository.save(cancion1);
        CancionPlaylist cancionPlaylist1 = new CancionPlaylist();
        cancionPlaylist1.setCancion(cancion1);
        cancionPlaylist1.setPlaylist(playlist);
        cancionPlaylist1.setOrden(2);
        cancionPlaylist1 = cancionPlaylistRepository.save(cancionPlaylist1);

        Cancion cancion2 = new Cancion();
        cancion2.setTitulo("Fix You");
        cancion2.setArtista(artista);
        cancion2.setGenero("Rock Alternativo");
        cancion2.setDuracion("4:55");
        cancion2.setFechaSubida(java.time.LocalDateTime.now());
        cancion2 = cancionRepository.save(cancion2);
        CancionPlaylist cancionPlaylist2 = new CancionPlaylist();
        cancionPlaylist2.setCancion(cancion2);
        cancionPlaylist2.setPlaylist(playlist);
        cancionPlaylist2.setOrden(1);
        cancionPlaylist2 = cancionPlaylistRepository.save(cancionPlaylist2);

        Cancion cancion3 = new Cancion();
        cancion3.setTitulo("Viva la vida");
        cancion3.setArtista(artista);
        cancion3.setGenero("Rock Alternativo");
        cancion3.setDuracion("4:02");
        cancion3.setFechaSubida(java.time.LocalDateTime.now());
        cancion3 = cancionRepository.save(cancion3);
        CancionPlaylist cancionPlaylist3 = new CancionPlaylist();
        cancionPlaylist3.setCancion(cancion3);
        cancionPlaylist3.setPlaylist(playlist);
        cancionPlaylist3.setOrden(3);
        cancionPlaylist3 = cancionPlaylistRepository.save(cancionPlaylist3);

        var cancionesEnPlaylistOpt = cancionPlaylistRepository.findByPlaylistIdPlaylistOrderByOrdenAsc(playlist.getIdPlaylist());
        var cancionesEnPlaylist = cancionesEnPlaylistOpt.orElse(Collections.emptyList());
        assertEquals(3, cancionesEnPlaylist.size());
        assertEquals("Fix You", cancionesEnPlaylist.get(0).getCancion().getTitulo()); // Primero
        assertEquals("Yellow", cancionesEnPlaylist.get(1).getCancion().getTitulo()); // Segundo
        assertEquals("Viva la vida", cancionesEnPlaylist.get(2).getCancion().getTitulo()); // Tercero
        // limpiar
        cancionPlaylistRepository.delete(cancionPlaylist1);
        cancionPlaylistRepository.delete(cancionPlaylist2);
        cancionPlaylistRepository.delete(cancionPlaylist3);
        playlistRepository.delete(playlist);
        cancionRepository.delete(cancion3);
        cancionRepository.delete(cancion2);
        cancionRepository.delete(cancion1);
        artistaRepository.delete(artista);
        usuarioRepository.delete(usuario);
    }

    /** Test para verificar que findByPlaylistIdPlaylistOrderByOrdenDesc
     * devuelve las canciones en el orden correcto (descendente) según
     * el campo 'orden' en la entidad CancionPlaylist.
     */
    @Test
    void findByPlaylistIdPlaylistOrderByOrdenDesc_Test() {
        Usuario usuario = new Usuario();
        usuario.setUsername("Radiohead");
        usuario.setEmail("radio@head.com");
        usuario.setPassword("karmaPolice");
        usuario = usuarioRepository.save(usuario);
        Artista artista = new Artista();
        artista.setBiografia("Banda británica de rock alternativo.");
        artista.setUsuario(usuario);
        artista = artistaRepository.save(artista);
        usuario.setArtista(artista);
        usuarioRepository.save(usuario);

        Playlist playlist = new Playlist();
        playlist.setNombre("Rock Alternativo 2");
        playlist.setDescripcion("Más canciones de rock alternativo.");
        playlist.setUsuario(usuario);
        playlist.setFechaCreacion(java.time.LocalDateTime.now());
        playlist = playlistRepository.save(playlist);
        Cancion cancion1 = new Cancion();
        cancion1.setTitulo("Creep");
        cancion1.setArtista(artista);
        cancion1.setGenero("Rock Alternativo");
        cancion1.setDuracion("3:56");
        cancion1.setFechaSubida(java.time.LocalDateTime.now());
        cancion1 = cancionRepository.save(cancion1);
        CancionPlaylist cancionPlaylist1 = new CancionPlaylist();
        cancionPlaylist1.setCancion(cancion1);
        cancionPlaylist1.setPlaylist(playlist);
        cancionPlaylist1.setOrden(1);
        cancionPlaylist1 = cancionPlaylistRepository.save(cancionPlaylist1);

        Cancion cancion2 = new Cancion();
        cancion2.setTitulo("Karma Police");
        cancion2.setArtista(artista);
        cancion2.setGenero("Rock Alternativo");
        cancion2.setDuracion("4:21");
        cancion2.setFechaSubida(java.time.LocalDateTime.now());
        cancion2 = cancionRepository.save(cancion2);
        CancionPlaylist cancionPlaylist2 = new CancionPlaylist();
        cancionPlaylist2.setCancion(cancion2);
        cancionPlaylist2.setPlaylist(playlist);
        cancionPlaylist2.setOrden(2);
        cancionPlaylist2 = cancionPlaylistRepository.save(cancionPlaylist2);

        Cancion cancion3 = new Cancion();
        cancion3.setTitulo("No Surprises");
        cancion3.setArtista(artista);
        cancion3.setGenero("Rock Alternativo");
        cancion3.setDuracion("3:49");
        cancion3.setFechaSubida(java.time.LocalDateTime.now());
        cancion3 = cancionRepository.save(cancion3);
        CancionPlaylist cancionPlaylist3 = new CancionPlaylist();
        cancionPlaylist3.setCancion(cancion3);
        cancionPlaylist3.setPlaylist(playlist);
        cancionPlaylist3.setOrden(3);
        cancionPlaylist3 = cancionPlaylistRepository.save(cancionPlaylist3);

        var cancionesEnPlaylistOpt = cancionPlaylistRepository.findByPlaylistIdPlaylistOrderByOrdenDesc(playlist.getIdPlaylist());
        var cancionesEnPlaylist = cancionesEnPlaylistOpt.orElse(Collections.emptyList());
        assertEquals(3, cancionesEnPlaylist.size());
        assertEquals("No Surprises", cancionesEnPlaylist.get(0).getCancion().getTitulo()); // Primero
        assertEquals("Karma Police", cancionesEnPlaylist.get(1).getCancion().getTitulo()); // Segundo
        assertEquals("Creep", cancionesEnPlaylist.get(2).getCancion().getTitulo()); // Tercero
        // limpiar
        cancionPlaylistRepository.delete(cancionPlaylist1);
        cancionPlaylistRepository.delete(cancionPlaylist2);
        cancionPlaylistRepository.delete(cancionPlaylist3);
        playlistRepository.delete(playlist);
        cancionRepository.delete(cancion3);
        cancionRepository.delete(cancion2);
        cancionRepository.delete(cancion1);
        artistaRepository.delete(artista);
        usuarioRepository.delete(usuario);
    }

    /** Test para verificar la correcta recuperación
     * de las canciones asociadas a una playlist específica
     * filtradas por género utilizando el metodo
     * findByPlaylistIdAndCancionGeneroInOrderByOrdenAsc.
     */
    @Test
    void findByCancionGeneroInPlaylistTest() {
        Usuario usuario = new Usuario();
        usuario.setUsername("Hombre G");
        usuario.setEmail("hombres@gmail.com");
        usuario.setPassword("devuelvemeaMiChica");
        usuario = usuarioRepository.save(usuario);
        Artista artista = new Artista();
        artista.setBiografia("Banda española de pop rock.");
        artista.setUsuario(usuario);
        artista = artistaRepository.save(artista);
        usuario.setArtista(artista);
        usuarioRepository.save(usuario);

        Playlist playlist = new Playlist();
        playlist.setNombre("Pop Rock Español");
        playlist.setDescripcion("Las mejores canciones de pop rock español.");
        playlist.setUsuario(usuario);
        playlist.setFechaCreacion(java.time.LocalDateTime.now());
        playlist = playlistRepository.save(playlist);

        Cancion cancion1 = new Cancion();
        cancion1.setTitulo("Devuélveme a mi chica");
        cancion1.setArtista(artista);
        cancion1.setGenero("Pop");
        cancion1.setDuracion("3:30");
        cancion1.setFechaSubida(java.time.LocalDateTime.now());
        cancion1 = cancionRepository.save(cancion1);
        CancionPlaylist cancionPlaylist1 = new CancionPlaylist();
        cancionPlaylist1.setCancion(cancion1);
        cancionPlaylist1.setPlaylist(playlist);
        cancionPlaylist1.setOrden(1);
        cancionPlaylist1 = cancionPlaylistRepository.save(cancionPlaylist1);

        Cancion cancion2 = new Cancion();
        cancion2.setTitulo("Sueltate el pelo");
        cancion2.setArtista(artista);
        cancion2.setGenero("Rock");
        cancion2.setDuracion("4:00");
        cancion2.setFechaSubida(java.time.LocalDateTime.now());
        cancion2 = cancionRepository.save(cancion2);
        CancionPlaylist cancionPlaylist2 = new CancionPlaylist();
        cancionPlaylist2.setCancion(cancion2);
        cancionPlaylist2.setPlaylist(playlist);
        cancionPlaylist2.setOrden(2);
        cancionPlaylist2 = cancionPlaylistRepository.save(cancionPlaylist2);

        // Por Genero Pop
        List<String> generosBuscados = Arrays.asList("Pop");
        var cancionesEnPlaylistOpt = cancionPlaylistRepository.findByPlaylistIdAndCancionGeneroInOrderByOrdenAsc(playlist.getIdPlaylist(), generosBuscados);
        var cancionesEnPlaylist = cancionesEnPlaylistOpt.orElse(Collections.emptyList());
        assertEquals(1, cancionesEnPlaylist.size());
        assertEquals("Devuélveme a mi chica", cancionesEnPlaylist.get(0).getCancion().getTitulo());

        // Por Genero Rock
        List<String> generosBuscados2 = Arrays.asList("Rock");
        var cancionesEnPlaylistOpt2 = cancionPlaylistRepository.findByPlaylistIdAndCancionGeneroInOrderByOrdenAsc(playlist.getIdPlaylist(), generosBuscados2);
        var cancionesEnPlaylist2 = cancionesEnPlaylistOpt2.orElse(Collections.emptyList());
        assertEquals(1, cancionesEnPlaylist2.size());
        assertEquals("Sueltate el pelo", cancionesEnPlaylist2.get(0).getCancion().getTitulo());

        // Por Genero Rock y Pop (2 genrero)
        List<String> generosBuscados3 = Arrays.asList("Rock", "Pop");
        var cancionesEnPlaylistOpt3 = cancionPlaylistRepository.findByPlaylistIdAndCancionGeneroInOrderByOrdenAsc(playlist.getIdPlaylist(), generosBuscados3);
        var cancionesEnPlaylist3 = cancionesEnPlaylistOpt3.orElse(Collections.emptyList());
        assertEquals(2, cancionesEnPlaylist3.size());
        assertEquals("Devuélveme a mi chica", cancionesEnPlaylist3.get(0).getCancion().getTitulo());
        assertEquals("Sueltate el pelo", cancionesEnPlaylist3.get(1).getCancion().getTitulo());
        assertTrue(cancionesEnPlaylist3.getFirst().getOrden() < cancionesEnPlaylist3.getLast().getOrden());

        // limpiar
        cancionPlaylistRepository.delete(cancionPlaylist1);
        cancionPlaylistRepository.delete(cancionPlaylist2);
        playlistRepository.delete(playlist);
        cancionRepository.delete(cancion2);
        cancionRepository.delete(cancion1);
        artistaRepository.delete(artista);
        usuarioRepository.delete(usuario);

    }

    /** Test para verificar la correcta recuperación
     * de las canciones asociadas a una playlist específica
     * filtradas por título utilizando el metodo
     * findByPlaylistIdAndCancionTituloInOrderByOrdenAsc.
     */
    @Test
    void findByCancionTituloInPlaylistTest() {
        // Configuración inicial: crear usuario, artista, canciones, playlist y cancionPlaylist
        Usuario usuario = new Usuario();
        usuario.setUsername("Peso Pluma");
        usuario.setEmail("pesopluma@gmail.com");
        usuario.setPassword("bzs");
        usuario = usuarioRepository.save(usuario);
        Artista artista = new Artista();
        artista.setBiografia("Cantante mexicano de música regional.");
        artista.setUsuario(usuario);
        artista = artistaRepository.save(artista);
        usuario.setArtista(artista);
        usuarioRepository.save(usuario);

        Playlist playlist = new Playlist();
        playlist.setNombre("Música Regional");
        playlist.setDescripcion("Las mejores canciones de música regional.");
        playlist.setUsuario(usuario);
        playlist.setFechaCreacion(java.time.LocalDateTime.now());
        playlist = playlistRepository.save(playlist);

        Cancion cancion1 = new Cancion();
        cancion1.setTitulo("PRC");
        cancion1.setArtista(artista);
        cancion1.setGenero("Regional Mexicano");
        cancion1.setDuracion("3:10");
        cancion1.setFechaSubida(java.time.LocalDateTime.now());
        cancion1 = cancionRepository.save(cancion1);

        CancionPlaylist cancionPlaylist1 = new CancionPlaylist();
        cancionPlaylist1.setCancion(cancion1);
        cancionPlaylist1.setPlaylist(playlist);
        cancionPlaylist1.setOrden(1);
        cancionPlaylist1 = cancionPlaylistRepository.save(cancionPlaylist1);

        Cancion cancion2 = new Cancion();
        cancion2.setTitulo("Ella baila sola");
        cancion2.setArtista(artista);
        cancion2.setGenero("Regional Mexicano");
        cancion2.setDuracion("3:45");
        cancion2.setFechaSubida(java.time.LocalDateTime.now());
        cancion2 = cancionRepository.save(cancion2);
        CancionPlaylist cancionPlaylist2 = new CancionPlaylist();
        cancionPlaylist2.setCancion(cancion2);
        cancionPlaylist2.setPlaylist(playlist);
        cancionPlaylist2.setOrden(2);
        cancionPlaylist2 = cancionPlaylistRepository.save(cancionPlaylist2);

        // Buscar por título "PRC"
        List<String> titulosBuscados = Arrays.asList("PRC");
        var cancionesEnPlaylistOpt = cancionPlaylistRepository.findByPlaylistIdAndCancionTituloInOrderByOrdenAsc(playlist.getIdPlaylist(), titulosBuscados);
        var cancionesEnPlaylist = cancionesEnPlaylistOpt.orElse(Collections.emptyList());
        assertEquals(1, cancionesEnPlaylist.size());
        assertEquals("PRC", cancionesEnPlaylist.get(0).getCancion().getTitulo());

        List<String> titulosBuscados2 = Arrays.asList("Ella baila sola");
        var cancionesEnPlaylistOpt2 = cancionPlaylistRepository.findByPlaylistIdAndCancionTituloInOrderByOrdenAsc(playlist.getIdPlaylist(), titulosBuscados2);
        var cancionesEnPlaylist2 = cancionesEnPlaylistOpt2.orElse(Collections.emptyList());
        assertEquals(1, cancionesEnPlaylist2.size());
        assertEquals("Ella baila sola", cancionesEnPlaylist2.get(0).getCancion().getTitulo());

        // limpiar
        cancionPlaylistRepository.delete(cancionPlaylist1);
        cancionPlaylistRepository.delete(cancionPlaylist2);
        playlistRepository.delete(playlist);
        cancionRepository.delete(cancion2);
        cancionRepository.delete(cancion1);
        artistaRepository.delete(artista);
        usuarioRepository.delete(usuario);

    }

    /** Test para verificar la correcta recuperación
     * de las canciones asociadas a una playlist específica
     * filtradas por artista utilizando el metodo
     * findByPlaylistIdAndCancionArtistaNombreOrderByOrdenAsc.
     */
    @Test
    void findByCancionArtistaInPlaylistTest() {
        Usuario usuario = new Usuario();
        usuario.setUsername("Leiva");
        usuario.setEmail("leiva@ojo.com");
        usuario.setPassword("pólvora");
        usuario = usuarioRepository.save(usuario);
        Artista artista = new Artista();
        artista.setBiografia("Cantante y compositor español de rock.");
        artista.setUsuario(usuario);
        artista = artistaRepository.save(artista);
        usuario.setArtista(artista);
        usuarioRepository.save(usuario);

        Playlist playlist = new Playlist();
        playlist.setNombre("Rock Español");
        playlist.setDescripcion("Las mejores canciones de rock español.");
        playlist.setUsuario(usuario);
        playlist.setFechaCreacion(java.time.LocalDateTime.now());
        playlist = playlistRepository.save(playlist);

        Cancion cancion1 = new Cancion();
        cancion1.setTitulo("Lobos");
        cancion1.setArtista(artista);
        cancion1.setGenero("Rock");
        cancion1.setDuracion("4:15");
        cancion1.setFechaSubida(java.time.LocalDateTime.now());
        cancion1 = cancionRepository.save(cancion1);
        CancionPlaylist cancionPlaylist1 = new CancionPlaylist();
        cancionPlaylist1.setCancion(cancion1);
        cancionPlaylist1.setPlaylist(playlist);
        cancionPlaylist1.setOrden(1);
        cancionPlaylist1 = cancionPlaylistRepository.save(cancionPlaylist1);

        // Buscar por artista "Leiva"
        List<Artista> artistasBuscados = Arrays.asList(artista);
        var cancionesEnPlaylistOpt = cancionPlaylistRepository.findByPlaylistIdAndCancionArtistaNombreOrderByOrdenAsc(playlist.getIdPlaylist(), "Leiva");
        var cancionesEnPlaylist = cancionesEnPlaylistOpt.orElse(Collections.emptyList());
        assertEquals(1, cancionesEnPlaylist.size());
        assertEquals("Lobos", cancionesEnPlaylist.get(0).getCancion().getTitulo());

        // limpiar
        cancionPlaylistRepository.delete(cancionPlaylist1);
        playlistRepository.delete(playlist);
        cancionRepository.delete(cancion1);
        artistaRepository.delete(artista);
        usuarioRepository.delete(usuario);
    }

    /** Test para verificar que al eliminar una canción,
     * también se elimina la relación correspondiente
     * en la tabla CancionPlaylist.
     */
    @Test
    void removeCancionAndCancionPlaylistFromPlaylistTest() {
        Usuario usuario = new Usuario();
        usuario.setUsername("The Offspring");
        usuario.setEmail("offsrping@mail.com");
        usuario.setPassword("selfEsteem");
        usuario = usuarioRepository.save(usuario);
        Artista artista = new Artista();
        artista.setBiografia("Banda estadounidense de punk rock.");
        artista.setUsuario(usuario);
        artista = artistaRepository.save(artista);
        usuario.setArtista(artista);
        usuarioRepository.save(usuario);

        Playlist playlist = new Playlist();
        playlist.setNombre("Punk Rock Anthems");
        playlist.setDescripcion("Las mejores canciones de punk rock.");
        playlist.setUsuario(usuario);
        playlist.setFechaCreacion(java.time.LocalDateTime.now());
        playlist = playlistRepository.save(playlist);

        Cancion cancion = new Cancion();
        cancion.setTitulo("Why Don't You Get a Job?");
        cancion.setArtista(artista);
        cancion.setGenero("Punk Rock");
        cancion.setDuracion("2:48");
        cancion.setFechaSubida(java.time.LocalDateTime.now());
        cancion = cancionRepository.save(cancion);

        CancionPlaylist cancionPlaylist = new CancionPlaylist();
        cancionPlaylist.setCancion(cancion);
        cancionPlaylist.setPlaylist(playlist);
        cancionPlaylist.setOrden(1);
        cancionPlaylist = cancionPlaylistRepository.save(cancionPlaylist);

        // Verificar que la canción y la relación existen
        Optional<CancionPlaylist> cpOpt = cancionPlaylistRepository.findById(cancionPlaylist.getId());
        assertTrue(cpOpt.isPresent());
        Optional<Cancion> cOpt = cancionRepository.findById(cancion.getIdCancion());
        assertTrue(cOpt.isPresent());

        // Eliminar la canción
        cancionRepository.delete(cancion);
        // Verificar que la relación CancionPlaylist también se ha eliminado
        Optional<CancionPlaylist> deletedCpOpt = cancionPlaylistRepository.findById(cancionPlaylist.getId());
        assertFalse(deletedCpOpt.isPresent());
        // Verificar que la canción ya no existe
        Optional<Cancion> deletedCOpt = cancionRepository.findById(cancion.getIdCancion());
        assertFalse(deletedCOpt.isPresent());

        // limpieza de datos
        playlistRepository.delete(playlist);
        artistaRepository.delete(artista);
        usuarioRepository.delete(usuario);
    }

    /**
     * Test para verificar que al eliminar un artista,
     * también se eliminan las canciones asociadas
     * y las relaciones correspondientes en la tabla CancionPlaylist.
     */
    @Test
    void removeArtistaAndCancionPlaylistFromPlaylistTest() {
        Usuario usuario = new Usuario();
        usuario.setUsername("The Weeknd");
        usuario.setEmail("itsalmostweek@end.com");
        usuario.setPassword("blindingLights");
        usuario = usuarioRepository.save(usuario);
        Artista artista = new Artista();
        artista.setBiografia("Cantante canadiense de R&B y pop.");
        artista.setUsuario(usuario);
        artista = artistaRepository.save(artista);
        usuario.setArtista(artista);
        usuarioRepository.save(usuario);

        Playlist playlist = new Playlist();
        playlist.setNombre("Top Hits");
        playlist.setDescripcion("Las mejores canciones del momento.");
        playlist.setUsuario(usuario);
        playlist.setFechaCreacion(java.time.LocalDateTime.now());
        playlist = playlistRepository.save(playlist);

        Cancion cancion = new Cancion();
        cancion.setTitulo("Blinding Lights");
        cancion.setArtista(artista);
        cancion.setGenero("Pop");
        cancion.setDuracion("3:20");
        cancion.setFechaSubida(java.time.LocalDateTime.now());
        cancion = cancionRepository.save(cancion);

        CancionPlaylist cancionPlaylist = new CancionPlaylist();
        cancionPlaylist.setCancion(cancion);
        cancionPlaylist.setPlaylist(playlist);
        cancionPlaylist.setOrden(1);
        cancionPlaylist = cancionPlaylistRepository.save(cancionPlaylist);

        // Verificar que la canción y la relación existen
        Optional<CancionPlaylist> cpOpt = cancionPlaylistRepository.findById(cancionPlaylist.getId());
        assertTrue(cpOpt.isPresent());
        Optional<Cancion> cOpt = cancionRepository.findById(cancion.getIdCancion());
        assertTrue(cOpt.isPresent());
        // Eliminar el artista
        artistaRepository.delete(artista);
        // Verificar que la canción ya no existe
        Optional<Cancion> deletedCOpt = cancionRepository.findById(cancion.getIdCancion());
        assertFalse(deletedCOpt.isPresent());
        // Verificar que la relación CancionPlaylist también se ha eliminado
        Optional<CancionPlaylist> deletedCpOpt = cancionPlaylistRepository.findById(cancionPlaylist.getId());
        assertFalse(deletedCpOpt.isPresent());

        // limpieza de datos
        playlistRepository.delete(playlist);
        usuarioRepository.delete(usuario);

    }

    /**
     * Test para verificar que al eliminar un usuario,
     * también se eliminan las canciones asociadas
     * y las relaciones correspondientes en la tabla CancionPlaylist.
     */
    @Test
    void removeUsuarioAndCancionPlaylistFromPlaylistTest() {
        Usuario usuario = new Usuario();
        usuario.setUsername("The Cranberries");
        usuario.setEmail("cranberries@zom.com");
        usuario.setPassword("dreams");
        usuario = usuarioRepository.save(usuario);
        Artista artista = new Artista();
        artista.setBiografia("Banda irlandesa de rock alternativo.");
        artista.setUsuario(usuario);
        artista = artistaRepository.save(artista);
        usuario.setArtista(artista);
        usuarioRepository.save(usuario);

        Playlist playlist = new Playlist();
        playlist.setNombre("Rock Alternativo Internacional");
        playlist.setDescripcion("Las mejores canciones de rock alternativo internacional.");
        playlist.setUsuario(usuario);
        playlist.setFechaCreacion(java.time.LocalDateTime.now());
        playlist = playlistRepository.save(playlist);

        Cancion cancion = new Cancion();
        cancion.setTitulo("Linger");
        cancion.setArtista(artista);
        cancion.setGenero("Rock Alternativo");
        cancion.setDuracion("4:34");
        cancion.setFechaSubida(java.time.LocalDateTime.now());
        cancion = cancionRepository.save(cancion);
        CancionPlaylist cancionPlaylist = new CancionPlaylist();
        cancionPlaylist.setCancion(cancion);
        cancionPlaylist.setPlaylist(playlist);
        cancionPlaylist.setOrden(1);
        cancionPlaylist = cancionPlaylistRepository.save(cancionPlaylist);

        // Verificar que la canción y la relación existen
        Optional<CancionPlaylist> cpOpt = cancionPlaylistRepository.findById(cancionPlaylist.getId());
        assertTrue(cpOpt.isPresent());
        Optional<Cancion> cOpt = cancionRepository.findById(cancion.getIdCancion());
        assertTrue(cOpt.isPresent());
        // Eliminar el usuario
        usuarioRepository.delete(usuario);
        // Verificar que la canción ya no existe
        Optional<Cancion> deletedCOpt = cancionRepository.findById(cancion.getIdCancion());
        assertFalse(deletedCOpt.isPresent());
        // Verificar que la relación CancionPlaylist también se ha eliminado
        Optional<CancionPlaylist> deletedCpOpt = cancionPlaylistRepository.findById(cancionPlaylist.getId());
        assertFalse(deletedCpOpt.isPresent());
    }

    /**
     * Test para verificar que al eliminar una playlist,
     * también se eliminan las relaciones correspondientes
     * en la tabla CancionPlaylist.
     */
    @Test
    void deletePlaylistAndCascadeDeleteCancionPlaylistTest() {
        Usuario usuario = new Usuario();
        usuario.setUsername("Twenty One Pilots");
        usuario.setEmail("21pil@ts.com");
        usuario.setPassword("stressedout");
        usuario = usuarioRepository.save(usuario);
        Artista artista = new Artista();
        artista.setBiografia("Dúo estadounidense de música alternativa.");
        artista.setUsuario(usuario);
        artista = artistaRepository.save(artista);
        usuario.setArtista(artista);
        usuarioRepository.save(usuario);

        Playlist playlist = new Playlist();
        playlist.setNombre("Alternative Hits");
        playlist.setDescripcion("Las mejores canciones alternativas.");
        playlist.setUsuario(usuario);
        playlist.setFechaCreacion(java.time.LocalDateTime.now());
        playlist = playlistRepository.save(playlist);

        Cancion cancion = new Cancion();
        cancion.setTitulo("Ride");
        cancion.setArtista(artista);
        cancion.setGenero("Alternative");
        cancion.setDuracion("3:34");
        cancion.setFechaSubida(java.time.LocalDateTime.now());
        cancion = cancionRepository.save(cancion);
        CancionPlaylist cancionPlaylist = new CancionPlaylist();
        cancionPlaylist.setCancion(cancion);
        cancionPlaylist.setPlaylist(playlist);
        cancionPlaylist.setOrden(1);
        cancionPlaylist = cancionPlaylistRepository.save(cancionPlaylist);

        // Verificar que la relación existe
        Optional<CancionPlaylist> cpOpt = cancionPlaylistRepository.findById(cancionPlaylist.getId());
        assertTrue(cpOpt.isPresent());
        // Eliminar la playlist
        playlistRepository.delete(playlist);
        // Verificar que la relación CancionPlaylist también se ha eliminado
        Optional<CancionPlaylist> deletedCpOpt = cancionPlaylistRepository.findById(cancionPlaylist.getId());
        assertFalse(deletedCpOpt.isPresent());
        // limpieza de datos
        cancionRepository.delete(cancion);
        artistaRepository.delete(artista);
        usuarioRepository.delete(usuario);

    }
   }
