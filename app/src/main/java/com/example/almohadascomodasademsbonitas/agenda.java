package com.example.almohadascomodasademsbonitas;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class agenda extends AppCompatActivity {
    private TextView tv;
    private Button bt, btSig, btAnt;
    private String[] nombreMeses;

    private Calendar cal = Calendar.getInstance();
    private int anio = cal.get(Calendar.YEAR);
    private int mes = cal.get(Calendar.MONTH);
    private int dia = cal.get(Calendar.DAY_OF_MONTH);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);

        tv = findViewById(R.id.tv);
        bt = findViewById(R.id.bt);
        btSig = findViewById(R.id.btSigMes);
        btAnt = findViewById(R.id.btAntMes);

        nombreMeses = getResources().getStringArray(R.array.nombremeses); // Array con los nombres de los meses

        // Obtener la fecha actual


        String nombreMesActual = nombreMeses[mes];
        String fecha = nombreMesActual + " " + anio;
        tv.setText(fecha);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCalendario(v);
            }
        });

        btSig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mes < 11) {
                    mes++;
                    String nombreMesActual = nombreMeses[mes];
                    String fecha = nombreMesActual + " " + anio;
                    tv.setText(fecha);
                }

                if (mes >= 11) {
                    btSig.setEnabled(false); // Desactiva el botón si mes es mayor o igual a 11
                }
            }
        });


        btAnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mes > 0) {
                    mes--;
                    String nombreMesActual = nombreMeses[mes - 1];
                    String fecha = nombreMesActual + " " + anio;
                    tv.setText(fecha);
                }

                if (mes <= 0) {
                    btAnt.setEnabled(false); // Desactiva el botón si mes es menor o igual a 0
                }
            }
        });

    }

    public void abrirCalendario(View v) {

        DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String fecha = dayOfMonth + "/" + month + "/" + year;
                tv.setText(fecha);
            }
        }, anio, mes, dia);
        dpd.show();
    }
}