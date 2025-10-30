package com.example.MDAI_Proyecto.data.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad SolicitudVerificacion que representa una solicitud de verificación de un usuario.
 * Relacionada muchos a uno con Usuario.
 */
@Entity
@Table(name = "Solicitud_Verificacion")
public class SolicitudVerificacion {
    /** Atributos */
    /** Identificador único de la solicitud */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_solicitud")
    private Long idSolicitud;

    /** Relación muchos a uno con Usuario */
    @ManyToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id")
    private Usuario usuario;

    /** Mensaje de verificación proporcionado por el usuario */
    @Column(name = "mensaje_verificacion", nullable = false)
    private String mensajeVerificacion;

    /** Fecha y hora de la solicitud */
    @Column(name = "fecha_solicitud")
    private LocalDateTime fechaSolicitud;

    /** Estado de la solicitud (pendiente, aprobada, rechazada) */
    @Column(name = "estado")
    private String estado;

    /** Getters y setters */
    public Long getIdSolicitud() { return idSolicitud; }
    public void setIdSolicitud(Long idSolicitud) { this.idSolicitud = idSolicitud; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public String getMensajeVerificacion() { return mensajeVerificacion; }
    public void setMensajeVerificacion(String mensajeVerificacion) { this.mensajeVerificacion = mensajeVerificacion; }
    public LocalDateTime getFechaSolicitud() { return fechaSolicitud; }
    public void setFechaSolicitud(LocalDateTime fechaSolicitud) { this.fechaSolicitud = fechaSolicitud; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
