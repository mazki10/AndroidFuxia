package com.example.almohadascomodasademsbonitas.pedidos;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.almohadascomodasademsbonitas.BBDD.DBconexion;
import com.example.almohadascomodasademsbonitas.R;

public class ModificarDatosActivity extends AppCompatActivity {

    private EditText editTextComercial;
    private EditText editTextPartner;
    private EditText editTextDescripcion;
    private EditText editTextFechaPedido;
    private EditText editTextFechaEnvio;
    private Button btnGuardarCambios;
    String comercial;
    String partner;
    String fecha_envio;
String fecha_pedido;
    int idPedido;
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

        btnGuardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtiene el texto de los EditText y lo almacena en variables
                comercial = editTextComercial.getText().toString();
                partner = editTextPartner.getText().toString();
                fecha_envio  = editTextFechaEnvio.getText().toString();
                fecha_pedido  = editTextFechaPedido.getText().toString();
                 idPedido = getIntent().getIntExtra("ID_PEDIDO", -1); // -1 es el valor predeterminado si no se encuentra el extra

                // Aquí puedes realizar cualquier operación con los datos ingresados
                // Por ejemplo, mostrar los valores en un Toast
                Toast.makeText(ModificarDatosActivity.this, "Texto 1: " + comercial + ", Texto 2: " + partner, Toast.LENGTH_SHORT).show();
                ModificarBBDD();
            }
        });
    }



    public void ModificarBBDD(){
        DBconexion bbdd = new  DBconexion(this,"ACAB2.db",null,1);
        SQLiteDatabase database = bbdd.getWritableDatabase();

        Cursor linea = database.rawQuery("UPDATE CAB_PEDIDOS SET ID_COMERCIAL = "+comercial+",SET ID_PARTNER = "+Integer.parseInt(partner)+",SET FECHA_PEDIDO="+fecha_pedido+",SET FECHA_ENVIO="+fecha_envio+"WHERE ID_PEDIDO = "+idPedido,null);
        linea.close();
    }


}
