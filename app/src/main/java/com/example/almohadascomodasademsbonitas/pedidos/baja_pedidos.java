package com.example.almohadascomodasademsbonitas.pedidos;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.almohadascomodasademsbonitas.BBDD.DBconexion;
import com.example.almohadascomodasademsbonitas.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;

public class baja_pedidos extends AppCompatActivity {

    private ArrayAdapter<String> adapter;
    int idPedido;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bajas_pedido);

        //copiarXmlDesdeAssets();

        ListView listView = findViewById(R.id.lvPartners);
        ArrayList<String> datosDeXml = leerDatosDesdeXmlEnMemoriaInterna();

        // Crea un ArrayAdapter para enlazar los datos a la interfaz de usuario
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, datosDeXml);
        listView.setAdapter(adapter);

        Button btnBorrar = findViewById(R.id.btNuevo);
        //Button btRrfs = findViewById(R.id.btRfr2);

        btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SparseBooleanArray checkedItemPositions = listView.getCheckedItemPositions();

                // Iterar sobre las posiciones seleccionadas
                for (int i = 0; i < checkedItemPositions.size(); i++) {
                    int position = checkedItemPositions.keyAt(i);

                    // Verificar si el elemento en esta posición está seleccionado
                    if (checkedItemPositions.valueAt(i)) {

                        idPedido = obtenerIdPedido(datosDeXml.get(position));

                        // Eliminar el elemento de la lista
                        datosDeXml.remove(position);
                        borrarenBBDD();
                    }
                }

                // Notificar al adaptador sobre los cambios
                adapter.notifyDataSetChanged();

                // Opcionalmente, guardar los cambios en el archivo XML
                guardarDatosEnXmlEnMemoriaInterna(datosDeXml);
            }
        });

     /*   btRrfs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> datosDeXml = leerDatosDesdeXmlEnMemoriaInterna();
                adapter.clear();
                adapter.addAll(datosDeXml);
                adapter.notifyDataSetChanged();
            }
        });*/
    }
    private void guardarDatosEnXmlEnMemoriaInterna(ArrayList<String> datosDeXml) {
        try {
            FileOutputStream fos = openFileOutput("pedidos2.xml", MODE_PRIVATE);
            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(fos, "UTF-8");
            serializer.startDocument(null, Boolean.TRUE);
            serializer.startTag(null, "pedidos");

            for (String pedido : datosDeXml) {
                serializer.startTag(null, "pedido");
                serializer.text(pedido);
                serializer.endTag(null, "pedido");
            }

            serializer.endTag(null, "pedidos");
            serializer.endDocument();
            serializer.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para leer datos desde el archivo XML


    private void copiarXmlDesdeAssets() {
        try {
            // Verificar si el archivo ya existe en la memoria interna
            File file = new File(getFilesDir(), "pedidos2.xml");
            if (!file.exists()) {
                // Copiar el archivo desde assets a la memoria interna
                InputStream is = getAssets().open("pedidos.xml");
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
            FileInputStream fis = openFileInput("pedidos2.xml");

            // Crea un XmlPullParser
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(fis, null);

            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG && "pedido".equals(parser.getName())) {
                    // Comienza un nuevo elemento pedido
                    StringBuilder currentData = new StringBuilder();

                    while (!(eventType == XmlPullParser.END_TAG && "pedido".equals(parser.getName()))) {
                        // Lee el contenido del pedido
                        if (eventType == XmlPullParser.TEXT) {
                            String text = parser.getText();

                            // Agrega el contenido del texto al StringBuilder
                            currentData.append(text).append("\n");
                        }
                        eventType = parser.next();
                    }

                    // Fin del elemento pedido, agrega la cadena con el contenido del pedido
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

    // Método para obtener el id_pedido del texto del pedido
    private int obtenerIdPedido(String pedido) {
        try {
            // Crea un XmlPullParser
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(new StringReader(pedido)); // Configura el parser con el texto del pedido

            int eventType = parser.getEventType();
            idPedido = -1; // Valor por defecto si no se encuentra el ID_PEDIDO

            // Recorre el XML hasta encontrar el ID_PEDIDO
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG && "id_pedido".equals(parser.getName())) {
                    // Obtiene el ID_PEDIDO del pedido actual
                    idPedido = Integer.parseInt(parser.nextText());
                    break; // Sale del bucle una vez que se ha encontrado el ID_PEDIDO
                }
                eventType = parser.next();
            }

            return idPedido;
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // Si hay algún error, devuelve -1
        }
    }

public void borrarenBBDD(){
    DBconexion bbdd = new  DBconexion(this,"ACAB2.db",null,1);
    SQLiteDatabase database = bbdd.getWritableDatabase();

    Cursor linea = database.rawQuery("DELETE FROM CAB_PEDIDOS WHERE ID_PEDIDO = "+idPedido,null);
    linea.close();
}
}
