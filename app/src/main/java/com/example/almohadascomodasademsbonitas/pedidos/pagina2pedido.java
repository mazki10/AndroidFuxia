package com.example.almohadascomodasademsbonitas.pedidos;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.almohadascomodasademsbonitas.pedidos.Pedido;
import com.example.almohadascomodasademsbonitas.R;

import org.xmlpull.v1.XmlSerializer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class pagina2pedido extends AppCompatActivity {
    private ArrayList<Pedido> listaPedidos = new ArrayList<>();
    private String partners;
    private String comerciales;
    private String pro_eleguido;
    int producto_eleguido;
    private int contadorIdPedido = 0;  // Contador global para el id_pedido
    private int contadorNFactura = 1;
    String fecha;

    private int precioPorProducto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pagina2pedidos);

        // Obtiene los datos del Bundle
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            listaPedidos = (ArrayList<Pedido>) extras.getSerializable("pedidos");
            fecha = extras.getString("fecha");
            partners =  extras.getString("partners");
            comerciales =  extras.getString("comerciales");
            pro_eleguido = extras.getString("pro_elegido");
            mostrarPedidosEnListViews();
            producto_eleguido = Integer.parseInt(pro_eleguido);

            // Verifica si el archivo XML ya existe
            if (isXmlFileExist()) {
                // Elimina el archivo existente si es necesario
                Button buttonGuardar = findViewById(R.id.button);

                // Agregar un OnClickListener al botón
                buttonGuardar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Llamada al método para guardar la información
                        guardarEnXML(listaPedidos);
                    }
                });
            }


        }
      /*  Button buttonGuardar = findViewById(R.id.button);

        // Agregar un OnClickListener al botón
        buttonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Llamada al método para guardar la información
                guardarEnXML(listaPedidos);
            }
        });*/
    }

    private void borrarDatosXML() {
        File file = new File(getFilesDir(), "pedidos.xml");
        if (file.exists()) {
            deleteFile("pedidos.xml");
            Toast.makeText(this, "Datos XML eliminados correctamente.", Toast.LENGTH_SHORT).show();

            // Vuelve a crear el documento XML
            guardarEnXML(listaPedidos);
        } else {
            Toast.makeText(this, "No hay datos XML para eliminar.", Toast.LENGTH_SHORT).show();
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


    private void guardarEnXML(ArrayList<Pedido> listaPedidos) {
        try {
            // Obtener la fecha actual
            String fechaActual = fecha;

            // Nuevo pedido con el campo id_pedido
            String xmlData = "";

            // Si el archivo XML no existe o ha sido borrado, crea la etiqueta pedidos
            if (!isXmlFileExist()) {
                xmlData = "<pedidos>\n";
            } else {
                // Si el archivo XML ya existe, lee su contenido existente
                String contenidoExistente = leerContenidoXML().trim(); // Asegúrate de que no haya espacios en blanco alrededor

                // Busca la posición de la última etiqueta </pedido>
                int lastPedidoIndex = contenidoExistente.lastIndexOf("</pedido>");

                if (lastPedidoIndex == 0) {
                    // Trunca el contenido hasta la última etiqueta </pedido>
                    xmlData = contenidoExistente.substring(0, lastPedidoIndex);
                } else {
                    // Si no se encuentra la última etiqueta </pedido>, usa el contenido existente tal como está
                    xmlData = contenidoExistente;
                }
            }
            // Continúa con la lógica para agregar nuevos datos...

            // Cierra el pedido anterior si ya existe
            // Cierra el pedido anterior si ya existe
            if (contadorIdPedido > 1) {
                xmlData += "  </pedido>\n";
            }

// Inicia un nuevo pedido
            xmlData += "  <pedido>\n";
            xmlData += "    <id_pedido>" + contadorIdPedido + "</id_pedido>\n";
            xmlData += "    <id_partner>" + partners + "</id_partner>\n";
            xmlData += "    <id_comercial>" + comerciales + "</id_comercial>\n";

            int precioTotal = 0;

            for (int i = 0; i < listaPedidos.size(); i++) {
                // Agregar información de productos (puedes adaptar esta lógica según tus necesidades)
                xmlData += "    <producto>\n";
                xmlData += "      <descripcion>" + listaPedidos.get(i).getImagen() + "</descripcion>\n";
                xmlData += "      <cantidad>" + listaPedidos.get(i).getCantidad() + "</cantidad>\n";
                xmlData += "      <descuento>0</descuento>\n";
                xmlData += "      <precio_un>30</precio_un>\n";
                xmlData += "    </producto>\n";

                precioTotal += listaPedidos.get(i).getCantidad() * 30;
            }

// Agregar la fecha, precio total y número de factura
            xmlData += "    <fecha>" + fechaActual + "</fecha>\n";
            xmlData += "    <precio_total>" + precioTotal + "</precio_total>\n";
            xmlData += "    <n_factura>" + contadorNFactura + "</n_factura>\n";

// Cierra el pedido
            xmlData += "  </pedido>\n";

            // Cierra la etiqueta pedidos
            xmlData += "</pedidos>\n";

            // Sobrescribir el archivo
            FileOutputStream fos = openFileOutput("pedidos.xml", MODE_PRIVATE);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fos);
            outputStreamWriter.write(xmlData);

            // Cerrar y flushear el OutputStreamWriter
            outputStreamWriter.flush();
            outputStreamWriter.close();
            fos.close();

            // Incrementa los contadores solo si se ha creado un nuevo pedido
            contadorNFactura++;
            contadorIdPedido++;

            // Leer y mostrar el contenido del archivo para verificar
            String contenido = leerContenidoXML();
            Toast.makeText(this, "Información añadida a XML correctamente:\n" + contenido, Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            // Manejar excepciones
            Toast.makeText(this, "Error al añadir datos al XML: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private String leerContenidoXML() {
        try {
            FileInputStream fis = openFileInput("pedidos.xml");
            InputStreamReader inputStreamReader = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }

            bufferedReader.close();
            inputStreamReader.close();
            fis.close();

            return stringBuilder.toString();

        } catch (Exception e) {
            // Manejar excepciones
            return "Error al leer el contenido del archivo XML: " + e.getMessage();
        }
    }
}




