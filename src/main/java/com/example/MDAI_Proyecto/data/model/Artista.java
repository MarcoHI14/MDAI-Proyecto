package com.example.MDAI_Proyecto.data.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Artista")
public class Artista {
    @Id
    private Long idArtista;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id_usuario", referencedColumnName = "id")
    private Usuario usuario;

    @Column(name = "biografia")
    private String biografia;

    // Relación OneToMany con Cancion (lado inverso: Cancion.artista es el owning side) CASCADA
    @OneToMany(mappedBy = "artista", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cancion> canciones = new ArrayList<>();

    // Getters y setters
    public Long getIdArtista() { return idArtista; }
    public void setIdArtista(Long idArtista) { this.idArtista = idArtista; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public String getBiografia() { return biografia; }
    public void setBiografia(String biografia) { this.biografia = biografia; }

    // Getters/Setters para canciones
    public List<Cancion> getCanciones() {
        return canciones;
    }

    public void setCanciones(List<Cancion> canciones) {
        this.canciones = canciones;
    }

    // Métodos auxiliares para mantener la relación bidireccional
    public void addCancion(Cancion cancion) {
        if (cancion == null) return;

        // Si la canción ya está asignada a otro artista distinto -> error
        Artista artistaActual = cancion.getArtista();
        if (artistaActual != null) {
            Long idActual = artistaActual.getIdArtista();
            Long idThis = this.getIdArtista();
            // Si ambos ids existen y son distintos -> pertenece a otro artista
            if (idActual != null && idThis != null && !Objects.equals(idActual, idThis)) {
                throw new IllegalArgumentException("La canción ya pertenece a otro artista");
            }
            // Si alguno de los ids es null, comparamos por referencia: si no es el mismo objeto y ambos tienen usuario con id distintos -> error
            if ((idActual == null || idThis == null) && artistaActual != this) {
                Long userActualId = artistaActual.getUsuario() != null ? artistaActual.getUsuario().getId() : null;
                Long thisUserId = this.getUsuario() != null ? this.getUsuario().getId() : null;
                if (userActualId != null && thisUserId != null && !Objects.equals(userActualId, thisUserId)) {
                    throw new IllegalArgumentException("La canción ya pertenece a otro artista");
                }
            }
        }

        // Evitar duplicados en la lista
        if (!this.canciones.contains(cancion)) {
            this.canciones.add(cancion);
            cancion.setArtista(this);
        }
    }

    public void removeCancion(Cancion cancion) {
        if (cancion == null) return;
        if (this.canciones.remove(cancion)) {
            cancion.setArtista(null);
        }
    }
}
