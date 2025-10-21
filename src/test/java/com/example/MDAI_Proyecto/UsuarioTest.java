package com.example.MDAI_Proyecto;

import com.example.MDAI_Proyecto.data.model.Usuario;
import com.example.MDAI_Proyecto.data.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
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
        assertNotNull(guardado.getId());
        assertEquals("Testeador1", guardado.getUsername());
        assertEquals("1234", guardado.getPassword());
        assertEquals("testeador1@email.com", guardado.getEmail());

        usuarioRepository.delete(guardado);
        boolean existe = usuarioRepository.findById(guardado.getId()).isPresent();
        assertFalse(existe);

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
        assertEquals(3, guardados.size());

        List<Usuario> users = (List<Usuario>) usuarioRepository.findAll();
        assertTrue(users.size() >= 3);
        assertEquals(3, users.size());

        Usuario Testeador2 = usuarioRepository.findByUsername("Testeador2").orElse(null);
        assertNotNull(Testeador2);
        assertEquals("Testeador2", Testeador2.getUsername());
        assertEquals("2345", Testeador2.getPassword());
        assertEquals("testeador2@email.com", Testeador2.getEmail());

        Usuario Testeador3 = usuarioRepository.findByUsername("Testeador3").orElse(null);
        assertNotNull(Testeador3);
        assertEquals("Testeador3", Testeador3.getUsername());
        assertEquals("3456", Testeador3.getPassword());
        assertEquals("testeador3@email.com", Testeador3.getEmail());

        usuarioRepository.deleteAll(guardados); // ësto elimina los usuarios en BD, pero mantienen en memoria
        assertTrue(usuarioRepository.findByUsername("Testeador2").isEmpty());
        assertTrue(usuarioRepository.findByUsername("Testeador3").isEmpty());
        assertTrue(usuarioRepository.findByUsername("Testeador4").isEmpty());
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
        assertNotNull(Testeador2);
        assertEquals("Testeador2", Testeador2.getUsername());
        assertEquals("2345", Testeador2.getPassword());

        Usuario Testeador3 = usuarioRepository.findByUsername("Testeador3").orElse(null);
        assertNotNull(Testeador3);
        assertEquals("Testeador3", Testeador3.getUsername());
        assertEquals("3456", Testeador3.getPassword());

        usuarioRepository.deleteAll(List.of(usuario2, usuario3, usuario4));
        assertTrue(usuarioRepository.findByUsername("Testeador4").isEmpty());
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
        assertTrue(Testeador2.isPresent());
        assertEquals("Testeador2", Testeador2.get().getUsername());
        assertEquals("2345", Testeador2.get().getPassword());
        assertEquals("testeador2@gmail.com", Testeador2.get().getEmail());
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
        assertNotNull(modificado);
        assertEquals("TesteadorModificado", modificado.getUsername());
        assertEquals("4321", modificado.getPassword());
        assertEquals("eltester@hotmail.com", modificado.getEmail());
        assertEquals(1, usuarioRepository.count());

        assertFalse(usuarioRepository.findByUsername("TesteadorModificado").isEmpty());
    }

    @Test
    void findByIdTest() {
        Usuario usuario = new Usuario();
        usuario.setUsername("Testeador1");
        usuario.setPassword("1234");
        usuario.setEmail("eltester@hotmail.com");
        Usuario guardado = usuarioRepository.save(usuario);
        Long id = guardado.getId();
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(id);
        assertTrue(usuarioEncontrado.isPresent());
        assertEquals("Testeador1", usuarioEncontrado.get().getUsername());
        assertEquals("1234", usuarioEncontrado.get().getPassword());
        assertEquals("eltester@hotmail.com", usuarioEncontrado.get().getEmail());
        usuarioRepository.delete(guardado);
    }

    @Test
    void noDuplicarUsuariosTest() {
        Usuario usuario = new Usuario();
        usuario.setUsername("Duplicado");
        usuario.setPassword("1234");
        usuario.setEmail("duplicado@email.com");

        // Guardar el usuario por primera vez
        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        try {
            // Intentar guardar el mismo usuario nuevamente
            Usuario usuarioDuplicado = new Usuario();
            usuarioDuplicado.setUsername("Duplicado");
            usuarioDuplicado.setPassword("5678");
            usuarioDuplicado.setEmail("duplicado2@email.com");

            usuarioRepository.save(usuarioDuplicado);
            fail("Se pudo añadir dos veces el mismo usuario, ERROR, no debería ser posible.");
        } catch (Exception e) {
            System.out.println("Excepción capturada correctamente al intentar duplicar un usuario: " + e.getMessage());
        }

        // Limpiar la base de datos
        usuarioRepository.delete(usuarioGuardado);
    }
}
