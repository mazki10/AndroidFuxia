package com.example.almohadascomodasademsbonitas.pedidos;

import java.util.HashMap; // Importa la clase HashMap si aún no lo has hecho

public class ProductosManager {
    // Supongamos que tienes una estructura de datos que almacena los precios de los productos por su nombre
    private HashMap<String, Integer> preciosProductos;

    // Constructor de la clase
    public ProductosManager() {
        preciosProductos = new HashMap<>();
        // Aquí puedes inicializar los precios de tus productos
        preciosProductos.put("NombreProducto1", 100); // Ejemplo de precio para un producto
        preciosProductos.put("NombreProducto2", 150); // Ejemplo de precio para otro producto
        // Agrega más productos según sea necesario
    }

    // Método para obtener el precio de un producto dado su nombre
    public int obtenerPrecioArticulo(String nombreProducto) {
        // Verifica si el nombre del producto existe en el mapa de precios
        if (preciosProductos.containsKey(nombreProducto)) {
            // Devuelve el precio correspondiente al nombre del producto
            return preciosProductos.get(nombreProducto);
        } else {
            // Devuelve un valor predeterminado o maneja la situación de que el producto no exista
            return 0; // O cualquier otro valor predeterminado que desees
        }
    }
}

