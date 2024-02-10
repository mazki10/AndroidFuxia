package com.example.almohadascomodasademsbonitas.agenda;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.almohadascomodasademsbonitas.BBDD.DBconexion;
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
    private TextView tvFec;
    private DBconexion dbHelper;
    private SQLiteDatabase db;
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
        tvFec = findViewById(R.id.tvFec);
        btFec = findViewById(R.id.botFec);
        btNue = findViewById(R.id.botNue);

        dbHelper = new DBconexion(this, "ACAB2.db", null, 1);
        db = dbHelper.getWritableDatabase();
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
    private void actualizarTextView() {
        if (fechaSeleccionada != null && horaSeleccionada != null) {
            String fechaActividad = fechaSeleccionada + " " + horaSeleccionada;
            tvFec.setText(fechaActividad);
        }
    }
    private int obtenerMayorId() {
        int mayorId = 0;
        try {
            // Realiza la consulta SELECT para obtener la mayor ID
            String selectQuery = "SELECT IFNULL(MAX(ACTIVIDAD), 0) FROM AGENDA";
            Cursor cursor = db.rawQuery(selectQuery, null);

            // Si hay resultados, obtén la mayor ID
            if (cursor.moveToFirst()) {
                mayorId = cursor.getInt(0);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mayorId;
    }



    private void agregarActividad() {
        String titulo = edtTit.getText().toString();
        String descripcion = edtDes.getText().toString();
        String hora = obtenerHoraSeleccionada();
        String fecha = obtenerFechaSeleccionada();

        if (!titulo.isEmpty() && fechaSeleccionada != null && horaSeleccionada != null) {
            try {
                // Obtén la mayor ID actual y suma 1 para asignar la nueva ID
                int nuevaId = obtenerMayorId() + 1;

                // Realiza la inserción con la nueva ID
                String insertQuery = "INSERT INTO AGENDA (ACTIVIDAD, TITULO, DESCRIPCION, FECHA, HORA) VALUES (?, ?, ?, ?, ?)";
                db.execSQL(insertQuery, new Object[]{nuevaId, titulo, descripcion, fecha, hora});

                db.close();

                Toast.makeText(this, "Actividad agregada correctamente", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error al agregar actividad", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Por favor, rellena el título, la fecha y la hora", Toast.LENGTH_SHORT).show();
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
                actualizarTextView();
            }
        }, anio, mes, dia);
        dpd.show();
    }
    public void devolverHora(View v) {
        TimePickerDialog tpd = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                horaSeleccionada = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                actualizarTextView();
            }
        }, hora, minuto, true);
        tpd.show();
    }
}
