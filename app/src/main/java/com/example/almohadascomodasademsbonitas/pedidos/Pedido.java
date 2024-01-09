package com.example.almohadascomodasademsbonitas.pedidos;
import java.io.Serializable;

public class Pedido implements Serializable {
    private String imagen;
    private int cantidad;
    private int idPedido;  // Nuevo campo para el ID del pedido

    public Pedido(String imagen, int cantidad, int idPedido) {
        this.imagen = imagen;
        this.cantidad = cantidad;
        this.idPedido = idPedido;
    }

    public Pedido() {
        this.imagen = "";
        this.cantidad = 0;
        this.idPedido = 0;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getImagen() {
        return imagen;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }
}


