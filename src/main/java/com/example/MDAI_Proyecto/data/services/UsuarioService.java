package com.example.MDAI_Proyecto.data.services;

import java.util.List;
import com.example.MDAI_Proyecto.data.model.Usuario;

/**
 * Servicio para la entidad Usuario.
 * Proporciona métodos para realizar operaciones relacionadas con Usuario.
 */
public interface UsuarioService {

    List<Usuario> findAll();

    Usuario obtenerUsuarioByUsername(String username);

    Usuario obtenerUsuarioByEmail(String email);

    Usuario obtenerUsuarioPorId(Long id);

    Usuario guardarUsuario(Usuario usuario);

    void eliminarUsuario(Usuario usuario);
}
