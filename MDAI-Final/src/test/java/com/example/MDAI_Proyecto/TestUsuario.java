package com.example.MDAI_Proyecto;

import com.example.MDAI_Proyecto.data.model.Usuario;
import com.example.MDAI_Proyecto.data.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TestUsuario {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testUsuarioConstructor() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("Juan");
        usuario.setEmail("juan@example.com");

        assertEquals(1L, usuario.getId());
        assertEquals("Juan", usuario.getUsername());
        assertEquals("juan@example.com", usuario.getEmail());
    }

    @Test
    void testUsuarioEquals() {
        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);

        Usuario usuario2 = new Usuario();
        usuario2.setId(1L);

        assertEquals(usuario1.getEmail(), usuario2.getEmail());
        assertEquals(usuario1.getId(), usuario2.getId());
        assertEquals(usuario1.getUsername(), usuario2.getUsername());
    }

    @Test
    void testFindUserByUsername() {
        Usuario usuario = new Usuario();
        usuario.setUsername("testuser");
        usuario.setPassword("password123");
        usuario.setEmail("testuser@example.com");

        userRepository.save(usuario);
        List<Usuario> foundUser = userRepository.findUserByUsername("testuser");
        assertFalse(foundUser.isEmpty());
        assertEquals("testuser", foundUser.get(0).getUsername());
    }
}
