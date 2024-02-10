package com.example.almohadascomodasademsbonitas.agenda;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.almohadascomodasademsbonitas.BBDD.DBconexion;
import com.example.almohadascomodasademsbonitas.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class agenda extends AppCompatActivity {
    private TextView tv;
    private Button btBus, btSig, btAnt, btAn;
    private ListView lvAct;
    private String[] nombreMeses;
    private DBconexion dbHelper;
    private SQLiteDatabase db;
    private Calendar cal = Calendar.getInstance();
    private int anio = cal.get(Calendar.YEAR);
    private int mes = cal.get(Calendar.MONTH);
    private int dia = cal.get(Calendar.DAY_OF_MONTH);

    private ArrayList<Actividad> actividades = new ArrayList<>();
    private ArrayAdapter<Actividad> adapter;

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

        dbHelper = new DBconexion(this, "ACAB2.db", null, 1);
        db = dbHelper.getWritableDatabase();

        nombreMeses = getResources().getStringArray(R.array.nombremeses);

        String nombreMesActual = nombreMeses[mes];
        String fecha = nombreMesActual + " " + anio;
        tv.setText(fecha);

        adapter = new ActividadAdapter(this, actividades);
        lvAct.setAdapter(adapter);

        cargarDatosDesdeBD();
        adapter.notifyDataSetChanged();

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

                cargarDatosDesdeBD();
                adapter.notifyDataSetChanged(); // Notifica al adapter sobre los cambios en los datos
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

                cargarDatosDesdeBD();
                adapter.notifyDataSetChanged(); // Notifica al adapter sobre los cambios en los datos
            }
        });

        lvAct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Actividad actividadSeleccionada = actividades.get(position);

                Intent intent = new Intent(agenda.this, modificar_agenda.class);
                intent.putExtra("actividad", actividadSeleccionada);
                intent.putExtra("fecha", actividadSeleccionada.getFecha());
                intent.putExtra("hora", actividadSeleccionada.getHora());
                startActivity(intent);
            }
        });

    }
    @Override
    protected void onResume(){
        super.onResume();
        cargarDatosDesdeBD();
        adapter.notifyDataSetChanged();
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

                cargarDatosDesdeBD();
                cargarActividadesDeFechaSeleccionada(dayOfMonth, month, year);
            }
        }, anio, mes, dia);
        dpd.show();
    }


    private void cargarActividadesDeFechaSeleccionada(int dayOfMonth, int month, int year) {
        String fechaSeleccionada = dayOfMonth + "/" + (month + 1) + "/" + year;

        ArrayList<Actividad> actividadesFechaSeleccionada = new ArrayList<>();

        for (Actividad actividad : actividades) {
            if (actividad.getFecha().equals(fechaSeleccionada)) {
                actividadesFechaSeleccionada.add(actividad);
            }
        }

        adapter.clear();
        adapter.addAll(actividadesFechaSeleccionada);
        adapter.notifyDataSetChanged();
    }

    private void cargarDatosDesdeBD() {
        actividades.clear(); // Limpia la lista actual de actividades

        try {
            // Realizar la consulta SELECT
            String selectQuery = "SELECT ACTIVIDAD, TITULO, DESCRIPCION, FECHA, HORA FROM AGENDA";
            Cursor cursor = db.rawQuery(selectQuery, null);

            // Verificar si hay resultados y procesarlos
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    Actividad actividad = construirActividadDesdeCursor(cursor);

                    // Verificar si la actividad es del mes y año seleccionados antes de agregarla a la lista
                    if (esActividadDelMesYAnioSeleccionados(actividad.getFecha())) {
                        actividades.add(actividad);
                    }
                }

                // Cerrar el cursor después de usarlo
                cursor.close();
            }

            adapter.notifyDataSetChanged(); // Notifica al adapter sobre los cambios en los datos
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Actividad construirActividadDesdeCursor(Cursor cursor) {
        Actividad actividad = new Actividad();

        // Verificar si la columna existe antes de intentar acceder a sus datos
        int idIndex = cursor.getColumnIndex("ACTIVIDAD");
        if (idIndex >= 0) {
            actividad.setId(cursor.getInt(idIndex));
        }

        int tituloIndex = cursor.getColumnIndex("TITULO");
        if (tituloIndex >= 0) {
            actividad.setTitulo(cursor.getString(tituloIndex));
        }

        int descripcionIndex = cursor.getColumnIndex("DESCRIPCION");
        if (descripcionIndex >= 0) {
            actividad.setDescripcion(cursor.getString(descripcionIndex));
        }

        int fechaIndex = cursor.getColumnIndex("FECHA");
        if (fechaIndex >= 0) {
            actividad.setFecha(cursor.getString(fechaIndex));
        }

        int horaIndex = cursor.getColumnIndex("HORA");
        if (horaIndex >= 0) {
            actividad.setHora(cursor.getString(horaIndex));
        }

        return actividad;
    }


    private boolean esActividadDelMesYAnioSeleccionados(String fechaActividad) {
        // Formato de fecha para comparar
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        try {
            // Convertir la fecha de la actividad a un objeto Date para comparar
            Date fecha = dateFormat.parse(fechaActividad);

            // Crear un objeto Calendar con la fecha de la actividad
            Calendar calActividad = Calendar.getInstance();
            calActividad.setTime(fecha);

            // Comparar el mes y año de la actividad con el mes y año actuales
            int mesActividad = calActividad.get(Calendar.MONTH);
            int anioActividad = calActividad.get(Calendar.YEAR);

            // Verificar si coincide con el mes y año actuales (mes y anio son variables globales)
            return (mesActividad == mes && anioActividad == anio);
        } catch (ParseException e) {
            e.printStackTrace();
            return false; // Manejo de errores, retorna false si hay algún problema al analizar la fecha
        }
    }

    public class ActividadAdapter extends ArrayAdapter<Actividad> {

        public ActividadAdapter(Context context, ArrayList<Actividad> actividades) {
            super(context, 0, actividades);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            Actividad actividad = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_actividad, parent, false);
            }

            TextView textTitulo = convertView.findViewById(R.id.textTitulo);
            TextView textFechaHora = convertView.findViewById(R.id.textFechaHora);
            TextView textDescripcion = convertView.findViewById(R.id.textDescripcion);

            if (actividad != null) {
                textTitulo.setText(actividad.getTitulo());
                textFechaHora.setText(actividad.getFechaActividad() + " " + actividad.getHora());
                textDescripcion.setText(actividad.getDescripcion());
            }

            return convertView;
        }
    }


    public void nuevoAgenda(View v) {
        Intent intent = new Intent(this, nuevo_agenda.class);
        startActivity(intent);
    }
}
