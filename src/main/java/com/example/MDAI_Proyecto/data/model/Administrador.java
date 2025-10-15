package com.example.MDAI_Proyecto.data.model;

import jakarta.persistence.*;

@Entity
@Table(name = "administrador")
public class Administrador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_admin")
    private String id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String contrasena;

    // Constructor de la clase Administrador
    public Administrador(String nombre, String id, String contrasena) {
        this.nombre = nombre;
        this.id = id;
        this.contrasena = contrasena;
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