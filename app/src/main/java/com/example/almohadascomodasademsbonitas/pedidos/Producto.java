package com.example.almohadascomodasademsbonitas.pedidos;

public class Producto {
    private String descripcion;
    private int cantidad;
    private double descuento;
    private double precio_un;

    public Producto(String descripcion, int cantidad, double descuento, double precio_un) {
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.descuento = descuento;
        this.precio_un = precio_un;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getDescuento() {
        return descuento;
    }

    public double getPrecio_un() {
        return precio_un;
    }
}
