package com.example.almohadascomodasademsbonitas.pedidos;

    public class Lin_Pedido {
    private int id_pedido;
    private int id_linea;
    private int cantidad;
    private double descuento;
    private double precio_un;
    private double precio_total;

        public Lin_Pedido(int id_pedido, int id_linea, int cantidad, double descuento, double precio_un, double precio_total) {
            this.id_pedido = id_pedido;
            this.id_linea = id_linea;
            this.cantidad = cantidad;
            this.descuento = descuento;
            this.precio_un = precio_un;
            this.precio_total = precio_total;
        }

        public int getId_pedido() {
            return id_pedido;
        }

        public void setId_pedido(int id_pedido) {
            this.id_pedido = id_pedido;
        }

        public int getId_linea() {
            return id_linea;
        }

        public void setId_linea(int id_linea) {
            this.id_linea = id_linea;
        }

        public int getCantidad() {
            return cantidad;
        }

        public void setCantidad(int cantidad) {
            this.cantidad = cantidad;
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

        public double getPrecio_total() {
            return precio_total;
        }

        public void setPrecio_total(double precio_total) {
            this.precio_total = precio_total;
        }

        public Lin_Pedido(){
        this.id_pedido = 0;
        this.id_linea = 0;
        this.cantidad = 0;
        this.descuento = 0;
        this.precio_un = 0;
        this.precio_total = 0;
    }
}
