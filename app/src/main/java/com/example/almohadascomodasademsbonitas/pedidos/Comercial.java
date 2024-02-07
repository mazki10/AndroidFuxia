package com.example.almohadascomodasademsbonitas.pedidos;


import java.io.Serializable;
import java.time.LocalDate;

public class Comercial{

  private String nombre;
  private String appellido1;
  private String apellido2;
  private String dni;
  private String direccion;
  private String email;
  private int zona1;
  private int zona2;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAppellido1() {
        return appellido1;
    }

    public void setAppellido1(String appellido1) {
        this.appellido1 = appellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getZona1() {
        return zona1;
    }

    public void setZona1(int zona1) {
        this.zona1 = zona1;
    }

    public int getZona2() {
        return zona2;
    }

    public void setZona2(int zona2) {
        this.zona2 = zona2;
    }


    public Comercial(String nombre, String appellido1, String apellido2, String dni, String direccion, String email, int zona1, int zona2) {
        this.nombre = nombre;
        this.appellido1 = appellido1;
        this.apellido2 = apellido2;
        this.dni = dni;
        this.direccion = direccion;
        this.email = email;
        this.zona1 = zona1;
        this.zona2 = zona2;

    }

    public Comercial() {
        this.nombre="";
        this.appellido1="";
        this.apellido2="";
        this.dni="";
        this.direccion="";
        this.email="";
        this.zona1=0;
        this.zona2=0;
    }
}
