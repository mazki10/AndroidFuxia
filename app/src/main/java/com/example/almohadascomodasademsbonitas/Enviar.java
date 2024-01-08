package com.example.almohadascomodasademsbonitas;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;

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
    static String[][] arrayPartner;
    static String[][] arrayPedidos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar);

        bEnviar = findViewById(R.id.bgmail_enviar);
        bGenerar = findViewById(R.id.bgmail_crearXML);

        bEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

    }

    public void GenerarXML() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        // Obtener el contexto de la aplicación de Android
        Context context = Enviar.this;

        // Utilizar el directorio interno de la aplicación para almacenar los archivos
        File internalDir = context.getFilesDir();

        fEnvio = new File(internalDir, "envio.xml");
        File fPedido = new File(internalDir, "pedidos.xml");
        File fPartner = new File(internalDir, "partners.xml");

        leerPedidos(fPedido);
        leerPartners(fPartner);

        llenar_fichero(factory);

        mostrarContenidoXML();
    }

    public void leerPartners(File xml) {
        try {
            FileInputStream fis = new FileInputStream(xml);
            // Crear el objeto Document
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(fis);
            // Normalizar el documento
            document.getDocumentElement().normalize();
            // Obtener la lista de nodos "partner"
            NodeList nodeList = document.getElementsByTagName("partner");

            arrayPartner = new String[nodeList.getLength()][8];

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element partnerElement = (Element) node;
                    // Obtener los elementos internos
                    String idPartners = partnerElement.getElementsByTagName("id_partners").item(0).getTextContent();
                    String nombre = partnerElement.getElementsByTagName("nombre").item(0).getTextContent();
                    String cif = partnerElement.getElementsByTagName("cif").item(0).getTextContent();
                    String direccion = partnerElement.getElementsByTagName("direccion").item(0).getTextContent();
                    String telefono = partnerElement.getElementsByTagName("telefono").item(0).getTextContent();
                    String email = partnerElement.getElementsByTagName("email").item(0).getTextContent();
                    String personaDeContacto = partnerElement.getElementsByTagName("persona_de_contacto").item(0).getTextContent();
                    String idZona = partnerElement.getElementsByTagName("id_zona").item(0).getTextContent();
                    System.out.println(idPartners);
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
            fis.close();
        } catch (Exception e) {
            // Manejar excepciones
            System.out.println("Error al leer el contenido del archivo XML: " + e.getMessage());
        }
    }

    private void leerPedidos(File xml) {
        try {
            FileInputStream fis = new FileInputStream(xml);
            // Crear el objeto Document
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(fis);
            // Normalizar el documento
            document.getDocumentElement().normalize();
            // Obtener la lista de nodos "pedido"
            NodeList nodeList = document.getElementsByTagName("pedido");

            arrayPedidos = new String[nodeList.getLength()][8];

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element pedidoElement = (Element) node;
                    // Obtener los elementos internos del pedido
                    String idPedido = pedidoElement.getElementsByTagName("ID_PEDIDO").item(0).getTextContent();
                    String nLinea = pedidoElement.getElementsByTagName("N_LINEA").item(0).getTextContent();
                    String idArticulo = pedidoElement.getElementsByTagName("ID_ARTICULO").item(0).getTextContent();
                    String cantidad = pedidoElement.getElementsByTagName("CANTIDAD").item(0).getTextContent();
                    String descuento = pedidoElement.getElementsByTagName("DESCUENTO").item(0).getTextContent();
                    String precioUd = pedidoElement.getElementsByTagName("PRECIO_UD").item(0).getTextContent();
                    String precioTot = pedidoElement.getElementsByTagName("PRECIO_TOT").item(0).getTextContent();
                    String idTrans = pedidoElement.getElementsByTagName("ID_TRANS").item(0).getTextContent();

                    // Almacenar los datos en el array bidimensional
                    arrayPedidos[i][0] = idPedido;
                    arrayPedidos[i][1] = nLinea;
                    arrayPedidos[i][2] = idArticulo;
                    arrayPedidos[i][3] = cantidad;
                    arrayPedidos[i][4] = descuento;
                    arrayPedidos[i][5] = precioUd;
                    arrayPedidos[i][6] = precioTot;
                    arrayPedidos[i][7] = idTrans;
                }
            }
            fis.close();
        } catch (Exception e) {
            // Manejar excepciones
            System.out.println("Error al leer el contenido del archivo XML: " + e.getMessage());
        }
    }


    public void llenar_fichero(DocumentBuilderFactory factory) {
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            DOMImplementation implementation = builder.getDOMImplementation();
            Document document = implementation.createDocument(null, "Envio", null);
            document.setXmlVersion("1.0");

            // Agregar datos de arrayPedidos
            for (int i = 0; i < arrayPedidos.length; i++) {
                Element pedidoElement = document.createElement("pedido");
                document.getDocumentElement().appendChild(pedidoElement);

                CrearElemento("ID_PEDIDO", arrayPedidos[i][0], pedidoElement, document);
                CrearElemento("N_LINEA", arrayPedidos[i][1], pedidoElement, document);
                CrearElemento("ID_ARTICULO", arrayPedidos[i][2], pedidoElement, document);
                CrearElemento("CANTIDAD", arrayPedidos[i][3], pedidoElement, document);
                CrearElemento("DESCUENTO", arrayPedidos[i][4], pedidoElement, document);
                CrearElemento("PRECIO_UD", arrayPedidos[i][5], pedidoElement, document);
                CrearElemento("PRECIO_TOT", arrayPedidos[i][6], pedidoElement, document);
                CrearElemento("ID_TRANS", arrayPedidos[i][7], pedidoElement, document);
            }

            // Agregar datos de arrayPartner
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

            Source source = new DOMSource(document);
            Result result = new StreamResult(fEnvio);
            indentarXML(source, result);
            System.out.println("Datos guardados en el archivo XML con formato.");
        } catch (Exception e) {
            System.err.println("Error: " + e);
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
            return "Error al leer el contenido del XML";
        }
    }
}