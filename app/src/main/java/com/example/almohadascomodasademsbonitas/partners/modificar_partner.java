package com.example.almohadascomodasademsbonitas.partners;

import android.os.Bundle;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.almohadascomodasademsbonitas.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;

public class modificar_partner extends AppCompatActivity {

    private EditText editTextNombre, editTextCif, editTextDireccion, editTextTelefono,
            editTextEmail, editTextComercial, editTextZona;

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
        editTextComercial = findViewById(R.id.editTextText5);
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
        editTextComercial.setText(data.get(5));
        editTextEmail.setText(data.get(6));
        editTextZona.setText(data.get(7));
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }










































}
