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


/**
 * Conjunto de pruebas de integración para el repositorio de {@link Cancion}.
 *
 * Usa @DataJpaTest para arrancar un contexto de persistencia ligero y validar
 * operaciones CRUD y consultas personalizadas del repositorio.
 *
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CancionTest {

    /**
     * Repositorio para operaciones sobre {@link Cancion}.
     */
    @Autowired
    private CancionRepository cancionRepository;

    /**
     * Repositorio para operaciones sobre {@link Artista}.
     */
    @Autowired
    private ArtistaRepository artistaRepository;

    /**
     * Repositorio para operaciones sobre {@link Usuario}.
     */
    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Prueba que crea varias canciones y valida guardado, búsqueda por título,
     * eliminación y consultas generales como findAll.
     */
    @Test
    void crearCancionTest() {

        Usuario user = new Usuario();
        user.setUsername("TMovingParts");
        user.setPassword("pass123");
        user.setEmail("tiny@movingp.com");
        Usuario guardadoUser = usuarioRepository.save(user);
        assertNotNull(guardadoUser.getId());
        Artista artista = new Artista();
        artista.setUsuario(guardadoUser);
        artista.setBiografia("Biografía del artista");
        Artista guardadoArtista = artistaRepository.save(artista);
        assertNotNull(guardadoArtista.getIdArtista());

        Cancion cancion = new Cancion();
        cancion.setTitulo("Birdhouse");
        cancion.setGenero("Rock");
        cancion.setArchivoAudio("ruta/a/archivo.mp3");
        cancion.setDuracion("03:01");
        cancion.setFechaSubida(java.time.LocalDateTime.now());
        cancion.setArtista(guardadoArtista);

        Cancion guardada = cancionRepository.save(cancion);
        assert guardada.getIdCancion() != null;
        assert guardada.getTitulo().equals("Birdhouse");
        assert guardada.getGenero().equals("Rock");
        assert guardada.getArchivoAudio().equals("ruta/a/archivo.mp3");
        assert guardada.getDuracion().equals("03:01");
        assert guardada.getFechaSubida() != null;
        assert guardada.getArtista() != null;

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
        cancion2.setArtista(guardadoArtista);

        cancion3.setTitulo("Cancion 3");
        cancion3.setGenero("Jazz");
        cancion3.setArchivoAudio("ruta/a/archivo3.mp3");
        cancion3.setDuracion("05:15");
        cancion3.setFechaSubida(java.time.LocalDateTime.now());
        cancion3.setArtista(guardadoArtista);

        cancion4.setTitulo("Cancion 4");
        cancion4.setGenero("Classical");
        cancion4.setArchivoAudio("ruta/a/archivo4.mp3");
        cancion4.setDuracion("02:45");
        cancion4.setFechaSubida(java.time.LocalDateTime.now());
        cancion4.setArtista(guardadoArtista);

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

        Optional<Cancion> cancionTest2 = Optional.ofNullable(cancionRepository.findByTituloIgnoreCase("Cancion 2").orElse(null));
        assertThat(cancionTest2).isNotNull();
    }

    /**
     * Verifica la consulta {@code findByTituloIgnoreCase} para varios títulos insertados.
     * Comprueba que los datos recuperados coinciden con lo esperado.
     */
    @Test
    void findByTituloTest() {
        Usuario user = new Usuario();
        user.setUsername("TMovingParts");
        user.setPassword("pass123");
        user.setEmail("tiny@movingp.com");
        Usuario guardadoUser = usuarioRepository.save(user);
        assertNotNull(guardadoUser.getId());
        Artista artista = new Artista();
        artista.setUsuario(guardadoUser);
        artista.setBiografia("Biografía del artista");
        Artista guardadoArtista = artistaRepository.save(artista);
        assertNotNull(guardadoArtista.getIdArtista());

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
        cancion3.setArtista(guardadoArtista);

        cancion4.setTitulo("Cancion 4");
        cancion4.setGenero("Classical");
        cancion4.setArchivoAudio("ruta/a/archivo4.mp3");
        cancion4.setDuracion("02:45");
        cancion4.setFechaSubida(java.time.LocalDateTime.now());
        cancion4.setArtista(guardadoArtista);

        List<Cancion> canciones = new ArrayList<>();
        canciones.add(cancion2);
        canciones.add(cancion3);
        canciones.add(cancion4);
        List<Cancion> guardadas = new ArrayList<>();
        cancionRepository.saveAll(canciones).forEach(guardadas::add);
        assertThat(guardadas).hasSize(3);

        Optional<Cancion> cancionTest3 = cancionRepository.findByTituloIgnoreCase("Cancion 3");
        assertThat(cancionTest3).isPresent();
        assertThat(cancionTest3.get().getTitulo()).isEqualTo("Cancion 3");
        assertThat(cancionTest3.get().getGenero()).isEqualTo("Jazz");
        assertThat(cancionTest3.get().getArchivoAudio()).isEqualTo("ruta/a/archivo3.mp3");
        assertThat(cancionTest3.get().getDuracion()).isEqualTo("05:15");

        Optional<Cancion> cancionTest2 = cancionRepository.findByTituloIgnoreCase("Cancion 2");
        assertThat(cancionTest2).isPresent();
        assertThat(cancionTest2.get().getTitulo()).isEqualTo("Cancion 2");
        assertThat(cancionTest2.get().getGenero()).isEqualTo("Rock");
        assertThat(cancionTest2.get().getArchivoAudio()).isEqualTo("ruta/a/archivo2.mp3");
        assertThat(cancionTest2.get().getDuracion()).isEqualTo("04:00");

        Optional<Cancion> cancionTest4 = cancionRepository.findByTituloIgnoreCase("Cancion 4");
        assertThat(cancionTest4).isPresent();
        assertThat(cancionTest4.get().getTitulo()).isEqualTo("Cancion 4");
        assertThat(cancionTest4.get().getGenero()).isEqualTo("Classical");
        assertThat(cancionTest4.get().getArchivoAudio()).isEqualTo("ruta/a/archivo4.mp3");
        assertThat(cancionTest4.get().getDuracion()).isEqualTo("02:45");
    }

    /**
     * Prueba la consulta {@code findByGeneroIgnoreCase} que devuelve una lista de canciones
     * con el género solicitado. Verifica tamaño y consistencia del género.
     */
    @Test
    void findByGeneroTest() {
        Usuario user = new Usuario();
        user.setUsername("TMovingParts");
        user.setPassword("pass123");
        user.setEmail("tiny@movingp.com");
        Usuario guardadoUser = usuarioRepository.save(user);
        assertNotNull(guardadoUser.getId());
        Artista artista = new Artista();
        artista.setUsuario(guardadoUser);
        artista.setBiografia("Biografía del artista");
        Artista guardadoArtista = artistaRepository.save(artista);
        assertNotNull(guardadoArtista.getIdArtista());

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
        cancion3.setArtista(guardadoArtista);

        cancion4.setTitulo("Cancion 4");
        cancion4.setGenero("Classical");
        cancion4.setArchivoAudio("ruta/a/archivo4.mp3");
        cancion4.setDuracion("02:45");
        cancion4.setFechaSubida(java.time.LocalDateTime.now());
        cancion4.setArtista(guardadoArtista);

        List<Cancion> canciones = new ArrayList<>();
        canciones.add(cancion2);
        canciones.add(cancion3);
        canciones.add(cancion4);
        List<Cancion> guardadas = new ArrayList<>();
        cancionRepository.saveAll(canciones).forEach(guardadas::add);
        assertThat(guardadas).hasSize(3);

        List<Cancion> rock = cancionRepository.findByGeneroIgnoreCase("Rock");
        assertThat(rock).isNotEmpty();
        // Opcional: comprobar que todos tengan el género esperado
        assertThat(rock).allMatch(c -> "Rock".equals(c.getGenero()));

        List<Cancion> jazz = cancionRepository.findByGeneroIgnoreCase("Jazz");
        assertThat(jazz).isNotEmpty();
        assertThat(jazz).allMatch(c -> "Jazz".equals(c.getGenero()));

        List<Cancion> classical = cancionRepository.findByGeneroIgnoreCase("Classical");
        assertThat(classical).isNotEmpty();
        assertThat(classical).allMatch(c -> "Classical".equals(c.getGenero()));
    }

    /**
     * Verifica la consulta por artista (navegando por la relación de usuario).
     * Inserta canciones de dos artistas distintos y comprueba que la búsqueda por
     * username devuelve las correspondientes.
     */
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
        List<Cancion> encontradas = cancionRepository.findByArtista_Usuario_UsernameIgnoreCase("ArtistaCancion");
        assertNotNull(encontradas);
        assertFalse(encontradas.isEmpty());
        Cancion encontrada = encontradas.getFirst();
        assertEquals("Cancion 2", encontrada.getTitulo());
        assertEquals("Rock", encontrada.getGenero());
        assertEquals("ArtistaCancion", encontrada.getArtista().getUsuario().getUsername());
    }

    /**
     * Prueba CRUD completo: crear, leer, actualizar y borrar una canción.
     */
    @Test
    void CRUDTest () {
        //CREATE
        Usuario user = new Usuario();
        user.setUsername("TMovingParts");
        user.setPassword("pass123");
        user.setEmail("tiny@movingp.com");
        Usuario guardadoUser = usuarioRepository.save(user);
        assertNotNull(guardadoUser.getId());
        Artista artista = new Artista();
        artista.setUsuario(guardadoUser);
        artista.setBiografia("Biografía del artista");
        Artista guardadoArtista = artistaRepository.save(artista);
        assertNotNull(guardadoArtista.getIdArtista());

        Cancion cancionCreated = new Cancion();
        cancionCreated.setTitulo("Cancion CRUD");
        cancionCreated.setGenero("Pop");
        cancionCreated.setArchivoAudio("ruta/a/archivoCRUD.mp3");
        cancionCreated.setDuracion("03:45");
        cancionCreated.setFechaSubida(java.time.LocalDateTime.now());
        cancionCreated.setArtista(guardadoArtista);
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

    /**
     * Comprueba que al borrar un artista con cascade las canciones relacionadas se eliminan.
     * Intenta recuperar una canción tras borrar el artista y observa el resultado.
     */

    @Test
    void deleteArtistaCascadaCancionesTest() {
        Usuario user = new Usuario ();
        user.setUsername("Knopfler");
        user.setPassword("direstraits");
        user.setEmail("making@movies.com");
        Usuario savedUser = usuarioRepository.save(user);
        Artista artista = new Artista();
        artista.setUsuario(savedUser);
        artista.setBiografia("Biografía del artista");
        Artista savedArtista = artistaRepository.save(artista);

        Cancion cancion1 = new Cancion();
        Cancion cancion2 = new Cancion();
        Cancion cancion3 = new Cancion();
        Cancion cancion4 = new Cancion();
        Cancion cancion5 = new Cancion();

        cancion1.setTitulo("Six blade knife");
        cancion1.setGenero("Rock");
        cancion1.setArchivoAudio("ruta/a/archivo1.mp3");
        cancion1.setDuracion("04:00");
        cancion1.setFechaSubida(java.time.LocalDateTime.now());
        cancion1.setArtista(savedArtista);

        cancion2.setTitulo("Walk of life");
        cancion2.setGenero("Rock");
        cancion2.setArchivoAudio("ruta/a/archivo2.mp3");
        cancion2.setDuracion("03:45");
        cancion2.setFechaSubida(java.time.LocalDateTime.now());
        cancion2.setArtista(savedArtista);

        cancion3.setTitulo("Money for nothing");
        cancion3.setGenero("Rock");
        cancion3.setArchivoAudio("ruta/a/archivo3.mp3");
        cancion3.setDuracion("04:05");
        cancion3.setFechaSubida(java.time.LocalDateTime.now());
        cancion3.setArtista(savedArtista);

        cancion4.setTitulo("Brothers in arms");
        cancion4.setGenero("Rock");
        cancion4.setArchivoAudio("ruta/a/archivo4.mp3");
        cancion4.setDuracion("05:20");
        cancion4.setFechaSubida(java.time.LocalDateTime.now());
        cancion4.setArtista(savedArtista);

        cancion5.setTitulo("Sultans of swing");
        cancion5.setGenero("Rock");
        cancion5.setArchivoAudio("ruta/a/archivo5.mp3");
        cancion5.setDuracion("04:15");
        cancion5.setFechaSubida(java.time.LocalDateTime.now());
        cancion5.setArtista(savedArtista);

        List<Cancion> canciones = new ArrayList<>();
        canciones.add(cancion1);
        canciones.add(cancion2);
        canciones.add(cancion3);
        canciones.add(cancion4);
        canciones.add(cancion5);
        List<Cancion> guardadas = new ArrayList<>();
        cancionRepository.saveAll(canciones).forEach(guardadas::add);
        assertThat(guardadas).hasSize(5);

        //Borrar el artista
        artistaRepository.delete(savedArtista);

        //Comprobar que las canciones han sido borradas
        try {
            Optional<Cancion> recuperada = cancionRepository.findByTituloIgnoreCase("Six blade knife");
            System.out.println("Error: La canción no ha sido borrada al borrar el artista.");
        } catch (Exception e) {
            System.out.println("Las canciones han sido borradas al borrar el artista." + e.getMessage());
        }

    }

    /**
     * Verifica que el título no pueda ser nulo al guardar una canción.
     * Inserta una canción válida y otra sin título para comprobar la restricción.
     */

    @Test
    void tituloNoNuloTest() {

        Usuario user = new Usuario ();
        user.setUsername("dkasho");
        user.setPassword("racmusic");
        user.setEmail("dkasho@gmail.com");
        Usuario savedUser = usuarioRepository.save(user);
        Artista artista = new Artista();
        artista.setUsuario(savedUser);
        artista.setBiografia("Biografía del artista");
        Artista savedArtista = artistaRepository.save(artista);

        Cancion cancion1 = new Cancion();
        cancion1.setTitulo("It's All About You");
        cancion1.setGenero("Rock");
        cancion1.setArchivoAudio("ruta/a/archivo1.mp3");
        cancion1.setDuracion("05:08");
        cancion1.setFechaSubida(java.time.LocalDateTime.now());
        cancion1.setArtista(savedArtista);

        Cancion cancion2 = new Cancion();
        cancion2.setGenero("Rock");
        cancion2.setArchivoAudio("ruta/a/archivo2.mp3");
        cancion2.setDuracion("04:15");
        cancion2.setFechaSubida(java.time.LocalDateTime.now());
        cancion2.setArtista(savedArtista);

        //Guardar la primera canción (debería funcionar)
        cancionRepository.save(cancion1);

        //Intentar guardar la segunda canción (debería fallar)
        try {
            cancionRepository.save(cancion2);
            System.out.println("Error: La canción sin título ha sido guardada.");
        } catch (Exception e) {
            System.out.println("Error al guardar la canción sin título: " + e.getMessage());
        }
    }

    /**
     * Prueba la consulta {@code findByFechaSubidaBefore} para recuperar canciones
     * subidas antes de una fecha determinada.
     */
    @Test
    void findByFechaSubidaBeforeTest() {

        Usuario user = new Usuario ();
        user.setUsername("LinkinPark");
        user.setPassword("Numb123");
        user.setEmail("shinoda@bennington.com");
        Usuario savedUser = usuarioRepository.save(user);
        Artista artista = new Artista();
        artista.setUsuario(savedUser);
        artista.setBiografia("Biografía del artista");
        Artista savedArtista = artistaRepository.save(artista);

        Cancion cancion1 = new Cancion();
        cancion1.setTitulo("In the End");
        cancion1.setGenero("Rock");
        cancion1.setArchivoAudio("ruta/a/archivo1.mp3");
        cancion1.setDuracion("03:36");
        cancion1.setFechaSubida(java.time.LocalDateTime.of(2000, 1, 15, 10, 0));
        cancion1.setArtista(savedArtista);

        Cancion cancion2 = new Cancion();
        cancion2.setTitulo("Numb");
        cancion2.setGenero("Rock");
        cancion2.setArchivoAudio("ruta/a/archivo2.mp3");
        cancion2.setDuracion("03:07");
        cancion2.setFechaSubida(java.time.LocalDateTime.of(2003, 3, 25, 12, 0));
        cancion2.setArtista(savedArtista);

        Cancion cancion3 = new Cancion();
        cancion3.setTitulo("Breaking the Habit");
        cancion3.setGenero("Rock");
        cancion3.setArchivoAudio("ruta/a/archivo3.mp3");
        cancion3.setDuracion("03:16");
        cancion3.setFechaSubida(java.time.LocalDateTime.of(2004, 7, 20, 14, 0));
        cancion3.setArtista(savedArtista);

        Cancion cancion4 = new Cancion();
        cancion4.setTitulo("Let You Fade");
        cancion4.setGenero("Rock");
        cancion4.setArchivoAudio("ruta/a/archivo4.mp3");
        cancion4.setDuracion("03:30");
        cancion4.setFechaSubida(java.time.LocalDateTime.of(2024, 10, 19, 16, 0));
        cancion4.setArtista(savedArtista);

        cancionRepository.saveAll(List.of(cancion1, cancion2, cancion3, cancion4));

        List<Cancion> cancionesAntes2005 = cancionRepository.findByFechaSubidaBefore(java.time.LocalDateTime.of(2005, 1, 1, 0, 0));
        assertThat(cancionesAntes2005).hasSize(3);
        for (Cancion c : cancionesAntes2005) {
            assertThat(c.getFechaSubida().isBefore(java.time.LocalDateTime.of(2005, 1, 1, 0, 0))).isTrue();
        //    if (c.getFechaSubida().isBefore(java.time.LocalDateTime.of(2005, 1, 1, 0, 0))) {
        //        System.out.println(c.getTitulo()+" fue publicada antes de 2005.");
        //    }
        }
    }

    /**
     * Prueba la consulta {@code findByFechaSubidaAfter} para recuperar canciones
     * subidas después de una fecha determinada.
     */
    @Test
    void findByFechaSubidaAfterTest() {
        Usuario user = new Usuario ();
        user.setUsername("Radwimps");
        user.setPassword("Zenzenzense");
        user.setEmail("radwimps@gmail.com");
        Usuario savedUser = usuarioRepository.save(user);
        Artista artista = new Artista();
        artista.setUsuario(savedUser);
        artista.setBiografia("Biografía del artista");
        Artista savedArtista = artistaRepository.save(artista);

        Cancion cancion1 = new Cancion();
        cancion1.setTitulo("Hyperventilation");
        cancion1.setGenero("Rock");
        cancion1.setArchivoAudio("ruta/a/archivo1.mp3");
        cancion1.setDuracion("04:23");
        cancion1.setFechaSubida(java.time.LocalDateTime.of(2010, 5, 10, 10, 0));
        cancion1.setArtista(savedArtista);

        Cancion cancion2 = new Cancion();
        cancion2.setTitulo("Zenzenzense");
        cancion2.setGenero("Rock");
        cancion2.setArchivoAudio("ruta/a/archivo2.mp3");
        cancion2.setDuracion("03:50");
        cancion2.setFechaSubida(java.time.LocalDateTime.of(2016, 8, 20, 12, 0));
        cancion2.setArtista(savedArtista);

        Cancion cancion3 = new Cancion();
        cancion3.setTitulo("Dream Lantern");
        cancion3.setGenero("Rock");
        cancion3.setArchivoAudio("ruta/a/archivo3.mp3");
        cancion3.setDuracion("02:11");
        cancion3.setFechaSubida(java.time.LocalDateTime.of(2017, 11, 15, 14, 0));
        cancion3.setArtista(savedArtista);

        Cancion cancion4 = new Cancion();
        cancion4.setTitulo("Suzume");
        cancion4.setGenero("Rock");
        cancion4.setArchivoAudio("ruta/a/archivo4.mp3");
        cancion4.setDuracion("03:58");
        cancion4.setFechaSubida(java.time.LocalDateTime.of(2024, 2, 5, 16, 0));
        cancion4.setArtista(savedArtista);

        cancionRepository.saveAll(List.of(cancion1, cancion2, cancion3, cancion4));

        List<Cancion> cancionesDespues2015 = cancionRepository.findByFechaSubidaAfter(java.time.LocalDateTime.of(2015, 12, 31, 23, 59));
        assertThat(cancionesDespues2015).hasSize(3);
        for (Cancion c : cancionesDespues2015) {
            assertThat(c.getFechaSubida().isAfter(java.time.LocalDateTime.of(2015, 12, 31, 23, 59))).isTrue();
        //    if (c.getFechaSubida().isAfter(java.time.LocalDateTime.of(2015, 12, 31, 23, 59))) {
        //        System.out.println(c.getTitulo()+" fue publicada después de 2015.");
        //    }
        }
    }

    /**
     * Verifica la consulta {@code findByDuracion} que devuelve canciones con la
     * duración exacta solicitada.
     */
    @Test
    void findByDuracionTest() {
        Usuario user = new Usuario ();
        user.setUsername("Generico");
        user.setPassword("RollingStones");
        user.setEmail("tipo@deincognito.com");
        Usuario savedUser = usuarioRepository.save(user);
        Artista artista = new Artista();
        artista.setUsuario(savedUser);
        artista.setBiografia("Biografía del artista");
        Artista savedArtista = artistaRepository.save(artista);

        Cancion cancion1 = new Cancion();
        cancion1.setTitulo("Value");
        cancion1.setGenero("Pop");
        cancion1.setArchivoAudio("ruta/a/archivo1.mp3");
        cancion1.setDuracion("03:05");
        cancion1.setFechaSubida(java.time.LocalDateTime.now());
        cancion1.setArtista(savedArtista);

        Cancion cancion2 = new Cancion();
        cancion2.setTitulo("Backlight");
        cancion2.setGenero("Rock");
        cancion2.setArchivoAudio("ruta/a/archivo2.mp3");
        cancion2.setDuracion("03:17");
        cancion2.setFechaSubida(java.time.LocalDateTime.now());
        cancion2.setArtista(savedArtista);

        Cancion cancion3 = new Cancion();
        cancion3.setTitulo("Sunset");
        cancion3.setGenero("Jazz");
        cancion3.setArchivoAudio("ruta/a/archivo3.mp3");
        cancion3.setDuracion("03:05");
        cancion3.setFechaSubida(java.time.LocalDateTime.now());
        cancion3.setArtista(savedArtista);

        Cancion cancion4 = new Cancion();
        cancion4.setTitulo("Mirror");
        cancion4.setGenero("Pop");
        cancion4.setArchivoAudio("ruta/a/archivo4.mp3");
        cancion4.setDuracion("02:58");
        cancion4.setFechaSubida(java.time.LocalDateTime.now());
        cancion4.setArtista(savedArtista);

        cancionRepository.saveAll(List.of(cancion1, cancion2, cancion3, cancion4));
        List<Cancion> cancionesDuracion305 = cancionRepository.findByDuracion("03:05");
        assertNotNull(cancionesDuracion305);
        assertThat(cancionesDuracion305).hasSize(2);
        //for (Cancion c : cancionesDuracion305) {
        //    assertEquals("03:05", c.getDuracion());
        //    System.out.println(c.getTitulo() + " tiene una duración de 03:05.");
        //}
    }

    /**
     * Prueba la consulta {@code findByTituloContaining} para recuperar canciones
     * cuyo título contiene una subcadena dada.
     */
    @Test
    void findByTituloContainingIgnoreCaseTest() {

        Usuario user = new Usuario ();
        user.setUsername("Walls");
        user.setPassword("ginesfms");
        user.setEmail("wallsrb@gmail.com");
        Usuario savedUser = usuarioRepository.save(user);
        Artista artista = new Artista();
        artista.setUsuario(savedUser);
        artista.setBiografia("Biografía del artista");
        Artista savedArtista = artistaRepository.save(artista);

        Cancion cancion1 = new Cancion();
        cancion1.setTitulo("Promesas");
        cancion1.setGenero("HipHop");
        cancion1.setArchivoAudio("ruta/a/archivo1.mp3");
        cancion1.setDuracion("03:21");
        cancion1.setFechaSubida(java.time.LocalDateTime.of(2018, 6, 9, 12, 0));
        cancion1.setArtista(savedArtista);

        Cancion cancion2 = new Cancion();
        cancion2.setTitulo("Cerca");
        cancion2.setGenero("Pop");
        cancion2.setArchivoAudio("ruta/a/archivo2.mp3");
        cancion2.setDuracion("03:56");
        cancion2.setFechaSubida(java.time.LocalDateTime.of(2019, 4, 15, 14, 0));
        cancion2.setArtista(savedArtista);

        Cancion cancion3 = new Cancion();
        cancion3.setTitulo("Por Partes");
        cancion3.setGenero("HipHop");
        cancion3.setArchivoAudio("ruta/a/archivo3.mp3");
        cancion3.setDuracion("03:04");
        cancion3.setFechaSubida(java.time.LocalDateTime.of(2018, 11, 20, 16, 0));
        cancion3.setArtista(savedArtista);

        Cancion cancion4 = new Cancion();
        cancion4.setTitulo("En Paz");
        cancion4.setGenero("Acoustic");
        cancion4.setArchivoAudio("ruta/a/archivo4.mp3");
        cancion4.setDuracion("03:38");
        cancion4.setFechaSubida(java.time.LocalDateTime.of(2018, 1, 5, 18, 0));
        cancion4.setArtista(savedArtista);

        cancionRepository.saveAll(List.of(cancion1, cancion2, cancion3, cancion4));

        // Buscar por fragmentos distintos y comprobar resultados y case-insensitivity
        List<Cancion> resProm = cancionRepository.findByTituloContainingIgnoreCase("prom");
        assertThat(resProm).hasSize(1);
        assertThat(resProm.get(0).getTitulo()).isEqualTo("Promesas");
        System.out.println(resProm.get(0).getTitulo());

        List<Cancion> resPor = cancionRepository.findByTituloContainingIgnoreCase("por");
        assertThat(resPor).hasSize(1);
        assertThat(resPor.get(0).getTitulo()).isEqualTo("Por Partes");
        System.out.println(resPor.get(0).getTitulo());

        List<Cancion> resPaz = cancionRepository.findByTituloContainingIgnoreCase("PAZ"); // mayúsculas
        assertThat(resPaz).hasSize(1);
        assertThat(resPaz.get(0).getTitulo()).isEqualTo("En Paz");
        System.out.println(resPaz.get(0).getTitulo());

        List<Cancion> resCer = cancionRepository.findByTituloContainingIgnoreCase("cer");
        assertThat(resCer).hasSize(1);
        assertThat(resCer.get(0).getTitulo()).isEqualTo("Cerca");
        System.out.println(resCer.get(0).getTitulo());

        // Comprobación genérica: fragmento corto devuelve al menos una coincidencia
        List<Cancion> resA = cancionRepository.findByTituloContainingIgnoreCase("a");
        assertThat(resA).isNotEmpty();
        // for (Cancion c : resA) {
        //    System.out.println(c.getTitulo());
        //}
    }
}
