package com.example.almohadascomodasademsbonitas.BBDD;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.almohadascomodasademsbonitas.R;
import com.example.almohadascomodasademsbonitas.agenda.Actividad;
import com.example.almohadascomodasademsbonitas.pedidos.Lin_Pedido;
import com.example.almohadascomodasademsbonitas.pedidos.Cab_Pedido;
import com.example.almohadascomodasademsbonitas.pedidos.Comercial;
import com.example.almohadascomodasademsbonitas.LogIn.InicioSesion;

import java.time.LocalDate;
import java.util.ArrayList;

public class DBconexion extends SQLiteOpenHelper {
    private  Context  ourContext;
    public static final String NOMBRE_BD = "ACAB";
    public static final int VERSION_BD = 1;
    String sqlCreateLINPED = "CREATE TABLE LIN_PEDIDOS (ID_PEDIDO INTEGER, ID_LINEA INTEGER, CANTIDAD INTEGER, DESCUENTO DOUBLE, PRECIO_UN DOUBLE, PRECIO_TOTAL DOUBLE, FOREIGN KEY (ID_PEDIDO) REFERENCES CAB_PEDIDOS(ID_PEDIDO))";
    String sqlCreateCAPPED = "CREATE TABLE CAB_PEDIDOS (ID_PEDIDO INTEGER,ID_COMERCIAL TEXT, ID_PARTNER INTEGER, DESCRIPCION TEXT, FECHA_PEDIDO DATE, FECHA_ENVIO DATE,ENTREGADO BOOLEAN, FOREIGN KEY (ID_COMERCIAL) REFERENCES COMERCIALES(DNI),FOREIGN KEY (ID_PARTNER) REFERENCES PARTNERS(ID_PARTNER))";
    String sqlCreatePAR = "CREATE TABLE PARTNERS (ID_PARTNER INTEGER, NOMBRE TEXT, CIF TEXT, DIRECCION TEXT, TELEFONO INTEGER, COMERCIAL INTEGER, EMAIL TEXT, ZONA INTEGER, FECHA DATE)";
    String sqlCreateAGE = "CREATE TABLE AGENDA (ACTIVIDAD INTEGER, TITULO TEXT, DESCRIPCION TEXT, FECHA DATE, HORA DATE)";
    String sqlCreateCOM = "CREATE TABLE COMERCIALES (NOMBRE TEXT, APELLIDO1 TEXT, APELLIDO2 TEXT, DNI TEXT, DIRECCION TEXT, EMAIL TEXT, ZONA1 INTEGER, ZONA2 INTEGER)";
    String sqlCreateArt = "CREATE TABLE ARTICULOS (ID_ARTICULO INTEGER, ID_PROVEEDOR INTEGER, DESCRIPCION TEXT, PRECIO_VENTA DOUBLE, PRECIO_COSTE DOUBLE, EXISTENCIAS INTEGER, STOCK_MAX INTEGER, STOCK_MIN INTEGER, FEC_ULT_ENT DATE, FEC_ULT_SAL DATE )";
    String sqlCreateLOG = "CREATE TABLE LOGIN (USER TEXT, PASSWORD TEXT,  SESION BOOLEAN, ID_COMERCIAL TEXT,FOREIGN KEY(ID_COMERCIAL) REFERENCES COMERCIALES(DNI))";


    public DBconexion(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public DBconexion(Context context, String dbPath) {
        super(context, dbPath, null, VERSION_BD);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreateArt);
        db.execSQL(sqlCreateLINPED);
        db.execSQL(sqlCreateCAPPED);
        db.execSQL(sqlCreatePAR);
        db.execSQL(sqlCreateAGE);
        db.execSQL(sqlCreateCOM);
        db.execSQL(sqlCreateLOG);

        // Agrega logs para verificar la creación de tablas
        Log.d("DBconexion", "Tablas creadas correctamente");

        // Inserta datos después de verificar la creación de tablas
        insertarDatosIniciales(db);
    }


    private void insertarDatosIniciales(SQLiteDatabase db) {


        ArrayList<Cab_Pedido> cab_pedidos =  new ArrayList<>();
        ArrayList<Lin_Pedido> lin_pedidos =  new ArrayList<>();
       // ArrayList<Partner> partners = new ArrayList<>();
        ArrayList<Actividad> agendas = new ArrayList<>();
        ArrayList<Comercial> comerciales = new ArrayList<>();
        ArrayList<InicioSesion> LogIn = new ArrayList<>();


        // Crear instancias de la clase Comercial con información real
        comerciales.add(new Comercial("Juan", "Pérez", "Gómez", "12345678A", "Calle 123, Ciudad1", "juan@example.com", 25, 2));
        comerciales.add(new Comercial("María", "López", "Martínez", "87654321B", "Avenida 456, Ciudad2", "maria@example.com", 30, 3));
        comerciales.add(new Comercial("Carlos", "García", "Fernández", "56789012C", "Plaza 789, Ciudad3", "carlos@example.com", 28, 4));
        comerciales.add(new Comercial("Ana", "Martínez", "Sánchez", "34567890D", "Calle 012, Ciudad4", "ana@example.com", 35, 5));
        comerciales.add(new Comercial("Pedro", "Rodríguez", "Ramírez", "23456789E", "Avenida 901, Ciudad5", "pedro@example.com", 32, 6));

        LogIn.add(new InicioSesion("a", "a", false, "12345678A"));


      /*  cab_pedidos.add(new Cab_Pedido(1,1,"jordi", LocalDate.of(2024,2,1),LocalDate.of(2024,2,3),true));
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
            String insertQuery = "INSERT INTO LIN_PEDIDOS (ID_PEDIDO, ID_LINEA, CANTIDAD, DESCUENTO, PRECIO_UN, PRECIO_TOTAL) " +
                    "VALUES (" + lin_pedidos.get(i).getId_pedido() + ", " + lin_pedidos.get(i).getId_linea() + ", '" + lin_pedidos.get(i).getCantidad() + "', '" +
                    lin_pedidos.get(i).getDescuento() + "', '" + lin_pedidos.get(i).getPrecio_un() + "', " + lin_pedidos.get(i).getPrecio_total() + ")";
            db.execSQL(insertQuery);
        }*/

        // Insertar comerciales en la tabla correspondiente
        for (int i = 0; i < comerciales.size(); i++) {
            String insertQuery = "INSERT INTO COMERCIALES (NOMBRE, APELLIDO1, APELLIDO2, DNI, DIRECCION, EMAIL, ZONA1, ZONA2) " +
                    "VALUES ('" + comerciales.get(i).getNombre() + "', '" + comerciales.get(i).getAppellido1() + "', '" + comerciales.get(i).getApellido2() + "', '" +
                    comerciales.get(i).getDni() + "', '" + comerciales.get(i).getDireccion() + "', '" + comerciales.get(i).getEmail() + "', " +
                    comerciales.get(i).getZona1() + ", " + comerciales.get(i).getZona2() + ")";
            db.execSQL(insertQuery);
        }

// Insertar inicio de sesion en la tabla correspondiente
        for (int i = 0; i < LogIn.size(); i++) {
            String insertQuery = "INSERT INTO LOGIN (USER, PASSWORD, SESION, ID_COMERCIAL) " +
                    "VALUES ('" + LogIn.get(i).getUsuario() + "', '" + LogIn.get(i).getPassWord() + "', " +
                    LogIn.get(i).getActivo() + ", '" + LogIn.get(i).getComercial() + "')";
            db.execSQL(insertQuery);
        }

        Log.d("DBconexion", "Datos iniciales insertados correctamente");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
