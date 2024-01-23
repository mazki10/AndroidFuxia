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

    public Cab_Pedido(int id_pedido, int id_partner, String descripcion, LocalDate fecha_pedido, LocalDate fecha_envio, boolean estado_pedido) {
        this.id_pedido = id_pedido;
        this.id_partner = id_partner;
        this.descripcion = descripcion;
        this.fecha_pedido = fecha_pedido;
        this.fecha_envio = fecha_envio;
        this.estado_pedido = estado_pedido;
    }
}
