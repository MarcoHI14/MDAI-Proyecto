package com.example.MDAI_Proyecto.data.services;

import com.example.MDAI_Proyecto.data.model.Usuario;
import com.example.MDAI_Proyecto.data.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioServiceImplement implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    /**
     * Constructor para inyección de dependencias.
     *
     * @param usuarioRepository El repositorio de usuarios.
     */
    public UsuarioServiceImplement(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Obtiene todos los usuarios del repositorio.
     *
     * @return Una lista de todos los usuarios.
     */
    public List<Usuario> findAll() {
        return (List<Usuario>) usuarioRepository.findAll();
    }

    /**
     * Obtiene un usuario por su nombre de usuario.
     *
     * @param username El nombre de usuario del usuario a obtener.
     * @return El usuario si se encuentra, o null si no.
     */
    public Usuario obtenerUsuarioByUsername(String username){
        Usuario usuario = usuarioRepository.findByUsername(username).orElse(null);
        return usuario;
    }

    /**
     * Obtiene un usuario por su correo electrónico.
     *
     * @param email El correo electrónico del usuario a obtener.
     * @return El usuario si se encuentra, o null si no.
     */
    public Usuario obtenerUsuarioByEmail(String email){
        Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
        return usuario;
    }

    /**
     * Obtiene un usuario por su ID.
     *
     * @param id El ID del usuario a obtener.
     * @return El usuario si se encuentra, o null si no.
     */
    public Usuario obtenerUsuarioPorId(Long id){
        Usuario usuario  = usuarioRepository.findById(id).orElse(null);
        return usuario;
    }

    /**
     * Guarda un usuario en el repositorio.
     *
     * @param usuario El usuario a guardar.
     * @return El usuario guardado.
     */
    public Usuario guardarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    /**
     * Elimina un usuario del repositorio.
     *
     * @param usuario El usuario a eliminar.
     */
    public void eliminarUsuario(Usuario usuario) {
        usuarioRepository.delete(usuario);
    }
}
