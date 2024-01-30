package com.example.almohadascomodasademsbonitas.pedidos;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.almohadascomodasademsbonitas.R;
import com.example.almohadascomodasademsbonitas.pedidos.Pedido;
import com.example.almohadascomodasademsbonitas.pedidos.PedidoAdapter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

// ...

public class baja_pedidos extends AppCompatActivity {

    RecyclerView listaPedidos;
    private ArrayList<Pedido> pedidos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bajas_pedido);

        listaPedidos = findViewById(R.id.recyclerView);
        pedidos = new ArrayList<>();  // Inicializa la lista aquí
        cargarDatosDesdeXML();

        // Configurar el RecyclerView con el adaptador
        PedidoAdapter pedidoAdapter = new PedidoAdapter(pedidos);
        listaPedidos.setAdapter(pedidoAdapter);
        listaPedidos.setLayoutManager(new LinearLayoutManager(this));
    }




    private void cargarDatosDesdeXML() {
        try {
            // Obtener la referencia al archivo XML en el directorio 'data'
            InputStream inputStream = getAssets().open("data/data/pedidos.xml");

            // Crear un constructor de documentos
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            // Parsear el archivo XML
            Document doc = dBuilder.parse(inputStream);
            doc.getDocumentElement().normalize();

            // Obtener la lista de nodos 'pedido'
            NodeList pedidoNodes = doc.getElementsByTagName("pedido");

            // Inicializar la lista de pedidos
            pedidos = new ArrayList<>();

            // Iterar sobre los nodos 'pedido'
            for (int i = 0; i < pedidoNodes.getLength(); i++) {
                Element pedidoElement = (Element) pedidoNodes.item(i);

                // Obtener datos del pedido
                int idPedido = Integer.parseInt(pedidoElement.getElementsByTagName("id_pedido").item(0).getTextContent());
                int idPartner = Integer.parseInt(pedidoElement.getElementsByTagName("id_partner").item(0).getTextContent());
                String idComercial = pedidoElement.getElementsByTagName("id_comercial").item(0).getTextContent();
                LocalDate fecha = LocalDate.parse(pedidoElement.getElementsByTagName("fecha").item(0).getTextContent());  // Ajusta según el formato de fecha
                double precioTotal = Double.parseDouble(pedidoElement.getElementsByTagName("precio_total").item(0).getTextContent());

                // Obtener detalles de los productos dentro del pedido
                NodeList productoNodes = pedidoElement.getElementsByTagName("producto");
                ArrayList<Pedido> productos = new ArrayList<>();

                for (int j = 0; j < productoNodes.getLength(); j++) {
                    Element productoElement = (Element) productoNodes.item(j);
                    String descripcion = productoElement.getElementsByTagName("descripcion").item(0).getTextContent();
                    int cantidad = Integer.parseInt(productoElement.getElementsByTagName("cantidad").item(0).getTextContent());
                    double descuento = Double.parseDouble(productoElement.getElementsByTagName("descuento").item(0).getTextContent());
                    double precioUn = Double.parseDouble(productoElement.getElementsByTagName("precio_un").item(0).getTextContent());

                    // Crear un objeto Pedido y agregarlo a la lista de productos dentro del pedido
                    Pedido producto = new Pedido("", cantidad, 0, 0, 0, 0, descripcion, descuento, precioUn, LocalDate.now(), 0);
                    productos.add(producto);
                }

                // Crear un objeto Pedido y agregarlo a la lista
                Pedido pedido = new Pedido("", 0, idPedido, 0, idPartner, Integer.parseInt(idComercial), "", 0, 0, fecha, precioTotal);
                pedido.setProductos(productos);
                pedidos.add(pedido);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}