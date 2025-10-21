package com.example.MDAI_Proyecto.data.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

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

    @ManyToOne
        @JoinColumn(name = "id_artista", referencedColumnName = "id_usuario")
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
    public void setArtista(Artista artista) { this.artista = artista; }
}

