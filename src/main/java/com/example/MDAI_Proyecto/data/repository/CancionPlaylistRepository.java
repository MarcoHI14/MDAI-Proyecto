package com.example.MDAI_Proyecto.data.repository;

import com.example.MDAI_Proyecto.data.model.CancionPlaylist;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CancionPlaylistRepository extends CrudRepository<CancionPlaylist, Long> {

}
