package com.example.almohadascomodasademsbonitas.partners;



import android.content.Intent;
import android.os.Bundle;
import android.util.Xml;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.almohadascomodasademsbonitas.Enviar;
import com.example.almohadascomodasademsbonitas.R;

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

        crearXmlEnMemoriaInterna();

        ListView listView = findViewById(R.id.lvPartners);
        ArrayList<String> datosDeXml = leerDatosDesdeXmlEnMemoriaInterna();

        // Agrega un listener al ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Obtiene el valor del elemento en la posición seleccionada
                String selectedItem = (String) parent.getItemAtPosition(position);

                // Extraer la primera fila y el primer número
                String[] lines = selectedItem.split("\n");
                if (lines.length > 0) {
                    // Obtener la primera fila
                    String primeraFila = lines[0];

                    // Extraer el primer número
                    String[] parts = primeraFila.split(" ");
                    if (parts.length > 0) {
                        String idPartner = parts[0];
                        openModificarPartnerActivity(idPartner  );
                    }
                }
            }
        });


        // Crea un ArrayAdapter para enlazar los datos a la interfaz de usuario
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, datosDeXml);
        listView.setAdapter(adapter);

        Button btNuevo = findViewById(R.id.btNuevo);

        btNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNuevoPartnerActivity();
            }
        });


    }
    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<String> datosDeXml = leerDatosDesdeXmlEnMemoriaInterna();
        adapter.clear();
        adapter.addAll(datosDeXml);
        adapter.notifyDataSetChanged();
        // Realizar acciones cuando la actividad recupera el foco
    }

    private void crearXmlEnMemoriaInterna() {
        try {
            // Verificar si el archivo ya existe en la memoria interna
            File file = new File(getFilesDir(), "partners.xml");

            if (!file.exists()) {
                // Crear un nuevo archivo XML en la memoria interna
                FileOutputStream os = openFileOutput("partners.xml", MODE_PRIVATE);

                // Escribir la primera línea y la etiqueta <partners>
                String inicioXml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<partners>\n</partners>";
                os.write(inicioXml.getBytes());

                // Cerrar el archivo
                os.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private ArrayList<String> leerDatosDesdeXmlEnMemoriaInterna() {
        ArrayList<String> datosDeXml = new ArrayList<>();

        try {
            FileInputStream fis = openFileInput("partners.xml");

            // Crea un XmlPullParser
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(fis, null);

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
        this.startActivity(intent);
    }
    private void openModificarPartnerActivity(String idPartner) {
        Intent intent = new Intent(this, modificar_partner.class);

        // Envía el ID del socio a la actividad modificar_partner
        intent.putExtra("_partner", idPartner);

        startActivity(intent);
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