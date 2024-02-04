package com.example.almohadascomodasademsbonitas.pedidos;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.almohadascomodasademsbonitas.BBDD.DBconexion;
import com.example.almohadascomodasademsbonitas.partners.Partner;
import com.example.almohadascomodasademsbonitas.pedidos.Lin_Pedido;
import com.example.almohadascomodasademsbonitas.pedidos.Cab_Pedido;
import com.example.almohadascomodasademsbonitas.agenda.Actividad;
import com.example.almohadascomodasademsbonitas.pedidos.actividad_pedido;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

import com.example.almohadascomodasademsbonitas.CircularLayout;
import com.example.almohadascomodasademsbonitas.R;

public class menu_Pedido extends AppCompatActivity {
    ArrayList<Cab_Pedido> cab_pedidos =  new ArrayList<>();
    ArrayList<Lin_Pedido> lin_pedidos =  new ArrayList<>();
    ArrayList<Partner> partners = new ArrayList<>();
    ArrayList<Comercial> comerciales = new ArrayList<>();
    ArrayList<Articulo> articulos = new ArrayList<>();
    Button botonAlta,botonBaja,botonModificacion,botonSalir;
    boolean permitirAcceso = true;
    private DBconexion dbHelper;
    private SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pedidos);

        dbHelper = new DBconexion(this, "ACAB.db", null, 1);
        db = dbHelper.getWritableDatabase();
        botonAlta = findViewById(R.id.buttonAlta);
        botonBaja = findViewById(R.id.buttonBaja);
        botonModificacion = findViewById(R.id.buttonModificar);
        botonSalir = findViewById(R.id.buttonSalir);

        botonSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        botonAlta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificarArchivosXML();
                if (permitirAcceso) {
                    Intent intent = new Intent(menu_Pedido.this, actividad_pedido.class);
                    menu_Pedido.this.startActivity(intent);
                }
            }
        });

        botonBaja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menu_Pedido.this, baja_pedidos.class);
                menu_Pedido.this.startActivity(intent);
            }
        });

        botonModificacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificarArchivosXML();
                if (permitirAcceso) {
                    Intent intent = new Intent(menu_Pedido.this, modificar_pedido.class);
                    menu_Pedido.this.startActivity(intent);
                }
            }
        });





        comerciales.add(new Comercial("12345678A","Jose","Alfredo","Jose","Dirigeme esta","sisi@gmail.com",2,1,"fuxia","fuxia123"));


        articulos.add(new Articulo(1,101,"jordi",50.0,25.0,100,200,10,LocalDate.of(2024,1,10),LocalDate.of(2024,1,20)));
        articulos.add(new Articulo(2,102,"bale",50.0,25.0,100,200,10,LocalDate.of(2024,1,10),LocalDate.of(2024,1,20)));
        articulos.add(new Articulo(3,103,"bob",50.0,25.0,100,200,10,LocalDate.of(2024,1,10),LocalDate.of(2024,1,20)));
        articulos.add(new Articulo(4,42,"cartas",50.0,25.0,100,200,10,LocalDate.of(2024,1,10),LocalDate.of(2024,1,20)));
        articulos.add(new Articulo(5,1312,"hello",50.0,25.0,100,200,10,LocalDate.of(2024,1,10),LocalDate.of(2024,1,20)));
        articulos.add(new Articulo(6,101,"patriota",50.0,25.0,100,200,10,LocalDate.of(2024,1,10),LocalDate.of(2024,1,20)));
        articulos.add(new Articulo(7,42,"pistola",50.0,25.0,100,200,10,LocalDate.of(2024,1,10),LocalDate.of(2024,1,20)));
        articulos.add(new Articulo(8,102,"verde",50.0,25.0,100,200,10,LocalDate.of(2024,1,10),LocalDate.of(2024,1,20)));


       /* for(int i=0; i<articulos.size();i++){
            String insertQuery = "INSERT INTO CAB_PEDIDOS (ID_PEDIDO, ID_PARTNER, DESCRIPCION, FECHA_PEDIDO, FECHA_ENVIO, ENTREGADO) " +
                    "VALUES (" + cab_pedidos.get(i).getId_pedido() + ", " + cab_pedidos.get(i).getId_partner() + ", '" + cab_pedidos.get(i).getDescripcion() + "', " +
                    cab_pedidos.get(i).getFecha_pedido() + ", " + cab_pedidos.get(i).getFecha_envio() + ", " + cab_pedidos.get(i).isEstado_pedido()  + ")";
            db.execSQL(insertQuery);
        }*/

      /*  for(int i=0; i<cab_pedidos.size();i++){
            String insertQuery = "INSERT INTO CAB_PEDIDOS (ID_PEDIDO, ID_PARTNER, DESCRIPCION, FECHA_PEDIDO, FECHA_ENVIO, ENTREGADO) " +
                    "VALUES (" + cab_pedidos.get(i).getId_pedido() + ", " + cab_pedidos.get(i).getId_partner() + ", '" + cab_pedidos.get(i).getDescripcion() + "', " +
                    cab_pedidos.get(i).getFecha_pedido() + ", " + cab_pedidos.get(i).getFecha_envio() + ", " + cab_pedidos.get(i).isEstado_pedido()  + ")";
            db.execSQL(insertQuery);
        }*/

        for(int i=0; i<lin_pedidos.size();i++){
            String insertQuery = "INSERT INTO ARTICULOS (ID_ARTICULO INTEGER, ID_PROVEEDOR INTEGER, DESCRIPCION TEXT, PRECIO_VENTA DOUBLE, PRECIO_COSTE DOUBLE, EXISTENCIAS INTEGER, STOCK_MAX INTEGER, STOCK_MIN INTEGER, FEC_ULT_ENT DATE, FEC_ULT_SAL DATE )" +
                    "VALUES (" + articulos.get(i).getId_articulo() + ", " + articulos.get(i).getId_proveedor() + ", '" + articulos.get(i).getDescripcion() + "', " +
                    articulos.get(i).getPrecio_venta() + ", " + articulos.get(i).getPrecio_coste() + ", " + articulos.get(i).getExistencias()+ ","+articulos.get(i).getStock_max()+","+articulos.get(i).getStock_min()+","+articulos.get(i).getFec_ult_ent()+","+articulos.get(i).getFec_ult_sal()+")";
            db.execSQL(insertQuery);
        }

    }

    /*    private void verificarArchivosXML() {
            File comercialesFile = new File(getFilesDir(), "comerciales.xml");
            File partnersFile = new File(getFilesDir(), "partners.xml");

            // Verifica la existencia de los archivos
            if (!comercialesFile.exists() || !partnersFile.exists()) {
                mostrarAlertDialog("Faltan archivos", "Los archivos comerciales.xml y/o partners.xml no están presentes.");
                permitirAcceso = false;
                return;
            }else {
                permitirAcceso = true;
            }

            // Verifica si los archivos están vacíos
            if (comercialesFile.length() == 0 || partnersFile.length() == 0) {
                mostrarAlertDialog("Archivos vacíos", "Los archivos comerciales.xml y/o partners.xml están vacíos.");
                permitirAcceso = false;
            }else {
                permitirAcceso = true;
            }
        }*/

    private void verificarArchivosXML() {
        permitirAcceso = true; // Restablecer el valor por defecto

        File comercialesFile = new File(getFilesDir(), "comerciales.xml");
        File partnersFile = new File(getFilesDir(), "partners.xml");

        // Verifica la existencia de los archivos
        if (!comercialesFile.exists()) {
            try {
                comercialesFile.createNewFile();
                // Escribe el contenido inicial en el archivo
                escribirContenidoInicialComerciales(comercialesFile);
            } catch (IOException e) {
                e.printStackTrace();
                mostrarAlertDialog("Error", "Error al crear el archivo comerciales.xml.");
                permitirAcceso = false;
                return;
            }
        }

        if (!partnersFile.exists()) {
            try {
                partnersFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                mostrarAlertDialog("Error", "Error al crear el archivo partners.xml.");
                permitirAcceso = false;
                return;
            }
        }

        // Verifica si los archivos están vacíos
        try {
            FileInputStream comercialesStream = new FileInputStream(comercialesFile);
            FileInputStream partnersStream = new FileInputStream(partnersFile);

            if (comercialesStream.available() == 0 || partnersStream.available() == 0) {
                mostrarAlertDialog("Archivos vacíos", "Los archivos comerciales.xml y/o partners.xml están vacíos.");
                permitirAcceso = false;
            }

            comercialesStream.close();
            partnersStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlertDialog("Error", "Error al leer los archivos XML.");
            permitirAcceso = false;
        }
    }

    private void escribirContenidoInicialComerciales(File comercialesFile) {
        try {
            FileOutputStream outputStream = new FileOutputStream(comercialesFile);
            StringBuilder contenidoInicial = new StringBuilder();
            contenidoInicial.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            contenidoInicial.append("<comerciales>\n");

            for (Comercial comercial : comerciales) {
                contenidoInicial.append("    <comercial>\n");
                contenidoInicial.append("        <nombre>").append(comercial.getNombre()).append("</nombre>\n");
                contenidoInicial.append("        <apellido1>").append(comercial.getAppellido1()).append("</apellido1>\n");
                contenidoInicial.append("        <apellido2>").append(comercial.getApellido2()).append("</apellido2>\n");
                contenidoInicial.append("        <dni>").append(comercial.getDni()).append("</dni>\n");
                contenidoInicial.append("        <direccion>").append(comercial.getDireccion()).append("</direccion>\n");
                contenidoInicial.append("        <email>").append(comercial.getEmail()).append("</email>\n");
                contenidoInicial.append("        <zona1>").append(comercial.getZona1()).append("</zona1>\n");
                contenidoInicial.append("        <zona2>").append(comercial.getZona2()).append("</zona2>\n");
                contenidoInicial.append("    </comercial>\n");
            }

            contenidoInicial.append("</comerciales>\n");
            outputStream.write(contenidoInicial.toString().getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlertDialog("Error", "Error al escribir el contenido inicial en el archivo comerciales.xml.");
            permitirAcceso = false;
        }
    }


    private void mostrarAlertDialog(String titulo, String mensaje) {
            AlertDialog.Builder alerta = new AlertDialog.Builder(this);

            alerta.setMessage(mensaje)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Cierra la aplicación o realiza otras acciones necesarias
                          //  finish();
                        }
                    });

            AlertDialog dialog = alerta.create();
            dialog.setTitle(titulo);
            dialog.show();
        }

}