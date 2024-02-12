package com.example.almohadascomodasademsbonitas.partners;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.almohadascomodasademsbonitas.BBDD.DBconexion;
import com.example.almohadascomodasademsbonitas.R;

import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class nuevo_partner extends AppCompatActivity {

    private EditText editTextNombre, editTextDireccion, editTextTelefono, editTextEmail, editTextCIF, editTextZona;
    private Partner nuevoPartner;
    private DBconexion dbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nuevo_partner);

        editTextNombre = findViewById(R.id.editTextText3);
        editTextCIF = findViewById(R.id.editTextTextPostalAddress2);
        editTextDireccion = findViewById(R.id.editTextTextPostalAddress1);
        editTextTelefono = findViewById(R.id.editTextPhone);
        editTextEmail = findViewById(R.id.editTextTextEmailAddress);
        editTextZona = findViewById(R.id.editTextNumber2);
        Button buttonGuardar = findViewById(R.id.btguardar);
        nuevoPartner = new Partner();
        buttonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Validar la información antes de guardar
                    guardarInformacion();
                } catch (IllegalArgumentException e) {
                    // Capturar excepciones de validación
                    Toast.makeText(nuevo_partner.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void guardarInformacion() {
        try {
            String nombre = editTextNombre.getText().toString();
            String CIF = editTextCIF.getText().toString();
            String direccion = editTextDireccion.getText().toString();
            String telefono = editTextTelefono.getText().toString();
            String email = editTextEmail.getText().toString();
            String zona = editTextZona.getText().toString();

            // Validar que los campos no estén vacíos
            if (nombre.isEmpty()) {
                throw new IllegalArgumentException("El nombre es obligatorio");
            }
            if (CIF.isEmpty()) {
                throw new IllegalArgumentException("El CIF es obligatorio");
            } else if (!validarCIF(CIF)) {
                throw new IllegalArgumentException("El CIF no es válido");
            }

            if (direccion.isEmpty()) {
                throw new IllegalArgumentException("La dirección es obligatoria");
            }

            if (telefono.isEmpty()) {
                throw new IllegalArgumentException("El teléfono es obligatorio");
            } else if (telefono.length() != 9) {
                throw new IllegalArgumentException("El teléfono debe tener 9 números");
            }

            if (email.isEmpty()) {
                throw new IllegalArgumentException("El correo electrónico es obligatorio");
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                throw new IllegalArgumentException("El correo electrónico no es válido");
            }

            if (zona.isEmpty()) {
                throw new IllegalArgumentException("El campo zona es obligatorio");
            }

            // Obtener la fecha actual
            Date fechaActual = new Date();

            // Formatear la fecha a día, mes y año
            String formatoFecha = "dd/MM/yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(formatoFecha, Locale.getDefault());
            String fechaFormateada = sdf.format(fechaActual);

            String dniComercial = obtenerComercial();
            // Configurar el objeto nuevoPartner solo si la validación es exitosa
            nuevoPartner = new Partner();
            nuevoPartner.set_nombre(nombre);
            nuevoPartner.set_cif(CIF);
            nuevoPartner.set_direccion(direccion);
            nuevoPartner.set_telefono(Integer.valueOf(telefono));
            nuevoPartner.set_email(email);
            nuevoPartner.set_comercial(dniComercial);
            nuevoPartner.set_zona(Integer.valueOf(zona));
            nuevoPartner.set_fecha(fechaFormateada);

            Toast.makeText(this, "Información guardada correctamente", Toast.LENGTH_SHORT).show();
            guardarEnXML();
            finish();
        } catch (IllegalArgumentException e) {
            // Se captura la excepción específica para errores de validación
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();

            // Coloca el foco en el EditText correspondiente
            switch (e.getMessage()) {
                case "El nombre es obligatorio":
                    editTextNombre.requestFocus();
                    break;
                case "El CIF es obligatorio":
                case "El CIF no es válido":
                    editTextCIF.requestFocus();
                    break;
                case "La dirección es obligatoria":
                    editTextDireccion.requestFocus();
                    break;
                case "El teléfono es obligatorio":
                case "El teléfono debe tener 9 números":
                    editTextTelefono.requestFocus();
                    break;
                case "El correo electrónico es obligatorio":
                case "El correo electrónico no es válido":
                    editTextEmail.requestFocus();
                    break;
                case "El campo zona es obligatorio":
                    editTextZona.requestFocus();
                    break;
            }
        } catch (Exception e) {
            // Manejar otras excepciones
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validarCIF(String cif) {
        return cif.matches("[A-z]\\d{7}[A-z]");
    }

    private void guardarEnXML() {
        try {

            int cantidadPartners = obtenerNuevoIdPartner();

            // Abre un archivo en la memoria interna en modo de apendizaje
            FileOutputStream fos = openFileOutput("partners.xml", MODE_APPEND);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fos);

            // Si es el primer socio, abre la etiqueta <partners>
            if (cantidadPartners == 0) {
                outputStreamWriter.write("<partners>\n");
            }

            // Elimina la última línea (cierre de </partners>) si ya existe
            if (cantidadPartners > 0) {
                FileInputStream fis = openFileInput("partners.xml");
                InputStreamReader inputStreamReader = new InputStreamReader(fis);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                // Leer el archivo hasta la penúltima línea y copiar al nuevo archivo
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    if (!line.trim().equalsIgnoreCase("</partners>")) {
                        sb.append(line).append("\n");
                    }
                }

                // Cerrar el archivo existente
                bufferedReader.close();
                inputStreamReader.close();
                fis.close();

                // Sobrescribir el archivo con el contenido actualizado
                FileOutputStream fosUpdate = openFileOutput("partners.xml", MODE_PRIVATE);
                OutputStreamWriter oswUpdate = new OutputStreamWriter(fosUpdate);
                oswUpdate.write(sb.toString());

                // Cerrar el archivo actualizado
                oswUpdate.close();
                fosUpdate.close();
            }

            // Nuevo partner con el campo id_partner
            outputStreamWriter.write("  <partner>\n");
            outputStreamWriter.write("    <id_partners>" + (cantidadPartners) + "</id_partners>\n");
            outputStreamWriter.write("    <nombre>" + nuevoPartner.get_nombre() + "</nombre>\n");
            outputStreamWriter.write("    <cif>" + nuevoPartner.get_cif() + "</cif>\n");
            outputStreamWriter.write("    <direccion>" + nuevoPartner.get_direccion() + "</direccion>\n");
            outputStreamWriter.write("    <telefono>" + nuevoPartner.get_telefono() + "</telefono>\n");
            outputStreamWriter.write("    <comercial>" + nuevoPartner.get_comercial() + "</comercial>\n");
            outputStreamWriter.write("    <email>" + nuevoPartner.get_email() + "</email>\n");
            outputStreamWriter.write("    <zona>" + nuevoPartner.get_zona() + "</zona>\n");
            outputStreamWriter.write("    <fecha>" + nuevoPartner.get_fecha() + "</fecha>\n");
            outputStreamWriter.write("  </partner>\n");

            // Cierra la etiqueta </partners>
            outputStreamWriter.write("</partners>\n");

            // Cierra el archivo
            outputStreamWriter.close();
            fos.close();

            // Leer y mostrar el contenido del archivo para verificar
            String contenido = leerContenidoXML();

        } catch (Exception e) {
            // Manejar excepciones
            Toast.makeText(this, "Error al añadir datos al XML: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }




    private int obtenerNuevoIdPartner() {
        int nuevoId = 1;  // Valor predeterminado si no hay ningún socio aún

        try {

            dbHelper = new DBconexion(this, "ACAB2.db", null, 1);
            db = dbHelper.getReadableDatabase();

            // Realiza la consulta SQL para obtener el máximo id_partners
            String query = "SELECT MAX(id_partner) FROM partners";
            Cursor cursor = db.rawQuery(query, null);

            // Mueve el cursor al primer resultado
            if (cursor.moveToFirst()) {
                // Obtiene el valor máximo actual
                int maxId = cursor.getInt(0);
                // Calcula el nuevo ID agregándole uno al máximo actual
                nuevoId = maxId + 1;
            }

            // Cierra el cursor y la base de datos
            cursor.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return nuevoId;
    }





    private String leerContenidoXML() {
        try {
            FileInputStream fis = openFileInput("partners.xml");
            InputStreamReader inputStreamReader = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }

            bufferedReader.close();
            inputStreamReader.close();
            fis.close();

            return stringBuilder.toString();

        } catch (Exception e) {
            // Manejar excepciones
            return "Error al leer el contenido del archivo XML: " + e.getMessage();
        }
    }
    private String obtenerComercial(){
        String dniComercial = null;
        dbHelper = new DBconexion(this, "ACAB2.db", null, 1);
        db = dbHelper.getReadableDatabase();

        String query = "SELECT ID_COMERCIAL FROM LOGIN WHERE SESION = 1";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex("ID_COMERCIAL");
            // Recuperamos el valor del DNI del cursor
            dniComercial = cursor.getString(columnIndex);
        }

        cursor.close();
        return dniComercial;
    }



}
