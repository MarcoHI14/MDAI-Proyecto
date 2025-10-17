package com.example.MDAI_Proyecto;

import com.example.MDAI_Proyecto.data.model.Artista;
import com.example.MDAI_Proyecto.data.model.Usuario;
import com.example.MDAI_Proyecto.data.repository.ArtistaRepository;
import com.example.MDAI_Proyecto.data.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
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
}
