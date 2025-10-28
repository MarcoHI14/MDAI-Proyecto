package com.example.MDAI_Proyecto.data.repository;

import com.example.MDAI_Proyecto.data.model.CancionPlaylist;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CancionPlaylistRepository extends CrudRepository<CancionPlaylist, Long> {

    @Query("SELECT cp FROM CancionPlaylist cp WHERE cp.playlist.idPlaylist = :playlistId")
    Optional<List<CancionPlaylist>> findByPlaylistIdPlaylist(@Param("playlistId") Long playlistId);


}
