package com.example.almohadascomodasademsbonitas.pedidos;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import java.time.LocalDate;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
    private static final int REQUEST_PERMISSION_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pedidos);

        dbHelper = new DBconexion(this, "ACAB2.db", null, 1);
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
                if (ContextCompat.checkSelfPermission(menu_Pedido.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // Si no tienes permisos, solicitarlos al usuario
                    ActivityCompat.requestPermissions(menu_Pedido.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
                } else {
                    verificarArchivosXML();
                    if (permitirAcceso) {
                        Intent intent = new Intent(menu_Pedido.this, actividad_pedido.class);
                        menu_Pedido.this.startActivity(intent);
                    }
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





        comerciales.add(new Comercial("Jose","Alfredo","Alfredo","12345678A","Dirigeme esta","sisi@gmail.com",2,1));


        articulos.add(new Articulo(1,101,"jordi",50.0,25.0,100,200,10,LocalDate.of(2024,1,10),LocalDate.of(2024,1,20)));
        articulos.add(new Articulo(2,102,"bale",50.0,25.0,100,200,10,LocalDate.of(2024,1,10),LocalDate.of(2024,1,20)));
        articulos.add(new Articulo(3,103,"bob",50.0,25.0,100,200,10,LocalDate.of(2024,1,10),LocalDate.of(2024,1,20)));
        articulos.add(new Articulo(4,42,"cartas",50.0,25.0,100,200,10,LocalDate.of(2024,1,10),LocalDate.of(2024,1,20)));
        articulos.add(new Articulo(5,1312,"hello",50.0,25.0,100,200,10,LocalDate.of(2024,1,10),LocalDate.of(2024,1,20)));
        articulos.add(new Articulo(6,101,"patriota",50.0,25.0,100,200,10,LocalDate.of(2024,1,10),LocalDate.of(2024,1,20)));
        articulos.add(new Articulo(7,42,"pistola",50.0,25.0,100,200,10,LocalDate.of(2024,1,10),LocalDate.of(2024,1,20)));
        articulos.add(new Articulo(8,102,"verde",50.0,25.0,100,200,10,LocalDate.of(2024,1,10),LocalDate.of(2024,1,20)));





        for(int i=0; i<comerciales.size();i++){
            String insertQuery = "INSERT INTO COMERCIALES (NOMBRE, APELLIDO1, APELLIDO2, DNI, DIRECCION, EMAIL, ZONA1, ZONA2) " +
                    "VALUES ('" + comerciales.get(i).getNombre() + "', '" + comerciales.get(i).getAppellido1() + "', '" + comerciales.get(i).getApellido2() + "', '" +
                    comerciales.get(i).getDni() + "', '" + comerciales.get(i).getDireccion() + "', '" + comerciales.get(i).getEmail()+ "'," +
                    comerciales.get(i).getZona1() + "," + comerciales.get(i).getZona2() + ")";
            db.execSQL(insertQuery);
        }

        for (int i = 0; i < articulos.size(); i++) {
            String insertQuery = "INSERT INTO ARTICULOS (ID_ARTICULO, ID_PROVEEDOR, DESCRIPCION, PRECIO_VENTA, PRECIO_COSTE, EXISTENCIAS, STOCK_MAX, STOCK_MIN, FEC_ULT_ENT, FEC_ULT_SAL) " +
                    "VALUES (" + articulos.get(i).getId_articulo() + ", " + articulos.get(i).getId_proveedor() + ", '" + articulos.get(i).getDescripcion() + "', " +
                    articulos.get(i).getPrecio_venta() + ", " + articulos.get(i).getPrecio_coste() + ", " + articulos.get(i).getExistencias() + "," +
                    articulos.get(i).getStock_max() + "," + articulos.get(i).getStock_min() + ",'" + articulos.get(i).getFec_ult_ent() + "','" + articulos.get(i).getFec_ult_sal() + "')";
            Log.d("Insertion", "Inserting articulo: " + articulos.get(i).getDescripcion());
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
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Verificar si el código de solicitud es el mismo que el que utilizamos al solicitar permisos
        if (requestCode == REQUEST_PERMISSION_CODE) {
            // Verificar si el usuario concedió el permiso
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, continuar con la lógica para acceder al archivo
                verificarArchivosXML();
            } else {
                // Permiso denegado, puedes mostrar un mensaje al usuario o realizar otra acción
                Toast.makeText(this, "Permiso de almacenamiento denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void verificarArchivosXML() {
        permitirAcceso = true; // Restablecer el valor por defecto

        File descargasFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File comercialesFile = new File(descargasFolder, "comerciales.xml");
        File partnersFile = new File(descargasFolder, "partners.xml");

        // Verifica la existencia de los archivos en la carpeta Descargas
        if (!comercialesFile.exists()) {
            mostrarAlertDialog("Error", "El archivo comerciales.xml no se encontró en la carpeta Descargas.");
            permitirAcceso = false;
            return;
        }

        if (!partnersFile.exists()) {
            mostrarAlertDialog("Error", "El archivo partners.xml no se encontró en la carpeta Descargas.");
            permitirAcceso = false;
            return;
        }

        // Leer datos de los archivos en la carpeta de descargas
        String comercialesData;
        try {
            comercialesData = leerDatosArchivo(comercialesFile);
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlertDialog("Error", "Error al leer los datos del archivo comerciales.xml.");
            permitirAcceso = false;
            return;
        }

        String partnersData;
        try {
            partnersData = leerDatosArchivo(partnersFile);
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlertDialog("Error", "Error al leer los datos del archivo partners.xml.");
            permitirAcceso = false;
            return;
        }

        // Escribir los datos en los archivos correspondientes en la carpeta interna
        File filesFolder = getFilesDir();
        File comercialesInternal = new File(filesFolder, "comerciales.xml");
        File partnersInternal = new File(filesFolder, "partners.xml");

        try {
            if (!filesFolder.exists()) {
                filesFolder.mkdirs(); // Crea la carpeta interna si no existe
            }

            escribirDatosEnArchivo(comercialesData, comercialesInternal);
            escribirDatosEnArchivo(partnersData, partnersInternal);
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlertDialog("Error", "Error al crear los nuevos archivos en la carpeta interna.");
            permitirAcceso = false;
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

    /*private void escribirContenidoInicialComerciales(File comercialesFile) {
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
                contenidoInicial.append("        <user>").append(comercial.getUser()).append("</user>\n");
                contenidoInicial.append("        <password>").append(comercial.getPassword()).append("</password>\n");
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
    }*/


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