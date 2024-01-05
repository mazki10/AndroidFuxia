package com.example.almohadascomodasademsbonitas;

import static java.security.AccessController.getContext;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Xml;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class agenda extends AppCompatActivity {
    private TextView tv;
    private Button btBus, btSig, btAnt, btAn;
    private ListView lvAct;
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
        btBus = findViewById(R.id.btBus);
        btSig = findViewById(R.id.btSigMes);
        btAnt = findViewById(R.id.btAntMes);
        btAn = findViewById(R.id.btNue);
        lvAct = findViewById(R.id.lvAg);

        nombreMeses = getResources().getStringArray(R.array.nombremeses);

        String nombreMesActual = nombreMeses[mes];
        String fecha = nombreMesActual + " " + anio;
        tv.setText(fecha);

        // Obtén la lista de datos desde el XML
        ArrayList<String> datosDeXml = leerDatosDesdeXml();

        // Crea un ArrayAdapter para enlazar los datos al ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, datosDeXml);

        // Asigna el adaptador al ListView
        lvAct.setAdapter(adapter);

        btBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCalendario(v);
            }
        });
        btAn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nuevoAgenda(v);
            }
        });
        btAnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mes > 0) {
                    mes--;
                } else {
                    mes = 11;
                    anio--;
                }

                String nombreMesActual = nombreMeses[mes];
                String fecha = nombreMesActual + " " + anio;
                tv.setText(fecha);
            }
        });
        btSig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mes < 11) {
                    mes++;
                } else {
                    mes = 0;
                    anio++;
                }

                String nombreMesActual = nombreMeses[mes];
                String fecha = nombreMesActual + " " + anio;
                tv.setText(fecha);
            }
        });
    }
    public void abrirCalendario(View v) {
        DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                anio = year;
                mes = month;

                String nombreMesActual = nombreMeses[month];
                String fecha = nombreMesActual + " " + year;
                tv.setText(fecha);
            }
        }, anio, mes, dia);
        dpd.show();
    }

    private ArrayList<String> leerDatosDesdeXml() {
        ArrayList<String> datosDeXml = new ArrayList<>();

        try {
            File directory = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            File file = new File(directory, "actividades.xml");
            FileInputStream fis = new FileInputStream(file);

            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(fis, null);

            int eventType = parser.getEventType();
            StringBuilder currentData = new StringBuilder();
            boolean inActividad = false;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    if ("actividad".equals(parser.getName())) {
                        currentData = new StringBuilder();
                        inActividad = true;
                    }
                } else if (eventType == XmlPullParser.TEXT) {
                    currentData.append(parser.getText());
                } else if (eventType == XmlPullParser.END_TAG) {
                    if ("actividad".equals(parser.getName())) {
                        datosDeXml.add(currentData.toString().trim());
                        inActividad = false;
                    }
                }

                eventType = parser.next();
            }

            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return datosDeXml;
    }

    public void nuevoAgenda(View v) {
        Intent intent = new Intent(this, nuevo_agenda.class);

        startActivity(intent);
    }
}