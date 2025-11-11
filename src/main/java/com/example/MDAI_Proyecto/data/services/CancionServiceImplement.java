package com.example.MDAI_Proyecto.data.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.MDAI_Proyecto.data.model.Cancion;
import com.example.MDAI_Proyecto.data.repository.CancionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class CancionServiceImplement {

    private CancionRepository cancionRepository;

    public CancionServiceImplement(CancionRepository cancionRepository) {
        this.cancionRepository = cancionRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Cancion> findById(Long id) {
        if (id == null || id <= 0) {
            return Optional.empty(); // o lanzar IllegalArgumentException si prefieres
        }
        return cancionRepository.findById(id);
    }

    public List<Cancion> getAll() {
        Iterable<Cancion> iterable = cancionRepository.findAll();
        List<Cancion> result = new ArrayList<>();
        iterable.forEach(result::add);
        return result;
    }

    @Transactional(readOnly = true)
    public Optional<Cancion> findByTituloIgnoreCase (String titulo) {
        if (!StringUtils.hasText(titulo)) {
            return Optional.empty(); // o lanzar IllegalArgumentException si prefieres
        }
        return cancionRepository.findByTituloIgnoreCase(titulo);
    }

    @Transactional(readOnly=true)
    public List<Cancion> findByGeneroIgnoreCase (String genero) {
        if (!StringUtils.hasText(genero)) {
            return new ArrayList<>(); // o lanzar IllegalArgumentException si prefieres
        }
        return cancionRepository.findByGeneroIgnoreCase(genero);
    }

    @Transactional(readOnly = true)
    public List<Cancion> findbyArtistaName (String nombreArtista) {
        if (!StringUtils.hasText(nombreArtista)) {
            return new ArrayList<>(); // o lanzar IllegalArgumentException si prefieres
        }
        Iterable<Cancion> iterable = cancionRepository.findAll();
        List<Cancion> result = new ArrayList<>();
        for (Cancion cancion : iterable) {
            if (cancion.getArtista().getUsuario().getUsername().equalsIgnoreCase(nombreArtista)) {
                result.add(cancion);
            }
        }
        return result;
    }
}
