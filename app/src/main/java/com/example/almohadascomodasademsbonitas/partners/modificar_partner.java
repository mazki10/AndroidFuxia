package com.example.almohadascomodasademsbonitas.partners;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.almohadascomodasademsbonitas.BBDD.DBconexion;
import com.example.almohadascomodasademsbonitas.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class modificar_partner extends AppCompatActivity {

    private EditText editTextNombre, editTextCif, editTextDireccion, editTextTelefono,
            editTextEmail, editTextZona;
    private Partner nuevoPartner;
    private DBconexion dbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modificar_partner);

        // Inicializar los EditText
        editTextNombre = findViewById(R.id.editTextText3);
        editTextCif = findViewById(R.id.editTextTextPostalAddress2);
        editTextDireccion = findViewById(R.id.editTextTextPostalAddress1);
        editTextTelefono = findViewById(R.id.editTextPhone);
        editTextEmail = findViewById(R.id.editTextTextEmailAddress);
        editTextZona = findViewById(R.id.editTextNumber2);

        // Obtener el ID del socio de la actividad anterior
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("_partner")) {
            String idSocio = extras.getString("_partner");
            // Leer datos del socio con el ID proporcionado
            leerDatosSocio(idSocio);
        } else {
            Toast.makeText(this, "Error: ID del socio no proporcionado", Toast.LENGTH_SHORT).show();
            finish(); // Cerrar la actividad si no se proporciona el ID del socio
        }

        // Configurar el evento de clic para el botón de borrado
        Button btGuardar = findViewById(R.id.btguardar);
        btGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener el ID del socio de la actividad anterior
                Bundle extras = getIntent().getExtras();
                if (extras != null && extras.containsKey("_partner")) {
                    String idSocio = extras.getString("_partner");
                    // Borrar el socio con el ID proporcionado
                    guardarInformacion(idSocio);
                    // Añadir aquí cualquier acción adicional después de borrar el socio
                } else {
                    Toast.makeText(modificar_partner.this, "Error: ID del socio no proporcionado", Toast.LENGTH_SHORT).show();
                    finish(); // Cerrar la actividad si no se proporciona el ID del socio
                }
            }
        });

        // Configurar el evento de clic para el botón de borrado
        Button btBorrar = findViewById(R.id.btguardar1);
        btBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener el ID del socio de la actividad anterior
                Bundle extras = getIntent().getExtras();
                if (extras != null && extras.containsKey("_partner")) {
                    String idSocio = extras.getString("_partner");
                    // Borrar el socio con el ID proporcionado
                    borrarSocio(idSocio);
                    borrarPartnerBBDD(idSocio);
                    // Añadir aquí cualquier acción adicional después de borrar el socio
                } else {
                    Toast.makeText(modificar_partner.this, "Error: ID del socio no proporcionado", Toast.LENGTH_SHORT).show();
                    finish(); // Cerrar la actividad si no se proporciona el ID del socio
                }
            }
        });

    }

    private void leerDatosSocio(String idSocio) {
        try {
            FileInputStream fis = openFileInput("partners.xml");

            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(fis, null);

            int eventType = parser.getEventType();
            boolean socioEncontrado = false;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG && "partner".equals(parser.getName())) {
                    // Comienza un nuevo elemento partner
                    String currentId = "";
                    ArrayList<String> currentData = new ArrayList<>();

                    while (!(eventType == XmlPullParser.END_TAG && "partner".equals(parser.getName()))) {
                        if (eventType == XmlPullParser.START_TAG) {
                            String tagName = parser.getName();
                            eventType = parser.next();

                            // Leer el contenido del socio
                            if (eventType == XmlPullParser.TEXT) {
                                String text = parser.getText();

                                // Almacenar el contenido del texto
                                if ("id_partners".equals(tagName)) {
                                    currentId = text;
                                } else {
                                    currentData.add(text);
                                }
                            }
                        }
                        eventType = parser.next();
                    }

                    // Fin del elemento partner
                    if (currentId.equals(idSocio)) {
                        mostrarDatosSocio(currentData);
                        socioEncontrado = true;
                        break;
                    }
                }
                eventType = parser.next();
            }

            fis.close();

            if (!socioEncontrado) {
                Toast.makeText(this, "Error: Socio no encontrado", Toast.LENGTH_SHORT).show();
                finish(); // Cerrar la actividad si no se encuentra el socio
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mostrarDatosSocio(ArrayList<String> data) {
        // Mostrar datos del socio en los EditText
        editTextNombre.setText(data.get(1));
        editTextCif.setText(data.get(2));
        editTextDireccion.setText(data.get(3));
        editTextTelefono.setText(data.get(4));
        editTextEmail.setText(data.get(6));
        editTextZona.setText(data.get(7));
    }
    private void guardarInformacion(String idSocio) {
        try {
            String partner = idSocio;
            String nombre = editTextNombre.getText().toString();
            String CIF = editTextCif.getText().toString();
            String direccion = editTextDireccion.getText().toString();
            String telefono = editTextTelefono.getText().toString();
            String comercial = obtenerComercial();
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

            if (zona.isEmpty()) {
                throw new IllegalArgumentException("El campo zona es obligatorio");
            }

            // Obtener la fecha actual
            Date fechaActual = new Date();

            // Formatear la fecha a día, mes y año
            String formatoFecha = "dd/MM/yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(formatoFecha, Locale.getDefault());
            String fechaFormateada = sdf.format(fechaActual);

            // Configurar el objeto nuevoPartner solo si la validación es exitosa
            nuevoPartner = new Partner();
            nuevoPartner.set_id(partner);
            nuevoPartner.set_nombre(nombre);
            nuevoPartner.set_cif(CIF);
            nuevoPartner.set_direccion(direccion);
            nuevoPartner.set_telefono(Integer.valueOf(telefono));
            nuevoPartner.set_email(email);
            nuevoPartner.set_comercial(comercial);
            nuevoPartner.set_zona(Integer.valueOf(zona));
            nuevoPartner.set_fecha(fechaFormateada);

            Toast.makeText(this, "Información guardada correctamente", Toast.LENGTH_SHORT).show();
            borrarSocio(partner);
            guardarEnXML(partner);
            actualizarPartner(nuevoPartner);
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
                    editTextCif.requestFocus();
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
        return cif.matches("[A-Za-z]\\d{7}[A-z]");
    }
    private void borrarSocio(String idSocio) {
        try {
            FileInputStream fis = openFileInput("partners.xml");
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(fis, null);

            // Crear un nuevo archivo XML temporal
            File tempFile = new File(getFilesDir(), "temp_partners.xml");
            OutputStream os = new FileOutputStream(tempFile);
            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(os, "UTF-8");
            serializer.startDocument(null, Boolean.TRUE);
            serializer.text("\n");
            serializer.startTag(null, "partners");
            serializer.text("\n");

            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG && "partner".equals(parser.getName())) {
                    // Comienza un nuevo elemento partner
                    String currentId = "";
                    ArrayList<String> currentData = new ArrayList<>();
                    boolean writePartnerTag = false;

                    while (!(eventType == XmlPullParser.END_TAG && "partner".equals(parser.getName()))) {
                        if (eventType == XmlPullParser.START_TAG) {
                            String tagName = parser.getName();
                            eventType = parser.next();

                            // Leer el contenido del socio
                            if (eventType == XmlPullParser.TEXT) {
                                String text = parser.getText();

                                // Almacenar el contenido del texto
                                if ("id_partners".equals(tagName)) {
                                    currentId = text;
                                }
                                currentData.add(tagName);
                                currentData.add(text);
                            }
                        }
                        eventType = parser.next();
                    }

                    // Fin del elemento partner
                    if (!currentId.equals(idSocio)) {
                        writePartnerTag = true;
                    }

                    if (writePartnerTag) {
                        serializer.startTag(null, "partner");
                        serializer.text("\n");
                        for (int i = 2; i < currentData.size(); i += 2) {
                            serializer.text("    "); // Sangría
                            serializer.startTag(null, currentData.get(i));
                            serializer.text(currentData.get(i + 1));
                            serializer.endTag(null, currentData.get(i));
                            serializer.text("\n");
                        }
                        serializer.endTag(null, "partner");
                        serializer.text("\n");
                    }
                }
                eventType = parser.next();
            }

            serializer.text("\n");
            serializer.endTag(null, "partners");
            serializer.text("\n");
            serializer.endDocument();
            os.flush();
            os.close();
            fis.close();

            // Renombrar el nuevo archivo XML temporal al archivo original
            tempFile.renameTo(new File(getFilesDir(), "partners.xml"));
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Método para actualizar los datos de un socio
    // Método para actualizar los datos de un socio
    public void actualizarPartner(Partner partner) {
        DBconexion dbHelper = new DBconexion(this, "ACAB2.db", null, 1);
        db = dbHelper.getWritableDatabase();
        // Construir la sentencia SQL para la actualización
        String sql = "UPDATE PARTNERS SET " +
                "NOMBRE='" + partner.get_nombre() + "', " +
                "CIF='" + partner.get_cif() + "', " +
                "DIRECCION='" + partner.get_direccion() + "', " +
                "TELEFONO=" + partner.get_telefono() + ", " +
                "COMERCIAL='" + partner.get_comercial() + "', " +
                "EMAIL='" + partner.get_email() + "', " +
                "ZONA=" + partner.get_zona() +
                " WHERE ID_PARTNER=" + partner.get_id();

        // Ejecutar la sentencia SQL
        db.execSQL(sql);
        db.close();
    }
    public void borrarPartnerBBDD(String idPartner){
        DBconexion dbHelper = new DBconexion(this, "ACAB2.db", null, 1);
        db = dbHelper.getWritableDatabase();
        // Construir la sentencia SQL para la actualización
        String sql = "DELETE FROM PARTNERS WHERE ID_PARTNER = "+idPartner+"";

        // Ejecutar la sentencia SQL
        db.execSQL(sql);
        db.close();
    }

    private void guardarEnXML(String idSocio) {
        try {

            Integer cantidadPartners = Integer.parseInt(idSocio);

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

        } catch (Exception e) {
            // Manejar excepciones
            Toast.makeText(this, "Error al añadir datos al XML: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
