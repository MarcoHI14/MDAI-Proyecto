package com.example.MDAI_Proyecto.data.model;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * Entidad intermedia para la relación muchos a muchos entre Cancion y Playlist,
 * con un atributo adicional 'orden' para indicar la posición de la canción en la playlist.
 */
@Entity
@Table(
        name = "cancion_playlist",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_cancion", "id_playlist"})
)
public class CancionPlaylist {

    /** Atributos */
    /** Identificador único de la relación */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Relación muchos a uno con Playlist */
    @ManyToOne
    @JoinColumn(name = "id_playlist", nullable = false)
    private Playlist playlist;

    /** Relación muchos a uno con Cancion */
    @ManyToOne
    @JoinColumn(name = "id_cancion", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE) // asegurar cascada a nivel de BD si se genera DDL
    private Cancion cancion;

    /** Orden de la canción en la playlist */
    private int orden;

    /** Getters y Setters */
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Playlist getPlaylist() { return playlist; }
    /**
     * Establece la playlist asociada, manteniendo la relación bidireccional.
     * @param playlist
     */
    public void setPlaylist(Playlist playlist) {
        if (this.playlist != null && this.playlist.getCancionPlaylists() != null) {
            this.playlist.getCancionPlaylists().remove(this);
        }
        this.playlist = playlist;
        // No añadir aquí para evitar introducir instancias transitorias
    }
    public Cancion getCancion() { return cancion; }
    /**
     * Establece la canción asociada, manteniendo la relación bidireccional.
     * @param cancion
     */
    public void setCancion(Cancion cancion) {
        // quitar esta instancia de la lista de la canción anterior si existe
        if (this.cancion != null && this.cancion.getCancionPlaylists() != null) {
            this.cancion.getCancionPlaylists().remove(this);
        }
        this.cancion = cancion;
        // No añadir aquí para evitar introducir instancias transitorias en la colección
    }
    public int getOrden() { return orden; }
    public void setOrden(int orden) { this.orden = orden; }

    /**
     * Metodo para mantener la relación bidireccional después de persistir.
     */
    @PostPersist
    private void postPersist() {
        if (this.cancion != null && this.cancion.getCancionPlaylists() != null && !this.cancion.getCancionPlaylists().contains(this)) {
            this.cancion.getCancionPlaylists().add(this);
        }
        if (this.playlist != null && this.playlist.getCancionPlaylists() != null && !this.playlist.getCancionPlaylists().contains(this)) {
            this.playlist.getCancionPlaylists().add(this);
        }
    }

    /**
     * Metodo para mantener la relación bidireccional después de eliminar.
     */
    @PostRemove
    private void postRemove() {
        if (this.cancion != null && this.cancion.getCancionPlaylists() != null) {
            this.cancion.getCancionPlaylists().remove(this);
        }
        if (this.playlist != null && this.playlist.getCancionPlaylists() != null) {
            this.playlist.getCancionPlaylists().remove(this);
        }
    }
}
