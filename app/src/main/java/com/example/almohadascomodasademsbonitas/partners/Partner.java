package com.example.almohadascomodasademsbonitas.partners;

public class Partner {
    private Integer id;
    private String nombre;
    private String cif;
    private String direccion;
    private  Integer telefono;
    private Integer comercial;
    private String email;
    private Integer zona;
    private String fecha;
    public Partner(){
        //Constructor vacío
        }
    public void set_id(Integer id){ this.id = id;}
    public Integer get_id(){
        return this.id;
    }
    public void set_nombre(String nombre){ this.nombre = nombre;}
    public String get_nombre(){
            return this.nombre;
    }
    public void set_cif(String cif){this.cif = cif;}
    public String get_cif(){
        return cif;
    }
    public void set_direccion(String direccion){this.direccion = direccion;}
    public String get_direccion(){
        return this.direccion;
    }
    public void set_telefono(Integer telefono){this.telefono = telefono;}
    public Integer get_telefono(){
        return telefono;
    }
    public void set_comercial(Integer comercial){this.comercial = comercial;}
    public Integer get_comercial(){
        return comercial;
    }
    public void set_email(String email){this.email = email;}
    public String get_email(){
        return email;
    }
    public void set_zona(Integer zona){this.zona = zona;}
    public Integer get_zona(){
        return zona;
    }
    public void set_fecha(String fecha){this.fecha = fecha;}
    public String get_fecha(){
        return fecha;
    }

}

