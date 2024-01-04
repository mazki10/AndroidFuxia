package com.example.almohadascomodasademsbonitas;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class partners extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.partners);

        // Obtén una referencia al ListView en tu diseño
        ListView listView = findViewById(R.id.lvPartners);

        // Crea una lista de datos de ejemplo (puedes reemplazarlo con tus datos reales)
        ArrayList<String> datosDeEjemplo = new ArrayList<>();
        datosDeEjemplo.add("Nombre 1 - Dirección 1 - Teléfono 1 - Email 1 - Comercial 1");
        datosDeEjemplo.add("Nombre 2 - Dirección 2 - Teléfono 2 - Email 2 - Comercial 2");
        // Agrega más datos según sea necesario

        // Crea un ArrayAdapter para enlazar los datos a la interfaz de usuario
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, datosDeEjemplo);

        // Asigna el adaptador al ListView
        listView.setAdapter(adapter);

        // Obtén una referencia al botón btNuevo en tu diseño
        Button btNuevo = findViewById(R.id.btNuevo);

        // Agrega un OnClickListener al botón para abrir la actividad nuevo_partner
        btNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNuevoPartnerActivity();
            }
        });
    }

    private void openNuevoPartnerActivity() {
        // Abre la actividad nuevo_partner al hacer clic en el botón btNuevo
        Intent intent = new Intent(this, nuevo_partner.class);
        startActivity(intent);
    }
}
