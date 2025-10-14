package com.example.MDAI_Proyecto;

import com.example.MDAI_Proyecto.data.model.Usuario;
import com.example.MDAI_Proyecto.data.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UsuarioTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void crearUsuario() {
        Usuario usuario = new Usuario();
        usuario.setUsername("Testeador1");
        usuario.setPassword("1234");
        usuario.setEmail("testeador1@email.com");

        Usuario guardado = userRepository.save(usuario);

        assertThat(guardado.getId()).isNotNull();
        assertThat(guardado.getUsername()).isEqualTo("Testeador1");
        assertThat(guardado.getPassword()).isEqualTo("1234");
        assertThat(guardado.getEmail()).isEqualTo("testeador1@email.com");

        userRepository.delete(guardado);
        boolean existe = userRepository.findById(guardado.getId()).isPresent();
        assertThat(existe).isFalse();
    }

    @Test
    void crearUsuarios() {

        Usuario usuario2 = new Usuario();
        Usuario usuario3 = new Usuario();
        Usuario usuario4 = new Usuario();

        usuario2.setUsername("Testeador2");
        usuario2.setPassword("1234");
        usuario2.setEmail("testeador2@gmail.com");

        usuario3.setUsername("Testeador3");
        usuario3.setPassword("2345");
        usuario3.setEmail("testeador3@gmail.com");

        usuario4.setUsername("Testeador4");
        usuario4.setPassword("3456");
        usuario4.setEmail("testeador4@gmail.com");

        userRepository.save(usuario2);
        userRepository.save(usuario3);
        userRepository.save(usuario4);

        assertThat(userRepository.findAll()).hasSize(3);

        String nombre2=userRepository.findById(usuario2.getId()).get().getUsername();
        assertThat(nombre2).isEqualTo("Testeador2");
        String password2=userRepository.findById(usuario2.getId()).get().getPassword();
        assertThat(password2).isEqualTo("1234");
        String email2=userRepository.findById(usuario2.getId()).get().getEmail();
        assertThat(email2).isEqualTo("testeador2@gmail.com");

        String nombre3=userRepository.findById(usuario3.getId()).get().getUsername();
        assertThat(nombre3).isEqualTo("Testeador3");
        String password3=userRepository.findById(usuario3.getId()).get().getPassword();
        assertThat(password3).isEqualTo("2345");
        String email3=userRepository.findById(usuario3.getId()).get().getEmail();
        assertThat(email3).isEqualTo("testeador3@gmail.com");

        String nombre4=userRepository.findById(usuario4.getId()).get().getUsername();
        assertThat(nombre4).isEqualTo("Testeador4");
        String password4=userRepository.findById(usuario4.getId()).get().getPassword();
        assertThat(password4).isEqualTo("3456");
        String email4=userRepository.findById(usuario4.getId()).get().getEmail();
        assertThat(email4).isEqualTo("testeador4@gmail.com");
    }
}
