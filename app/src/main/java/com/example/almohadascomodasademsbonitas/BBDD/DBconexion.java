package com.example.almohadascomodasademsbonitas.BBDD;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.almohadascomodasademsbonitas.R;
import com.example.almohadascomodasademsbonitas.agenda.Actividad;
import com.example.almohadascomodasademsbonitas.pedidos.Lin_Pedido;
import com.example.almohadascomodasademsbonitas.pedidos.Cab_Pedido;

import java.time.LocalDate;
import java.util.ArrayList;

public class DBconexion extends SQLiteOpenHelper {

    String sqlCreateLINPED = "CREATE TABLE LIN_PEDIDOS (ID_PEDIDO INTEGER, ID_LINEA INTEGER, CANTIDAD INTEGER, DESCUENTO DOUBLE, PRECIO_UN DOUBLE, PRECIO_TOTAL DOUBLE)";
    String sqlCreateCAPPED = "CREATE TABLE CAB_PEDIDOS (ID_PEDIDO INTEGER, ID_PARTNER INTEGER, DESCRIPCION TEXT, FECHA_PEDIDO DATE, FECHA_ENVIO DATE,ENTREGADO BOOLEAN)";
    String sqlCreatePAR = "CREATE TABLE PARTNERS (ID_PARTNERS INTEGER, NOMBRE TEXT, CIF TEXT, DIRECCION TEXT, TELEFONO INTEGER, COMERCIAL INTEGER, EMAIL TEXT, ZONA INTEGER)";
    String sqlCreateAGE = "CREATE TABLE AGENDA (ACTIVIDAD INTEGER, TITULO TEXT, DESCRIPCION TEXT, FECHA DATE, HORA DATE)";
    String sqlCreateCOM = "CREATE TABLE COMERCIALES (NOMBRE TEXT, APELLIDO1 TEXT, APELLIDO2 TEXT, DNI TEXT, DIRECCION TEXT, EMAIL TEXT, ZONA1 INTEGER, ZONA2 INTEGER)";

    public DBconexion(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreateLINPED);
        db.execSQL(sqlCreateCAPPED);
        db.execSQL(sqlCreatePAR);
        db.execSQL(sqlCreateAGE);
        db.execSQL(sqlCreateCOM);


        ArrayList<Cab_Pedido> cab_pedidos =  new ArrayList<>();
        ArrayList<Lin_Pedido> lin_pedidos =  new ArrayList<>();
       // ArrayList<Partner> partners = new ArrayList<>();
        ArrayList<Actividad> agendas = new ArrayList<>();


        cab_pedidos.add(new Cab_Pedido(1,1,"jordi", LocalDate.of(2024,2,1),LocalDate.of(2024,2,3),true));
        cab_pedidos.add(new Cab_Pedido(2,2,"bale", LocalDate.of(2023,12,1), LocalDate.of(2023,12,4),true));
        cab_pedidos.add(new Cab_Pedido(3,1,"bob", LocalDate.of(2023,11,3), LocalDate.of(2023,11,5),true));
        cab_pedidos.add(new Cab_Pedido(4,3,"cartas", LocalDate.of(2024,1,6), LocalDate.of(2024,1,11),true));
        cab_pedidos.add(new Cab_Pedido(5,1,"hello", LocalDate.of(2023,9,6), LocalDate.of(2023,9,10),true));
        cab_pedidos.add(new Cab_Pedido(6,2,"patriota", LocalDate.of(2024,1,18), LocalDate.of(2024,1,23),false));
        cab_pedidos.add(new Cab_Pedido(7,4,"pistola", LocalDate.of(2024,1,8), LocalDate.of(2024,1,14),true));
        cab_pedidos.add(new Cab_Pedido(8,1,"verde", LocalDate.of(2022,3,1), LocalDate.of(2022,3,4),true));


        lin_pedidos.add(new Lin_Pedido(1,1,1,0,30,30));
        lin_pedidos.add(new Lin_Pedido(2,2,2,0,30,60));
        lin_pedidos.add(new Lin_Pedido(3,3,1,0,30,30));
        lin_pedidos.add(new Lin_Pedido(4,4,2,0,30,60));
        lin_pedidos.add(new Lin_Pedido(5,5,9,0,30,270));
        lin_pedidos.add(new Lin_Pedido(6,6,5,0,30,150));
        lin_pedidos.add(new Lin_Pedido(7,7,4,0,30,120));
        lin_pedidos.add(new Lin_Pedido(8,8,3,0,30,90));

        for(int i=0; i<cab_pedidos.size();i++){
                String insertQuery = "INSERT INTO CAB_PEDIDOS (ID_PEDIDO, ID_PARTNER, DESCRIPCION, FECHA_PEDIDO, FECHA_ENVIO, ENTREGADO) " +
                        "VALUES (" + cab_pedidos.get(i).getId_pedido() + ", " + cab_pedidos.get(i).getId_partner() + ", '" + cab_pedidos.get(i).getDescripcion() + "', '" +
                        cab_pedidos.get(i).getFecha_pedido() + "', '" + cab_pedidos.get(i).getFecha_envio() + "', " + cab_pedidos.get(i).isEstado_pedido()  + ")";
                db.execSQL(insertQuery);
        }

        for(int i=0; i<lin_pedidos.size();i++){
            String insertQuery = "INSERT INTO CAB_PEDIDOS (ID_PEDIDO, ID_PARTNER, DESCRIPCION, FECHA_PEDIDO, FECHA_ENVIO, ENTREGADO) " +
                    "VALUES (" + lin_pedidos.get(i).getId_pedido() + ", " + lin_pedidos.get(i).getId_linea() + ", '" + lin_pedidos.get(i).getCantidad() + "', '" +
                    lin_pedidos.get(i).getDescuento() + "', '" + lin_pedidos.get(i).getPrecio_un() + "', " + lin_pedidos.get(i).getPrecio_total() + ")";
            db.execSQL(insertQuery);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
