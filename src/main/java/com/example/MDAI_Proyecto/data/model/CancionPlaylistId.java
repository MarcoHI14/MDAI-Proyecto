package com.example.MDAI_Proyecto.data.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CancionPlaylistId implements Serializable {
    @Column(name = "id_playlist")
    private Integer idPlaylist;

    @Column(name = "id_cancion")
    private Integer idCancion;

    // Getters, setters, equals y hashCode
    public Integer getIdPlaylist() { return idPlaylist; }
    public void setIdPlaylist(Integer idPlaylist) { this.idPlaylist = idPlaylist; }
    public Integer getIdCancion() { return idCancion; }
    public void setIdCancion(Integer idCancion) { this.idCancion = idCancion; }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CancionPlaylistId that = (CancionPlaylistId) o;
        return Objects.equals(idPlaylist, that.idPlaylist) && Objects.equals(idCancion, that.idCancion);
    }
    @Override
    public int hashCode() {
        return Objects.hash(idPlaylist, idCancion);
    }
}

