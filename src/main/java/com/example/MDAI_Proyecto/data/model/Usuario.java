package com.example.MDAI_Proyecto.data.model;

import jakarta.persistence.*;

/**
 * Entidad Usuario que representa un usuario en el sistema.
 * Relacionada uno a uno con Artista.
 */
@Entity
@Table(name = "usuario")
public class Usuario {
    /** Atributos */
    /** Identificador único del usuario */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre de usuario único */
    @Column(nullable = false, unique = true)
    private String username;

    /** Contraseña del usuario */
    @Column(nullable = false)
    private String password;

    /** Correo electrónico único del usuario */
    @Column(nullable = false, unique = true)
    private String email;

    /** Relación bidireccional con Artista: opcional, permite cascada de borrado desde Usuario
     * Cuando se elimina un usuario, se elimina su artista asociado (si existe)
     */
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Artista artista;

    /** Obtiene el artista asociado a este usuario.
     *
     * @return el artista asociado, o null si no existe
     */
    Artista getArtista() { return artista;}

    /** Establece el artista asociado a este usuario.
     * Mantiene la consistencia de la relación bidireccional.
     *
     * @param artista el artista a asociar
     */
    public void setArtista(Artista artista) {
        this.artista = artista;
        if (artista != null && artista.getUsuario() != this) {
            artista.setUsuario(this);
        }
    }

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) {this.username = username; }
    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}
    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    /**
     * Comprueba si la contraseña proporcionada coincide con la del usuario.
     *
     * @param password la contraseña a comprobar
     * @return true si coinciden, false en caso contrario
     */
    public boolean comprobarPassword(String password) {
        return this.password != null && this.password.equals(password);
    }

}
