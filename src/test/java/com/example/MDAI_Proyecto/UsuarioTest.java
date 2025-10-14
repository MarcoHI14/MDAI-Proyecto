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
    void crearUsuarios() {
        Usuario usuario = new Usuario();
        usuario.setUsername("Testeador1");
        usuario.setPassword("1234");
        usuario.setEmail("testeador1@email.com");

        Usuario guardado = userRepository.save(usuario);//prueba
        assertThat(guardado.getId()).isNotNull();
        assertThat(guardado.getUsername()).isEqualTo("Testeador1");
        assertThat(guardado.getPassword()).isEqualTo("1234");
        assertThat(guardado.getEmail()).isEqualTo("testeador1@email.com");

        userRepository.delete(guardado);
        boolean existe = userRepository.findById(guardado.getId()).isPresent();
        assertThat(existe).isFalse();

        Usuario usuario2 = new Usuario();
        Usuario usuario3 = new Usuario();
    }

}
