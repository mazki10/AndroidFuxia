package com.example.almohadascomodasademsbonitas.agenda;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.almohadascomodasademsbonitas.BBDD.DBconexion;
import com.example.almohadascomodasademsbonitas.R;

import java.util.Calendar;
import java.util.Locale;

public class modificar_agenda extends AppCompatActivity {
    private EditText edtTit, edtDes;
    private Button btFec, btNue,btDel;
    private TextView tvFec;
    private DBconexion dbHelper;
    private SQLiteDatabase db;

    private Calendar cal = Calendar.getInstance();
    private int anio = cal.get(Calendar.YEAR);
    private int mes = cal.get(Calendar.MONTH);
    private int dia = cal.get(Calendar.DAY_OF_MONTH);
    private int hora = cal.get(Calendar.HOUR_OF_DAY);
    private int minuto = cal.get(Calendar.MINUTE);
    private String fechaSeleccionada;
    private String horaSeleccionada;
    private Actividad actividadSeleccionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_agenda); // Mismo diseño que nuevo_agenda

        edtTit = findViewById(R.id.edtTit);
        edtDes = findViewById(R.id.edtDes);
        tvFec = findViewById(R.id.tvFec);
        btFec = findViewById(R.id.botFec);
        btNue = findViewById(R.id.botNue);
        btDel = findViewById(R.id.botElm);



        dbHelper = new DBconexion(this, "ACAB2.db", null, 1);
        db = dbHelper.getWritableDatabase();
        btFec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                devolverHora(v);
                devolverFecha(v);
            }
        });

        btDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(modificar_agenda.this);
                builder.setTitle("Eliminar Actividad");
                builder.setMessage("¿Estás seguro de que deseas eliminar esta actividad?");
                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        eliminarActividad();
                        finish(); // Cerrar la actividad después de eliminar la actividad
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // No hacer nada si el usuario selecciona "No"
                    }
                });
                builder.show();
            }
        });

        Intent intent = getIntent();
        if (intent.hasExtra("actividad")) {
            actividadSeleccionada = (Actividad) intent.getSerializableExtra("actividad");
            fechaSeleccionada = intent.getStringExtra("fecha");
            horaSeleccionada = intent.getStringExtra("hora");
            cargarDatosActividad(actividadSeleccionada);
        }

        btNue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modificarActividad();
                finish();
            }
        });
    }

    // Resto del código sin cambios

    private void cargarDatosActividad(Actividad actividad) {
        if (actividad != null) {
            edtTit.setText(actividad.getTitulo());
            edtDes.setText(actividad.getDescripcion());
            tvFec.setText(actividad.getFecha() + " " + actividad.getHora());
            // Además, establecer fechaSeleccionada y horaSeleccionada si es necesario
            // Esto depende de cómo estén implementados los métodos obtenerFechaSeleccionada y obtenerHoraSeleccionada
        }
    }

    private void modificarActividad() {
        String titulo = edtTit.getText().toString();
        String descripcion = edtDes.getText().toString();
        String hora = obtenerHoraSeleccionada();
        String fecha = obtenerFechaSeleccionada();

        if (!titulo.isEmpty()) {
            try {
                // Realiza la actualización con los nuevos datos
                String updateQuery = "UPDATE AGENDA SET TITULO = ?, DESCRIPCION = ?, FECHA = ?, HORA = ? WHERE ACTIVIDAD = ?";
                db.execSQL(updateQuery, new Object[]{titulo, descripcion, fecha, hora, actividadSeleccionada.getId()});

                db.close();

                Toast.makeText(this, "Actividad modificada correctamente", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error al modificar actividad", Toast.LENGTH_SHORT).show();
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

    private void actualizarTextView() {
        if (fechaSeleccionada != null && horaSeleccionada != null) {
            String fechaActividad = fechaSeleccionada + " " + horaSeleccionada;
            tvFec.setText(fechaActividad);
        }
    }


    private void eliminarActividad() {
        try {
            // Obtener la ID de la actividad a eliminar
            int idActividadEliminar = actividadSeleccionada.getId();

            // Eliminar la actividad de la base de datos
            String deleteQuery = "DELETE FROM AGENDA WHERE ACTIVIDAD = ?";
            db.execSQL(deleteQuery, new Object[]{idActividadEliminar});

            Toast.makeText(modificar_agenda.this, "Actividad eliminada correctamente", Toast.LENGTH_SHORT).show();

            // Puedes llamar a cargarDatosDesdeBD para actualizar la lista después de la eliminación
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(modificar_agenda.this, "Error al eliminar actividad", Toast.LENGTH_SHORT).show();
        }
    }

}
