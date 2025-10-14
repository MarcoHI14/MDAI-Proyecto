package com.example.MDAI_Proyecto.data.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Artista")
public class Artista {
    @Id
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @OneToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id")
    private Usuario usuario;

    @Column(name = "biografia")
    private String biografia;

    // Getters y setters
    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public String getBiografia() { return biografia; }
    public void setBiografia(String biografia) { this.biografia = biografia; }
}

// No hay errores funcionales, solo advertencias de métodos no usados. No es necesario modificar la lógica.
