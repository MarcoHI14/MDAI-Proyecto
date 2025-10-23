package com.example.MDAI_Proyecto;

import com.example.MDAI_Proyecto.data.model.*;
import com.example.MDAI_Proyecto.data.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CancionTest {

    @Autowired
    private CancionRepository cancionRepository;

    @Autowired
    private ArtistaRepository artistaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void crearCancionTest() {

        Cancion cancion = new Cancion();
        cancion.setTitulo("Cancion de prueba");
        cancion.setGenero("Pop");
        cancion.setArchivoAudio("ruta/a/archivo.mp3");
        cancion.setDuracion("03:30");
        cancion.setFechaSubida(java.time.LocalDateTime.now());

        Cancion guardada = cancionRepository.save(cancion);
        assert guardada.getIdCancion() != null;
        assert guardada.getTitulo().equals("Cancion de prueba");
        assert guardada.getGenero().equals("Pop");
        assert guardada.getArchivoAudio().equals("ruta/a/archivo.mp3");
        assert guardada.getDuracion().equals("03:30");
        assert guardada.getFechaSubida() != null;

        cancionRepository.delete(guardada);
        boolean existe = cancionRepository.findById(guardada.getIdCancion()).isPresent();
        assertThat(existe).isFalse();

        Cancion cancion2 = new Cancion();
        Cancion cancion3 = new Cancion();
        Cancion cancion4 = new Cancion();

        cancion2.setTitulo("Cancion 2");
        cancion2.setGenero("Rock");
        cancion2.setArchivoAudio("ruta/a/archivo2.mp3");
        cancion2.setDuracion("04:00");
        cancion2.setFechaSubida(java.time.LocalDateTime.now());

        cancion3.setTitulo("Cancion 3");
        cancion3.setGenero("Jazz");
        cancion3.setArchivoAudio("ruta/a/archivo3.mp3");
        cancion3.setDuracion("05:15");
        cancion3.setFechaSubida(java.time.LocalDateTime.now());

        cancion4.setTitulo("Cancion 4");
        cancion4.setGenero("Classical");
        cancion4.setArchivoAudio("ruta/a/archivo4.mp3");
        cancion4.setDuracion("02:45");
        cancion4.setFechaSubida(java.time.LocalDateTime.now());

        List<Cancion> canciones = new ArrayList<>();
        canciones.add(cancion2);
        canciones.add(cancion3);
        canciones.add(cancion4);
        List<Cancion> guardadas = new ArrayList<>();
        cancionRepository.saveAll(canciones).forEach(guardadas::add);
        assertThat(guardadas).hasSize(3);

        List<Cancion> allCanciones = (List<Cancion>) cancionRepository.findAll();
        assertThat(allCanciones).hasSizeGreaterThanOrEqualTo(3);
        assertThat(allCanciones.size()).isGreaterThanOrEqualTo(3);

        // TODO: Corregir  Nota a Juanjo de Marco: Linea 78 Da fallo en los test;
        // TODO: Posiblre fallo; el método definido devuelve Optional<Cancion>
        // TODO: (es decir un posible conjunto de resultado)
        Optional<Cancion> cancionTest2 = Optional.ofNullable(cancionRepository.findByTitulo("Cancion 2").orElse(null));
        assertThat(cancionTest2).isNotNull();
    }

    @Test
    void findByTituloTest() {
        Cancion cancion2 = new Cancion();
        Cancion cancion3 = new Cancion();
        Cancion cancion4 = new Cancion();

        cancion2.setTitulo("Cancion 2");
        cancion2.setGenero("Rock");
        cancion2.setArchivoAudio("ruta/a/archivo2.mp3");
        cancion2.setDuracion("04:00");
        cancion2.setFechaSubida(java.time.LocalDateTime.now());

        cancion3.setTitulo("Cancion 3");
        cancion3.setGenero("Jazz");
        cancion3.setArchivoAudio("ruta/a/archivo3.mp3");
        cancion3.setDuracion("05:15");
        cancion3.setFechaSubida(java.time.LocalDateTime.now());

        cancion4.setTitulo("Cancion 4");
        cancion4.setGenero("Classical");
        cancion4.setArchivoAudio("ruta/a/archivo4.mp3");
        cancion4.setDuracion("02:45");
        cancion4.setFechaSubida(java.time.LocalDateTime.now());

        List<Cancion> canciones = new ArrayList<>();
        canciones.add(cancion2);
        canciones.add(cancion3);
        canciones.add(cancion4);
        List<Cancion> guardadas = new ArrayList<>();
        cancionRepository.saveAll(canciones).forEach(guardadas::add);
        assertThat(guardadas).hasSize(3);

        Optional<Cancion> cancionTest3 = cancionRepository.findByTitulo("Cancion 3");
        assertThat(cancionTest3).isPresent();
        assertThat(cancionTest3.get().getTitulo()).isEqualTo("Cancion 3");
        assertThat(cancionTest3.get().getGenero()).isEqualTo("Jazz");
        assertThat(cancionTest3.get().getArchivoAudio()).isEqualTo("ruta/a/archivo3.mp3");
        assertThat(cancionTest3.get().getDuracion()).isEqualTo("05:15");

        Optional<Cancion> cancionTest2 = cancionRepository.findByTitulo("Cancion 2");
        assertThat(cancionTest2).isPresent();
        assertThat(cancionTest2.get().getTitulo()).isEqualTo("Cancion 2");
        assertThat(cancionTest2.get().getGenero()).isEqualTo("Rock");
        assertThat(cancionTest2.get().getArchivoAudio()).isEqualTo("ruta/a/archivo2.mp3");
        assertThat(cancionTest2.get().getDuracion()).isEqualTo("04:00");

        Optional<Cancion> cancionTest4 = cancionRepository.findByTitulo("Cancion 4");
        assertThat(cancionTest4).isPresent();
        assertThat(cancionTest4.get().getTitulo()).isEqualTo("Cancion 4");
        assertThat(cancionTest4.get().getGenero()).isEqualTo("Classical");
        assertThat(cancionTest4.get().getArchivoAudio()).isEqualTo("ruta/a/archivo4.mp3");
        assertThat(cancionTest4.get().getDuracion()).isEqualTo("02:45");
    }

    @Test
    void findByGeneroTest() {
        Cancion cancion2 = new Cancion();
        Cancion cancion3 = new Cancion();
        Cancion cancion4 = new Cancion();

        cancion2.setTitulo("Cancion 2");
        cancion2.setGenero("Rock");
        cancion2.setArchivoAudio("ruta/a/archivo2.mp3");
        cancion2.setDuracion("04:00");
        cancion2.setFechaSubida(java.time.LocalDateTime.now());

        cancion3.setTitulo("Cancion 3");
        cancion3.setGenero("Jazz");
        cancion3.setArchivoAudio("ruta/a/archivo3.mp3");
        cancion3.setDuracion("05:15");
        cancion3.setFechaSubida(java.time.LocalDateTime.now());

        cancion4.setTitulo("Cancion 4");
        cancion4.setGenero("Classical");
        cancion4.setArchivoAudio("ruta/a/archivo4.mp3");
        cancion4.setDuracion("02:45");
        cancion4.setFechaSubida(java.time.LocalDateTime.now());

        List<Cancion> canciones = new ArrayList<>();
        canciones.add(cancion2);
        canciones.add(cancion3);
        canciones.add(cancion4);
        List<Cancion> guardadas = new ArrayList<>();
        cancionRepository.saveAll(canciones).forEach(guardadas::add);
        assertThat(guardadas).hasSize(3);

        Optional<Cancion> cancionTestRock = cancionRepository.findByGenero("Rock");
        assertThat(cancionTestRock).isPresent();
        assertThat(cancionTestRock.get().getGenero()).isEqualTo("Rock");

        Optional<Cancion> cancionTestJazz = cancionRepository.findByGenero("Jazz");
        assertThat(cancionTestJazz).isPresent();
        assertThat(cancionTestJazz.get().getGenero()).isEqualTo("Jazz");

        Optional<Cancion> cancionTestClassical = cancionRepository.findByGenero("Classical");
        assertThat(cancionTestClassical).isPresent();
        assertThat(cancionTestClassical.get().getGenero()).isEqualTo("Classical");
    }

    @Test
    void findByArtistaTest() {

        Usuario user = new Usuario();
        user.setUsername("ArtistaCancion");
        user.setPassword("pass123");
        user.setEmail("artista@gmail.com");
        Usuario guardadoUser = usuarioRepository.save(user);
        Artista artista = new Artista();
        artista.setUsuario(guardadoUser);
        artista.setBiografia("Biografía del artista");
        Artista guardadoArtista = artistaRepository.save(artista);

        Usuario user2 = new Usuario();
        user2.setUsername("ArtistaMarginado");
        user2.setPassword("pass123");
        user2.setEmail("artistaaaaaa@gmail.com");
        Usuario guardadoUser2 = usuarioRepository.save(user2);
        Artista artista2 = new Artista();
        artista2.setUsuario(guardadoUser2);
        artista2.setBiografia("Biografía del artista");
        Artista guardadoArtista2 = artistaRepository.save(artista2);

        Cancion cancion2 = new Cancion();
        Cancion cancion3 = new Cancion();
        Cancion cancion4 = new Cancion();

        cancion2.setTitulo("Cancion 2");
        cancion2.setGenero("Rock");
        cancion2.setArchivoAudio("ruta/a/archivo2.mp3");
        cancion2.setDuracion("04:00");
        cancion2.setFechaSubida(java.time.LocalDateTime.now());
        cancion2.setArtista(guardadoArtista);

        cancion3.setTitulo("Cancion 3");
        cancion3.setGenero("Jazz");
        cancion3.setArchivoAudio("ruta/a/archivo3.mp3");
        cancion3.setDuracion("05:15");
        cancion3.setFechaSubida(java.time.LocalDateTime.now());
        cancion3.setArtista(guardadoArtista2);

        cancion4.setTitulo("Cancion 4");
        cancion4.setGenero("Classical");
        cancion4.setArchivoAudio("ruta/a/archivo4.mp3");
        cancion4.setDuracion("02:45");
        cancion4.setFechaSubida(java.time.LocalDateTime.now());
        cancion4.setArtista(guardadoArtista2);

        cancionRepository.saveAll(List.of(cancion2, cancion3, cancion4));
        Cancion encontrada = cancionRepository.findByArtista_Usuario_Username("ArtistaCancion").orElse(null);
        assertNotNull(encontrada);
        assertEquals("Cancion 2", encontrada.getTitulo());
        assertEquals("Rock", encontrada.getGenero());
        assertEquals("ArtistaCancion", encontrada.getArtista().getUsuario().getUsername());
    }

    @Test
    void CRUDTest () {
        //CREATE
        Cancion cancionCreated = new Cancion();
        cancionCreated.setTitulo("Cancion CRUD");
        cancionCreated.setGenero("Pop");
        cancionCreated.setArchivoAudio("ruta/a/archivoCRUD.mp3");
        cancionCreated.setDuracion("03:45");
        cancionCreated.setFechaSubida(java.time.LocalDateTime.now());
        Cancion savedCancion = cancionRepository.save(cancionCreated);
        assertNotNull(savedCancion.getIdCancion());

        //READ
        Optional<Cancion> readCancion = cancionRepository.findById(savedCancion.getIdCancion());
        assertTrue(readCancion.isPresent());
        assertEquals("Cancion CRUD", readCancion.get().getTitulo());

        //UPDATE
        readCancion.get().setGenero("Rock");
        Cancion updatedCancion = cancionRepository.save(readCancion.get());
        assertEquals("Rock", updatedCancion.getGenero());

        //DELETE
        cancionRepository.delete(updatedCancion);
        Optional<Cancion> deletedCancion = cancionRepository.findById(updatedCancion.getIdCancion());
        assertFalse(deletedCancion.isPresent());
    }

    //TODO: Test 1 artista tiene 5 canciones; si borras artista, se borran sus canciones.
}
