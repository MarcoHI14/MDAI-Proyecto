package com.example.MDAI_Proyecto.data.model;

import jakarta.persistence.*;

@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    // Relación bidireccional con Artista: opcional, permite cascada de borrado desde Usuario
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private com.example.MDAI_Proyecto.data.model.Artista artista;

    public com.example.MDAI_Proyecto.data.model.Artista getArtista() {
        return artista;
    }

    public void setArtista(com.example.MDAI_Proyecto.data.model.Artista artista) {
        this.artista = artista;
        if (artista != null && artista.getUsuario() != this) {
            artista.setUsuario(this);
        }
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean comprobarPassword(String password) {
        return this.password != null && this.password.equals(password);
    }

}
