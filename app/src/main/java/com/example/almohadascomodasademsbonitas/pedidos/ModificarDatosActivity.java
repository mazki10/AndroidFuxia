package com.example.almohadascomodasademsbonitas.pedidos;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.almohadascomodasademsbonitas.BBDD.DBconexion;
import com.example.almohadascomodasademsbonitas.MainActivity;
import com.example.almohadascomodasademsbonitas.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class ModificarDatosActivity extends AppCompatActivity {

    private EditText editTextComercial;
    private EditText editTextPartner;
     EditText editTextDescripcion;
    private EditText editTextFechaPedido;
    private EditText editTextFechaEnvio;
    private Button btnGuardarCambios;
    String comercial;
    String partner;
    String fecha_envio;
    String fecha_pedido;
    int idPedido;
    String descripcionPedido;
    double precioUnitario;
    double precioTotal;
    double cantidad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_datos);

        // Obtener referencias a los elementos de interfaz de usuario en tu diseño
        editTextComercial = findViewById(R.id.comercial);
        editTextPartner = findViewById(R.id.partner);
        editTextFechaPedido = findViewById(R.id.fecha_pedido);
        editTextFechaEnvio = findViewById(R.id.fecha_envio);
        btnGuardarCambios = findViewById(R.id.btnGuardarCambios);

        // Obtener los datos enviados desde modificar_pedido
        idPedido = getIntent().getIntExtra("ID_PEDIDO", -1);
        descripcionPedido = getIntent().getStringExtra("DESCRIPCION_PEDIDO");
         precioUnitario = getIntent().getDoubleExtra("PRECIO_UNITARIO", 0.0);
         precioTotal = getIntent().getDoubleExtra("PRECIO_TOTAL", 0.0);
        cantidad = getIntent().getDoubleExtra("CANTIDAD", 0.0);

        // Llenar los EditText con los datos recibidos
        leerPedidoPorId(idPedido);

        btnGuardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtiene el texto de los EditText y lo almacena en variables
                comercial = editTextComercial.getText().toString();
                partner = editTextPartner.getText().toString();
                fecha_envio = editTextFechaEnvio.getText().toString();
                fecha_pedido = editTextFechaPedido.getText().toString();

                // Aquí puedes realizar cualquier operación con los datos ingresados
                // Por ejemplo, mostrar los valores en un Toast
                Toast.makeText(ModificarDatosActivity.this, "Comercial: " + comercial + ", Partner: " + partner + ", Fecha de Envío: " + fecha_envio + ", Fecha de Pedido: " + fecha_pedido, Toast.LENGTH_SHORT).show();

                // Llama a la función para eliminar el pedido existente de la base de datos
                borrarenBBDD();

                // Llama a la función para añadir un nuevo pedido al XML con los datos de los EditText
                añadirNuevoPedido();

                // Llama a la función para actualizar el pedido en la base de datos
                actualizarEnBBDD();

                Intent intent = new Intent(ModificarDatosActivity.this, menu_Pedido.class);
                startActivity(intent);

            }
        });
    }

    public void borrarenBBDD() {
        try {
            File file = new File(getFilesDir(), "pedidos2.xml");

            // Verificar si el archivo XML existe antes de intentar leerlo
            if (file.exists()) {
                // Crear un InputStream para leer el archivo XML
                FileInputStream fis = new FileInputStream(file);

                // Crear un DocumentBuilderFactory
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

                // Parsear el archivo XML y obtener el documento
                Document doc = dBuilder.parse(fis);

                // Normalizar el documento
                doc.getDocumentElement().normalize();

                // Obtener la lista de nodos de pedido
                NodeList pedidoList = doc.getElementsByTagName("pedido");

                // Iterar sobre la lista de pedidos
                for (int i = 0; i < pedidoList.getLength(); i++) {
                    Node pedidoNode = pedidoList.item(i);
                    if (pedidoNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element pedidoElement = (Element) pedidoNode;
                        // Obtener el valor del elemento "id_pedido"
                        Node idPedidoNode = pedidoElement.getElementsByTagName("id_pedido").item(0);
                        if (idPedidoNode != null) {
                            String idPedidoXML = idPedidoNode.getTextContent();
                            if (String.valueOf(idPedido).equals(idPedidoXML)) {
                                pedidoNode.getParentNode().removeChild(pedidoNode);
                                break; // Salir del bucle una vez eliminado el pedido
                            }
                        } else {
                            // Handle the case where the id_pedido node is missing or empty
                            Log.e("ModificarDatosActivity", "Error: id_pedido node is missing or empty");
                        }
                    }
                }

                // Guardar los cambios en el archivo XML
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(file);
                transformer.transform(source, result);

                Toast.makeText(ModificarDatosActivity.this, "Pedido eliminado del XML correctamente", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ModificarDatosActivity.this, "El archivo XML no existe", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(ModificarDatosActivity.this, "Error al eliminar el pedido del XML: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void añadirNuevoPedido() {
        try {
            File file = new File(getFilesDir(), "pedidos2.xml");

            // Obtener una instancia del DocumentBuilderFactory
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            // Si el archivo XML ya existe, leer su contenido existente
            Document doc;
            Element rootElement;
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                doc = dBuilder.parse(fis);
                fis.close();
                rootElement = doc.getDocumentElement();
            } else {
                // Si el archivo XML no existe, crear un nuevo documento XML con la etiqueta <pedidos>
                doc = dBuilder.newDocument();
                rootElement = doc.createElement("pedidos");
                doc.appendChild(rootElement);
                doc.appendChild(doc.createTextNode("\n")); // Salto de línea después de la etiqueta raíz
            }

            // Crear un nuevo elemento <pedido>
            Element pedidoElement = doc.createElement("pedido");
            rootElement.appendChild(pedidoElement);
            pedidoElement.appendChild(doc.createTextNode("\n  ")); // Salto de línea y sangría

            // Crear elementos dentro del pedido
            Element idPedidoElement = doc.createElement("id_pedido");
            idPedidoElement.appendChild(doc.createTextNode(String.valueOf(idPedido)));
            pedidoElement.appendChild(idPedidoElement);
            pedidoElement.appendChild(doc.createTextNode("\n  ")); // Salto de línea y sangría

            Element idComercialElement = doc.createElement("id_comercial");
            idComercialElement.appendChild(doc.createTextNode(comercial));
            pedidoElement.appendChild(idComercialElement);
            pedidoElement.appendChild(doc.createTextNode("\n  ")); // Salto de línea y sangría

            Element idPartnerElement = doc.createElement("id_partner");
            idPartnerElement.appendChild(doc.createTextNode(partner));
            pedidoElement.appendChild(idPartnerElement);
            pedidoElement.appendChild(doc.createTextNode("\n  ")); // Salto de línea y sangría

            // Obtener los productos del pedido de la base de datos
            List<Producto> listaProductos = obtenerProductosDelPedidoDesdeBD(idPedido);

// Iterar sobre todos los productos y agregarlos al pedido
            for (Producto producto : listaProductos) {
                Element productoElement = doc.createElement("producto");
                pedidoElement.appendChild(productoElement);
                pedidoElement.appendChild(doc.createTextNode("\n  ")); // Salto de línea y sangría

                Element idArticuloElement = doc.createElement("id_articulo");
                idArticuloElement.appendChild(doc.createTextNode(producto.getDescripcion()));
                productoElement.appendChild(idArticuloElement);
                pedidoElement.appendChild(doc.createTextNode("\n  ")); // Salto de línea y sangría

                Element cantidadElement = doc.createElement("cantidad");
                cantidadElement.appendChild(doc.createTextNode(String.valueOf(producto.getCantidad())));
                productoElement.appendChild(cantidadElement);
                pedidoElement.appendChild(doc.createTextNode("\n  ")); // Salto de línea y sangría

                Element precioUnElement = doc.createElement("precio_un");
                precioUnElement.appendChild(doc.createTextNode(String.valueOf(producto.getPrecio_un())));
                productoElement.appendChild(precioUnElement);
                pedidoElement.appendChild(doc.createTextNode("\n  ")); // Salto de línea y sangría
            }

            Element fechaElement = doc.createElement("fecha");
            fechaElement.appendChild(doc.createTextNode(fecha_pedido)); // Utiliza la fecha de pedido por ahora
            pedidoElement.appendChild(fechaElement);
            pedidoElement.appendChild(doc.createTextNode("\n  ")); // Salto de línea y sangría

            Element precioTotalElement = doc.createElement("precio_total");
            precioTotalElement.appendChild(doc.createTextNode(String.valueOf(precioTotal))); // Ajusta el precio total según sea necesario
            pedidoElement.appendChild(precioTotalElement);
            pedidoElement.appendChild(doc.createTextNode("\n  ")); // Salto de línea y sangría

            // Guardar el contenido en el archivo XML
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);

            Toast.makeText(ModificarDatosActivity.this, "Pedido añadido al XML correctamente", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ModificarDatosActivity", "Error: " + e.getMessage()); // Log the error message
            Toast.makeText(ModificarDatosActivity.this, "Error al añadir el pedido al XML: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public List<Producto> obtenerProductosDelPedidoDesdeBD(int idPedido) {
        ArrayList<Producto> listaProductos = new ArrayList<>();

        // Aquí realizarías la consulta a tu base de datos para obtener los productos del pedido con el ID dado
        // Por ejemplo, podrías ejecutar una consulta SQL que seleccione todos los productos asociados con el ID del pedido
        // y luego crear objetos Producto con los datos recuperados de la base de datos

        // Ejemplo de cómo podrías recuperar los productos de la base de datos
        DBconexion bbdd = new DBconexion(this, "ACAB2.db", null, 1);
        SQLiteDatabase database = bbdd.getWritableDatabase();


        Cursor cursor = database.rawQuery("SELECT DESCRIPCION,PRECIO_UN,CANTIDAD FROM CAB_PEDIDOS,LIN_PEDIDOS WHERE LIN_PEDIDOS.ID_PEDIDO = CAB_PEDIDOS.ID_PEDIDO",null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String idArticulo = cursor.getString(0);
                int cantidad = cursor.getInt(2);
                double precioUnitario = cursor.getDouble(1);

                // Crear un objeto Producto con los datos recuperados de la base de datos
                Producto producto = new Producto(idArticulo, cantidad, precioUnitario);

                // Agregar el producto a la lista de productos
                listaProductos.add(producto);
            } while (cursor.moveToNext());

            // Cerrar el cursor después de usarlo
            cursor.close();
        }

        // Devolver la lista de productos
        return listaProductos;
    }

    private void leerPedidoPorId(int idPedidoBuscado) {
        try {
            File file = new File(getFilesDir(), "pedidos2.xml");

            // Verificar si el archivo XML existe antes de intentar leerlo
            if (file.exists()) {
                // Crear un InputStream para leer el archivo XML
                FileInputStream fis = new FileInputStream(file);

                // Crear un DocumentBuilderFactory
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

                // Parsear el archivo XML y obtener el documento
                Document doc = dBuilder.parse(fis);

                // Normalizar el documento
                doc.getDocumentElement().normalize();

                // Obtener la lista de nodos de pedido
                NodeList pedidoList = doc.getElementsByTagName("pedido");

                // Iterar sobre la lista de pedidos
                for (int i = 0; i < pedidoList.getLength(); i++) {
                    Node node = pedidoList.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;

                        // Obtener el ID del pedido actual
                        String idPedido = element.getElementsByTagName("id_pedido").item(0).getTextContent();

                        // Verificar si el ID del pedido coincide con el ID buscado
                        if (idPedido.equals(String.valueOf(idPedidoBuscado))) {
                            // Mostrar todos los detalles del pedido encontrado
                            String idPartner = element.getElementsByTagName("id_partner").item(0).getTextContent();
                            String idComercial = element.getElementsByTagName("id_comercial").item(0).getTextContent();
                            String fechaPedido = element.getElementsByTagName("fecha").item(0).getTextContent();
                            String precioTotal = element.getElementsByTagName("precio_total").item(0).getTextContent();

                            // Mostrar la información del pedido encontrado en un Toast
                            Toast.makeText(this, "Pedido " + idPedido + ": Comercial: " + idComercial + ", Partner: " + idPartner + ", Fecha Pedido: " + fechaPedido + ", Precio Total: " + precioTotal, Toast.LENGTH_SHORT).show();

                            // Si deseas obtener información detallada de cada producto, puedes hacerlo aquí
                            NodeList productos = element.getElementsByTagName("producto");
                            for (int j = 0; j < productos.getLength(); j++) {
                                Node productoNode = productos.item(j);
                                if (productoNode.getNodeType() == Node.ELEMENT_NODE) {
                                    Element productoElement = (Element) productoNode;
                                    String idArticulo = productoElement.getElementsByTagName("id_articulo").item(0).getTextContent();
                                    String cantidad = productoElement.getElementsByTagName("cantidad").item(0).getTextContent();
                                    String precioUnidad = productoElement.getElementsByTagName("precio_un").item(0).getTextContent();
                                    Toast.makeText(this, "Producto " + (j + 1) + ": ID Artículo: " + idArticulo + ", Cantidad: " + cantidad + ", Precio Unidad: " + precioUnidad, Toast.LENGTH_SHORT).show();
                                }
                            }
                            // Salir del bucle después de encontrar el pedido
                            break;
                        }
                    }
                }

                // Cierra el FileInputStream
                fis.close();
            } else {
                // Si el archivo no existe, muestra un mensaje de error
                Toast.makeText(this, "El archivo XML 'pedidos2.xml' no existe", Toast.LENGTH_SHORT).show();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error: Archivo XML no encontrado", Toast.LENGTH_SHORT).show();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    /* public void añadirNuevoPedido() {
        DBconexion bbdd = new DBconexion(this, "ACAB2.db", null, 1);
        SQLiteDatabase database = bbdd.getWritableDatabase();

        // Inserta un nuevo pedido con los nuevos datos
        String query = "INSERT INTO CAB_PEDIDOS (ID_PEDIDO, ID_COMERCIAL, ID_PARTNER, DESCRIPCION, FECHA_PEDIDO, FECHA_ENVIO) " +
                "VALUES (" + idPedido + ", '" + comercial + "', " + Integer.parseInt(partner) + ", '" + descripcionPedido + "', '" + fecha_pedido + "', '" + fecha_envio + "')";
        database.execSQL(query);

        // Cerrar la conexión con la base de datos
        database.close();
    }*/
   public void actualizarEnBBDD() {
       try {
           DBconexion bbdd = new DBconexion(this, "ACAB2.db", null, 1);
           SQLiteDatabase database = bbdd.getWritableDatabase();

           // Crea el ContentValues con los nuevos valores
           database.execSQL("UPDATE CAB_PEDIDOS SET ID_COMERCIAL = '"+comercial+"', ID_PARTNER = "+partner+", FECHA_PEDIDO = '"+fecha_pedido+"', FECHA_ENVIO = '"+fecha_envio+" 'WHERE ID_PEDIDO = " + idPedido+"'");




           // Cierra la conexión con la base de datos
           database.close();
       } catch (Exception e) {
           e.printStackTrace();
           Log.e("ModificarDatosActivity", "Error: " + e.getMessage()); // Log the error message
           Toast.makeText(ModificarDatosActivity.this, "Error al actualizar el pedido en la base de datos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
       }
   }

}