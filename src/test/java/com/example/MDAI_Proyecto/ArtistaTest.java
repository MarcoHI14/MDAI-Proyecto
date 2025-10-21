package com.example.MDAI_Proyecto;

import com.example.MDAI_Proyecto.data.model.Artista;
import com.example.MDAI_Proyecto.data.model.Cancion;
import com.example.MDAI_Proyecto.data.model.Usuario;
import com.example.MDAI_Proyecto.data.repository.ArtistaRepository;
import com.example.MDAI_Proyecto.data.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
public class ArtistaTest {

    @Autowired
    private ArtistaRepository artistaRepository;

    @Autowired
    private UsuarioRepository userRepository;

    @Test
    void crearArtistaComoUsuarioTest() {
        // Por errores pasados...
        artistaRepository.deleteAll();
        userRepository.deleteAll();

        Usuario usuario = new Usuario();
        usuario.setUsername("Bunbury");
        usuario.setEmail("bunbury@hotmail.com");
        usuario.setPassword("enrique");
        Usuario usuarioGuardado = userRepository.save(usuario);

        Artista artista = new Artista();
        artista.setUsuario(usuarioGuardado);
        artista.setBiografia("Soy un artista español de rock.");
        Artista artistaGuardado = artistaRepository.save(artista);

        assertNotNull(artistaGuardado.getIdArtista());
        assertEquals("Soy un artista español de rock.", artistaGuardado.getBiografia());
        assertEquals(usuarioGuardado.getId(), artistaGuardado.getIdArtista());
        assertEquals("Bunbury", artistaGuardado.getUsuario().getUsername());

        Usuario usuario2 = new  Usuario();
        usuario2.setUsername("Leiva");
        usuario2.setEmail("leiva@gmail.com");
        usuario2.setPassword("pereza");
        Usuario usuarioGuardado2 = userRepository.save(usuario2);

        Artista artista2 = new Artista();
        artista2.setUsuario(usuarioGuardado2);
        artista2.setBiografia("Soy un artista español de pop rock.");
        Artista artistaGuardado2 = artistaRepository.save(artista2);

        assertNotNull(artistaGuardado2.getIdArtista());
        assertEquals("Soy un artista español de pop rock.", artistaGuardado2.getBiografia());
        assertEquals(usuarioGuardado2.getId(), artistaGuardado2.getIdArtista());
        assertEquals("Leiva", artistaGuardado2.getUsuario().getUsername());

        assertNotEquals(artistaGuardado.getIdArtista(), artistaGuardado2.getIdArtista());

        try {        // Intentar añadir dos veces al mismo artista
            Artista artistaDuplicado = new Artista();
            artistaDuplicado.setUsuario(usuarioGuardado);
            artistaDuplicado.setBiografia("Intento duplicado de Bunbury.");
            artistaRepository.save(artistaDuplicado);
            fail("Se pudo añadir dos veces al mismo artista, ERROR, no debería ser posible.");
        } catch (Exception e) {
            System.out.println("Excepción capturada correctamente al intentar duplicar un artista: " + e.getMessage());
        }
        artistaRepository.deleteAll();
        userRepository.deleteAll();
        assertEquals(0, artistaRepository.count());
        assertEquals(0, userRepository.count());
    }

    @Test
    void findByUsuarioEmailTest() {
        Usuario usuario = new Usuario();
        usuario.setUsername("Shakira");
        usuario.setEmail("shake@gamil.com");
        usuario.setPassword("hips");
        Usuario usuarioGuardado = userRepository.save(usuario);
        Artista artista = new Artista();
        artista.setUsuario(usuarioGuardado);
        artista.setBiografia("Artista colombiana de pop y reguetón.");
        artistaRepository.save(artista);

        Usuario usuario2 = new Usuario();
        usuario2.setUsername("Fito");
        usuario2.setEmail("fitipaldi@yahoo.com");
        usuario2.setPassword("fitomania");
        Usuario usuarioGuardado2 = userRepository.save(usuario2);
        Artista artista2 = new Artista();
        artista2.setUsuario(usuarioGuardado2);
        artista2.setBiografia("Artista español de rock.");
        artistaRepository.save(artista2);

        Artista encontrado = artistaRepository.findByUsuarioEmail("fitipaldi@yahoo.com").orElse(null);
        assertNotNull(encontrado);
        assertEquals("Fito", encontrado.getUsuario().getUsername());
        assertEquals("Artista español de rock.", encontrado.getBiografia());

        Artista encontrado2 = artistaRepository.findByUsuarioEmail("shake@gamil.com").orElse(null);
        assertNotNull(encontrado2);
        assertEquals("Shakira", encontrado2.getUsuario().getUsername());
        assertEquals("Artista colombiana de pop y reguetón.", encontrado2.getBiografia());

        Artista noEncontrado = artistaRepository.findByUsuarioEmail("sabina@rockmail.com").orElse(null);
        assertNull(noEncontrado);

        artistaRepository.deleteAll();
        userRepository.deleteAll();

        assertEquals(0, artistaRepository.count());
        assertEquals(0, userRepository.count());

    }

