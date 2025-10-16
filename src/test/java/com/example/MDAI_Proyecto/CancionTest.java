package com.example.MDAI_Proyecto;

import com.example.MDAI_Proyecto.data.repository.CancionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CancionTest {

    @Autowired
    private CancionRepository cancionRepository;
}
