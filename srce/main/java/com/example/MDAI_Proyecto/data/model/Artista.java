package com.example.MDAI_Proyecto.data.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Entidad Artista que representa a un artista en el sistema.
 * Relacionado uno a uno con Usuario y uno a muchos con Cancion.
 */
@Entity
@Table(name = "Artista")
public class Artista {
    /** Atributos */

    /** Identificador del artista, que es el mismo que el del usuario asociado */
    @Id
    private Long idArtista;

    /** Relación OneToOne con Usuario */
    @OneToOne
    @MapsId
    @JoinColumn(name = "id_usuario", referencedColumnName = "id")
    private Usuario usuario;

    /** Biografía del artista */
    @Column(name = "biografia")
    private String biografia;


    /** Relación OneToMany con Cancion (lado inverso: Cancion.artista es el owning side) CASCADA
    *Cuando se elimina un artista, se eliminan sus canciones (orphanRemoval)
    * Lista de canciones asociadas al artista */
    @OneToMany(mappedBy = "artista", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cancion> canciones = new ArrayList<>();

    /** Getters y Setters */
    public Long getIdArtista() { return idArtista; }
    public void setIdArtista(Long idArtista) { this.idArtista = idArtista; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public String getBiografia() { return biografia; }
    public void setBiografia(String biografia) { this.biografia = biografia; }
    public List<Cancion> getCanciones() { return canciones;}
    public void setCanciones(List<Cancion> canciones) { this.canciones = canciones;}

    // Métodos auxiliares para mantener la relación bidireccional
    /** Añade una canción a la lista del artista y establece la relación.
     * @param cancion
     */
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

    /**
     * Remueve una canción de la lista del artista y desvincula la relación.
     * @param cancion
     */
    public void removeCancion(Cancion cancion) {
        if (cancion == null) return;
        if (this.canciones.remove(cancion)) {
            cancion.setArtista(null);
        }
    }
}
