package package com.example.MDAI_Proyecto.data.repository;

import com.example.MDAI_PROYECTO.data.model.Playlist;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaylistRepository extends CrudRepository<User, Long> {
}