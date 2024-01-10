package com.example.almohadascomodasademsbonitas.partners;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.almohadascomodasademsbonitas.R;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class nuevo_partner extends AppCompatActivity {

    private EditText editTextNombre, editTextDireccion, editTextTelefono, editTextComercial, editTextEmail, editTextCIF, editTextZona;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nuevo_partner);

        editTextNombre = findViewById(R.id.editTextText3);
        editTextCIF = findViewById(R.id.editTextTextPostalAddress2);
        editTextDireccion = findViewById(R.id.editTextTextPostalAddress1);
        editTextTelefono = findViewById(R.id.editTextPhone);
        editTextEmail = findViewById(R.id.editTextTextEmailAddress);
        editTextComercial = findViewById(R.id.editTextText5);
        editTextZona = findViewById(R.id.editTextNumber2);
        Button buttonGuardar = findViewById(R.id.btguardar);
        buttonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarEnXML(guardarInformacion());
            }
        });
    }

    private ArrayList<String> guardarInformacion() {
        ArrayList<String> partner = null;
        try {
            partner = new ArrayList<>();
            String nombre = editTextNombre.getText().toString();
            String CIF = editTextCIF.getText().toString();
            String direccion = editTextDireccion.getText().toString();
            String telefono = editTextTelefono.getText().toString();
            String comercial = editTextComercial.getText().toString();
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

            if (comercial.isEmpty()) {
                throw new IllegalArgumentException("El campo comercial es obligatorio");
            }
            if (zona.isEmpty()){
                throw new IllegalArgumentException("El campo zona es obligatorio");
            }
            partner.add(editTextNombre.getText().toString());
            partner.add(editTextCIF.getText().toString());
            partner.add(editTextDireccion.getText().toString());
            partner.add(editTextTelefono.getText().toString());
            partner.add(editTextComercial.getText().toString());
            partner.add(editTextEmail.getText().toString());
            partner.add(editTextZona.getText().toString());

            Toast.makeText(this, "Información guardada correctamente", Toast.LENGTH_SHORT).show();

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

                case "El campo comercial es obligatorio":
                    editTextComercial.requestFocus();
                    break;
                case "El campo zona es obligatorio":
                    editTextZona.requestFocus();
                    break;
            }
        } catch (Exception e) {
            // Manejar otras excepciones
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return partner;
    }
    private boolean validarCIF(String cif) {
        return cif.matches("[A-HJNP-SUW-Za-hjnp-su-wz]\\d{7}[A-HJNP-SUW-Za-hjnp-su-wz]");
    }

    private void guardarEnXML(ArrayList<String> partner) {
        try {
            int cantidadPartners = contarPartners();

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
            outputStreamWriter.write("    <id_partners>" + (cantidadPartners + 1) + "</id_partners>\n");
            outputStreamWriter.write("    <nombre>" + partner.get(0) + "</nombre>\n");
            outputStreamWriter.write("    <cif>" + partner.get(1) + "</cif>\n");
            outputStreamWriter.write("    <direccion>" + partner.get(2) + "</direccion>\n");
            outputStreamWriter.write("    <telefono>" + partner.get(3) + "</telefono>\n");
            outputStreamWriter.write("    <comercial>" + partner.get(4) + "</comercial>\n");
            outputStreamWriter.write("    <email>" + partner.get(5) + "</email>\n");
            outputStreamWriter.write("    <zona>" + partner.get(6) + "</zona>\n");
            outputStreamWriter.write("  </partner>\n");

            // Cierra la etiqueta </partners>
            outputStreamWriter.write("</partners>\n");

            // Cierra el archivo
            outputStreamWriter.close();
            fos.close();

            // Leer y mostrar el contenido del archivo para verificar
            String contenido = leerContenidoXML();
            Toast.makeText(this, "Información añadida a XML correctamente:\n" + contenido, Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            // Manejar excepciones
            Toast.makeText(this, "Error al añadir datos al XML: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }



    private int contarPartners() {
        int cantidad = 0;
        try {
            FileInputStream fis = openFileInput("partners.xml");
            InputStreamReader inputStreamReader = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;

            // Contar líneas que comienzan con "<partner"
            while ((line = bufferedReader.readLine()) != null) {
                if (line.trim().startsWith("<partner")) {
                    cantidad++;
                }
            }

            bufferedReader.close();
            inputStreamReader.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cantidad;
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
}
