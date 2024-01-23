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

        public Lin_Pedido(){
        this.id_pedido = 0;
        this.id_linea = 0;
        this.cantidad = 0;
        this.descuento = 0;
        this.precio_un = 0;
        this.precio_total = 0;
    }
}
