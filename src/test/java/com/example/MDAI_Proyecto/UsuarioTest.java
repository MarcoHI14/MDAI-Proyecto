package com.example.MDAI_Proyecto;

import com.example.MDAI_Proyecto.data.model.Usuario;
import com.example.MDAI_Proyecto.data.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UsuarioTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void crearUsuariosTest() {
        Usuario usuario = new Usuario();
        usuario.setUsername("Testeador1");
        usuario.setPassword("1234");
        usuario.setEmail("testeador1@email.com");

        Usuario guardado = usuarioRepository.save(usuario);//prueba
        assertThat(guardado.getId()).isNotNull();
        assertThat(guardado.getUsername()).isEqualTo("Testeador1");
        assertThat(guardado.getPassword()).isEqualTo("1234");
        assertThat(guardado.getEmail()).isEqualTo("testeador1@email.com");

        usuarioRepository.delete(guardado);
        boolean existe = usuarioRepository.findById(guardado.getId()).isPresent();
        assertThat(existe).isFalse();

        Usuario usuario2 = new Usuario();
        Usuario usuario3 = new Usuario();
        Usuario usuario4 = new Usuario();

        usuario2.setUsername("Testeador2");
        usuario2.setPassword("2345");
        usuario2.setEmail("testeador2@email.com");

        usuario3.setUsername("Testeador3");
        usuario3.setPassword("3456");
        usuario3.setEmail("testeador3@email.com");

        usuario4.setUsername("Testeador4");
        usuario4.setPassword("4567");
        usuario4.setEmail("testeador4@email.com");

        List<Usuario> usuarios = new ArrayList<>();
        usuarios.add(usuario2);
        usuarios.add(usuario3);
        usuarios.add(usuario4);
        List<Usuario> guardados = new ArrayList<>();
        usuarioRepository.saveAll(usuarios).forEach(guardados::add);
        assertThat(guardados).hasSize(3);

        List<Usuario> users = (List<Usuario>) usuarioRepository.findAll();
        assertThat(users).hasSizeGreaterThanOrEqualTo(3);
        assertEquals(3, users.size());

        Usuario Testeador2 = usuarioRepository.findByUsername("Testeador2").orElse(null);
        assertThat(Testeador2).isNotNull();
        assertThat(Testeador2.getUsername()).isEqualTo("Testeador2");
        assertThat(Testeador2.getPassword()).isEqualTo("2345");
        assertThat(Testeador2.getEmail()).isEqualTo("testeador2@email.com");

        Usuario Testeador3 = usuarioRepository.findByUsername("Testeador3").orElse(null);
        assertThat(Testeador3).isNotNull();
        assertThat(Testeador3.getUsername()).isEqualTo("Testeador3");
        assertThat(Testeador3.getPassword()).isEqualTo("3456");
        assertThat(Testeador3.getEmail()).isEqualTo("testeador3@email.com");

        usuarioRepository.deleteAll(guardados); // ësto elimina los usuarios en BD, pero mantienen en memoria
        assertThat(usuarioRepository.findByUsername("Testeador2")).isEmpty();
        assertThat(usuarioRepository.findByUsername("Testeador3")).isEmpty();
        assertThat(usuarioRepository.findByUsername("Testeador4")).isEmpty();
    }

    @Test
    void findByUsernameTest() {
        Usuario usuario2 = new Usuario();
        Usuario usuario3 = new Usuario();
        Usuario usuario4 = new Usuario();

        usuario2.setUsername("Testeador2");
        usuario2.setPassword("2345");
        usuario2.setEmail("testeador2@email.com");

        usuario3.setUsername("Testeador3");
        usuario3.setPassword("3456");
        usuario3.setEmail("testeador3@email.com");

        usuario4.setUsername("Testeador4");
        usuario4.setPassword("4567");
        usuario4.setEmail("testeador4@email.com");

        usuarioRepository.saveAll(List.of(usuario2, usuario3, usuario4));

        Usuario Testeador2 = usuarioRepository.findByUsername("Testeador2").orElse(null);
        assertThat(Testeador2).isNotNull();
        assertThat(Testeador2.getUsername()).isEqualTo("Testeador2");
        assertThat(Testeador2.getPassword()).isEqualTo("2345");

        Usuario Testeador3 = usuarioRepository.findByUsername("Testeador3").orElse(null);
        assertThat(Testeador3).isNotNull();
        assertThat(Testeador3.getUsername()).isEqualTo("Testeador3");
        assertThat(Testeador3.getPassword()).isEqualTo("3456");

        usuarioRepository.deleteAll(List.of(usuario2, usuario3, usuario4));
        assertThat(usuarioRepository.findByUsername("Testeador4")).isEmpty();
    }

    @Test
    void findByEmailTest() {
        Usuario usuario2 = new Usuario();
        Usuario usuario3 = new Usuario();

        usuario2.setUsername("Testeador2");
        usuario2.setPassword("2345");
        usuario2.setEmail("testeador2@gmail.com");

        usuario3.setUsername("Testeador3");
        usuario3.setPassword("2345");
        usuario3.setEmail("testeador3@gmail.com");

        usuarioRepository.saveAll(List.of(usuario2, usuario3));
        Optional<Usuario> Testeador2 = usuarioRepository.findByEmail("testeador2@gmail.com");
        assertThat(Testeador2).isPresent();
        assertThat(Testeador2.get().getUsername()).isEqualTo("Testeador2");
        assertThat(Testeador2.get().getPassword()).isEqualTo("2345");
        assertThat(Testeador2.get().getEmail()).isEqualTo("testeador2@gmail.com");
        usuarioRepository.deleteAll(List.of(usuario2, usuario3));
    }

    @Test
    void comprobarPasswordTest() {
        Usuario usuario = new Usuario();
        usuario.setUsername("Testeador1");
        usuario.setPassword("1234");
        usuario.setEmail("testeador1@gmail.com");
        usuarioRepository.save(usuario);

        assertTrue(usuario.comprobarPassword("1234"));
        assertFalse(usuario.comprobarPassword("wrongPassword"));
        usuarioRepository.delete(usuario);
    }

    @Test
    void cambiarAtributosTest() {
        Usuario usuario = new Usuario();
        usuario.setUsername("Testeador1");
        usuario.setPassword("1234");
        usuario.setEmail("testeador1@gmail.com");
        usuarioRepository.save(usuario);

        usuario.setUsername("TesteadorModificado");
        usuario.setPassword("4321");
        usuario.setEmail("eltester@hotmail.com");
        usuarioRepository.save(usuario);

        assertTrue(usuarioRepository.findByUsername("TesteadorModificado").isPresent());
        Usuario modificado = usuarioRepository.findByUsername("TesteadorModificado").orElse(null);
        assertThat(modificado).isNotNull();
        assertThat(modificado.getUsername()).isEqualTo("TesteadorModificado");
        assertThat(modificado.getPassword()).isEqualTo("4321");
        assertThat(modificado.getEmail()).isEqualTo("eltester@hotmail.com");
        assertThat(usuarioRepository.count()).isEqualTo(1);

        usuarioRepository.delete(modificado);
        assertThat(usuarioRepository.findByUsername("TesteadorModificado")).isEmpty();
    }
}
