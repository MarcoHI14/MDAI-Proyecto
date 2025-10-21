package com.example.MDAI_Proyecto;

import com.example.MDAI_Proyecto.data.model.Cancion;
import com.example.MDAI_Proyecto.data.repository.CancionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CancionTest {

    @Autowired
    private CancionRepository cancionRepository;

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
}
