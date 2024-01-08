package com.example.almohadascomodasademsbonitas.pedidos;
import java.io.Serializable;

public class Pedido implements Serializable {
    private String imagen;
    private int cantidad;

    public Pedido(String imagen, int cantidad) {
        this.imagen = imagen;
        this.cantidad = cantidad;
    }

    public Pedido() {
        this.imagen = "";
        this.cantidad = 0;
    }

    /* public void setImagen(int numero) {

        if (numero==2131165416){
            imagen = "jordi";
        } else if (numero==2131165412) {
            imagen = "bale";
        }else if (numero==2131165413) {
            imagen = "bob";
        } else if (numero==2131165414) {
            imagen = "cartas";
        } else if (numero==2131165415) {
            imagen = "hello";
        } else if (numero==2131165417) {
            imagen = "patriota";
        } else if (numero==2131165418) {
            imagen = "pistola";
        } else if (numero==2131165419) {
            imagen = "verde";
        }

    }*/


    public String getImagen() {
        return imagen;
    }

    public int getCantidad() {
        return cantidad;
    }
}

