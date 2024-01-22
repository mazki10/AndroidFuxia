package com.example.almohadascomodasademsbonitas.BBDD;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBconexion extends SQLiteOpenHelper {
    String sqlCreatePED = "CREATE TABLE PEDIDOS (ID_PEDIDO INTEGER, ID_PARTNER INTEGER, ID_COMERCIAL INTEGER,  DESCRIPCION TEXT, CANTIDAD INTEGER, DESCUENTO DOUBLE, PRECIO_UN DOUBLE, FECHA DATE, PRECIO_TOTAL DOUBLE)";
    String sqlCreatePAR = "CREATE TABLE PARTNERS (ID_PARTNERS INTEGER, NOMBRE TEXT, CIF TEXT, DIRECCION TEXT, TELEFONO INTEGER, COMERCIAL INTEGER, EMAIL TEXT, ZONA INTEGER)";
    String sqlCreateAGE = "CREATE TABLE AGENDA (ACTIVIDAD INTEGER, TITULO TEXT, DESCRIPCION TEXT, FECHA DATE, HORA DATE)";
    String sqlCreateCOM = "CREATE TABLE COMERCIALES (NOMBRE TEXT, APELLIDO1 TEXT, APELLIDO2 TEXT, DNI TEXT, DIRECCION TEXT, EMAIL TEXT, ZONA1 INTEGER, ZONA2 INTEGER)";

    public DBconexion(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreatePED);
        db.execSQL(sqlCreatePAR);
        db.execSQL(sqlCreateAGE);
        db.execSQL(sqlCreateCOM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
