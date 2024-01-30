package com.example.almohadascomodasademsbonitas.pedidos;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class Pedido implements Serializable {
    private String imagen;
    private int cantidad;
    private int idPedido;  // Nuevo campo para el ID del pedido
    private int id_partner;
    private int id_comercial;
    private String descripcion;
    private double descuento;
    private double precio_un;
    private LocalDate fecha;  // Cambia de LocalDate a Date
    private double precio_total;

private int precioarticulo;
    private ArrayList<Pedido> productos;

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
        this.imagen = "jordi";
        this.cantidad = 0;
        this.idPedido = 0;
        this.precioarticulo = 0;
        this.id_partner = 0;
        this.id_comercial = 0;
        this.descripcion = "jordi";
        this.descuento = 0;
        this.precio_un = 0;
        this.fecha = LocalDate.of(0,0,0);
        this.precio_total = 0;

    }

    public int getId_partner() {
        return id_partner;
    }
    public ArrayList<Pedido> getProductos() {
        return productos;
    }
    public void setId_partner(int id_partner) {
        this.id_partner = id_partner;
    }

    public int getId_comercial() {
        return id_comercial;
    }

    public void setId_comercial(int id_comercial) {
        this.id_comercial = id_comercial;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public double getPrecio_un() {
        return precio_un;
    }

    public void setPrecio_un(double precio_un) {
        this.precio_un = precio_un;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public double getPrecio_total() {
        return precio_total;
    }

    public void setPrecio_total(double precio_total) {
        this.precio_total = precio_total;
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

    public void setProductos(ArrayList<Pedido> productos) {
        this.productos = productos;
    }


}


