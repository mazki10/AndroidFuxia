package com.example.almohadascomodasademsbonitas.pedidos;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.almohadascomodasademsbonitas.BBDD.DBconexion;
import com.example.almohadascomodasademsbonitas.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class modificar_pedido extends AppCompatActivity {

    private ArrayAdapter<String> adapter;
    int id_Pedido;
    ArrayList<String> datosDeXml;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modificar_pedido);

        // Copia el archivo XML desde assets a la memoria interna
      //  copiarXmlDesdeAssets();

        ListView listView = findViewById(R.id.lvPartners);
        //datosDeXml = leerDatosDesdeXmlEnMemoriaInterna();

        // Crea un ArrayAdapter para enlazar los datos a la interfaz de usuario

        int cont = contPedidos();

        if (cont!=0){
            datosDeXml=mostrarDatosBBDD();
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, datosDeXml);
            listView.setAdapter(adapter);
        }







        // Agregar el OnItemClickListener aquí
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Obtiene el valor del elemento en la posición seleccionada
                String selectedItem = (String) parent.getItemAtPosition(position);

                // Extraer la ID del socio
                String[] lines = selectedItem.split("\n");
                if (lines.length > 0) {
                    String idPartner = lines[0];

                    String idPedidoStr = idPartner.split(":")[1].trim();
                    int idPedido = Integer.parseInt(idPedidoStr);
                 //  String descripcionPedido = lines[11]; // Suponiendo que la descripción está en la segunda línea

                    String Nom_comercial = lines[1].split(":")[1].trim();
                    String Nom_partner = lines[2].split(":")[1].trim();
                    String descripcion = lines[3].split(":")[1].trim();

                    String precioTotalStr = lines[lines.length-1].split(":")[1].trim();
                    double precio_total = Double.parseDouble(precioTotalStr);

                 //   ArrayList<Producto> productos = obtenerProductosDelPedido(Integer.parseInt(idPedidoStr));

                    // Crear un Intent para iniciar la otra actividad (ModificarDatosActivity)
                    Intent intent = new Intent(modificar_pedido.this, ModificarDatosActivity.class);

                    // Agregar los datos del pedido como extras al Intent
                    intent.putExtra("ID_PEDIDO", idPedido);
                    intent.putExtra("COMERCIAL", Nom_comercial);
                    intent.putExtra("PARTNER", Nom_partner);
                    intent.putExtra("DESCRIPCION", descripcion);
                    intent.putExtra("PRECIO_TOTAL", precio_total);

                  //  intent.putExtra("CANTIDAD", Double.parseDouble(cantidad));

                    // Iniciar la otra actividad
                    startActivity(intent);
                }
            }
        });
        Button btnModificar = findViewById(R.id.btnModificar);

        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí obtén el ID del pedido seleccionado, por ejemplo:
                int idPedidoSeleccionado = obtenerIdPedidoSeleccionadoDesdeXml(listView);
                String descripcionPedidoSeleccionado = obtenerDescripcionPedidoSeleccionadoDesdeXml(listView);

                // Crear un Intent para iniciar la otra actividad (ModificarDatosActivity)
                Intent intent = new Intent(modificar_pedido.this, ModificarDatosActivity.class);

                // Agregar el ID del pedido como extra al Intent
                intent.putExtra("ID_PEDIDO", idPedidoSeleccionado);
                intent.putExtra("DESCRIPCION_PEDIDO", descripcionPedidoSeleccionado);

                // Iniciar la otra actividad
                startActivity(intent);
            }
        });

    }
    // Método para abrir la actividad de modificar socio
    // Método para abrir la actividad de modificar pedido
    private void openModificarPedidosActivity(int idPedido) {
        // Crear un Intent para iniciar la actividad ModificarPedidoActivity
        Intent intent = new Intent(modificar_pedido.this, ModificarDatosActivity.class);
        // Agregar el ID del pedido como extra al Intent

        intent.putExtra("ID_PEDIDO", idPedido);
        // Iniciar la actividad
        startActivity(intent);
    }


    private ArrayList<Producto> obtenerProductosDelPedido(int idPedido) {
        ArrayList<Producto> productos = new ArrayList<>();

        try {
            // Obtener el archivo XML de pedidos
            File file = new File(getFilesDir(), "pedidos.xml");
            FileInputStream fis = new FileInputStream(file);

            // Crear un XmlPullParser
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(fis, null);

            // Avanzar hasta el nodo del pedido deseado
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG && "pedido".equals(parser.getName())) {
                    // Obtener el ID del pedido actual
                    int idPedidoActual = Integer.parseInt(parser.getAttributeValue(null, "id_pedido"));

                    // Verificar si este es el pedido que estamos buscando
                    if (idPedidoActual == idPedido) {
                        // Avanzar al nodo de productos
                        while (eventType != XmlPullParser.END_DOCUMENT) {
                            if (eventType == XmlPullParser.START_TAG && "producto".equals(parser.getName())) {
                                // Leer los detalles del producto
                                String idArticulo = "";
                                int cantidad = 0;
                                double precioUnitario = 0.0;

                                while (!(eventType == XmlPullParser.END_TAG && "producto".equals(parser.getName()))) {
                                    if (eventType == XmlPullParser.START_TAG) {
                                        if ("id_articulo".equals(parser.getName())) {
                                            idArticulo = parser.nextText();
                                        } else if ("cantidad".equals(parser.getName())) {
                                            cantidad = Integer.parseInt(parser.nextText());
                                        } else if ("precio_un".equals(parser.getName())) {
                                            precioUnitario = Double.parseDouble(parser.nextText());
                                        }
                                    }
                                    eventType = parser.next();
                                }

                                // Agregar el producto a la lista
                                productos.add(new Producto(idArticulo, cantidad, precioUnitario));
                            }
                            eventType = parser.next();
                        }
                    }
                }
                eventType = parser.next();
            }

            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return productos;
    }


    public int contPedidos(){
        int cont=0;


        DBconexion  dbHelper = new DBconexion(this, "ACAB2.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String consulta = "SELECT COUNT(ID_PEDIDO) FROM CAB_PEDIDOS";
        Cursor cursor = db.rawQuery(consulta,null);

        if (cursor.moveToFirst()) {
            cont = cursor.getInt(0);
        }

            return cont;
    }


    public ArrayList<String> mostrarDatosBBDD() {
        ArrayList<String> datos = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        DBconexion dbHelper = new DBconexion(this, "ACAB2.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor linea = db.rawQuery("SELECT \n" +
                "    CAB_PEDIDOS.ID_PEDIDO, \n" +
                "    COMERCIALES.NOMBRE AS Nombre_Comercial, \n" +
                "    PARTNERS.NOMBRE AS Nombre_Partner, \n" +
                "    DESCRIPCION, \n" +
                "    SUM(LIN_PEDIDOS.CANTIDAD) AS Cantidad_Total, \n" +
                "    AVG(LIN_PEDIDOS.PRECIO_UN) AS Precio_Unitario_Promedio, \n" +
                "    MAX(CAB_PEDIDOS.FECHA_PEDIDO) AS Fecha_Pedido, \n" +
                "    SUM(LIN_PEDIDOS.PRECIO_TOTAL) AS Precio_Total \n" +
                "FROM \n" +
                "    PARTNERS \n" +
                "JOIN \n" +
                "    CAB_PEDIDOS ON PARTNERS.ID_PARTNER = CAB_PEDIDOS.ID_PARTNER \n" +
                "JOIN \n" +
                "    COMERCIALES ON COMERCIALES.DNI = CAB_PEDIDOS.ID_COMERCIAL \n" +
                "JOIN \n" +
                "    LIN_PEDIDOS ON CAB_PEDIDOS.ID_PEDIDO = LIN_PEDIDOS.ID_PEDIDO \n" +
                "GROUP BY \n" +
                "    CAB_PEDIDOS.ID_PEDIDO, \n" +
                "    COMERCIALES.NOMBRE, \n" +
                "    PARTNERS.NOMBRE, \n" +
                "    DESCRIPCION;", null);

        while (linea.moveToNext()) {
            int id_pedido = linea.getInt(0);
            String nombreComercial = linea.getString(1);
            String nombrepartner = linea.getString(2);
            String descripcion = linea.getString(3);
            int cantidadPedidos = linea.getInt(4);
            double precio_un = linea.getDouble(5);

            String fechaStr = linea.getString(6); // Aquí obtenemos la fecha como cadena de texto
            LocalDate fecha = null;
            if (fechaStr != null && !fechaStr.isEmpty()) {
                try {
                    fecha = LocalDate.parse(fechaStr.substring(0, 4), formatter); // Tomamos los primeros 10 caracteres para la fecha
                } catch (DateTimeParseException e) {
                    // Manejar el caso en que la fecha no se pueda analizar correctamente
                    e.printStackTrace(); // Otra opción: mostrar un mensaje de error al usuario
                }
            }


            double precio_total = linea.getDouble(7);

            // Construye la cadena de texto para agregar a la lista
            StringBuilder textBuilder = new StringBuilder();
            textBuilder.append("Id_Pedido:").append(id_pedido).append("\n")
                    .append("Comercial:").append(nombreComercial).append("\n")
                    .append("Partner:").append(nombrepartner).append("\n")
                    .append("Descripcion:").append(descripcion).append("\n")
                    .append("Cantidad:").append(cantidadPedidos).append("\n")
                    .append("Precio Unitario:").append(precio_un).append("\n");
            if (fecha != null) {
                textBuilder.append("Fecha:").append(fecha).append("\n");
            }
            textBuilder.append("Precio Total:").append(precio_total).append("\n");

            datos.add(textBuilder.toString());
        }

        return datos;
    }




   /*  @Override
    protected void onResume() {
        super.onResume();

         ListView listView = findViewById(R.id.lvPartners);
         //datosDeXml = leerDatosDesdeXmlEnMemoriaInterna();

         // Crea un ArrayAdapter para enlazar los datos a la interfaz de usuario

         int cont = contPedidos();

         if (cont!=0){
             datosDeXml=mostrarDatosBBDD();
             adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, datosDeXml);
             listView.setAdapter(adapter);
         }



         // Agregar el OnItemClickListener aquí
         listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 // Obtiene el valor del elemento en la posición seleccionada
                 String selectedItem = (String) parent.getItemAtPosition(position);

                 // Extraer la ID del socio
                 String[] lines = selectedItem.split("\n");
                 if (lines.length > 0) {
                     String idPartner = lines[0];

                     String idPedidoStr = idPartner.split(":")[1].trim();
                     int idPedido = Integer.parseInt(idPedidoStr);
                     //  String descripcionPedido = lines[11]; // Suponiendo que la descripción está en la segunda línea

                     String Nom_comercial = lines[1].split(":")[1].trim();
                     String Nom_partner = lines[2].split(":")[1].trim();
                     String descripcion = lines[3].split(":")[1].trim();

                     String precioTotalStr = lines[lines.length-1].split(":")[1].trim();
                     double precio_total = Double.parseDouble(precioTotalStr);

                     //   ArrayList<Producto> productos = obtenerProductosDelPedido(Integer.parseInt(idPedidoStr));

                     // Crear un Intent para iniciar la otra actividad (ModificarDatosActivity)
                     Intent intent = new Intent(modificar_pedido.this, ModificarDatosActivity.class);

                     // Agregar los datos del pedido como extras al Intent
                     intent.putExtra("ID_PEDIDO", idPedido);
                     intent.putExtra("COMERCIAL", Nom_comercial);
                     intent.putExtra("PARTNER", Nom_partner);
                     intent.putExtra("DESCRIPCION", descripcion);
                     intent.putExtra("PRECIO_TOTAL", precio_total);

                     //  intent.putExtra("CANTIDAD", Double.parseDouble(cantidad));

                     // Iniciar la otra actividad
                     startActivity(intent);
                 }
             }
         });
         Button btnModificar = findViewById(R.id.btnModificar);

         btnModificar.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 // Aquí obtén el ID del pedido seleccionado, por ejemplo:
                 int idPedidoSeleccionado = obtenerIdPedidoSeleccionadoDesdeXml(listView);
                 String descripcionPedidoSeleccionado = obtenerDescripcionPedidoSeleccionadoDesdeXml(listView);

                 // Crear un Intent para iniciar la otra actividad (ModificarDatosActivity)
                 Intent intent = new Intent(modificar_pedido.this, ModificarDatosActivity.class);

                 // Agregar el ID del pedido como extra al Intent
                 intent.putExtra("ID_PEDIDO", idPedidoSeleccionado);
                 intent.putExtra("DESCRIPCION_PEDIDO", descripcionPedidoSeleccionado);

                 // Iniciar la otra actividad
                 startActivity(intent);
             }
         });
    }*/

    private void beginXMLparsingPedidos() {
        try {
            File file = new File(getFilesDir(), "pedidos.xml");

            // Verificar si el archivo existe antes de intentar leerlo
            if (file.exists()) {
                InputStream is = new FileInputStream(file);

                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                Document doc = documentBuilder.parse(is);
                doc.getDocumentElement().normalize();

                NodeList nodeList = doc.getElementsByTagName("pedido");

                // Iterar sobre cada elemento <pedido> en el archivo XML
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);

                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;

                        // Extraer el ID del pedido
                        String idPedido = element.getElementsByTagName("id_pedido").item(0).getTextContent();
                        // Utiliza el ID del pedido según tus necesidades
                        Log.d("XML Parsing", "ID del pedido: " + idPedido);
                    }
                }
            } else {
                Log.e("XML Parsing", "El archivo XML 'pedidos.xml' no existe");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }

    // Método para obtener la descripción del pedido seleccionado desde el XML
    private String obtenerDescripcionPedidoSeleccionadoDesdeXml(ListView listView) {
        String descripcionPedidoSeleccionado = "";

        // Obtener el índice del elemento seleccionado en el ListView
        int position = listView.getCheckedItemPosition();

        // Verificar si se ha seleccionado un elemento
        if (position != ListView.INVALID_POSITION) {
            // Obtener la lista de datos desde el XML
            ArrayList<String> datosDeXml = leerDatosDesdeXmlEnMemoriaInterna();

            // Obtener el pedido seleccionado
            descripcionPedidoSeleccionado = datosDeXml.get(position);

            // Puedes ajustar esta lógica según la estructura real de tus datos en el XML
        }

        return descripcionPedidoSeleccionado;
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
            File file = new File(getFilesDir(), "pedidos2.xml");
            FileInputStream fis = new FileInputStream(file);
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