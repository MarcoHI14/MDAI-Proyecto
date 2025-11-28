package com.example.MDAI_Proyecto.data.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Usuario que representa un usuario en el sistema.
 * Relacionada uno a uno con Artista.
 */
@Entity
@Table(name = "usuario")
public class Usuario {

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

    /** Relación uno a muchos con SolicitudVerificacion: cuando se elimina un usuario,
     * también se eliminan sus solicitudes (cascade REMOVE). Se mantiene como colección
     * bidireccional con mappedBy = "usuario" en la entidad SolicitudVerificacion.
     */
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<SolicitudVerificacion> solicitudes = new ArrayList<>();

    /** Relación uno a muchos con Playlist: cuando se elimina un usuario,
     * también se eliminan sus playlists asociadas (cascade REMOVE + orphanRemoval).
     */
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Playlist> playlists = new ArrayList<>();

    /** Obtiene el artista asociado a este usuario.
     *
     * @return el artista asociado, o null si no existe
     */
    public Artista getArtista() { return artista;}

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

    /** Obtiene la lista de solicitudes asociadas a este usuario. */
    public List<SolicitudVerificacion> getSolicitudes() { return solicitudes; }

    /** Añade una solicitud y mantiene la relación bidireccional. */
    public void addSolicitud(SolicitudVerificacion solicitud) {
        if (solicitud != null && !solicitudes.contains(solicitud)) {
            solicitudes.add(solicitud);
            solicitud.setUsuario(this);
        }
    }

    /** Elimina una solicitud y mantiene la relación bidireccional. */
    public void removeSolicitud(SolicitudVerificacion solicitud) {
        if (solicitudes.remove(solicitud)) {
            solicitud.setUsuario(null);
        }
    }

    /** Obtiene la lista de playlists asociadas a este usuario. */
    public List<Playlist> getPlaylists() { return playlists; }

    /** Añade una playlist y mantiene la relación bidireccional. */
    public void addPlaylist(Playlist playlist) {
        if (playlist != null && !playlists.contains(playlist)) {
            playlists.add(playlist);
            playlist.setUsuario(this);
        }
    }

    /** Elimina una playlist y mantiene la relación bidireccional. */
    public void removePlaylist(Playlist playlist) {
        if (playlists.remove(playlist)) {
            playlist.setUsuario(null);
        }
    }

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
