package com.example.almohadascomodasademsbonitas.pedidos;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.almohadascomodasademsbonitas.CircularLayout;
import com.example.almohadascomodasademsbonitas.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

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
    private static final int REQUEST_PERMISSION_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pedidos);
        botonAlta = findViewById(R.id.buttonAlta);
        botonBaja = findViewById(R.id.buttonBaja);
        botonModificacion = findViewById(R.id.buttonModificar);
        botonSalir = findViewById(R.id.buttonSalir);
        dbHelper = new DBconexion(this, "ACAB2.db", null, 1);
        db = dbHelper.getWritableDatabase();
        botonSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        botonAlta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Si no tienes permisos, solicitarlos al usuario
                verificarArchivosXML();
                if (permitirAcceso) {
                    File file = new File(getFilesDir(), "articulos.xml");
                    cargarArticulosDesdeXML(file); // Cargar datos desde el archivo XML
                    Intent intent = new Intent(menu_Pedido.this, actividad_pedido.class);
                    menu_Pedido.this.startActivity(intent);
                } else {
                    //crearComercialesXML();
                    //crearPartnersXML();
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
    }



    private void cargarArticulosDesdeXML(File file) {
        dbHelper = new DBconexion(this, "ACAB2.db", null, 1);
        db = dbHelper.getWritableDatabase();
        try {

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);

            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("articulo");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    int id = Integer.parseInt(element.getElementsByTagName("id_articulo").item(0).getTextContent());
                    int idProveedor = Integer.parseInt(element.getElementsByTagName("id_proveedor").item(0).getTextContent());
                    String descripcion = element.getElementsByTagName("descripcion").item(0).getTextContent();
                    double precioVenta = Double.parseDouble(element.getElementsByTagName("precio_venta").item(0).getTextContent());
                    double precioCoste = Double.parseDouble(element.getElementsByTagName("precio_coste").item(0).getTextContent());
                    int existencias = Integer.parseInt(element.getElementsByTagName("existencias").item(0).getTextContent());
                    int stockMax = Integer.parseInt(element.getElementsByTagName("stock_max").item(0).getTextContent());
                    int stockMin = Integer.parseInt(element.getElementsByTagName("stock_min").item(0).getTextContent());
                    String fecUltEnt = (element.getElementsByTagName("fec_ult_ent").item(0).getTextContent());
                    String fecUltSal = (element.getElementsByTagName("fec_ult_sal").item(0).getTextContent());

                    Articulo articulo = new Articulo(id, idProveedor, descripcion, precioVenta, precioCoste, existencias, stockMax, stockMin, fecUltEnt,fecUltSal );
                    articulos.add(articulo);

                    if(!existeArticuloEnBaseDeDatos(id)){
                        String insertQuery = "INSERT INTO ARTICULOS (ID_ARTICULO, ID_PROVEEDOR, DESCRIPCION, PRECIO_VENTA, PRECIO_COSTE, EXISTENCIAS, STOCK_MAX, STOCK_MIN, FEC_ULT_ENT, FEC_ULT_SAL) " +
                                "VALUES (" + id + ", " + idProveedor + ", '" + descripcion + "', " +
                                precioVenta + ", " + precioCoste + ", " + existencias + ", " +
                                stockMax + ", " + stockMin + ",'"+fecUltEnt.toString()+"','"+fecUltSal.toString()+"')";
                        db.execSQL(insertQuery);
                        Log.d("Consulta", "Query de inserción ejecutado: " + insertQuery);

                    }


                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void verificarArchivosXML() {
        permitirAcceso = true; // Restablecer el valor por defecto

        // Verificar la existencia de los datos en la tabla COMERCIALES
        Cursor cursor = db.rawQuery("SELECT * FROM COMERCIALES", null);

        // Verifica si hay algún registro en la tabla COMERCIALES
        if (cursor.getCount() == 0) {
            mostrarAlertDialog("Error", "No se encontraron datos en la tabla COMERCIALES.");
            permitirAcceso = false;
        }

        cursor.close();
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
    private boolean existeArticuloEnBaseDeDatos(Integer idArticulo) {
        dbHelper = new DBconexion(this, "ACAB2.db", null, 1);
        db = dbHelper.getWritableDatabase();
        String query = "SELECT COUNT(*) FROM ARTICULOS WHERE ID_ARTICULO = ?";
        String[] selectionArgs = {idArticulo.toString()};
        try {
            // Ejecutar la consulta y obtener el resultado
            Cursor cursor = db.rawQuery(query, selectionArgs);
            if (cursor.moveToFirst()) {
                int count = cursor.getInt(0);
                cursor.close();
                return count > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // Si hay algún error o no se encuentra el id_partner, retornar falso
    }

}