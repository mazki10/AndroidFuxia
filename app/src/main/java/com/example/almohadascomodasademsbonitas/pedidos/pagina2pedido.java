package com.example.almohadascomodasademsbonitas.pedidos;

import android.os.Bundle;
import android.util.Xml;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.almohadascomodasademsbonitas.R;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class pagina2pedido extends AppCompatActivity {
    private ArrayList<Pedido> listaPedidos = new ArrayList<>();
    private int partners;
    private String comerciales;
    private String pro_eleguido;
    int producto_eleguido;
    private int contadorIdPedido = 1;  // Contador global para el id_pedido
    private int contadorNFactura = 1;
    String fecha;

    private int precioPorProducto;
    private int precioTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pagina2pedidos);

        // Obtiene los datos del Bundle
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            listaPedidos = (ArrayList<Pedido>) extras.getSerializable("pedidos");
            fecha = extras.getString("fecha");
            partners =  extras.getInt("partners");
            comerciales =  extras.getString("comerciales");
            pro_eleguido = extras.getString("pro_elegido");
            mostrarPedidosEnListViews();
            producto_eleguido = Integer.parseInt(pro_eleguido);

            // Verifica si el archivo XML ya existe
            if (!isXmlFileExist()) {
                try {
                    crearYGuardarXML();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private boolean isXmlFileExist() {
        File file = new File(getFilesDir(), "pedidos.xml");
        return file.exists();
    }

    private void mostrarPedidosEnListViews() {
        // Obtén referencias a los ListViews en tu layout
        ListView listViewProductos = findViewById(R.id.listviewProductos);
        ListView listViewCantidad = findViewById(R.id.listviewCantidad);
        ListView listViewDescuento = findViewById(R.id.listviewDescuento);
        ListView listViewPrecioUnitario = findViewById(R.id.listviewPrecioUnitario);

        // Crea ArrayLists para almacenar la información de los pedidos
        ArrayList<String> nombresProductos = new ArrayList<>();
        ArrayList<String> cantidades = new ArrayList<>();
        ArrayList<String> descuentos = new ArrayList<>();
        ArrayList<String> preciosUnitarios = new ArrayList<>();  // Nuevo ArrayList para precios unitarios

        int totalPrecioUnitario = 0;  // Variable para almacenar la suma de precios unitarios

        // Itera sobre la lista de pedidos y agrega la información correspondiente a los ArrayLists
        for (Pedido pedido : listaPedidos) {
            nombresProductos.add(pedido.getImagen());
            cantidades.add(String.valueOf(pedido.getCantidad()));
            // Agrega "0" como descuento para cada pedido (puedes ajustar esto según tus necesidades)
            descuentos.add("0");

            int precioUnitario = 30;  // Valor fijo de 30 para cada pedido
            preciosUnitarios.add(String.valueOf(precioUnitario));
            totalPrecioUnitario += precioUnitario;  // Suma el precio unitario al total
        }

        // Crea ArrayAdapter para cada ListView
        ArrayAdapter<String> adapterProductos = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nombresProductos);
        ArrayAdapter<String> adapterCantidades = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cantidades);
        ArrayAdapter<String> adapterDescuentos = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, descuentos);
        ArrayAdapter<String> adapterPrecioUnitario = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, preciosUnitarios);

        // Asigna los adapters a los ListViews
        listViewProductos.setAdapter(adapterProductos);
        listViewCantidad.setAdapter(adapterCantidades);
        listViewDescuento.setAdapter(adapterDescuentos);
        listViewPrecioUnitario.setAdapter(adapterPrecioUnitario);

        // Muestra la suma total de precios unitarios en textView9
        TextView textViewTotalPrecioUnitario = findViewById(R.id.textView9);
        textViewTotalPrecioUnitario.setText(String.valueOf(totalPrecioUnitario)+"€");
    }


    private void crearYGuardarXML() throws IOException {
        XmlSerializer xmlSerializer = Xml.newSerializer();
        try {
            // Abre un nuevo FileOutputStream y un OutputStreamWriter para escribir en el archivo XML
            FileOutputStream fos;
            File file = new File(getFilesDir(), "pedidos.xml");

            if (file.exists()) {
                // Si el archivo ya existe, abre el FileOutputStream en modo APPEND
                fos = openFileOutput("pedidos.xml", MODE_APPEND);
            } else {
                // Si el archivo no existe, crea un nuevo FileOutputStream en modo PRIVATE
                fos = openFileOutput("pedidos.xml", MODE_PRIVATE);
            }

            OutputStreamWriter osw = new OutputStreamWriter(fos);

          //  File file = new File(getFilesDir(), "pedidos.xml");
            String filePath = file.getAbsolutePath();
            System.out.println("La ubicación exacta del archivo pedidos.xml es: " + filePath);


            // Inicializa el XmlSerializer con el OutputStreamWriter
            xmlSerializer.setOutput(osw);

            // Comienza el documento XML con la etiqueta raíz
            xmlSerializer.startDocument("UTF-8", true);
            xmlSerializer.startTag(null, "pedidos");
            precioPorProducto=0;
            // Itera sobre la lista de pedidos y agrega cada pedido como un elemento en el archivo XML
            for (Pedido pedido : listaPedidos) {
                xmlSerializer.startTag(null, "pedido");

                // Agrega la información específica del pedido como subelementos
                xmlSerializer.startTag(null, "id_pedido");
                xmlSerializer.text(String.valueOf(contadorIdPedido));  // Utiliza el contador para id_pedido
                xmlSerializer.endTag(null, "id_pedido");

                xmlSerializer.startTag(null, "id_partner");
                xmlSerializer.text(String.valueOf(partners));  // Puedes ajustar según tus necesidades
                xmlSerializer.endTag(null, "id_partner");

                xmlSerializer.startTag(null, "id_comercial");
                xmlSerializer.text(comerciales);  // Puedes ajustar según tus necesidades
                xmlSerializer.endTag(null, "id_comercial");
                precioTotal = 0;
                // Itera sobre los productos de cada pedido
                for (int i = 0; i < producto_eleguido; i++) {
                    xmlSerializer.startTag(null, "producto");

                    xmlSerializer.startTag(null, "descripcion");
                    xmlSerializer.text(pedido.getImagen());  // Obtén la descripción del producto desde el pedido
                    xmlSerializer.endTag(null, "descripcion");

                    xmlSerializer.startTag(null, "cantidad");
                    xmlSerializer.text(String.valueOf(pedido.getCantidad()));  // Puedes ajustar según tus necesidades
                    xmlSerializer.endTag(null, "cantidad");

                    xmlSerializer.startTag(null, "descuento");
                    xmlSerializer.text("0");  // Puedes ajustar según tus necesidades
                    xmlSerializer.endTag(null, "descuento");

                    xmlSerializer.startTag(null, "precio_un");
                    xmlSerializer.text("30");  // Puedes ajustar según tus necesidades
                    xmlSerializer.endTag(null, "precio_un");
                    precioPorProducto=pedido.getCantidad()*30;
                    xmlSerializer.endTag(null, "producto");

                    precioTotal+=precioPorProducto;
                }

                xmlSerializer.startTag(null, "fecha");
                xmlSerializer.text(fecha);  // Puedes ajustar según tus necesidades
                xmlSerializer.endTag(null, "fecha");

                xmlSerializer.startTag(null, "precio_total");
                xmlSerializer.text(String.valueOf(precioTotal));  // Puedes ajustar según tus necesidades
                xmlSerializer.endTag(null, "precio_total");

                xmlSerializer.startTag(null, "n_factura");
                xmlSerializer.text(String.valueOf(contadorNFactura));  // Puedes ajustar según tus necesidades
                xmlSerializer.endTag(null, "n_factura");

                xmlSerializer.endTag(null, "pedido");

                contadorNFactura++;
                contadorIdPedido++;  // Incrementa el contador de id_pedido
            }

            // Cierra las etiquetas y finaliza el documento XML
            xmlSerializer.endTag(null, "pedidos");
            xmlSerializer.endDocument();

            // Cierra el OutputStreamWriter
            osw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


