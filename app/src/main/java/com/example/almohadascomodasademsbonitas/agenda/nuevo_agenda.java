package com.example.almohadascomodasademsbonitas.agenda;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Environment;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.almohadascomodasademsbonitas.R;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

public class nuevo_agenda extends AppCompatActivity {
    private EditText edtTit, edtDes;
    private Button btFec, btNue;
    private String FILE_NAME = "actividades.xml";

    private Calendar cal = Calendar.getInstance();
    private int anio = cal.get(Calendar.YEAR);
    private int mes = cal.get(Calendar.MONTH);
    private int dia = cal.get(Calendar.DAY_OF_MONTH);
    private int hora = cal.get(Calendar.HOUR_OF_DAY);
    private int minuto = cal.get(Calendar.MINUTE);
    private String fechaSeleccionada;
    private String horaSeleccionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_agenda);

        edtTit = findViewById(R.id.edtTit);
        edtDes = findViewById(R.id.edtDes);
        btFec = findViewById(R.id.botFec);
        btNue = findViewById(R.id.botNue);

        btFec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                devolverHora(v);
                devolverFecha(v);
            }
        });

        btNue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarActividad();
                finish();
            }
        });
    }
    private void agregarActividad() {
        String titulo = edtTit.getText().toString();
        String descripcion = edtDes.getText().toString();
        String hora = obtenerHoraSeleccionada();
        String fecha = obtenerFechaSeleccionada();
        try {
            File directory = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            File file = new File(directory, FILE_NAME);
            FileOutputStream fileos = new FileOutputStream(file, true); // 'true' para añadir al archivo existente

            XmlSerializer xmlSerializer = Xml.newSerializer();
            xmlSerializer.setOutput(fileos, "UTF-8");

            xmlSerializer.startTag(null, "actividad");

            xmlSerializer.startTag(null, "titulo");
            xmlSerializer.text(titulo);
            xmlSerializer.endTag(null, "titulo");

            xmlSerializer.startTag(null, "descripcion");
            xmlSerializer.text(descripcion);
            xmlSerializer.endTag(null, "descripcion");

            xmlSerializer.startTag(null, "fecha");
            xmlSerializer.text(fecha);
            xmlSerializer.endTag(null, "fecha");

            xmlSerializer.startTag(null, "hora");
            xmlSerializer.text(hora);
            xmlSerializer.endTag(null, "hora");

            xmlSerializer.endTag(null, "actividad");
            xmlSerializer.endDocument();

            xmlSerializer.flush();
            fileos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String obtenerFechaSeleccionada() {
        return fechaSeleccionada;
    }
    public String obtenerHoraSeleccionada() {
        return horaSeleccionada;
    }
    public void devolverFecha(View v) {
        DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                fechaSeleccionada = dayOfMonth + "/" + (month + 1) + "/" + year;
            }
        }, anio, mes, dia);
        dpd.show();
    }
    public void devolverHora(View v) {
        TimePickerDialog tpd = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                horaSeleccionada = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
            }
        }, hora, minuto, true);
        tpd.show();
    }
}
