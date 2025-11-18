package com.example.MDAI_Proyecto.data.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Cancion que representa una canción en el sistema.
 * Relacionada muchos a uno con Artista y uno a muchos con CancionPlaylist.
 */
@Entity
@Table(name = "Cancion")
public class Cancion {
    /** Atributos */

    /** Identificador único de la canción */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cancion")
    private Long idCancion;

    /** Título de la canción */
    @Column(nullable = false)
    private String titulo;

    /** Género musical de la canción */
    private String genero;
    /** Ruta o URL del archivo de audio */
    private String archivoAudio;

    /** Duración de la canción en formato "mm:ss" */
    @Column(name = "duracion")
    private String duracion;

    /** Fecha y hora en que se subió la canción */
    @Column(name = "fecha_subida")
    private LocalDateTime fechaSubida;

    /** Relación con CancionPlaylist: lado inverso, para permitir cascada al eliminar una Cancion
    * Cuando se elimina una canción, se eliminan sus asociaciones en CancionPlaylist (orphanRemoval) */
    @OneToMany(mappedBy = "cancion", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<CancionPlaylist> cancionPlaylists = new ArrayList<>();

    /** Relación muchos a uno con Artista */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_artista", nullable = false)
    private Artista artista;

    /** Getters y Setters */
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
    public void setCancionPlaylists(List<CancionPlaylist> cancionPlaylists) { this.cancionPlaylists = cancionPlaylists; }
    public List<CancionPlaylist> getCancionPlaylists() { return cancionPlaylists; }


    /**
     * Establece el artista de la canción, manteniendo la relación bidireccional.
     * @param artista
     */
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


    // Helpers para mantener la relación en memoria
    /**
     * Añade una relación CancionPlaylist a la canción, manteniendo la relación bidireccional.
     * @param cp
     */
    public void addCancionPlaylist(CancionPlaylist cp) {
        if (cp == null) return;
        if (!this.cancionPlaylists.contains(cp)) {
            this.cancionPlaylists.add(cp);
            cp.setCancion(this);
        }
    }

    /**
     * Elimina una relación CancionPlaylist de la canción, manteniendo la relación bidireccional.
     * @param cp
     */
    public void removeCancionPlaylist(CancionPlaylist cp) {
        if (cp == null) return;
        if (this.cancionPlaylists.remove(cp)) {
            cp.setCancion(null);
        }
    }

}
