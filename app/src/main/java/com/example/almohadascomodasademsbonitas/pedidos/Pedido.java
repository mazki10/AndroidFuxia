package com.example.almohadascomodasademsbonitas.pedidos;
import java.io.Serializable;
import java.time.LocalDate;

public class Pedido implements Serializable {
    private String imagen;
    private int cantidad;
    private int idPedido;  // Nuevo campo para el ID del pedido
    private int id_partner;
    private int id_comercial;
    private String descripcion;
    private double descuento;
    private double precio_un;
    private LocalDate fecha;
    private double precio_total;
private int precioarticulo;
    public Pedido(String imagen, int cantidad, int idPedido, int precioarticulo, int id_partner, int id_comercial, String descripcion, double descuento,double precio_un, LocalDate fecha, double precio_total) {
        this.imagen = imagen;
        this.cantidad = cantidad;
        this.idPedido = idPedido;
        this.precioarticulo = precioarticulo;
        this.id_partner = id_partner;
        this.id_comercial = id_comercial;
        this.descripcion = descripcion;
        this.descuento = descuento;
        this.precio_un = precio_un;
        this.fecha = fecha;
        this.precio_total = precio_total;
    }

    public Pedido() {
        this.imagen = "";
        this.cantidad = 0;
        this.idPedido = 0;
        this.precioarticulo = 0;
        this.id_partner = 0;
        this.id_comercial = 0;
        this.descripcion = "";
        this.descuento = 0;
        this.precio_un = 0;
        this.fecha = LocalDate.of(0,0,0);
        this.precio_total = 0;
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
    public int getPrecioarticulo() {
        return precioarticulo;
    }
    public void setPrecioarticulo(int precioarticulo){this.precioarticulo=precioarticulo;}
    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }
}


