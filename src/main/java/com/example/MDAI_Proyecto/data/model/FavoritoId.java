package com.example.MDAI_Proyecto.data.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class FavoritoId implements Serializable {
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(name = "id_cancion")
    private Integer idCancion;

    // Getters, setters, equals y hashCode
    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }
    public Integer getIdCancion() { return idCancion; }
    public void setIdCancion(Integer idCancion) { this.idCancion = idCancion; }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FavoritoId that = (FavoritoId) o;
        return Objects.equals(idUsuario, that.idUsuario) && Objects.equals(idCancion, that.idCancion);
    }
    @Override
    public int hashCode() {
        return Objects.hash(idUsuario, idCancion);
    }
}

