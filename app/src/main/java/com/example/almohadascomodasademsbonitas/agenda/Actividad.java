package com.example.almohadascomodasademsbonitas.agenda;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;


public class Actividad implements Serializable {
    private int id;
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
    public String getFechaActividad() {
        try {
            // Formato para analizar la fecha
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

            // Analizar la fecha almacenada en formato "dd/MM/yyyy"
            Date date = dateFormat.parse(fecha);

            // Crear un objeto Calendar y establecer la fecha analizada
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            // Obtener el día de la semana y el día del mes
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

            // Array con nombres de los días de la semana
            String[] nombresDiasSemana = new String[]{"Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado"};

            // Obtener el nombre del día de la semana según el número
            String nombreDiaSemana = nombresDiasSemana[dayOfWeek - 1]; // -1 porque los días de la semana comienzan en 1

            // Combinar el nombre del día de la semana con el día del mes
            return nombreDiaSemana + " " + dayOfMonth;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return fecha; // En caso de error, devuelve la fecha sin modificar
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

