package com.example.MDAI_Proyecto;

import com.example.MDAI_Proyecto.data.model.Artista;
import com.example.MDAI_Proyecto.data.model.Playlist;
import com.example.MDAI_Proyecto.data.model.Usuario;
import com.example.MDAI_Proyecto.data.repository.ArtistaRepository;
import com.example.MDAI_Proyecto.data.repository.CancionRepository;
import com.example.MDAI_Proyecto.data.repository.PlaylistRepository;
import com.example.MDAI_Proyecto.data.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PlaylistTest {

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private CancionRepository cancionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ArtistaRepository artistaRepository;

    /** Prueba para crear y guardar una Playlist asociada a un Usuario */
    @Test
    void crearPlaylistTest() {
        Usuario user = new Usuario();
        user.setUsername("UsuarioPlaylist");
        user.setPassword("pass123");
        user.setEmail("usuario@gmail.com");
        Usuario guardadoUser = usuarioRepository.save(user);
        assertNotNull(guardadoUser.getId());

        // Aquí iría el código para crear y guardar una Playlist asociada al usuario creado
        Playlist playlist = new Playlist();
        playlist.setNombre("Mi Playlist");
        playlist.setDescripcion("Descripción de mi playlist");
        playlist.setUsuario(guardadoUser);
        Playlist guardadaPlaylist = playlistRepository.save(playlist);

        assert guardadaPlaylist.getIdPlaylist() != null;
        assert guardadaPlaylist.getNombre().equals("Mi Playlist");
        assert guardadaPlaylist.getDescripcion().equals("Descripción de mi playlist");
        assert guardadaPlaylist.getUsuario().getId().equals(guardadoUser.getId());
        assert guardadaPlaylist.getUsuario().getUsername().equals("UsuarioPlaylist");
        assert guardadaPlaylist.getUsuario().getEmail().equals("usuario@gmail.com");
    }

    /** Prueba para buscar una Playlist por su nombre */
    @Test
    void findByNombreTest() {

        Usuario user = new Usuario();
        user.setUsername("UsuarioPlaylist");
        user.setPassword("pass123");
        user.setEmail("usuario@gmail.com");
        Usuario guardadoUser = usuarioRepository.save(user);

        // Aquí iría el código para crear y guardar una Playlist asociada al usuario creado
        Playlist playlist2 = new Playlist();
        playlist2.setNombre("Segunda Playlist");
        playlist2.setDescripcion("Descripción de mi playlist");
        playlist2.setUsuario(guardadoUser);

        Playlist playlist3 = new Playlist();
        playlist3.setNombre("Tercera Playlist");
        playlist3.setDescripcion("Descripción de mi playlist");
        playlist3.setUsuario(guardadoUser);

        Playlist playlist4 = new Playlist();
        playlist4.setNombre("Cuarta Playlist");
        playlist4.setDescripcion("Descripción de mi playlist");
        playlist4.setUsuario(guardadoUser);

        playlistRepository.saveAll(List.of(playlist2, playlist3, playlist4));

        Playlist encontrada2 = playlistRepository.findByNombre("Segunda Playlist").orElse(null);
        assertNotNull(encontrada2);
        assertEquals("Segunda Playlist", encontrada2.getNombre());

        Playlist encontrada3 = playlistRepository.findByNombre("Tercera Playlist").orElse(null);
        assertNotNull(encontrada3);
        assertEquals("Tercera Playlist", encontrada3.getNombre());

        Playlist encontrada4 = playlistRepository.findByNombre("Cuarta Playlist").orElse(null);
        assertNotNull(encontrada4);
        assertEquals("Cuarta Playlist", encontrada4.getNombre());

        playlistRepository.deleteAll(List.of(playlist2, playlist3, playlist4));
    }

    /** Prueba para buscar una Playlist por el nombre de usuario del Usuario asociado */
    @Test
    void findByUsuario_Username() {
        Usuario usuarioCreate = new Usuario();
        usuarioCreate.setUsername("User");
        usuarioCreate.setPassword("userpass");
        usuarioCreate.setEmail("user@dio.com");
        Usuario savedUser = usuarioRepository.save(usuarioCreate);
        assertNotNull(savedUser.getId());

        Usuario usuarioCreate2 = new Usuario();
        usuarioCreate2.setUsername("Edward");
        usuarioCreate2.setPassword("fma2003");
        usuarioCreate2.setEmail("brother@alchemist.com");
        Usuario savedUser2 = usuarioRepository.save(usuarioCreate2);
        assertNotNull(savedUser2.getId());

        Usuario usuarioCreate3 = new Usuario();
        usuarioCreate3.setUsername("Goodman");
        usuarioCreate3.setPassword("brba_btcasa");
        usuarioCreate3.setEmail("bettercall@saul.com");
        Usuario savedUser3 = usuarioRepository.save(usuarioCreate3);

        Playlist playlistCreate = new Playlist();
        playlistCreate.setNombre("Playlist1");
        playlistCreate.setDescripcion("Playlist creada para asignar artista");
        playlistCreate.setFechaCreacion(LocalDateTime.now());
        playlistCreate.setUsuario(savedUser);

        Playlist playlistCreate1 = new Playlist();
        playlistCreate1.setNombre("Playlist2");
        playlistCreate1.setDescripcion("Playlist creada para funcionamiento");
        playlistCreate1.setFechaCreacion(LocalDateTime.now());
        playlistCreate1.setUsuario(savedUser2);

        Playlist playlistCreate2 = new Playlist();
        playlistCreate2.setNombre("Playlist3");
        playlistCreate2.setDescripcion("Playlist creada para comprobar funcionamiento");
        playlistCreate2.setFechaCreacion(LocalDateTime.now());
        playlistCreate2.setUsuario(savedUser3);

        playlistRepository.saveAll(List.of(playlistCreate, playlistCreate1, playlistCreate2));

        Playlist encontrada = playlistRepository.findByUsuario_Username("User").orElse(null);
        assertNotNull(encontrada);
        assertEquals("User", encontrada.getUsuario().getUsername());

        Playlist encontrada1 = playlistRepository.findByUsuario_Username("Edward").orElse(null);
        assertNotNull(encontrada1);
        assertEquals("Edward", encontrada1.getUsuario().getUsername());

        Playlist encontrada2 = playlistRepository.findByUsuario_Username("Goodman").orElse(null);
        assertNotNull(encontrada2);
        assertEquals("Goodman", encontrada2.getUsuario().getUsername());

    }

    /** Prueba CRUD completa para la entidad Playlist */
    @Test
    void CRUDTest () {

        //CREATE
        Playlist playlistCreate = new Playlist();
        playlistCreate.setNombre("PlaylistCRUD");
        playlistCreate.setDescripcion("Playlist creada para comprobar funcionamiento CRUD");
        playlistCreate.setFechaCreacion(LocalDateTime.now());
        Playlist savedPlaylist = playlistRepository.save(playlistCreate);
        assertNotNull(savedPlaylist);

        //READ
        Optional<Playlist> readPlaylist = playlistRepository.findById(savedPlaylist.getIdPlaylist());
        assertTrue(readPlaylist.isPresent());
        assertEquals("PlaylistCRUD", readPlaylist.get().getNombre());


        Usuario usuarioCreate = new Usuario();
        usuarioCreate.setUsername("CRUDUser");
        usuarioCreate.setPassword("crudpass");
        usuarioCreate.setEmail("crusaders@dio.com");
        Usuario savedUser = usuarioRepository.save(usuarioCreate);
        assertNotNull(savedUser.getId());
        //UPDATE
        readPlaylist.get().setUsuario(savedUser);
        readPlaylist.get().setDescripcion("Playlist Modificada");
        assertEquals(savedUser, readPlaylist.get().getUsuario());
        assertEquals("Playlist Modificada", readPlaylist.get().getDescripcion());

        //DELETE
        playlistRepository.delete(savedPlaylist);
        Optional<Playlist> deletedPlaylist = playlistRepository.findById(savedPlaylist.getIdPlaylist());
        assertFalse(deletedPlaylist.isPresent());
        assertNotNull(savedUser); //Comprobamos que el usuario asignado a la playlist no se borre
    }

    /** Prueba para verificar que al eliminar un Usuario se eliminan sus Playlists asociadas */
    @Test
    void eliminarUsuarioEliminaPlaylist () {
        Usuario user = new Usuario();
        user.setUsername("TwoDoorCinemaClub");
        user.setPassword("music123");
        user.setEmail("movietheater@gmail.com");
        Usuario guardadoUser = usuarioRepository.save(user);
        Artista artista = new Artista();
        artista.setUsuario(guardadoUser);
        artista.setBiografia("Biografía de TDCC");
        Artista guardadoArtista = artistaRepository.save(artista);

        Playlist playlist = new Playlist();
        playlist.setNombre("Tourist History");
        playlist.setDescripcion("Album de debut de TDCC");
        playlist.setUsuario(guardadoUser);
        Playlist guardadaPlaylist = playlistRepository.save(playlist);

        // Verificar que la playlist está guardada
        assertNotNull(guardadaPlaylist.getIdPlaylist());

        // Eliminar el usuario
        usuarioRepository.delete(guardadoUser);

        // Verificar que la playlist también ha sido eliminada
        Optional<Playlist> deletedPlaylist = playlistRepository.findById(guardadaPlaylist.getIdPlaylist());
        assertFalse(deletedPlaylist.isPresent());
    }
}
