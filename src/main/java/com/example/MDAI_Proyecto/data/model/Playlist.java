package com.example.MDAI_Proyecto.data.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Playlist")
public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_playlist")
    private Long idPlaylist;

    @Column(nullable = false)
    private String nombre;

    private String descripcion;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @ManyToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id")
    private Usuario usuario;

    // Relación con CancionPlaylist para permitir cascada al eliminar la playlist
    @OneToMany(mappedBy = "playlist", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<CancionPlaylist> cancionPlaylists = new ArrayList<>();

    // Getters y setters
    public Long getIdPlaylist() { return idPlaylist; }
    public void setIdPlaylist(Long idPlaylist) { this.idPlaylist = idPlaylist; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public List<CancionPlaylist> getCancionPlaylists() { return cancionPlaylists; }
    public void setCancionPlaylists(List<CancionPlaylist> cancionPlaylists) { this.cancionPlaylists = cancionPlaylists; }

    // Helpers para mantener la relación bidireccional
    public void addCancionPlaylist(CancionPlaylist cp) {
        if (cp == null) return;
        if (!this.cancionPlaylists.contains(cp)) {
            this.cancionPlaylists.add(cp);
            cp.setPlaylist(this);
        }
    }

    public void removeCancionPlaylist(CancionPlaylist cp) {
        if (cp == null) return;
        if (this.cancionPlaylists.remove(cp)) {
            cp.setPlaylist(null);
        }
    }
}
