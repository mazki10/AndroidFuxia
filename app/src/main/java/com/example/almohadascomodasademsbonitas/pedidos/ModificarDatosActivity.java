package com.example.almohadascomodasademsbonitas.pedidos;

import android.os.Bundle;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.almohadascomodasademsbonitas.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class ModificarDatosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_datos);
        Bundle extras = getIntent().getExtras();

        // Obtén la posición del pedido seleccionado del intent
        int posicionPedido =extras.getInt("POSICION_PEDIDO");

        ArrayList<String> datosDeXml = leerDatosDesdeXmlEnMemoriaInterna();

        if (posicionPedido != -1 && posicionPedido < datosDeXml.size()) {
            // Aquí puedes usar los datos del pedido en la posición especificada
            String pedidoSeleccionado = datosDeXml.get(posicionPedido);

            // Muestra los datos en los campos de edición o realiza las acciones necesarias
            // (puedes agregar EditText u otros elementos de interfaz de usuario según tus necesidades)
            String[] campos = pedidoSeleccionado.split(";");
            EditText fecha_envio = findViewById(R.id.fecha_envio);

            EditText comercial = findViewById(R.id.comercial);
            EditText nombre = findViewById(R.id.descripcion);
            EditText partner = findViewById(R.id.partner);
            EditText fecha_pedido = findViewById(R.id.fecha_pedido);


            if (campos.length >= 5) { // Verifica si hay al menos 5 campos separados por ";"
                comercial.setText(campos[0]);
                partner.setText(campos[1]);
                nombre.setText(campos[2]);
                fecha_pedido.setText(campos[3]);
                fecha_envio.setText(campos[4]);
            } else {

            }
            // Agrega el listener al botón para guardar cambios
            Button btnGuardarCambios = findViewById(R.id.btnGuardarCambios);
            btnGuardarCambios.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Guarda los cambios en el XML
                    String nuevoPedido = comercial.getText().toString().trim() + ";" +
                            partner.getText().toString().trim() + ";" +
                            nombre.getText().toString().trim()+ ";" +
                            fecha_envio.getText().toString().trim()+";"+
                            fecha_pedido.getText().toString().trim();

                    datosDeXml.set(posicionPedido, nuevoPedido);
                    guardarDatosEnXmlEnMemoriaInterna(datosDeXml);

                    // Puedes mostrar un mensaje de éxito o realizar otras acciones necesarias
                    // Toast.makeText(ModificarDatosActivity.this, "Cambios guardados", Toast.LENGTH_SHORT).show();

                    // Cierra la actividad después de guardar los cambios
                    finish();
                }
            });
        } else {
            // Manejo de error si la posición del pedido no es válida
            // Toast.makeText(this, "Error: posición de pedido no válida", Toast.LENGTH_SHORT).show();
            finish(); // Cierra la actividad si hay un error
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
}
