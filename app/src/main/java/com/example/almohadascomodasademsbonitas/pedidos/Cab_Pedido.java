package com.example.almohadascomodasademsbonitas.pedidos;

import java.io.Serializable;
import java.time.LocalDate;

public class Cab_Pedido{
private int id_pedido;
private int id_partner;
private String descripcion;
private LocalDate fecha_pedido;
private LocalDate fecha_envio;
private boolean estado_pedido;

    public  Cab_Pedido(){
        this.id_pedido = 0;
        this.id_partner = 0;
        this.descripcion = "";
        this.fecha_pedido = LocalDate.of(0,0,0);
        this.fecha_envio = LocalDate.of(0,0,0);
        this.estado_pedido = false;
    }

    public int getId_pedido() {
        return id_pedido;
    }

    public void setId_pedido(int id_pedido) {
        this.id_pedido = id_pedido;
    }

    public int getId_partner() {
        return id_partner;
    }

    public void setId_partner(int id_partner) {
        this.id_partner = id_partner;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFecha_pedido() {
        return fecha_pedido;
    }

    public void setFecha_pedido(LocalDate fecha_pedido) {
        this.fecha_pedido = fecha_pedido;
    }

    public LocalDate getFecha_envio() {
        return fecha_envio;
    }

    public void setFecha_envio(LocalDate fecha_envio) {
        this.fecha_envio = fecha_envio;
    }

    public boolean isEstado_pedido() {
        return estado_pedido;
    }

    public void setEstado_pedido(boolean estado_pedido) {
        this.estado_pedido = estado_pedido;
    }

    public Cab_Pedido(int id_pedido, int id_partner, String descripcion, LocalDate fecha_pedido, LocalDate fecha_envio, boolean estado_pedido) {
        this.id_pedido = id_pedido;
        this.id_partner = id_partner;
        this.descripcion = descripcion;
        this.fecha_pedido = fecha_pedido;
        this.fecha_envio = fecha_envio;
        this.estado_pedido = estado_pedido;
    }
}
