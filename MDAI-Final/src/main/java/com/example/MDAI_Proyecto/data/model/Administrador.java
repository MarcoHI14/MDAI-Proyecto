package com.example.MDAI_Proyecto.data.model;// No se requieren cambios funcionales, solo advertencias de métodos no usados y nombre de archivo correcto.

public class Administrador {
    // Atributos de la clase Administrador
    private String nombre;
    private String id;
    private String contrasena;

    // Constructor de la clase Administrador
    public Administrador(String nombre, String id, String contrasena) {
        this.nombre = nombre;
        this.id = id;
        this.contrasena = contrasena;
    }

    // Métodos de la clase Administrador
    public void agregarUsuario() {
        // Lógica para agregar un usuario
    }

    public void eliminarUsuario() {
        // Lógica para eliminar un usuario
    }

    public void modificarUsuario() {
        // Lógica para modificar un usuario
    }

    public void listarUsuarios() {
        // Lógica para listar usuarios
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}
