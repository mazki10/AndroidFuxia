package com.example.almohadascomodasademsbonitas;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.almohadascomodasademsbonitas.BBDD.DBconexion;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class Enviar extends AppCompatActivity {
    Button bEnviar, bGenerar;
    static File fEnvio;
    static File fPedido;
    static File fPartner;
    static String[][] arrayPartner;
    static String[][] arrayPedidos;

    private DBconexion dbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar);



        bEnviar = findViewById(R.id.bgmail_enviar);
        bGenerar = findViewById(R.id.bgmail_crearXML);

        dbHelper = new DBconexion(this, "ACAB2.db", null, 1);
        db = dbHelper.getReadableDatabase();

        bEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnviarXML();
            }
        });

        bGenerar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GenerarXML();
            }
        });
    }

    public void EnviarXML() {
        Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"destinatario@example.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Asunto del correo");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Cuerpo del correo");

        ArrayList<Uri> archivosAdjuntos = new ArrayList<>();

        // Verificar si el archivo de partners existe y agregarlo a la lista de archivos adjuntos
        File archivoPartners = new File(getFilesDir(), "ePartners.xml");
        if (archivoPartners.exists()) {
            Uri fileUriPartners = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", archivoPartners);
            archivosAdjuntos.add(fileUriPartners);
        }

        // Verificar si el archivo de pedidos existe y agregarlo a la lista de archivos adjuntos
        File archivoPedidos = new File(getFilesDir(), "ePedidos.xml");
        if (archivoPedidos.exists()) {
            Uri fileUriPedidos = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", archivoPedidos);
            archivosAdjuntos.add(fileUriPedidos);
        }

        // Verificar si hay archivos adjuntos para enviar
        if (archivosAdjuntos.isEmpty()) {
            Toast.makeText(this, "No hay archivos disponibles para enviar", Toast.LENGTH_SHORT).show();
            return;
        }

        // Agregar los archivos adjuntos al intent de correo electrónico
        emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, archivosAdjuntos);

        // Iniciar el intent para enviar el correo electrónico
        startActivity(Intent.createChooser(emailIntent, "Enviar correo electrónico"));

        // Eliminar los archivos después de enviarlos
        eliminarArchivos(archivoPartners, archivoPedidos);
    }

    private void eliminarArchivos(File archivoPartners, File archivoPedidos) {
        // Verificar si el archivo de partners existe y eliminarlo
        if (archivoPartners.exists()) {
            boolean eliminado = archivoPartners.delete();
            if (!eliminado) {
                Log.e("Error", "No se pudo eliminar el archivo ePartners.xml");
            }
        }

        // Verificar si el archivo de pedidos existe y eliminarlo
        if (archivoPedidos.exists()) {
            boolean eliminado = archivoPedidos.delete();
            if (!eliminado) {
                Log.e("Error", "No se pudo eliminar el archivo ePedidos.xml");
            }
        }
    }

    public void GenerarXML() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        // Obtener el contexto de la aplicación de Android
        Context context = Enviar.this;

        leerPartners();
        leerPedidos();

        if (arrayPartner == null && arrayPedidos == null) {
            Toast.makeText(this, "No hay datos disponibles para generar los archivos XML", Toast.LENGTH_SHORT).show();
            return;
        }

        llenar_fichero(factory);
    }

    private void leerPartners() {
        // Consulta SQL para seleccionar todos los registros de la tabla "partners"
        String query = "SELECT * FROM PARTNERS";

        Cursor cursor = null;

        try {
            cursor = db.rawQuery(query, null);

            if (cursor != null && cursor.moveToFirst()) {
                arrayPartner = new String[cursor.getCount()][8];

                int i = 0;
                do {
                    String idPartners = cursor.getString(0);
                    String nombre = cursor.getString(1);
                    String cif = cursor.getString(2);
                    String direccion = cursor.getString(3);
                    String telefono = cursor.getString(4);
                    String email = cursor.getString(5); // email
                    String personaDeContacto = cursor.getString(6);
                    String idZona = cursor.getString(7);

                    arrayPartner[i][0] = idPartners;
                    arrayPartner[i][1] = nombre;
                    arrayPartner[i][2] = cif;
                    arrayPartner[i][3] = direccion;
                    arrayPartner[i][4] = telefono;
                    arrayPartner[i][5] = email;
                    arrayPartner[i][6] = personaDeContacto;
                    arrayPartner[i][7] = idZona;

                    i++;
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Error", "Error al leer datos de la base de datos: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void leerPedidos() {
        // Consulta SQL para seleccionar todos los pedidos
        String query = "SELECT * FROM CAB_PEDIDOS";

        Cursor cursor = null;

        try {
            cursor = db.rawQuery(query, null);

            if (cursor != null && cursor.moveToFirst()) {
                arrayPedidos = new String[cursor.getCount()][7];

                int i = 0;
                do {
                    // Obtener los valores de cada columna para el pedido actual
                    String idPedido = cursor.getString(0);
                    String idPartner = cursor.getString(1);
                    String idComercial = cursor.getString(2);
                    String fecha = cursor.getString(4);

                    // Obtener los productos asociados al pedido
                    StringBuilder productosStringBuilder = new StringBuilder();
                    String productosQuery = "SELECT * FROM LIN_PEDIDOS WHERE id_pedido = ?";
                    Cursor productosCursor = db.rawQuery(productosQuery, new String[]{idPedido});
                    if (productosCursor != null && productosCursor.moveToFirst()) {
                        do {
                            String cantidad = productosCursor.getString(2);
                            String descuento = productosCursor.getString(3);
                            String precioUn = productosCursor.getString(4);

                            // Concatenar los datos del producto al StringBuilder
                            productosStringBuilder.append("Cantidad: ").append(cantidad)
                                    .append(", Descuento: ").append(descuento).append(", Precio Unitario: ").append(precioUn).append("\n");
                        } while (productosCursor.moveToNext());

                        productosCursor.close();
                    }

                    // Guardar los valores en el array
                    arrayPedidos[i][0] = idPedido;
                    arrayPedidos[i][1] = idPartner;
                    arrayPedidos[i][2] = idComercial;
                    arrayPedidos[i][3] = productosStringBuilder.toString();  // Productos
                    arrayPedidos[i][4] = fecha;

                    i++;
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Error", "Error al leer datos de la base de datos: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public void llenar_fichero(DocumentBuilderFactory factory) {
        crearXMLPedidos(factory);
        crearXMLPartners(factory);
    }

    public void crearXMLPartners(DocumentBuilderFactory factory) {
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            DOMImplementation implementation = builder.getDOMImplementation();
            Document document = implementation.createDocument(null, "Partners", null);
            document.setXmlVersion("1.0");

            if (arrayPartner != null && arrayPartner.length > 0 && arrayPartner[0].length == 8) {
                for (int i = 0; i < arrayPartner.length; i++) {
                    Element partnerElement = document.createElement("partner");
                    document.getDocumentElement().appendChild(partnerElement);

                    CrearElemento("id_partners", arrayPartner[i][0], partnerElement, document);
                    CrearElemento("nombre", arrayPartner[i][1], partnerElement, document);
                    CrearElemento("cif", arrayPartner[i][2], partnerElement, document);
                    CrearElemento("direccion", arrayPartner[i][3], partnerElement, document);
                    CrearElemento("telefono", arrayPartner[i][4], partnerElement, document);
                    CrearElemento("email", arrayPartner[i][5], partnerElement, document);
                    CrearElemento("persona_de_contacto", arrayPartner[i][6], partnerElement, document);
                    CrearElemento("id_zona", arrayPartner[i][7], partnerElement, document);
                }
            } else {
                Toast.makeText(this, "No hay partners", Toast.LENGTH_SHORT).show();
                return;
            }

            // Guardar el documento XML en el archivo
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(getFilesDir(), "ePartners.xml"));
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.transform(source, result);

            Toast.makeText(this, "Datos de partners guardados en el archivo XML ePartners.xml", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("Error", "Error al crear el archivo XML de partners: " + e.getMessage());
        }
    }

    public void crearXMLPedidos(DocumentBuilderFactory factory) {
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            DOMImplementation implementation = builder.getDOMImplementation();
            Document document = implementation.createDocument(null, "Pedidos", null);
            document.setXmlVersion("1.0");

            if (arrayPedidos != null && arrayPedidos.length > 0 && arrayPedidos[0].length == 7) {
                for (int i = 0; i < arrayPedidos.length; i++) {
                    Element pedidoElement = document.createElement("pedido");
                    document.getDocumentElement().appendChild(pedidoElement);

                    CrearElemento("id_pedido", arrayPedidos[i][0], pedidoElement, document);
                    CrearElemento("id_partner", arrayPedidos[i][1], pedidoElement, document);
                    CrearElemento("id_comercial", arrayPedidos[i][2], pedidoElement, document);

                    String productos = arrayPedidos[i][3];
                    String[] productosArray = productos.split("\n");

                    for (String producto : productosArray) {
                        String[] productoInfo = producto.split(", ");
                        Log.d("DEBUG", "Datos del producto: " + Arrays.toString(productoInfo)); // Agregar esta línea para depurar

                        Element productoElement = document.createElement("producto");
                        pedidoElement.appendChild(productoElement);

                        CrearElemento("cantidad", productoInfo[0].substring(productoInfo[0].indexOf(":") + 2), productoElement, document);
                        CrearElemento("descuento", productoInfo[1].substring(productoInfo[1].indexOf(":") + 2), productoElement, document);
                        CrearElemento("precio_un", productoInfo[2].substring(productoInfo[2].indexOf(":") + 2), productoElement, document);
                    }

                    CrearElemento("fecha", arrayPedidos[i][4], pedidoElement, document);
                }
            } else {
                Toast.makeText(this, "No hay pedidos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Guardar el documento
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(getFilesDir(), "ePedidos.xml"));
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.transform(source, result);

            Toast.makeText(this, "Datos de pedidos guardados en el archivo XML ePedidos.xml", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("Error", "Error al crear el archivo XML de pedidos: " + e.getMessage());
        }
    }

    public void CrearElemento(String datoEmple, String valor, Element raiz, Document document) {
        Element elem = document.createElement(datoEmple);
        Text text = document.createTextNode(valor);
        raiz.appendChild(elem);
        elem.appendChild(text);
    }
}