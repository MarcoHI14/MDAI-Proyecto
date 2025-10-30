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
//    @OnDelete(action = OnDeleteAction.CASCADE)
    private Cancion cancion;

    private int orden;

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Playlist getPlaylist() { return playlist; }
    public void setPlaylist(Playlist playlist) { this.playlist = playlist; }

    public Cancion getCancion() { return cancion; }
    public void setCancion(Cancion cancion) { this.cancion = cancion; }

    public int getOrden() { return orden; }
    public void setOrden(int orden) { this.orden = orden; }

}
