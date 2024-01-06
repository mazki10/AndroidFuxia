package com.example.almohadascomodasademsbonitas.agenda;

import java.util.Objects;

public class Actividad {
    private String titulo;
    private String descripcion;
    private String fecha;
    private String hora;

    public Actividad() {
        // Constructor vacío
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    @Override
    public String toString() {
        return fecha + " " + hora + "   Título: " + titulo + "\nDescripción: " + descripcion;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Actividad otraActividad = (Actividad) obj;
        return Objects.equals(titulo, otraActividad.titulo) &&
                Objects.equals(descripcion, otraActividad.descripcion) &&
                Objects.equals(fecha, otraActividad.fecha) &&
                Objects.equals(hora, otraActividad.hora);
    }
}
