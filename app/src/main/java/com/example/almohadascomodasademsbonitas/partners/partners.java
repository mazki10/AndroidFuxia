package com.example.almohadascomodasademsbonitas.partners;



import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Xml;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.almohadascomodasademsbonitas.BBDD.DBconexion;
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
    private DBconexion dbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.partners);
        crearXmlEnMemoriaInterna();
        dbHelper = new DBconexion(this, "ACAB2.db", null, 1);
        db = dbHelper.getWritableDatabase();
        ListView listView = findViewById(R.id.lvPartners);
        ArrayList<String> datosDeXmlSinFormato = leerDatosDesdeXmlEnMemoriaInterna();
        ArrayList<String> datosDeXml = generarFormatoListView(datosDeXmlSinFormato); // Obtener datos sin formato

        insertarDesdeXML(datosDeXmlSinFormato);
        // Agrega un listener al ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Obtiene el valor del elemento en la posición seleccionada
                String selectedItem = (String) parent.getItemAtPosition(position);

                // Extraer la ID del socio
                String[] lines = selectedItem.split("\n");
                if (lines.length > 0) {
                    String idPartner = lines[0];
                    openModificarPartnerActivity(idPartner);
                }
            }
        });

        // Crea un ArrayAdapter para enlazar los datos a la interfaz de usuario
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, datosDeXml); // Inicializa el adaptador
        listView.setAdapter(adapter); // Configura el adaptador en el ListView

        Button btNuevo = findViewById(R.id.btNuevo);

        btNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNuevoPartnerActivity();
            }
        });
    }
    private void insertarDesdeXML(ArrayList<String> datosDeXmlSinFormato){
        ArrayList<Partner> partners = new ArrayList<>();
        for (int i = 0; i < datosDeXmlSinFormato.size(); i+=9){
            if(!existeIdPartnerEnBaseDeDatos(datosDeXmlSinFormato.get(i))){
                partners.add(new Partner(Integer.parseInt(datosDeXmlSinFormato.get(i)) ,
                        datosDeXmlSinFormato.get(i+1),datosDeXmlSinFormato.get(i+2),datosDeXmlSinFormato.get(i+3),
                        Integer.parseInt(datosDeXmlSinFormato.get(i+4)),(datosDeXmlSinFormato.get(i+5)),
                        datosDeXmlSinFormato.get(i+6),Integer.parseInt(datosDeXmlSinFormato.get(i+7)) ,
                        datosDeXmlSinFormato.get(i+8)));
            }
        }
        for (int i = 0; i < partners.size();i++){
            String insertQuery = "INSERT INTO PARTNERS (ID_PARTNER, NOMBRE, CIF, DIRECCION, TELEFONO, COMERCIAL, EMAIL, ZONA, FECHA) " +
                    "VALUES (" + partners.get(i).get_id() + ", '" + partners.get(i).get_nombre() + "', '" +
                    partners.get(i).get_cif() + "', '" + partners.get(i).get_direccion() + "', " +
                    partners.get(i).get_telefono() + ", '" + partners.get(i).get_comercial() + "', '" +
                    partners.get(i).get_email() + "', " + partners.get(i).get_zona() + ", '" +
                    partners.get(i).get_fecha() + "')";

            db.execSQL(insertQuery);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        dbHelper = new DBconexion(this, "ACAB2.db", null, 1);
        db = dbHelper.getWritableDatabase();
        ArrayList<String> datosDeXmlSinFormato = leerDatosDesdeXmlEnMemoriaInterna();
        ArrayList<String> datosDeXml = generarFormatoListView(datosDeXmlSinFormato);
        insertarDesdeXML(datosDeXmlSinFormato);
        adapter.clear();
        adapter.addAll(datosDeXml);
        adapter.notifyDataSetChanged();
    }

    private void crearXmlEnMemoriaInterna() {
        try {
            File file = new File(getFilesDir(), "partners.xml");

            if (!file.exists()) {
                // Crear un nuevo archivo XML en la memoria interna
                FileOutputStream os = openFileOutput("partners.xml", MODE_PRIVATE);

                // Escribir la primera línea y la etiqueta <partners>
                String inicioXml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                                    "<partners>\n" +
                                    "</partners>";
                os.write(inicioXml.getBytes());
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

                    while (!(eventType == XmlPullParser.END_TAG && "partner".equals(parser.getName()))) {
                        if (eventType == XmlPullParser.START_TAG && !parser.getName().equals("partner")) {
                            // Lee el contenido del tag
                            String text = parser.nextText().trim();

                            // Agrega el contenido del tag al ArrayList
                            datosDeXml.add(text);
                        }
                        eventType = parser.next();
                    }
                }

                eventType = parser.next();
            }

            fis.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return datosDeXml;
    }
    private ArrayList<String> generarFormatoListView(ArrayList<String> datos) {
        ArrayList<String> formatoListView = new ArrayList<>();

        // Procesar los datos en bloques de 9 elementos
        int dataSize = datos.size();
        for (int i = 0; i + 8 < dataSize; i += 9) {
            StringBuilder blockData = new StringBuilder();
            blockData.append(datos.get(i)).append("\n")
                    .append("Nombre: ").append(datos.get(i + 1)).append("\n")
                    .append("CIF: ").append(datos.get(i + 2)).append("\n")
                    .append("Direccion: ").append(datos.get(i + 3)).append("\n")
                    .append("Telefono: ").append(datos.get(i + 4)).append("\n")
                    .append("Comercial: ").append(datos.get(i + 5)).append("\n")
                    .append("Email: ").append(datos.get(i + 6)).append("\n")
                    .append("Zona: ").append(datos.get(i + 7)).append("\n")
                    .append("Fecha de Registro: ").append(datos.get(i + 8)).append("\n");


            formatoListView.add(blockData.toString());
        }

        return formatoListView;
    }
    private boolean existeIdPartnerEnBaseDeDatos(String idPartner) {
        db = dbHelper.getWritableDatabase();
        String query = "SELECT COUNT(*) FROM PARTNERS WHERE ID_PARTNER = ?";
        String[] selectionArgs = {idPartner};
        try {
            // Ejecutar la consulta y obtener el resultado
            Cursor cursor = db.rawQuery(query, selectionArgs);
            if (cursor.moveToFirst()) {
                int count = cursor.getInt(0);
                cursor.close();
                return count > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // Si hay algún error o no se encuentra el id_partner, retornar falso
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