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
        crearPartnersXML();
        crearComercialesXML();
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






        comerciales.add(new Comercial("Jose","Alfredo","Alfredo","12345678A","Dirigeme esta","sisi@gmail.com",2,1));

        articulos.add(new Articulo(1,101,"jordi",50.0,25.0,100,200,10,LocalDate.of(2024,1,10),LocalDate.of(2024,1,20)));
        articulos.add(new Articulo(2,102,"bale",50.0,25.0,70,200,10,LocalDate.of(2024,1,10),LocalDate.of(2024,1,20)));
        articulos.add(new Articulo(3,103,"bob",50.0,25.0,50,200,10,LocalDate.of(2024,1,10),LocalDate.of(2024,1,20)));
        articulos.add(new Articulo(4,42,"cartas",50.0,25.0,80,200,10,LocalDate.of(2024,1,10),LocalDate.of(2024,1,20)));
        articulos.add(new Articulo(5,1312,"hello",50.0,25.0,130,200,10,LocalDate.of(2024,1,10),LocalDate.of(2024,1,20)));
        articulos.add(new Articulo(6,101,"patriota",50.0,25.0,100,200,10,LocalDate.of(2024,1,10),LocalDate.of(2024,1,20)));
        articulos.add(new Articulo(7,42,"pistola",50.0,25.0,140,200,10,LocalDate.of(2024,1,10),LocalDate.of(2024,1,20)));
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
/*
       private void verificarArchivosXML() {
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
   /* @Override
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
    }*/
private void crearPartnersXML() {
    try {
        File file = new File(getFilesDir(), "partners.xml"); // Ruta en el almacenamiento interno
        FileWriter writer = new FileWriter(file);
        writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        writer.write("<partners>\n");

        // Datos de los partners
        String[] nombres = {"Jose Antonio", "Manuel Laboa", "Manute Jagger"};
        String[] cif = {"ABC123456", "XYZ789012", "DEF345678"};
        String[] direcciones = {"Calle Ejemplo, 123", "Avenida de Prueba, 456", "Plaza de Muestra, 789"};
        String[] telefonos = {"123456789", "987654321", "555666777"};
        String[] emails = {"ejemplo@correo.com", "prueba@correo.com", "muestra@correo.com"};
        String[] personasContacto = {"Juan Pérez", "Maria Gómez", "Pedro Rodríguez"};
        int[] idZona = {1, 2, 3};

        // Escribir datos de partners
        for (int i = 0; i < nombres.length; i++) {
            writer.write("    <partner>\n");
            writer.write("        <id_partners>" + (i + 1) + "</id_partners>\n");
            writer.write("        <nombre>" + nombres[i] + "</nombre>\n");
            writer.write("        <cif>" + cif[i] + "</cif>\n");
            writer.write("        <direccion>" + direcciones[i] + "</direccion>\n");
            writer.write("        <telefono>" + telefonos[i] + "</telefono>\n");
            writer.write("        <email>" + emails[i] + "</email>\n");
            writer.write("        <persona_de_contacto>" + personasContacto[i] + "</persona_de_contacto>\n");
            writer.write("        <id_zona>" + idZona[i] + "</id_zona>\n");
            writer.write("    </partner>\n");
        }

        writer.write("</partners>\n");
        writer.close();
        System.out.println("Archivo partners.xml creado correctamente.");
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    private  void crearComercialesXML() {
        try {
            File file = new File(getFilesDir(), "comerciales.xml"); // Ruta en el almacenamiento interno
            FileWriter writer = new FileWriter(file);
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<comerciales>\n");

            // Datos de los comerciales
            String[] nombres = {"Juan", "Pedro", "Maria"};
            String[] apellido1 = {"Gomez", "Martínez", "Lopez"};
            String[] apellido2 = {"Perez", "Sanchez", "Garcia"};
            String[] dni = {"12345678X", "56789012Z", "98765432Y"};
            String[] direcciones = {"Calle Principal 123", "Plaza Principal 789", "98765432Y"};
            String[] emails = {"juan@example.com", "pedro@example.com", "maria@example.com"};
            int[] zona1 = {1, 3, 2};
            int[] zona2 = {2, 1, 3};

            // Escribir datos de comerciales
            for (int i = 0; i < nombres.length; i++) {
                writer.write("    <comercial>\n");
                writer.write("        <nombre>" + nombres[i] + "</nombre>\n");
                writer.write("        <apellido1>" + apellido1[i] + "</apellido1>\n");
                writer.write("        <apellido2>" + apellido2[i] + "</apellido2>\n");
                writer.write("        <dni>" + dni[i] + "</dni>\n");
                writer.write("        <direccion>" + direcciones[i] + "</direccion>\n");
                writer.write("        <email>" + emails[i] + "</email>\n");
                writer.write("        <zona1>" + zona1[i] + "</zona1>\n");
                writer.write("        <zona2>" + zona2[i] + "</zona2>\n");
                writer.write("    </comercial>\n");
            }

            writer.write("</comerciales>\n");
            writer.close();
            System.out.println("Archivo comerciales.xml creado correctamente.");
        } catch (IOException e) {
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