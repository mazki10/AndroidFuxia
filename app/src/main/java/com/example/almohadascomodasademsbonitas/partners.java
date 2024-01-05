package com.example.almohadascomodasademsbonitas;



import android.content.Intent;
import android.os.Bundle;
import android.util.Xml;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class partners extends AppCompatActivity {

    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.partners);

        // Copiar el archivo XML desde assets a la memoria interna
        copiarXmlDesdeAssets();

        // Obtén una referencia al ListView en tu diseño
        ListView listView = findViewById(R.id.lvPartners);

        // Crea una lista para almacenar los datos del archivo XML en la memoria interna
        ArrayList<String> datosDeXml = leerDatosDesdeXmlEnMemoriaInterna();

        // Crea un ArrayAdapter para enlazar los datos a la interfaz de usuario
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, datosDeXml);

        // Asigna el adaptador al ListView
        listView.setAdapter(adapter);

        // Obtén una referencia al botón btNuevo en tu diseño
        Button btNuevo = findViewById(R.id.btNuevo);

        // Agrega un OnClickListener al botón para abrir la actividad nuevo_partner
        btNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNuevoPartnerActivity();
            }
        });
    }

    private void copiarXmlDesdeAssets() {
        try {
            // Verificar si el archivo ya existe en la memoria interna
            File file = new File(getFilesDir(), "partners.xml");
            if (!file.exists()) {
                // Copiar el archivo desde assets a la memoria interna
                InputStream is = getAssets().open("partners.xml");
                FileOutputStream os = new FileOutputStream(file);

                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }

                is.close();
                os.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<String> leerDatosDesdeXmlEnMemoriaInterna() {
        ArrayList<String> datosDeXml = new ArrayList<>();

        try {
            // Abre el archivo XML desde la memoria interna
            FileInputStream fis = openFileInput("partners.xml");

            // Crea un XmlPullParser
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(fis, null);

            // Comienza a analizar el XML
            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG && "partner".equals(parser.getName())) {
                    // Comienza un nuevo elemento partner
                    StringBuilder currentData = new StringBuilder();

                    while (!(eventType == XmlPullParser.END_TAG && "partner".equals(parser.getName()))) {
                        // Lee el contenido del socio
                        if (eventType == XmlPullParser.TEXT) {
                            String text = parser.getText();

                            // Agrega el contenido del texto al StringBuilder
                            currentData.append(text).append("\n");
                        }
                        eventType = parser.next();
                    }

                    // Fin del elemento partner, agrega la cadena con el contenido del socio
                    datosDeXml.add(currentData.toString().trim());
                }

                eventType = parser.next();
            }

            fis.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return datosDeXml;
    }

    private void openNuevoPartnerActivity() {
        Intent intent = new Intent(this, nuevo_partner.class);
        startActivityForResult(intent, 1); // Usar startActivityForResult para obtener resultados de la actividad de nuevo_partner
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Actualizar la lista después de regresar de la actividad de nuevo_partner
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Limpiar y volver a leer los datos
            adapter.clear();
            adapter.addAll(leerDatosDesdeXmlEnMemoriaInterna());
            adapter.notifyDataSetChanged();
        }
    }
}
