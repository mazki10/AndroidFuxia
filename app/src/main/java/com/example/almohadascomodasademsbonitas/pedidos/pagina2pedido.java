package com.example.almohadascomodasademsbonitas.pedidos;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.almohadascomodasademsbonitas.BBDD.DBconexion;
import com.example.almohadascomodasademsbonitas.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class pagina2pedido extends AppCompatActivity {
    private ArrayList<Pedido> listaPedidos = new ArrayList<>();
    private String partners;
    private String comerciales;
    private String pro_eleguido;
    int producto_eleguido;
    private int contadorIdPedido = 0;  // Contador global para el id_pedido
   // private int contadorNFactura = 0;
    String fecha;
    Double descuento;
    int cantidad_total=0;
    private int precioPorProducto;
    Integer cantidad;
    private DBconexion dbconexion;
    private SQLiteDatabase db;

ArrayList <String> descripcion=new ArrayList<>();
String dni_comercial = "iker";
    double precioTotal = 0;
    double precioUnitario;
    //public pagina2pedido(DBconexion dbHelper) {
       // this.dbHelper = dbHelper;
    //}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pagina2pedidos);
        dbconexion = new  DBconexion(this,"ACAB2",null,1);
        SQLiteDatabase database = dbconexion.getWritableDatabase();


        Button buttonGuardar = findViewById(R.id.bTiN);

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

                actualizarContadoresDesdeXML();

                // Agregar un OnClickListener al botón

            } else {
                contadorIdPedido = 0;
          //      contadorNFactura = 0;
            }
        }


        Cursor cursor = database.rawQuery("SELECT DNI FROM COMERCIALES WHERE NOMBRE = '" + comerciales + "'", null);
        if (cursor != null && cursor.moveToFirst()) {
            int dniColumnIndex = cursor.getColumnIndex("DNI");
            if (dniColumnIndex != -1) {
                dni_comercial = cursor.getString(dniColumnIndex);
            } else {
                // La columna "DNI" no se encontró en el Cursor
                Log.e("Error", "La columna 'DNI' no se encontró en el Cursor");
            }
            cursor.close(); // Es importante cerrar el cursor después de su uso
        }

        buttonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Llamada al método para guardar la información
                guardarEnXML(listaPedidos);
                Intent intent = new Intent(pagina2pedido.this, com.example.almohadascomodasademsbonitas.pedidos.menu_Pedido.class);
                startActivity(intent);
            }
        });
    }


    // Elimina estas líneas de la parte inicial de guardarEnXML
    /*
    contadorIdPedido = 0;
    */



    private boolean isXmlFileExist() {
        File file = new File(getFilesDir(), "pedidos2.xml");
        Toast toast = Toast.makeText(this,String.valueOf(file.exists()),Toast.LENGTH_SHORT);
        toast.show();
        return file.exists();
    }

    // Elimina la línea del contadorIdPedido = 0; al principio del método onCreate.

    private void actualizarContadoresDesdeXML() {
        String contenidoExistente = leerContenidoXML();

        // Busca la posición de la última etiqueta <n_factura>
        int lastNFacturaIndex = contenidoExistente.lastIndexOf("<n_factura>");

        if (lastNFacturaIndex != -1) {
            // Si se encuentra la etiqueta <n_factura>, obtén su valor y actualiza el contador
            int endTagIndex = contenidoExistente.indexOf("</n_factura>", lastNFacturaIndex);
            String nFacturaValue = contenidoExistente.substring(lastNFacturaIndex + 11, endTagIndex);
         //   contadorNFactura = Integer.parseInt(nFacturaValue) + 1;
        }

        // Busca la posición de la última etiqueta <id_pedido>
        int lastIdPedidoIndex = contenidoExistente.lastIndexOf("<id_pedido>");

        if (lastIdPedidoIndex != -1) {
            // Si se encuentra la etiqueta <id_pedido>, obtén su valor y actualiza el contador
            int endTagIndex = contenidoExistente.indexOf("</id_pedido>", lastIdPedidoIndex);
            String idPedidoValue = contenidoExistente.substring(lastIdPedidoIndex + 11, endTagIndex);
            contadorIdPedido = Integer.parseInt(idPedidoValue);
        } else {
            // Si no se encuentra la etiqueta <id_pedido>, establece el contador en 0
            contadorIdPedido = 0;
        }
    }


