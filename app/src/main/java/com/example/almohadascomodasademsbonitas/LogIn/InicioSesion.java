package com.example.almohadascomodasademsbonitas.LogIn;

public class InicioSesion {
    private String usuario;
    private String passWord;
    private Boolean activo;
    private String comercial;

    // Constructor
    public InicioSesion(String usuario, String passWord, Boolean activo, String comercial) {
        this.usuario = usuario;
        this.passWord = passWord;
        this.activo = activo;
        this.comercial = comercial;
    }

    // Getters
    public String getUsuario() {
        return usuario;
    }

    public String getPassWord() {
        return passWord;
    }

    public Boolean getActivo() {
        return activo;
    }

    public String getComercial() {
        return comercial;
    }

    // Setters
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public void setComercial(String comercial) {
        this.comercial = comercial;
    }
}


