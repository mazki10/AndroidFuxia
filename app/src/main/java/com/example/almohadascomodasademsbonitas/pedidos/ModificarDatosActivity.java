package com.example.almohadascomodasademsbonitas.pedidos;

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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
   // String fecha_envio;
   // String fecha_pedido;
    int idPedido;
    String descripcionPedido;
    double precioUnitario;
    double precioTotal;
    double cantidad;
    LocalDate fecha_pedido;
    LocalDate fecha_envio;
    String comFinal;
    String paFinal;
    String deFinal;
    String fecha_enviotxt;
    String fechEfinal;
    LocalDate fecha_envio1;
    String fecha_pedidotxt;
    LocalDate fecha_pedido1;
    String fechPfinal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_datos);

        // Obtener referencias a los elementos de interfaz de usuario en tu diseño
        editTextComercial = findViewById(R.id.comercial);
        editTextPartner = findViewById(R.id.partner);
        editTextFechaPedido = findViewById(R.id.fecha_pedido);
        editTextFechaEnvio = findViewById(R.id.fecha_envio);
        editTextDescripcion = findViewById(R.id.editTextDescripcion);
        btnGuardarCambios = findViewById(R.id.btnGuardarCambios);

        // Obtener los datos enviados desde modificar_pedido
        idPedido = getIntent().getIntExtra("ID_PEDIDO",0);
        comercial = getIntent().getStringExtra("COMERCIAL");
        partner = getIntent().getStringExtra("PARTNER");
        descripcionPedido = getIntent().getStringExtra("DESCRIPCION");
        precioTotal = getIntent().getDoubleExtra("PRECIO_TOTAL",0.0);
        fecha_pedido = buscarFechaPedido();
        fecha_envio = buscarFechaEnvio();
        // Llenar los EditText con los datos recibidos
        //leerPedidoPorId(idPedido);

        editTextComercial.setText(comercial);
        editTextPartner.setText(partner);
        editTextDescripcion.setText(descripcionPedido);
        editTextFechaPedido.setText(String.valueOf(fecha_pedido));
        editTextFechaEnvio.setText(String.valueOf(fecha_envio));

        btnGuardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtiene el texto de los EditText y lo almacena en variables
                comercial = editTextComercial.getText().toString();

                if(comercial.equals("")){
                    comercial = getIntent().getStringExtra("COMERCIAL");;
                    editTextComercial.setText(comercial);
                    comFinal = comercial;
                }else{
                   if (buscarComercial(comercial)){
                       comFinal = comercial;
                   }else {
                       Toast.makeText(ModificarDatosActivity.this,"Comercial no esta registrado",Toast.LENGTH_SHORT);
                   }
                }

                partner = editTextPartner.getText().toString();

                if(partner.equals("")){
                    partner = getIntent().getStringExtra("PARTNER");;
                    editTextPartner.setText(partner);
                    paFinal = partner;
                }else{
                    if (buscarPartner(partner)){
                        paFinal = partner;
                    }else {
                        Toast.makeText(ModificarDatosActivity.this,"Partner no esta registrado",Toast.LENGTH_SHORT);
                    }
                }

                descripcionPedido = editTextDescripcion.getText().toString();

                if(descripcionPedido.equals("")){
                    descripcionPedido = getIntent().getStringExtra("DESCRIPCION");
                    editTextDescripcion.setText(descripcionPedido);
                    deFinal = descripcionPedido;
                }else{
                    if (buscarDescripcion(descripcionPedido)){
                        deFinal = descripcionPedido;
                    }else {
                        Toast.makeText(ModificarDatosActivity.this,"Producto no esta registrado",Toast.LENGTH_SHORT);
                    }
                }


                fecha_enviotxt = editTextFechaEnvio.getText().toString();

                if (fecha_enviotxt.equals("")) {
                    fecha_enviotxt = fecha_envio.toString(); // Aquí asumo que fecha_envio ya está en el formato deseado
                } else {
                    // Intenta parsear el String en un objeto LocalDate
                    try {
                        fecha_envio1 = LocalDate.parse(fecha_enviotxt, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        // Si la fecha se parsea correctamente, conviértela al formato de la base de datos
                        fechEfinal = fecha_envio1.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                    } catch (DateTimeParseException e) {
                        // Si el formato no es válido, muestra un mensaje de error
                        Toast.makeText(ModificarDatosActivity.this, "Formato de fecha incorrecto", Toast.LENGTH_SHORT).show();
                        return; // Sale del método onClick porque la fecha es inválida
                    }
                }

                fecha_pedidotxt = editTextFechaPedido.getText().toString();

                if (fecha_pedidotxt.equals("")) {
                    fecha_pedidotxt = fecha_pedido.toString(); // Utiliza la fecha de pedido por ahora
                } else {
                    // Intenta parsear la cadena como una fecha con el formato 'yyyy-MM-dd'
                    try {
                        fecha_pedido1 = LocalDate.parse(fecha_pedidotxt, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        fechPfinal = fecha_pedido1.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                    } catch (DateTimeParseException e) {
                        // Si el formato no es válido, muestra un mensaje de error
                        Toast.makeText(ModificarDatosActivity.this, "Formato de fecha incorrecto", Toast.LENGTH_SHORT).show();
                        return; // Sale del método onClick porque la fecha es inválida
                    }
                }



                String dni="";

                DBconexion dbHelper = new DBconexion(ModificarDatosActivity.this, "ACAB2.db", null, 1);
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                String consultaCom = "SELECT DNI FROM COMERCIALES WHERE NOMBRE = '"+comFinal+"'";
                Cursor cursor = db.rawQuery(consultaCom,null);

                if (cursor.moveToFirst()){
                    dni = cursor.getString(0);
                }


                int id_partner=0;

                String consultaPart = "SELECT ID_PARTNER FROM PARTNERS WHERE NOMBRE = '"+paFinal+"'";
                cursor = db.rawQuery(consultaPart,null);

                if (cursor.moveToFirst()){
                    id_partner = cursor.getInt(0);
                }
                String actualizar = "UPDATE CAB_PEDIDOS SET ID_COMERCIAL = '"+dni+"', ID_PARTNER = "+id_partner+", DESCRIPCION = '"+deFinal+"', FECHA_PEDIDO = '"+fechPfinal+"', FECHA_ENVIO = '"+fechEfinal+"' WHERE ID_PEDIDO = "+idPedido;
                db.execSQL(actualizar);
                Intent intent = new Intent(ModificarDatosActivity.this, menu_Pedido.class);
                startActivity(intent);

            }
        });
    }

    public boolean buscarDescripcion(String nombre){
        boolean esta = false;
        String n;
        DBconexion dbHelper = new DBconexion(this, "ACAB2.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Asegúrate de encerrar el valor de 'nombre' entre comillas simples en la consulta SQL
        String consulta = "SELECT ID_ARTICULO FROM ARTICULOS WHERE DESCRIPCION = ?";
        Cursor cursor = db.rawQuery(consulta, new String[]{nombre});

        if (cursor.moveToFirst()) {
            n = cursor.getString(0); // Obtener la fecha como una cadena
            // Si se encuentra un resultado, establece 'esta' como verdadero
            esta = true;
        }

        // Cierra el cursor y la base de datos
        cursor.close();
        db.close();

        return esta;
    }

    public boolean buscarComercial(String nombre){
        boolean esta = false;
        String n;
        DBconexion dbHelper = new DBconexion(this, "ACAB2.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Asegúrate de encerrar el valor de 'nombre' entre comillas simples en la consulta SQL
        String consulta = "SELECT APELLIDO1 FROM COMERCIALES WHERE NOMBRE = ?";
        Cursor cursor = db.rawQuery(consulta, new String[]{nombre});

        if (cursor.moveToFirst()) {
            n = cursor.getString(0); // Obtener la fecha como una cadena
            // Si se encuentra un resultado, establece 'esta' como verdadero
            esta = true;
        }

        // Cierra el cursor y la base de datos
        cursor.close();
        db.close();

        return esta;
    }


    public boolean buscarPartner(String nombre){
        boolean esta = false;
        int n;
        DBconexion dbHelper = new DBconexion(this, "ACAB2.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Asegúrate de encerrar el valor de 'nombre' entre comillas simples en la consulta SQL
        String consulta = "SELECT ID_PARTNER FROM PARTNERS WHERE NOMBRE = ?";
        Cursor cursor = db.rawQuery(consulta, new String[]{nombre});

        if (cursor.moveToFirst()) {
            n = cursor.getInt(0); // Obtener la fecha como una cadena
            // Si se encuentra un resultado, establece 'esta' como verdadero
            esta = true;
        }

        // Cierra el cursor y la base de datos
        cursor.close();
        db.close();

        return esta;
    }

    public LocalDate buscarFechaPedido(){
        LocalDate fecha = null; // Inicializar la fecha como nula

        DBconexion dbHelper = new DBconexion(this, "ACAB2.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String consulta = "SELECT FECHA_PEDIDO FROM CAB_PEDIDOS WHERE ID_PEDIDO = "+idPedido+"";
        Cursor cursor = db.rawQuery(consulta,null);

        if (cursor.moveToFirst()) {
            String fechaString = cursor.getString(0); // Obtener la fecha como una cadena
            if (fechaString != null && !fechaString.isEmpty()) { // Verificar si la cadena no es nula ni está vacía
                try {
                    // Intentar analizar la cadena de fecha con el formato esperado
                    fecha = LocalDate.parse(fechaString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                } catch (DateTimeParseException e) {
                    // Si ocurre un error al analizar la fecha, imprimir un mensaje de error y devolver null
                    Log.e("ModificarDatosActivity", "Error al analizar la fecha de envío: " + e.getMessage());
                }
            }
        }

        return fecha;
    }

    public LocalDate buscarFechaEnvio(){
        LocalDate fecha = null; // Inicializar la fecha como nula

        DBconexion dbHelper = new DBconexion(this, "ACAB2.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String consulta = "SELECT FECHA_ENVIO FROM CAB_PEDIDOS WHERE ID_PEDIDO = " + idPedido;
        Cursor cursor = db.rawQuery(consulta, null);

        if (cursor.moveToFirst()) {
            String fechaString = cursor.getString(0); // Obtener la fecha como una cadena
            if (fechaString != null && !fechaString.isEmpty()) { // Verificar si la cadena no es nula ni está vacía
                try {
                    // Intentar analizar la cadena de fecha con el formato esperado
                    fecha = LocalDate.parse(fechaString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                } catch (DateTimeParseException e) {
                    // Si ocurre un error al analizar la fecha, imprimir un mensaje de error y devolver null
                    Log.e("ModificarDatosActivity", "Error al analizar la fecha de envío: " + e.getMessage());
                }
            }
        }

        return fecha;
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
           // fechaElement.appendChild(doc.createTextNode(fecha_pedido)); // Utiliza la fecha de pedido por ahora
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
           database.execSQL("UPDATE CAB_PEDIDOS SET ID_COMERCIAL = '"+comercial+"', ID_PARTNER = "+partner+", FECHA_PEDIDO = '"+fecha_pedido+"', FECHA_ENVIO = '"+fecha_envio+"' WHERE ID_PEDIDO = " + idPedido);




           // Cierra la conexión con la base de datos
           database.close();
       } catch (Exception e) {
           e.printStackTrace();
           Log.e("ModificarDatosActivity", "Error: " + e.getMessage()); // Log the error message
           Toast.makeText(ModificarDatosActivity.this, "Error al actualizar el pedido en la base de datos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
       }
   }

}