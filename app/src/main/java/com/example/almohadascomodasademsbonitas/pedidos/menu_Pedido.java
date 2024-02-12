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

        dbHelper = new DBconexion(menu_Pedido.this, "ACAB2", null, 1);
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
                    // Si no tienes permisos, solicitarlos al usuario

                    verificarArchivosXML();
                if (permitirAcceso) {
                    Intent intent = new Intent(menu_Pedido.this, actividad_pedido.class);
                    menu_Pedido.this.startActivity(intent);
                }else{
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
                    LocalDate fecUltEnt = LocalDate.parse(element.getElementsByTagName("fec_ult_ent").item(0).getTextContent());
                    LocalDate fecUltSal = LocalDate.parse(element.getElementsByTagName("fec_ult_sal").item(0).getTextContent());

                    Articulo articulo = new Articulo(id, idProveedor, descripcion, precioVenta, precioCoste, existencias, stockMax, stockMin, fecUltEnt, fecUltSal);
                    articulos.add(articulo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void verificarArchivosXML() {
        permitirAcceso = true; // Restablecer el valor por defecto

        File filesFolder = getFilesDir();
        File comercialesFile = new File(filesFolder, "comerciales.xml");
        File partnersFile = new File(filesFolder, "partners.xml");

        // Verifica la existencia de los archivos en la carpeta interna
        if (!comercialesFile.exists() || !partnersFile.exists()) {
            mostrarAlertDialog("Error", "Los archivos comerciales.xml y/o partners.xml no se encontraron en la carpeta interna.");
            permitirAcceso = false;
            return;
        }

        // Verifica si los archivos están vacíos
        if (comercialesFile.length() == 0 || partnersFile.length() == 0) {
            // Si al menos uno de los archivos está vacío, procede a rellenarlos
            if (comercialesFile.length() == 0) {
                escribirContenidoInicialComerciales(comercialesFile);
            }
            if (partnersFile.length() == 0) {
                escribirContenidoInicialPartners(partnersFile);
            }
        }
    }


    private String leerDatosArchivo(File file) throws IOException {
        Log.d("Archivo", "Leyendo archivo desde: " + file.getAbsolutePath());

        FileInputStream inputStream = new FileInputStream(file);
        StringBuilder stringBuilder = new StringBuilder();
        int character;
        while ((character = inputStream.read()) != -1) {
            stringBuilder.append((char) character);
        }
        inputStream.close();

        String contenidoArchivo = stringBuilder.toString();
        Log.d("Archivo", "Contenido del archivo leído: " + contenidoArchivo);

        return contenidoArchivo;
    }



    private void escribirDatosEnArchivo(String data, File file) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(data.getBytes());
        outputStream.close();
    }


    private void escribirContenidoNuevoArchivo(File sourceFile, File destinationFile) throws IOException {
        FileInputStream inputStream = new FileInputStream(sourceFile);
        FileOutputStream outputStream = new FileOutputStream(destinationFile);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }

        inputStream.close();
        outputStream.close();
    }


    private void escribirContenidoInicialPartners(File partnersFile) {
        try {
            FileWriter writer = new FileWriter(partnersFile);
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<partners>\n");

            writer.write("</partners>\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlertDialog("Error", "Error al escribir el contenido inicial en el archivo partners.xml.");
            permitirAcceso = false;
        }
    }



    private void copyFile(File source, File destination) {
        try {
            FileInputStream inputStream = new FileInputStream(source);
            FileOutputStream outputStream = new FileOutputStream(destination);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlertDialog("Error de copia", "Error al copiar el archivo: " + e.getMessage());
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