// Asegúrate de que el contadorIdPedido = 0; esté presente solo una vez al principio de la clase.


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

            // Obtén el precio unitario del producto desde la base de datos
            precioUnitario = obtenerPrecioUnitarioDeBaseDeDatos(pedido.getImagen());
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
        textViewTotalPrecioUnitario.setText(String.valueOf(totalPrecioUnitario) + "€");
    }

    private double obtenerPrecioUnitarioDeBaseDeDatos(String nombreProducto) {
        double precioUnitario = 0.0;
        SQLiteDatabase database = dbconexion.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT PRECIO_VENTA FROM ARTICULOS WHERE DESCRIPCION = '"+nombreProducto+"'",null);
        if (cursor != null && cursor.moveToFirst()) {
            precioUnitario = cursor.getDouble(0);
            cursor.close();
        }
        return precioUnitario;
    }


    private void guardarEnXML(ArrayList<Pedido> listaPedidos) {
        try {
            // Obtener la fecha actual
            String fechaActual = fecha;

            // Nuevo pedido con el campo id_pedido
            String xmlData="";

            // Incrementa el contador solo si hay pedidos en la lista
            contadorIdPedido++;
            Toast toast = Toast.makeText(this,"empieza la parte de xml",Toast.LENGTH_SHORT);
            toast.show();
            // Si el archivo XML no existe o ha sido borrado, crea la etiqueta pedidos
            // Si el archivo XML no existe o ha sido borrado, crea la etiqueta pedidos

            if (!isXmlFileExist()) {
                xmlData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<pedidos>\n";
            } else {
                // Si el archivo XML ya existe, lee su contenido existente
                String contenidoExistente = leerContenidoXML().trim(); // Asegúrate de que no haya espacios en blanco alrededor

                // Busca la posición de la última etiqueta </pedidos>
                int lastPedidosIndex = contenidoExistente.lastIndexOf("</pedidos>");

                if (lastPedidosIndex == -1) {
                    // Si no se encuentra la última etiqueta </pedidos>, usa el contenido existente tal como está
                    xmlData += contenidoExistente;
                } else {
                    // Trunca el contenido hasta la última etiqueta </pedidos>
                    xmlData += contenidoExistente.substring(0, lastPedidosIndex);
                }
            }


            // Continúa con la lógica para agregar nuevos datos...

            // Inicia un nuevo pedido solo si hay productos en la lista
            if (!listaPedidos.isEmpty()) {
                xmlData += "  <pedido>\n";
                xmlData += "    <id_pedido>" + contadorIdPedido + "</id_pedido>\n";
                xmlData += "    <id_partner>" + partners + "</id_partner>\n";
                xmlData += "    <id_comercial>" + comerciales + "</id_comercial>\n";





                for (int i = 0; i < listaPedidos.size(); i++) {
                    cantidad_total += listaPedidos.get(i).getCantidad();
                    // Agregar información de productos
                    xmlData += "    <producto>\n";
                    xmlData += "      <id_articulo>" + listaPedidos.get(i).getImagen() + "</id_articulo>\n";
                    xmlData += "      <cantidad>" + listaPedidos.get(i).getCantidad() + "</cantidad>\n";
                    // Obtener el precio unitario del producto desde la base de datos
                    double precioUnitario = obtenerPrecioUnitarioDeBaseDeDatos(listaPedidos.get(i).getImagen());
                    xmlData += "      <precio_un>" + precioUnitario + "</precio_un>\n";
                    xmlData += "    </producto>\n";
                    descripcion.add(listaPedidos.get(i).getImagen());
                    cantidad = listaPedidos.get(i).getCantidad();
                    precioTotal += listaPedidos.get(i).getCantidad() * precioUnitario;
                }


                if (cantidad_total>=1&&cantidad_total<=2){
                    descuento = 1.0;
                    precioTotal= precioTotal*1.0;
                }else if(cantidad_total>=3&&cantidad_total<=7) {
                    descuento = 15.0;
                    precioTotal= precioTotal*0.15;
                } else if (cantidad_total>=7&&cantidad_total<=20) {
                    descuento = 25.0;
                    precioTotal= precioTotal*0.25;
                } else if (cantidad_total>20) {
                    descuento = 50.0;
                    precioTotal= precioTotal*0.5;

                }

                // Agregar la fecha, precio total y número de factura
                xmlData += "    <fecha>" + fechaActual + "</fecha>\n";
                xmlData += "    <precio_total>" + precioTotal + "</precio_total>\n";
             //   xmlData += "    <n_factura>" + contadorNFactura + "</n_factura>\n";

                // Cierra el pedido
                xmlData += "  </pedido>\n";
            }

            // Cierra la etiqueta pedidos
            xmlData += "</pedidos>\n";

            // Sobrescribir el archivo
            FileOutputStream fos = openFileOutput("pedidos2.xml", MODE_PRIVATE);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fos);
            outputStreamWriter.write(xmlData);
Toast toast2 = Toast.makeText(this,"empieza la parte de BBDD",Toast.LENGTH_SHORT);
toast2.show();
            // Dentro de tu método guardarEnXML después de crear el xmlData
// Crea o obtén una instancia de la base de datos
     //       pagina2pedido pagina2pedidoActivity = new pagina2pedido(dbHelper);

            dbconexion = new DBconexion(this, "ACAB2.db", null, 1);
            db = dbconexion.getWritableDatabase();
            db.beginTransaction();
            try {

                String query = "SELECT MAX(ID_PEDIDO) FROM CAB_PEDIDOS";
                Cursor cursor = db.rawQuery(query, null);
                int nuevoId = 1;
                // Mueve el cursor al primer resultado
                if (cursor.moveToFirst()) {
                    // Obtiene el valor máximo actual
                    int maxId = cursor.getInt(0);
                    // Calcula el nuevo ID agregándole uno al máximo actual
                    nuevoId = maxId + 1;
                }
                String insertQuery = "INSERT INTO CAB_PEDIDOS (ID_PEDIDO, ID_COMERCIAL, ID_PARTNER, DESCRIPCION, FECHA_PEDIDO, FECHA_ENVIO, ENTREGADO) " +
                        "VALUES (" + nuevoId + ", '" + dni_comercial + "', " + partners + ", '" +
                        descripcion.get(0).toString() + "', '" + fecha + "', '" + fecha + "', 1)";
                db.execSQL(insertQuery);
                Log.d("Insertion", "Inserting cab_pedido: " + insertQuery);

                String insertQuery1 = "UPDATE ARTICULOS SET EXISTENCIAS = (EXISTENCIAS - "+ cantidad +") WHERE DESCRIPCION = '"+ descripcion.get(0).toString()+"'";
                db.execSQL(insertQuery1);

                for (int i = 0; i < listaPedidos.size(); i++) {
                    String insertQuery2 = "INSERT INTO LIN_PEDIDOS (ID_PEDIDO, ID_LINEA, CANTIDAD, DESCUENTO, PRECIO_UN, PRECIO_TOTAL) " +
                            "VALUES (" + nuevoId + ", " + (i + 1) + ", " + listaPedidos.get(i).getCantidad() + ", " +
                            descuento + ", " + listaPedidos.get(i).getPrecio_un() + ", " + listaPedidos.get(i).getPrecio_total() + ")";
                    db.execSQL(insertQuery2);
                    Log.d("Insertion", "Inserting lin_pedido: " + insertQuery);
                }



                db.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error al añadir datos a la base de datos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            } finally {
                db.endTransaction();
            }

            // Cerrar y flushear el OutputStreamWriter
            outputStreamWriter.flush();
            outputStreamWriter.close();
            fos.close();

            // Leer y mostrar el contenido del archivo para verificar
            String contenido = leerContenidoXML();
            Toast.makeText(this, "Información añadida a XML correctamente:\n" + contenido, Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            // Manejar excepciones
            Toast.makeText(this, "Error al añadir datos al XML: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private SQLiteDatabase getWritableDatabase() {
        // Verificar si dbHelper está inicializado
        if (dbconexion != null) {
            // Llamar al método getWritableDatabase() de DBconexion y retornar el resultado
            return dbconexion.getWritableDatabase();
        } else {
            // Si dbHelper no está inicializado, retornar null o manejar el caso según tus necesidades
            return null;
        }
    }
    private String leerContenidoXML() {
        try {
            FileInputStream fis = openFileInput("pedidos2.xml");
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
