package com.example.almohadascomodasademsbonitas.pedidos;

import java.time.LocalDate;

public class Articulo {
    private int id_articulo;
    private int id_proveedor;
    private String descripcion;
    private Double precio_venta;
    private Double precio_coste;
    private int  existencias;
    private int stock_max;
    private int stock_min;
    private String fec_ult_ent;
    private String fec_ult_sal;

    public Articulo() {
        this.id_articulo = 0;
        this.id_proveedor = 0;
        this.descripcion = "";
        this.precio_venta = 0.0;
        this.precio_coste = 0.0;
        this.existencias = 0;
        this.stock_max = 0;
        this.stock_min = 0;
        this.fec_ult_ent ="";
        this.fec_ult_sal = "";
    }

    public Articulo(int id_articulo, int id_proveedor, String descripcion, Double precio_venta, Double precio_coste, int existencias, int stock_max, int stock_min, String fec_ult_ent, String fec_ult_sal) {
        this.id_articulo = id_articulo;
        this.id_proveedor = id_proveedor;
        this.descripcion = descripcion;
        this.precio_venta = precio_venta;
        this.precio_coste = precio_coste;
        this.existencias = existencias;
        this.stock_max = stock_max;
        this.stock_min = stock_min;
        this.fec_ult_ent = fec_ult_ent;
        this.fec_ult_sal = fec_ult_sal;
    }

    public int getId_articulo() {
        return id_articulo;
    }

    public void setId_articulo(int id_articulo) {
        this.id_articulo = id_articulo;
    }

    public int getId_proveedor() {
        return id_proveedor;
    }

    public void setId_proveedor(int id_proveedor) {
        this.id_proveedor = id_proveedor;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getPrecio_venta() {
        return precio_venta;
    }

    public void setPrecio_venta(Double precio_venta) {
        this.precio_venta = precio_venta;
    }

    public Double getPrecio_coste() {
        return precio_coste;
    }

    public void setPrecio_coste(Double precio_coste) {
        this.precio_coste = precio_coste;
    }

    public int getExistencias() {
        return existencias;
    }

    public void setExistencias(int existencias) {
        this.existencias = existencias;
    }

    public int getStock_max() {
        return stock_max;
    }

    public void setStock_max(int stock_max) {
        this.stock_max = stock_max;
    }

    public int getStock_min() {
        return stock_min;
    }

    public void setStock_min(int stock_min) {
        this.stock_min = stock_min;
    }

    public String getFec_ult_ent() {
        return fec_ult_ent;
    }

    public void setFec_ult_ent(String fec_ult_ent) {
        this.fec_ult_ent = fec_ult_ent;
    }

    public String getFec_ult_sal() {
        return fec_ult_sal;
    }

    public void setFec_ult_sal(String fec_ult_sal) {
        this.fec_ult_sal = fec_ult_sal;
    }
}