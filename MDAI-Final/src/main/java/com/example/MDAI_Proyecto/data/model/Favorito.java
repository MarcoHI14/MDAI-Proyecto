package com.example.MDAI_Proyecto.data.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Favorito")
public class Favorito {
    @EmbeddedId
    private FavoritoId id;

    @ManyToOne
    @MapsId("idUsuario")
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne
    @MapsId("idCancion")
    @JoinColumn(name = "id_cancion")
    private Cancion cancion;

    @Column(name = "fecha_agregado")
    private LocalDateTime fechaAgregado;

    // Getters y setters
    public FavoritoId getId() { return id; }
    public void setId(FavoritoId id) { this.id = id; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public Cancion getCancion() { return cancion; }
    public void setCancion(Cancion cancion) { this.cancion = cancion; }
    public LocalDateTime getFechaAgregado() { return fechaAgregado; }
    public void setFechaAgregado(LocalDateTime fechaAgregado) { this.fechaAgregado = fechaAgregado; }
}

