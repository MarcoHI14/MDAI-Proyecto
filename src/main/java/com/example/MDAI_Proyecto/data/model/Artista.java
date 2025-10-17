package com.example.MDAI_Proyecto.data.model;

import jakarta.persistence.*;

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

    // Getters y setters
    public Long getIdArtista() { return idArtista; }
    public void setIdArtista(Long idArtista) { this.idArtista = idArtista; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public String getBiografia() { return biografia; }
    public void setBiografia(String biografia) { this.biografia = biografia; }
}
