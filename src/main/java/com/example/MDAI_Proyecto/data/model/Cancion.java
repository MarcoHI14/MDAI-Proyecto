package com.example.MDAI_Proyecto.data.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Cancion")
public class Cancion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cancion")
    private Long idCancion;

    @Column(nullable = false)
    private String titulo;

    private String genero;
    private String archivoAudio;

    @Column(name = "duracion")
    private String duracion;

    @Column(name = "fecha_subida")
    private LocalDateTime fechaSubida;

    // Relación con CancionPlaylist: lado inverso, para permitir cascada al eliminar una Cancion
    @OneToMany(mappedBy = "cancion", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<CancionPlaylist> cancionPlaylists = new ArrayList<>();


    @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "id_artista", nullable = false)
    private Artista artista;

    // Getters y setters
    public Long getIdCancion() { return idCancion; }
    public void setIdCancion(Long idCancion) { this.idCancion = idCancion; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
    public String getArchivoAudio() { return archivoAudio; }
    public void setArchivoAudio(String archivoAudio) { this.archivoAudio = archivoAudio; }
    public String getDuracion() { return duracion; }
    public void setDuracion(String duracion) { this.duracion = duracion; }
    public LocalDateTime getFechaSubida() { return fechaSubida; }
    public void setFechaSubida(LocalDateTime fechaSubida) { this.fechaSubida = fechaSubida; }
    public Artista getArtista() { return artista; }
    public void setArtista(Artista artista) {
        // Quitar de artista anterior
        if (this.artista != null && this.artista.getCanciones() != null) {
            this.artista.getCanciones().remove(this);
        }
        this.artista = artista;
        // Añadir a la nueva lista del artista si procede
        if (artista != null && artista.getCanciones() != null && !artista.getCanciones().contains(this)) {
            artista.getCanciones().add(this);
        }
    }

    public List<CancionPlaylist> getCancionPlaylists() { return cancionPlaylists; }
    public void setCancionPlaylists(List<CancionPlaylist> cancionPlaylists) { this.cancionPlaylists = cancionPlaylists; }

    // Helpers para mantener la relación en memoria
    public void addCancionPlaylist(CancionPlaylist cp) {
        if (cp == null) return;
        if (!this.cancionPlaylists.contains(cp)) {
            this.cancionPlaylists.add(cp);
            cp.setCancion(this);
        }
    }

    public void removeCancionPlaylist(CancionPlaylist cp) {
        if (cp == null) return;
        if (this.cancionPlaylists.remove(cp)) {
            cp.setCancion(null);
        }
    }

}
