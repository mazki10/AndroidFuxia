package com.example.almohadascomodasademsbonitas;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Crear las tablas al iniciar la MainActivity
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // Asegúrate de cerrar la base de datos después de su uso
        db.close();
    }

    public class DatabaseHelper extends SQLiteOpenHelper {

        // Nombre de la base de datos
        private static final String DATABASE_NAME = "ACAB.db";
        // Versión de la base de datos
        private static final int DATABASE_VERSION = 1;

        // Sentencias CREATE TABLE para tus tablas
        String sqlCreateLINPED = "CREATE TABLE LIN_PEDIDOS (ID_PEDIDO INTEGER, ID_LINEA INTEGER, CANTIDAD INTEGER, DESCUENTO DOUBLE, PRECIO_UN DOUBLE, PRECIO_TOTAL DOUBLE)";
        String sqlCreateCAPPED = "CREATE TABLE CAB_PEDIDOS (ID_PEDIDO INTEGER, ID_PARTNER INTEGER, DESCRIPCION TEXT, FECHA_PEDIDO DATE, FECHA_ENVIO DATE,ENTREGADO BOOLEAN)";
        String sqlCreatePAR = "CREATE TABLE PARTNERS (ID_PARTNERS INTEGER, NOMBRE TEXT, CIF TEXT, DIRECCION TEXT, TELEFONO INTEGER, COMERCIAL INTEGER, EMAIL TEXT, ZONA INTEGER)";
        String sqlCreateAGE = "CREATE TABLE AGENDA (ACTIVIDAD INTEGER, TITULO TEXT, DESCRIPCION TEXT, FECHA DATE, HORA DATE)";
        String sqlCreateCOM = "CREATE TABLE COMERCIALES (NOMBRE TEXT, APELLIDO1 TEXT, APELLIDO2 TEXT, DNI TEXT, DIRECCION TEXT, EMAIL TEXT, ZONA1 INTEGER, ZONA2 INTEGER)";

        // Constructor
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        // Este método se llama cuando se crea la base de datos por primera vez
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(sqlCreateLINPED);
            db.execSQL(sqlCreateCAPPED);
            db.execSQL(sqlCreatePAR);
            db.execSQL(sqlCreateAGE);
            db.execSQL(sqlCreateCOM);
            // Agrega aquí las sentencias CREATE TABLE para las demás tablas si las tienes
        }

        // Este método se llama cuando se actualiza la base de datos
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Aquí puedes realizar operaciones de actualización si es necesario
            // Puedes dejarlo vacío si no necesitas realizar acciones específicas al actualizar
        }
    }
}
