package com.example.MDAI_Proyecto;

import com.example.MDAI_Proyecto.data.model.Artista;
import com.example.MDAI_Proyecto.data.model.Usuario;
import com.example.MDAI_Proyecto.data.repository.ArtistaRepository;
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
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UsuarioTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ArtistaRepository artistaRepository;

    /** Test para crear, leer, actualizar y eliminar usuarios en la base de datos. **/
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

    /** Test para buscar usuarios por su nombre de usuario. Usando findByUsernameTest. **/
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

    /** Test para buscar usuarios por su email. Usando findByEmailTest. **/
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

    /** Test para comprobar el método comprobarPassword de la clase Usuario. **/
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

    /** Test para cambiar los atributos de un usuario existente. **/
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

    /** Test para buscar un usuario por su ID. Usando findById. **/
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

    /** Test para comprobar que no se pueden duplicar usuarios en la base de datos. **/
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

    /** Test CRUD completo para la entidad Usuario. **/
    @Test
        // CRUD Test Especificado
    void CRUDtest() {
        // Create
        Usuario usuarioCreate = new Usuario();
        usuarioCreate.setUsername("CRUDUser");
        usuarioCreate.setPassword("crudpass");
        usuarioCreate.setEmail("crusaders@dio.com");

        Usuario savedUser = usuarioRepository.save(usuarioCreate);
        assertNotNull(savedUser.getId());

        // Read
        Optional<Usuario> usuarioLeer = usuarioRepository.findById(savedUser.getId());
        assertTrue(usuarioLeer.isPresent());
        assertEquals("CRUDUser", usuarioLeer.get().getUsername());
        assertEquals("crudpass", usuarioLeer.get().getPassword());
        assertEquals("crusaders@dio.com", usuarioLeer.get().getEmail());

        // Update
        Usuario usuarioUpdate = usuarioLeer.get();
        usuarioUpdate.setUsername("UPDATEUser");
        usuarioUpdate.setPassword("crudpass2");
        usuarioUpdate.setEmail("stardust@jojo.com");

        Usuario updatedUser = usuarioRepository.save(usuarioUpdate);
        assertEquals("UPDATEUser", updatedUser.getUsername());
        assertEquals("crudpass2", updatedUser.getPassword());
        assertEquals("stardust@jojo.com", updatedUser.getEmail());

        // Delete
        usuarioRepository.delete(updatedUser);
        Optional<Usuario> usuarioDeleted = usuarioRepository.findById(updatedUser.getId());
        assertFalse(usuarioDeleted.isPresent());

    }

    /** Test para comprobar la eliminación en cascada de un artista al eliminar su usuario asociado. **/
    @Test
    void eliminarUsuarioyArtistaCascadaTest() {
        // Crear y guardar un usuario
        Usuario usuario = new Usuario();
        usuario.setUsername("ArtistaUsuario");
        usuario.setPassword("artistapass");
        usuario.setEmail("usuarti@gmail.com");
        Artista artista = new Artista();
        artista.setBiografia("Biografia del artista");
        artista.setUsuario(usuario);
        usuario.setArtista(artista);

        Usuario guardado = usuarioRepository.save(usuario);
        Artista artistaguardado = artistaRepository.save(artista);


        // Verificar que el artista se ha guardado correctamente
        assertNotNull(artistaguardado.getIdArtista());
        assertEquals("Biografia del artista", artistaguardado.getBiografia());
        assertEquals(guardado.getId(), artistaguardado.getUsuario().getId());

        // Eliminar el usuario
        usuarioRepository.delete(guardado);
        // Verificar que el artista asociado también se ha eliminado
        Optional<Artista> artistaEliminado = artistaRepository.findById(artistaguardado.getIdArtista());
        assertFalse(artistaEliminado.isPresent());

    }

    /**Test para comprobar que el username no puede repetirse entre usuarios.**/
    @Test
    void usuarioUsernameUnique() {
        Usuario usuario = new Usuario();
        usuario.setUsername("Marco");
        usuario.setPassword("M14");
        usuario.setEmail("marco@unex.es");
        usuarioRepository.save(usuario);

        Usuario usuario1 = new Usuario();
        usuario1.setUsername("Marco");
        usuario1.setPassword("M15");
        usuario1.setEmail("marco2@uex.com");
        try {
            usuarioRepository.save(usuario1);
            fail("Se pudo añadir dos veces el mismo username, ERROR, no debería ser posible.");
            usuarioRepository.delete(usuario1);
        } catch (Exception e) {
            System.out.println("Excepción capturada correctamente al intentar duplicar un username: " + e.getMessage());
        }

        usuarioRepository.delete(usuario);
    }

    /**Test para comprobar que el username de un usuario no puede ser nulo.**/
    @Test
    void usuarioUsernameNullable() {
        Usuario usuario = new Usuario();
        usuario.setUsername(null);
        usuario.setPassword("M14");
        usuario.setEmail("m14@gamil.org");
        try {
            usuarioRepository.save(usuario);
            fail("Se pudo añadir un usuario con username nulo, ERROR, no debería ser posible.");
            usuarioRepository.delete(usuario);
        } catch (Exception e) {
            System.out.println("Excepción capturada correctamente al intentar añadir un usuario con username nulo: " + e.getMessage());
        }

    }

    /**Test para comprobar que el password de un usuario no puede ser nulo.**/
    @Test
    void usuarioPasswordNullable() {
        Usuario usuario = new Usuario();
        usuario.setUsername("Marco");
        usuario.setPassword(null);
        usuario.setEmail("marc0@dub.com");
        try {
            usuarioRepository.save(usuario);
            fail("Se pudo añadir un usuario con password nulo, ERROR, no debería ser posible.");
            usuarioRepository.delete(usuario);
        } catch (Exception e) {
            System.out.println("Excepción capturada correctamente al intentar añadir un usuario con password nulo: " + e.getMessage());
        }
    }

    /**Test para comprobar que el email no puede repetirse entre usuarios.**/
    @Test
    void usuarioEmailUnique() {
        Usuario usuario = new Usuario();
        usuario.setUsername("Marco1");
        usuario.setPassword("M14");
        usuario.setEmail("original@mail.com");
        usuarioRepository.save(usuario);

        Usuario usuario1 = new Usuario();
        usuario1.setUsername("Marco2");
        usuario1.setPassword("M15");
        usuario1.setEmail("original@mail.com");

        try {
            usuarioRepository.save(usuario1);
            fail("Se pudo añadir dos veces el mismo email, ERROR, no debería ser posible.");
            usuarioRepository.delete(usuario1);
        } catch (Exception e) {
            System.out.println("Excepción capturada correctamente al intentar duplicar un email: " + e.getMessage());
        }
        usuarioRepository.delete(usuario);
    }

    /**Test para comprobar que el email de un usuario no puede ser nulo.**/
    @Test
    void usuarioEmailNullable() {
        Usuario usuario = new Usuario();
        usuario.setUsername("Marco");
        usuario.setPassword("M14");
        usuario.setEmail(null);
        try {
            usuarioRepository.save(usuario);
            fail("Se pudo añadir un usuario con email nulo, ERROR, no debería ser posible.");
            usuarioRepository.delete(usuario);
        } catch (Exception e) {
            System.out.println("Excepción capturada correctamente al intentar añadir un usuario con email nulo: " + e.getMessage());
        }
    }
}