    @Test
    void findByUsuarioUsernameTest() {
        Usuario usuario = new Usuario();
        usuario.setUsername("Sabina");
        usuario.setEmail("jsabina@oldmail.com");
        usuario.setPassword("19días");
        Usuario usuarioGuardado = userRepository.save(usuario);
        Artista artista = new Artista();
        artista.setUsuario(usuarioGuardado);
        artista.setBiografia("De los grandes españoles de rock y baladas.");
        artistaRepository.save(artista);

        Usuario usuario2 = new Usuario();
        usuario2.setUsername("Mecano");
        usuario2.setEmail("mecano@gmail.com");
        usuario2.setPassword("hijo de la luna");
        Usuario usuarioGuardado2 = userRepository.save(usuario2);
        Artista artista2 = new Artista();
        artista2.setUsuario(usuarioGuardado2);
        artista2.setBiografia("Grupo español de pop de los 80 y 90.");
        artistaRepository.save(artista2);

        Artista encontrado = artistaRepository.findByUsuarioUsername("Mecano").orElse(null);
        assertNotNull(encontrado);
        assertEquals("Mecano", encontrado.getUsuario().getUsername());
        assertEquals("Grupo español de pop de los 80 y 90.", encontrado.getBiografia());

        Artista encontrado2 = artistaRepository.findByUsuarioUsername("Sabina").orElse(null);
        assertNotNull(encontrado2);
        assertEquals("Sabina", encontrado2.getUsuario().getUsername());
        assertEquals("De los grandes españoles de rock y baladas.", encontrado2.getBiografia());

        Artista noEncontrado = artistaRepository.findByUsuarioUsername("Héroes del Silencio").orElse(null);
        assertNull(noEncontrado);

        artistaRepository.deleteAll();
        userRepository.deleteAll();

        assertEquals(0, artistaRepository.count());
        assertEquals(0, userRepository.count());

    }

    @Test
    void artistaSinUsuarioNoSeCreaTest() {
        Artista artista = new Artista();
        artista.setBiografia("Artista sin usuario asociado.");

        try {
            artistaRepository.save(artista);
            fail("Se pudo guardar un artista sin usuario asociado, ERROR.");
        } catch (Exception e) {
            System.out.println("Excepción capturada correctamente al intentar guardar un artista sin usuario: " + e.getMessage());
        }

        assertEquals(0, artistaRepository.count());
    }

    @Test
    void artistaCancionesTest() {
        Usuario usuario = new Usuario();
        usuario.setUsername("Dua Lipa");
        usuario.setEmail("lip@mail.com");
        usuario.setPassword("newrules");
        Usuario usuarioGuardado = userRepository.save(usuario);
        Artista artista = new Artista();
        artista.setUsuario(usuarioGuardado);
        artista.setBiografia("Artista británica de pop.");
        Artista artistaGuardado = artistaRepository.save(artista);

        assertNotNull(artistaGuardado.getIdArtista());
        assertEquals(0, artistaGuardado.getCanciones().size());

        Cancion cancion1 = new Cancion();
        cancion1.setTitulo("New Rules");
        cancion1.setGenero("Pop");
        cancion1.setArchivoAudio("ruta/a/newrules.mp3");
        cancion1.setDuracion("03:29");
        cancion1.setArtista(artista);
        cancion1.setFechaSubida(java.time.LocalDateTime.now());

        artistaGuardado.addCancion(cancion1);
        artistaRepository.save(artistaGuardado);
        Artista artistaConCancion = artistaRepository.findById(artistaGuardado.getIdArtista()).orElse(null);
        assertNotNull(artistaConCancion);
        assertEquals(1, artistaConCancion.getCanciones().size());

        Usuario usuario2 = new Usuario();
        usuario2.setUsername("Blur");
        usuario2.setEmail("blur@mail.com");
        usuario2.setPassword("song2");
        Usuario usuarioGuardado2 = userRepository.save(usuario2);
        Artista artista2 = new Artista();
        artista2.setUsuario(usuarioGuardado2);
        artista2.setBiografia("Banda británica de rock alternativo.");
        Artista artistaGuardado2 = artistaRepository.save(artista2);

        Cancion cancion2 = new Cancion();
        cancion2.setTitulo("Song 2");
        cancion2.setGenero("Rock Alternativo");
        cancion2.setArchivoAudio("ruta/a/song2.mp3");
        cancion2.setDuracion("02:02");
        cancion2.setArtista(artista2);
        cancion2.setFechaSubida(java.time.LocalDateTime.now());

        artistaGuardado2.addCancion(cancion2);
        artistaRepository.save(artistaGuardado2);
        Artista artista2ConCancion = artistaRepository.findById(artistaGuardado2.getIdArtista()).orElse(null);
        assertNotNull(artista2ConCancion);
        assertEquals(1, artista2ConCancion.getCanciones().size());

        try {
            // asignar misma canción a otro artista -> addCancion debe lanzar excepcin
            artistaGuardado.addCancion(cancion2);
            fail("Se pudo asignar una canción a dos artistas distintos, ERROR.");
        } catch (Exception e) {
            System.out.println("Excepción capturada correctamente al intentar asignar una canción a dos artistas: " + e.getMessage());
        }

        artistaRepository.deleteAll();
        userRepository.deleteAll();

        assertEquals(0, artistaRepository.count());
        assertEquals(0, userRepository.count());

    }
}
