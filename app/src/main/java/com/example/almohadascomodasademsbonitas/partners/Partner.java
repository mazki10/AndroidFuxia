package com.example.almohadascomodasademsbonitas.partners;

public class Partner {
    private Integer id;
    private String nombre;
    private String cif;
    private String direccion;
    private  Integer telefono;
    private String comercial;
    private String email;
    private Integer zona;
    private String fecha;
    public Partner(){
        //Constructor vacío
        }
    public Partner(Integer id, String nombre, String cif, String direccion, Integer telefono, String comercial, String email, Integer zona, String fecha) {
        this.id = id;
        this.nombre = nombre;
        this.cif = cif;
        this.direccion = direccion;
        this.telefono = telefono;
        this.comercial = comercial;
        this.email = email;
        this.zona = zona;
        this.fecha = fecha;
    }
    public void set_id(String id){ this.id = Integer.parseInt(id);}
    public Integer get_id(){ return this.id;}
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
    public void set_comercial(String comercial){this.comercial = comercial;}
    public String get_comercial(){
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

