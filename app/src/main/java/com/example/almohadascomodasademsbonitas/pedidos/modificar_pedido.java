package com.example.almohadascomodasademsbonitas.pedidos;

import android.content.Intent;
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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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

public class modificar_pedido extends AppCompatActivity {

    private ArrayAdapter<String> adapter;
    String pedidoActualizado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modificar_pedido);

        //copiarXmlDesdeAssets();

        ListView listView = findViewById(R.id.lvPartners);
        ArrayList<String> datosDeXml = leerDatosDesdeXmlEnMemoriaInterna();

        // Crea un ArrayAdapter para enlazar los datos a la interfaz de usuario
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, datosDeXml);
        listView.setAdapter(adapter);

        Button btnBorrar = findViewById(R.id.btnModificar);
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
                        // Abre la actividad para modificar los datos del pedido seleccionado
                        abrirModificarDatosActivity(position);

                        // Opcionalmente, puedes eliminar el elemento de la lista aquí
                        // datosDeXml.remove(position);
                    }
                }

                guardarDatosEnXmlEnMemoriaInterna(leerDatosDesdeXmlEnMemoriaInterna());

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

    private void abrirModificarDatosActivity(int position) {
        // Obtener los datos del pedido seleccionado
        String pedidoSeleccionado = adapter.getItem(position);

        // Obtener la fecha y el precio final del pedido seleccionado
        String fechaPedido = obtenerFechaPedido(pedidoSeleccionado);
        double precioFinal = obtenerPrecioFinal(pedidoSeleccionado);

        // Crear un Intent para abrir la nueva actividad
        Intent intent = new Intent(modificar_pedido.this, ModificarDatosActivity.class);

        // Agrega la posición del pedido seleccionado como extra en el intent
        intent.putExtra("POSICION_PEDIDO", position);
        // Agrega los datos del pedido seleccionado como extra en el intent
        intent.putExtra("PEDIDO_SELECCIONADO", pedidoSeleccionado);
        // Agrega la fecha como extra en el intent
        intent.putExtra("FECHA_PEDIDO", fechaPedido);
        // Agrega el precio final como extra en el intent
        intent.putExtra("PRECIO_FINAL", precioFinal);

        // Inicia la nueva actividad
        startActivity(intent);
    }

    // Método para obtener la fecha del pedido a partir de su representación como cadena
    private String obtenerFechaPedido(String pedido) {
        try {
            // Crear un XmlPullParser
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(new StringReader(pedido));

            // Variable para almacenar la fecha del pedido
            String fechaPedido = "";

            // Iterar sobre el XML
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG && "fecha".equals(parser.getName())) {
                    // Se encontró la etiqueta <FECHA_PEDIDO>, leer su valor
                    parser.next();
                    fechaPedido = parser.getText();
                    break;  // Salir del bucle una vez que se haya encontrado la fecha del pedido
                }
                eventType = parser.next();
            }

            return fechaPedido;
        } catch (Exception e) {
            e.printStackTrace();
            return ""; // Devolver una cadena vacía en caso de error
        }
    }


    // Método para obtener el precio final del pedido a partir de su representación como cadena
    private double obtenerPrecioFinal(String pedido) {
        try {
            // Crear un XmlPullParser
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(new StringReader(pedido));

            // Variable para almacenar el precio final del pedido
            double precioFinal = 0.0;

            // Iterar sobre el XML
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG && "precio_total".equals(parser.getName())) {
                    // Se encontró la etiqueta <PRECIO_TOT>, leer su valor y convertirlo a double
                    parser.next();
                    precioFinal = Double.parseDouble(parser.getText());
                    break;  // Salir del bucle una vez que se haya encontrado el precio final
                }
                eventType = parser.next();
            }

            return precioFinal;
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0; // Devolver 0.0 en caso de error
        }
    }


    private void guardarDatosEnXmlEnMemoriaInterna(ArrayList<String> datosDeXml) {
        try {
            FileOutputStream fos = openFileOutput("pedidos.xml", MODE_PRIVATE);
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
            File file = new File(getFilesDir(), "pedidos.xml");
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
            FileInputStream fis = openFileInput("pedidos.xml");

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                if (data != null && data.hasExtra("PEDIDO_ACTUALIZADO")) {
                     pedidoActualizado = data.getStringExtra("PEDIDO_ACTUALIZADO");
                    // Aquí puedes hacer lo que necesites con el pedido actualizado
                    // Por ejemplo, puedes mostrarlo en un Toast
                    Toast.makeText(this, "Pedido Actualizado: " + pedidoActualizado, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
