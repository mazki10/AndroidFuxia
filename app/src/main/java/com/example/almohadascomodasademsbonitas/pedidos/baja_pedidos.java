package com.example.almohadascomodasademsbonitas.pedidos;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class baja_pedidos extends AppCompatActivity {

    private ArrayAdapter<String> adapter;
    ArrayList<String>datosDeXml;
    int idPedido=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bajas_pedido);

        //copiarXmlDesdeAssets();

        ListView listView = findViewById(R.id.lvPartners);
        //datosDeXml = leerDatosDesdeXmlEnMemoriaInterna();

        // Crea un ArrayAdapter para enlazar los datos a la interfaz de usuario

        int cont = contPedidos();

        if (cont!=0){
            datosDeXml=mostrarDatosBBDD();
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, datosDeXml);
            listView.setAdapter(adapter);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Obtiene el valor del elemento en la posición seleccionada
                String selectedItem = (String) parent.getItemAtPosition(position);

                // Extraer la ID del pedido
                String[] lines = selectedItem.split("\n");
                if (lines.length > 0) {
                    String idPedidoStr = lines[0].split(":")[1].trim();
                    idPedido = Integer.parseInt(idPedidoStr);

                    // Aquí puedes hacer lo que necesites con la idPedido, como mostrarla en un TextView, etc.
                }
            }
        });




        Button btnBorrar = findViewById(R.id.btNuevo);
        //Button btRrfs = findViewById(R.id.btRfr2);

        btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SparseBooleanArray checkedItemPositions = listView.getCheckedItemPositions();

                // Iterar sobre las posiciones seleccionadas
                for (int i = 0; i < checkedItemPositions.size(); i++) {
                    int position = checkedItemPositions.keyAt(i);

                    // Verificar si el elemento en esta posición está seleccionado
                    if (checkedItemPositions.valueAt(i)) {
                        idPedido = obtenerIdPedido(datosDeXml.get(position));

                        // Eliminar el elemento de la lista
                        datosDeXml.remove(position);
                        borrarenBBDD(idPedido);
                    }
                }

                // Notificar al adaptador sobre los cambios
                adapter.notifyDataSetChanged();

                // Vuelve a cargar los datos de la base de datos
                datosDeXml = mostrarDatosBBDD();
                adapter.clear();
                adapter.addAll(datosDeXml);
                adapter.notifyDataSetChanged();
            }
        });


     /*   btRrfs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> datosDeXml = leerDatosDesdeXmlEnMemoriaInterna();
                adapter.clear();
                adapter.addAll(datosDeXml);
                adapter.notifyDataSetChanged();
            }
        });*/
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








    // Método para obtener el id_pedido del texto del pedido
    private int obtenerIdPedido(String pedido) {
        int idPedido = -1; // Valor por defecto si no se encuentra el ID_PEDIDO

        // Dividir la cadena de texto del pedido en líneas
        String[] lines = pedido.split("\n");
        if (lines.length > 0) {
            // Iterar sobre las líneas para encontrar la que contiene el ID del pedido
            for (String line : lines) {
                if (line.startsWith("Id_Pedido:")) {
                    // Extraer el ID del pedido de la línea y convertirlo a entero
                    String idPedidoStr = line.split(":")[1].trim();
                    idPedido = Integer.parseInt(idPedidoStr);
                    break; // Salir del bucle una vez que se ha encontrado el ID del pedido
                }
            }
        }

        return idPedido;
    }


    public void borrarenBBDD(int id){
        DBconexion bbdd = new  DBconexion(this,"ACAB2.db",null,1);
        SQLiteDatabase database = bbdd.getWritableDatabase();

        // Define la cláusula WHERE para eliminar el pedido con el ID proporcionado
        String whereClause = "ID_PEDIDO = ?";
        // Define el valor del ID como un array de strings
        String[] whereArgs = {String.valueOf(id)};

        // Ejecuta la eliminación
        int rowsDeleted = database.delete("CAB_PEDIDOS", whereClause, whereArgs);
        Log.d("BORRAR", "Filas eliminadas: " + rowsDeleted);
        int rowsDeletedL = database.delete("LIN_PEDIDOS", whereClause, whereArgs);
        Log.d("BORRAR", "Filas eliminadas: " + rowsDeleted);
    }

}
