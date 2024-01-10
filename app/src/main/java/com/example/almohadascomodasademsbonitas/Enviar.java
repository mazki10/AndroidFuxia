package com.example.almohadascomodasademsbonitas;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar);

        fEnvio = new File(getFilesDir(), "envio.xml");
        fPedido = new File(getFilesDir(), "pedidos.xml");
        fPartner = new File(getFilesDir(), "partners.xml");

        bEnviar = findViewById(R.id.bgmail_enviar);
        bGenerar = findViewById(R.id.bgmail_crearXML);

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

    public void EnviarXML(){
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"destinatario@example.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Asunto del correo");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Cuerpo del correo");

        Uri fileUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", fEnvio);
        emailIntent.putExtra(Intent.EXTRA_STREAM, fileUri);

        startActivity(Intent.createChooser(emailIntent, "Enviar correo electrónico"));
    }

    public void GenerarXML() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        // Obtener el contexto de la aplicación de Android
        Context context = Enviar.this;

        leerPedidos(fPedido);
        leerPartners(fPartner);

        llenar_fichero(factory);

        mostrarContenidoXML();
    }

    private static String obtenerTextoElemento(Element elemento, String nombreEtiqueta) {
        Node nodo = elemento.getElementsByTagName(nombreEtiqueta).item(0);
        if (nodo != null && nodo.getNodeType() == Node.ELEMENT_NODE) {
            return nodo.getTextContent();
        }
        return ""; // O cualquier valor predeterminado que desees devolver en caso de nulo
    }

    private static String obtenerTextoElemento(Element elemento, String nombrePadre, String nombreEtiqueta) {
        NodeList nodeList = elemento.getElementsByTagName(nombrePadre);
        if (nodeList.getLength() > 0) {
            Element elementoPadre = (Element) nodeList.item(0);
            return obtenerTextoElemento(elementoPadre, nombreEtiqueta);
        }
        return "";
    }

    private void leerPartners(File xml) {
        String datos = leerContenidoXML(xml);
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(datos)));

            NodeList nodeList = document.getElementsByTagName("partner");
            arrayPartner = new String[nodeList.getLength()][8];

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element partnerElement = (Element) node;
                    String idPartners = obtenerTextoElemento(partnerElement, "id_partners");
                    String nombre = obtenerTextoElemento(partnerElement, "nombre");
                    String cif = obtenerTextoElemento(partnerElement, "cif");
                    String direccion = obtenerTextoElemento(partnerElement, "direccion");
                    String telefono = obtenerTextoElemento(partnerElement, "telefono");
                    String email = obtenerTextoElemento(partnerElement, "email");
                    String personaDeContacto = obtenerTextoElemento(partnerElement, "persona_de_contacto");
                    String idZona = obtenerTextoElemento(partnerElement, "id_zona");

                    arrayPartner[i][0] = idPartners;
                    arrayPartner[i][1] = nombre;
                    arrayPartner[i][2] = cif;
                    arrayPartner[i][3] = direccion;
                    arrayPartner[i][4] = telefono;
                    arrayPartner[i][5] = email;
                    arrayPartner[i][6] = personaDeContacto;
                    arrayPartner[i][7] = idZona;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Error", "Error al leer partners.xml: " + e.getMessage());
        }
    }

    private void leerPedidos(File xml) {
        String datos = leerContenidoXML(xml);
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(datos)));

            NodeList nodeList = document.getElementsByTagName("pedido");
            arrayPedidos = new String[nodeList.getLength()][9];

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element pedidoElement = (Element) node;
                    String idPedido = obtenerTextoElemento(pedidoElement, "id_pedido");
                    String idPartner = obtenerTextoElemento(pedidoElement, "id_partner");
                    String idComercial = obtenerTextoElemento(pedidoElement, "id_comercial");

                    NodeList productos = pedidoElement.getElementsByTagName("producto");
                    for (int j = 0; j < productos.getLength(); j++) {
                        Node productoNode = productos.item(j);
                        if (productoNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element productoElement = (Element) productoNode;
                            String cantidad = obtenerTextoElemento(productoElement, "cantidad");
                            String descuento = obtenerTextoElemento(productoElement, "descuento");
                            String precioUd = obtenerTextoElemento(productoElement, "precio_un");

                            // Adjust the indices based on the structure of your array
                            int index = i * productos.getLength() + j;
                            arrayPedidos[index][0] = idPedido;
                            arrayPedidos[index][1] = idPartner;
                            arrayPedidos[index][2] = idComercial;
                            arrayPedidos[index][3] = cantidad;
                            arrayPedidos[index][4] = descuento;
                            arrayPedidos[index][5] = precioUd;
                            // You might need to adjust the indices accordingly

                            // Continue with other elements if needed
                        }
                    }

                    String fecha = obtenerTextoElemento(pedidoElement, "fecha");
                    String precioTotal = obtenerTextoElemento(pedidoElement, "precio_total");
                    String idTransporte = obtenerTextoElemento(pedidoElement, "n_factura");

                    // Adjust the index based on the structure of your array
                    int lastIndex = (i + 1) * productos.getLength() - 1;
                    arrayPedidos[lastIndex][6] = precioTotal;
                    arrayPedidos[lastIndex][7] = idTransporte;
                    arrayPedidos[lastIndex][8] = fecha;
                    // Continue with other elements if needed
                }
            }
            Toast.makeText(this, arrayPedidos[0][0], Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Error", "Error al leer pedidos.xml: " + e.getMessage());
        }
    }



    public void llenar_fichero(DocumentBuilderFactory factory) {
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            DOMImplementation implementation = builder.getDOMImplementation();
            Document document = implementation.createDocument(null, "Envio", null);
            document.setXmlVersion("1.0");

            // Agregar datos de arrayPedidos
            if (arrayPedidos != null && arrayPedidos.length > 0 && arrayPedidos[0].length == 9) {
                for (int i = 0; i < arrayPedidos.length; i++) {
                    Element pedidoElement = document.createElement("pedido");
                    document.getDocumentElement().appendChild(pedidoElement);

                    CrearElemento("id_pedido", arrayPedidos[i][0], pedidoElement, document);
                    CrearElemento("id_partner", arrayPedidos[i][1], pedidoElement, document);
                    CrearElemento("id_comercial", arrayPedidos[i][2], pedidoElement, document);

                    // Agregar datos de productos
                    for (int j = 3; j < 9; j += 3) {
                        Element productoElement = document.createElement("producto");
                        pedidoElement.appendChild(productoElement);
                        CrearElemento("descripcion", "producto" + ((j - 3) / 3 + 1), productoElement, document);
                        CrearElemento("cantidad", arrayPedidos[i][j], productoElement, document);
                        CrearElemento("precio_un", arrayPedidos[i][j + 1], productoElement, document);
                    }

                    CrearElemento("fecha", arrayPedidos[i][8], pedidoElement, document);
                    CrearElemento("precio_total", arrayPedidos[i][9], pedidoElement, document);
                    CrearElemento("n_factura", arrayPedidos[i][10], pedidoElement, document);
                }
            }else {
                Toast.makeText(this, "No pedidos", Toast.LENGTH_SHORT).show();
            }

            // Agregar datos de arrayPartner
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
                Toast.makeText(this, "No partner", Toast.LENGTH_SHORT).show();
            }

            Source source = new DOMSource(document);
            Result result = new StreamResult(fEnvio);
            indentarXML(source, result);

            Toast.makeText(this, "Datos guardados en el archivo XML con formato.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("Error","Error de todooo: " + e);
        }
    }


    public void indentarXML(Source source, Result result) {
        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            transformer.transform(source, result);
        } catch (Exception e){
            System.err.println("Error: " + e);
        }
    }

    public void CrearElemento(String datoEmple, String valor, Element raiz, Document document) {
        Element elem = document.createElement(datoEmple);
        Text text = document.createTextNode(valor);
        raiz.appendChild(elem);
        elem.appendChild(text);
    }

    private void mostrarContenidoXML() {
        try {
            // Leer el contenido del archivo XML
            String xmlContent = leerContenidoXML(fEnvio);

            // Mostrar el contenido en un Toast
            Toast.makeText(Enviar.this, "Contenido del XML:\n" + xmlContent, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String leerContenidoXML(File xmlFile) {
        // Implementa la lógica para leer el contenido del archivo XML y convertirlo a una cadena
        // Aquí proporciono un ejemplo básico utilizando FileReader y BufferedReader
        try {
            BufferedReader br = new BufferedReader(new FileReader(xmlFile));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            br.close();
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Error", "Error al leer envios: "+e);
            return "Error al leer el contenido del XML";
        }
    }
}