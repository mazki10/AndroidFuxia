package com.example.almohadascomodasademsbonitas.pedidos;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.almohadascomodasademsbonitas.BBDD.DBconexion;
import com.example.almohadascomodasademsbonitas.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class ModificarDatosActivity extends AppCompatActivity {

    private EditText editTextComercial;
    private EditText editTextPartner;
    private EditText editTextDescripcion;
    private EditText editTextFechaPedido;
    private EditText editTextFechaEnvio;
    private Button btnGuardarCambios;
    private ArrayList<String> datosOriginales; // Guardar los datos originales
    private int position;
    String comercial;
    String partner;
    String descripcion;
    String fechaPedido;
    String fechaEnvio;
    String fecha;
    double precioFinal;
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

        // Recuperar los datos originales del Intent
        datosOriginales = getIntent().getStringArrayListExtra("PEDIDO_SELECCIONADO");
        position = getIntent().getIntExtra("POSICION_PEDIDO", -1);
         fecha = getIntent().getStringExtra("FECHA_PEDIDO");
         precioFinal = getIntent().getDoubleExtra("PRECIO_FINAL", 0.0);

        // Mostrar los datos originales en los EditText
        if (datosOriginales != null && datosOriginales.size() >= 5) {
            editTextComercial.setText(datosOriginales.get(0));
            editTextPartner.setText(datosOriginales.get(1));
            editTextFechaPedido.setText(fechaPedido);
            editTextFechaEnvio.setText(Double.toString(precioFinal));
        }

        // Configurar el botón para guardar los cambios
        btnGuardarCambios.setOnClickListener(v -> guardarCambios());
    }


    private ArrayList<String> leerDatosDesdeXmlEnMemoriaInterna() {
        ArrayList<String> datosDeXml = new ArrayList<>();

        try {
            FileInputStream fis = openFileInput("pedidos.xml");

            // Crea un XmlPullParser
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(fis, null);

            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG && "pedido".equals(parser.getName())) {
                    // Comienza un nuevo elemento pedido
                    StringBuilder currentData = new StringBuilder();

                    while (!(eventType == XmlPullParser.END_TAG && "pedido".equals(parser.getName()))) {
                        // Lee el contenido del pedido
                        if (eventType == XmlPullParser.TEXT) {
                            String text = parser.getText();

                            // Agrega el contenido del texto al StringBuilder
                            currentData.append(text).append("\n");
                        }
                        eventType = parser.next();
                    }

                    // Fin del elemento pedido, agrega la cadena con el contenido del pedido
                    datosDeXml.add(currentData.toString().trim());
                }

                eventType = parser.next();
            }

            fis.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return datosDeXml;
    }

    private void guardarDatosEnXmlEnMemoriaInterna(ArrayList<String> datosDeXml) {
        try {
            FileOutputStream fos = openFileOutput("pedidos.xml", MODE_PRIVATE);
            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(fos, "UTF-8");
            serializer.startDocument(null, Boolean.TRUE);
            serializer.startTag(null, "pedidos");

            for (String pedido : datosDeXml) {
                serializer.startTag(null, "pedido");
                serializer.text(pedido);
                serializer.endTag(null, "pedido");
            }

            serializer.endTag(null, "pedidos");
            serializer.endDocument();
            serializer.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void guardarCambios() {
        // Obtener el ID del pedido seleccionado
        int idPedido = getIntent().getIntExtra("ID_PEDIDO", -1);
        if (idPedido == -1) {
            // Manejar el caso en el que no se pasa el ID del pedido
            Toast.makeText(this, "Error: ID del pedido no encontrado", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener los datos actualizados del pedido de los EditText
        comercial = editTextComercial.getText().toString().trim();
        partner = editTextPartner.getText().toString().trim();
        fechaPedido = editTextFechaPedido.getText().toString().trim();
        fechaEnvio = editTextFechaEnvio.getText().toString().trim();

        // Recuperar la lista actual de pedidos del XML
        ArrayList<String> datosDeXml = leerDatosDesdeXmlEnMemoriaInterna();

        // Crear el pedido actualizado con los nuevos datos
        String pedidoActualizado = "Id_Pedido: "+idPedido +"\n" +
                "Comercial: " + comercial + "\n " +
                "Partner: " + partner + "\n" +
                "Fecha:" + fecha + "\n" +
                "Precio Total:" + precioFinal;

        // Actualizar el pedido en la lista si existe
        if (position != -1 && position < datosDeXml.size()) {
            datosDeXml.set(position, pedidoActualizado);
        }

        // Guardar los cambios en el XML
        guardarDatosEnXmlEnMemoriaInterna(datosDeXml);

        // Modificar la base de datos
        modificarBaseDeDatos(idPedido, comercial, partner, descripcion, fechaPedido, fechaEnvio);

        // Mostrar un mensaje de éxito
        Toast.makeText(this, "Cambios guardados", Toast.LENGTH_SHORT).show();

        // Devolver los datos actualizados al pedido seleccionado
        Intent intent = new Intent(ModificarDatosActivity.this,com.example.almohadascomodasademsbonitas.pedidos.modificar_pedido.class);
        intent.putExtra("PEDIDO_ACTUALIZADO", pedidoActualizado);
        startActivity(intent);
        // Cerrar la actividad de modificación
        finish();
    }


    private void modificarBaseDeDatos(int  idPedido,String comercial, String partner, String descripcion, String fechaPedido, String fechaEnvio) {
        // Abrir la base de datos en modo escritura
        DBconexion bd = new DBconexion(this,"ACAB2.db",null,1);
        SQLiteDatabase db = bd.getWritableDatabase();

        // Crear un nuevo mapa de valores, donde los nombres de las columnas son las claves
        Cursor cursor = db.rawQuery("UPDATE TABLE CAB_PEDIDOS " +
                "SET ID_COMERCIAL='"+comercial+"', " +
                "SET ID_PARTNER="+Integer.parseInt(partner)+"" +
                ", SET FECHA_PEDIDO = TO_DATE('DD/MM/YYYY','"+fechaPedido+"')" +
                ",SET  FECHA_ENVIO = TO_DATE('DD/MM/YYYY','"+fechaEnvio+"')" +
                "WHERE ID_PEDIDO= "+idPedido+"",null);
        cursor.close();
    }
}
