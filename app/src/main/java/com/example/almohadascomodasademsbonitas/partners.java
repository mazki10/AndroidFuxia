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

import java.io.InputStream;
import java.util.ArrayList;

public class partners extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.partners);

        // Obtén una referencia al ListView en tu diseño
        ListView listView = findViewById(R.id.lvPartners);

        // Crea una lista para almacenar los datos del archivo XML
        ArrayList<String> datosDeXml = leerDatosDesdeXml();

        // Crea un ArrayAdapter para enlazar los datos a la interfaz de usuario
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, datosDeXml);

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

    private ArrayList<String> leerDatosDesdeXml() {
        ArrayList<String> datosDeXml = new ArrayList<>();

        try {
            // Abre el archivo XML desde la carpeta assets (ajusta la ruta según tu estructura)
            InputStream is = getAssets().open("partners.xml");

            // Crea un XmlPullParser
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is, null);

            // Comienza a analizar el XML
            int eventType = parser.getEventType();
            StringBuilder currentData = new StringBuilder();
            boolean inPartner = false;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    if ("partner".equals(parser.getName())) {
                        // Comienza un nuevo elemento partner, inicia el StringBuilder y marca inPartner como true
                        currentData = new StringBuilder();
                        inPartner = true;
                    }
                } else if (eventType == XmlPullParser.TEXT) {
                    // Lee el texto entre las etiquetas y agrega al StringBuilder
                    currentData.append(parser.getText());
                } else if (eventType == XmlPullParser.END_TAG) {
                    if ("partner".equals(parser.getName())) {
                        // Fin del elemento partner, agrega la cadena con toda la información
                        datosDeXml.add(currentData.toString().trim());
                        // Marca inPartner como false
                        inPartner = false;
                    }
                }

                eventType = parser.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return datosDeXml;
    }





    private void openNuevoPartnerActivity() {
        Intent intent = new Intent(this, nuevo_partner.class);
        startActivity(intent);
    }

}
