package com.example.MDAI_Proyecto;

import com.example.MDAI_Proyecto.data.model.Cancion;
import com.example.MDAI_Proyecto.data.repository.CancionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
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
    }
}
