package com.example.MDAI_Proyecto.data.model;

import jakarta.persistence.*;

/**
 * Entidad Vehiculo que representa un vehículo asociado (opcionalmente) a un Usuario.
 */
@Entity
@Table(name = "vehiculo")
public class Vehiculo {
    // Identificador
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Marca del vehículo (p.ej. Toyota)
    @Column(nullable = false)
    private String marca;

    // Modelo del vehículo (p.ej. Corolla)
    @Column(nullable = false)
    private String modelo;

    // Matrícula única del vehículo
    @Column(nullable = false, unique = true)
    private String matricula;

    // Año de fabricación
    @Column(name = "anio")
    private Integer anio;

    // Relación ManyToOne con Usuario: varios vehículos pueden pertenecer a un mismo usuario.
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario propietario;

    // Constructores
    public Vehiculo() {
    }

    public Vehiculo(String marca, String modelo, String matricula, Integer anio, Usuario propietario) {
        this.marca = marca;
        this.modelo = modelo;
        this.matricula = matricula;
        this.anio = anio;
        this.propietario = propietario;
    }

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }

    public Integer getAnio() { return anio; }
    public void setAnio(Integer anio) { this.anio = anio; }

    public Usuario getPropietario() { return propietario; }
    public void setPropietario(Usuario propietario) { this.propietario = propietario; }

    @Override
    public String toString() {
        return "Vehiculo{" +
                "id=" + id +
                ", marca='" + marca + '\'' +
                ", modelo='" + modelo + '\'' +
                ", matricula='" + matricula + '\'' +
                ", anio=" + anio +
                '}';
    }
}

