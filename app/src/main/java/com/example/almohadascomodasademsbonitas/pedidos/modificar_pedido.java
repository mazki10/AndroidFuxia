package com.example.almohadascomodasademsbonitas.pedidos;

import android.content.Intent;
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
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;

public class modificar_pedido extends AppCompatActivity {

    private ArrayAdapter<String> adapter;
    int idPedido;
    ArrayList<String> datosDeXml;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modificar_pedido);

        // Copia el archivo XML desde assets a la memoria interna
        copiarXmlDesdeAssets();

        ListView listView = findViewById(R.id.lvPartners);
        datosDeXml = leerDatosDesdeXmlEnMemoriaInterna();

        // Crea un ArrayAdapter para enlazar los datos a la interfaz de usuario
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, datosDeXml);
        listView.setAdapter(adapter);

        Button btnModificar = findViewById(R.id.btnModificar);

        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí obtén el ID del pedido seleccionado, por ejemplo:
                int idPedidoSeleccionado = obtenerIdPedidoSeleccionadoDesdeXml(listView);

                // Crear un Intent para iniciar la otra actividad (ModificarDatosActivity)
                Intent intent = new Intent(modificar_pedido.this, ModificarDatosActivity.class);

                // Agregar el ID del pedido como extra al Intent
                intent.putExtra("ID_PEDIDO", idPedidoSeleccionado);

                // Iniciar la otra actividad
                startActivity(intent);
            }
        });



    }

    private int obtenerIdPedidoSeleccionadoDesdeXml(ListView listView) {
        int idPedidoSeleccionado = -1; // Valor predeterminado si no se selecciona ningún elemento

        // Obtener el índice del elemento seleccionado en el ListView
        int position = listView.getCheckedItemPosition();

        // Verificar si se ha seleccionado un elemento
        if (position != ListView.INVALID_POSITION) {
            // Obtener la lista de datos desde el XML
            ArrayList<String> datosDeXml = leerDatosDesdeXmlEnMemoriaInterna();

            // Obtener el pedido seleccionado
            String pedidoSeleccionado = datosDeXml.get(position);

            // Analizar el pedido para extraer el ID (suponiendo que el ID está en la primera posición)
            String[] partes = pedidoSeleccionado.split(" "); // Suponiendo que los datos están separados por espacios
            idPedidoSeleccionado = Integer.parseInt(partes[0]); // Suponiendo que el ID es el primer elemento

            // Puedes ajustar esta lógica según la estructura real de tus datos en el XML
        }

        return idPedidoSeleccionado;
    }
    @Override
    protected void onResume() {
        super.onResume();

        // Leer datos desde el archivo XML y actualizar la lista
        datosDeXml = leerDatosDesdeXmlEnMemoriaInterna();
        adapter.clear();
        adapter.addAll(datosDeXml);
        adapter.notifyDataSetChanged();
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

    private void copiarXmlDesdeAssets() {
        try {
            // Verificar si el archivo ya existe en la memoria interna
            File file = new File(getFilesDir(), "pedidos2.xml");
            if (!file.exists()) {
                // Copiar el archivo desde assets a la memoria interna
                InputStream is = getAssets().open("pedidos2.xml");
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


    private void editarPedidoEnXml(int idPedido, int nuevoIdPartner, String nuevoIdComercial) {
        try {
            // Obtener el archivo XML
            File xmlFile = new File(getFilesDir(), "pedidos2.xml");
            if (!xmlFile.exists()) {
                // Si el archivo no existe, salir de la función
                return;
            }

            // Crear un DocumentBuilder para analizar el archivo XML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);

            // Obtener la lista de nodos 'pedido'
            NodeList pedidoNodes = document.getElementsByTagName("pedido");

            // Iterar sobre los nodos 'pedido'
            for (int i = 0; i < pedidoNodes.getLength(); i++) {
                Element pedidoElement = (Element) pedidoNodes.item(i);

                // Obtener el atributo 'id' del pedido
                int idPedidoActual = Integer.parseInt(pedidoElement.getAttribute("id"));

                // Verificar si este es el pedido que deseas editar
                if (idPedidoActual == idPedido) {
                    // Obtener las etiquetas id_partner e id_comercial dentro del pedido
                    NodeList idPartnerNodes = pedidoElement.getElementsByTagName("id_partner");
                    NodeList idComercialNodes = pedidoElement.getElementsByTagName("id_comercial");

                    // Verificar si hay exactamente un nodo id_partner y un nodo id_comercial en el pedido
                    if (idPartnerNodes.getLength() == 1 && idComercialNodes.getLength() == 1) {
                        Element idPartnerElement = (Element) idPartnerNodes.item(0);
                        Element idComercialElement = (Element) idComercialNodes.item(0);

                        // Modificar los valores de las etiquetas id_partner e id_comercial
                        idPartnerElement.setTextContent(String.valueOf(nuevoIdPartner));
                        idComercialElement.setTextContent(String.valueOf(nuevoIdComercial));

                        // Guardar los cambios en el archivo XML
                        TransformerFactory transformerFactory = TransformerFactory.newInstance();
                        Transformer transformer = transformerFactory.newTransformer();
                        DOMSource source = new DOMSource(document);
                        StreamResult result = new StreamResult(xmlFile);
                        transformer.transform(source, result);

                        // Salir del bucle, ya que hemos encontrado y editado el pedido
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
