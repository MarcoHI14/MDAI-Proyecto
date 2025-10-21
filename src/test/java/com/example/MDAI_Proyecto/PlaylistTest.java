package com.example.MDAI_Proyecto;

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
}
