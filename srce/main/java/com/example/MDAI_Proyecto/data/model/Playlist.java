package com.example.MDAI_Proyecto.data.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Playlist que representa una lista de reproducción en el sistema.
 * Relacionada muchos a uno con Usuario y uno a muchos con CancionPlaylist.
 */
@Entity
@Table(name = "Playlist")
public class Playlist {
    /** Atributos */
    /** Identificador único de la playlist */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_playlist")
    private Long idPlaylist;

    /** Nombre de la playlist */
    @Column(nullable = false)
    private String nombre;

    /** Descripción de la playlist */
    private String descripcion;

    /** Fecha y hora de creación de la playlist */
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    /** Relación muchos a uno con Usuario */
    @ManyToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id")
    private Usuario usuario;

    /** Relación con CancionPlaylist para permitir cascada al eliminar la playlist
    * Cuando se elimina una playlist, se eliminan sus asociaciones en CancionPlaylist (orphanRemoval) */
    @OneToMany(mappedBy = "playlist", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<CancionPlaylist> cancionPlaylists = new ArrayList<>();

    /** Getters y Setters */
    public Long getIdPlaylist() { return idPlaylist; }
    public void setIdPlaylist(Long idPlaylist) { this.idPlaylist = idPlaylist; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) {
        // Romper relación con el usuario anterior
        if (this.usuario != null && this.usuario.getPlaylists() != null) {
            this.usuario.getPlaylists().remove(this);
        }
        // Establecer nuevo usuario
        this.usuario = usuario;
        // Añadir a la colección del nuevo usuario si no está ya presente
        if (usuario != null && usuario.getPlaylists() != null && !usuario.getPlaylists().contains(this)) {
            usuario.getPlaylists().add(this);
        }
    }
    public List<CancionPlaylist> getCancionPlaylists() { return cancionPlaylists; }
    public void setCancionPlaylists(List<CancionPlaylist> cancionPlaylists) { this.cancionPlaylists = cancionPlaylists; }

    // Helpers para mantener la relación bidireccional
    /**
     * Añade una CancionPlaylist a la lista y establece la relación.
     * @param cp
     */
    public void addCancionPlaylist(CancionPlaylist cp) {
        if (cp == null) return;
        if (!this.cancionPlaylists.contains(cp)) {
            this.cancionPlaylists.add(cp);
            cp.setPlaylist(this);
        }
    }

    /**
     * Elimina una CancionPlaylist de la lista y rompe la relación.
     * @param cp
     */
    public void removeCancionPlaylist(CancionPlaylist cp) {
        if (cp == null) return;
        if (this.cancionPlaylists.remove(cp)) {
            cp.setPlaylist(null);
        }
    }
}
