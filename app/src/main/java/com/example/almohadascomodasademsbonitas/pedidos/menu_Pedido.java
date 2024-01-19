package com.example.almohadascomodasademsbonitas.pedidos;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Context;
import com.example.almohadascomodasademsbonitas.pedidos.actividad_pedido;

import androidx.appcompat.app.AppCompatActivity;

import com.example.almohadascomodasademsbonitas.CircularLayout;
import com.example.almohadascomodasademsbonitas.R;

public class menu_Pedido extends AppCompatActivity {

    Button botonAlta,botonBaja,botonModificacion,botonSalir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pedidos);

        botonAlta = findViewById(R.id.buttonAlta);
        botonBaja = findViewById(R.id.buttonBaja);
        botonModificacion = findViewById(R.id.buttonModificar);
        botonSalir = findViewById(R.id.buttonSalir);

        botonSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });

        botonAlta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menu_Pedido.this, actividad_pedido.class);
                menu_Pedido.this.startActivity(intent);
            }
        });

    }



}