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

        // Obtén la posición del pedido seleccionado del intent
        int posicionPedido = getIntent().getIntExtra("POSICION_PEDIDO", -1);

        ArrayList<String> datosDeXml = leerDatosDesdeXmlEnMemoriaInterna();

        if (posicionPedido != -1 && posicionPedido < datosDeXml.size()) {
            // Aquí puedes usar los datos del pedido en la posición especificada
            String pedidoSeleccionado = datosDeXml.get(posicionPedido);

            // Muestra los datos en los campos de edición o realiza las acciones necesarias
            // (puedes agregar EditText u otros elementos de interfaz de usuario según tus necesidades)
            String[] campos = pedidoSeleccionado.split(";"); // Supongamos que los campos están separados por ";"

            EditText editTextNombrePedido = findViewById(R.id.editTextNombrePedido);
            editTextNombrePedido.setText(campos[0]); // Suponiendo que el nombre está en el primer campo

            EditText comercial = findViewById(R.id.comercial);
            comercial.setText(campos[1]); // Suponiendo que el otro campo 1 está en el segundo campo

            EditText descuento = findViewById(R.id.descuento);
            descuento.setText(campos[2]); // Suponiendo que el otro campo 1 está en el segundo campo

            EditText cantidad = findViewById(R.id.cantidad);
            cantidad.setText(campos[3]); // Suponiendo que el otro campo 2 está en el tercer campo

            // Agrega el listener al botón para guardar cambios
            Button btnGuardarCambios = findViewById(R.id.btnGuardarCambios);
            btnGuardarCambios.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Guarda los cambios en el XML
                    String nuevoPedido = editTextNombrePedido.getText().toString().trim() + ";" +
                            descuento.getText().toString().trim() + ";" +
                            cantidad.getText().toString().trim();

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
