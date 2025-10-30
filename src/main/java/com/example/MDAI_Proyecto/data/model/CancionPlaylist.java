package com.example.MDAI_Proyecto.data.model;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(
        name = "cancion_playlist",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_cancion", "id_playlist"})
)
public class CancionPlaylist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_playlist", nullable = false)
    private Playlist playlist;

    @ManyToOne
    @JoinColumn(name = "id_cancion", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE) // asegurar cascada a nivel de BD si se genera DDL
    private Cancion cancion;

    private int orden;

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Playlist getPlaylist() { return playlist; }
    public void setPlaylist(Playlist playlist) {
        if (this.playlist != null && this.playlist.getCancionPlaylists() != null) {
            this.playlist.getCancionPlaylists().remove(this);
        }
        this.playlist = playlist;
        // No añadir aquí para evitar introducir instancias transitorias
    }

    public Cancion getCancion() { return cancion; }
    public void setCancion(Cancion cancion) {
        // quitar esta instancia de la lista de la canción anterior si existe
        if (this.cancion != null && this.cancion.getCancionPlaylists() != null) {
            this.cancion.getCancionPlaylists().remove(this);
        }
        this.cancion = cancion;
        // No añadir aquí para evitar introducir instancias transitorias en la colección
    }

    @PostPersist
    private void postPersist() {
        if (this.cancion != null && this.cancion.getCancionPlaylists() != null && !this.cancion.getCancionPlaylists().contains(this)) {
            this.cancion.getCancionPlaylists().add(this);
        }
        if (this.playlist != null && this.playlist.getCancionPlaylists() != null && !this.playlist.getCancionPlaylists().contains(this)) {
            this.playlist.getCancionPlaylists().add(this);
        }
    }

    @PostRemove
    private void postRemove() {
        if (this.cancion != null && this.cancion.getCancionPlaylists() != null) {
            this.cancion.getCancionPlaylists().remove(this);
        }
        if (this.playlist != null && this.playlist.getCancionPlaylists() != null) {
            this.playlist.getCancionPlaylists().remove(this);
        }
    }

    public int getOrden() { return orden; }
    public void setOrden(int orden) { this.orden = orden; }

}
