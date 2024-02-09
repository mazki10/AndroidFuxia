package com.example.almohadascomodasademsbonitas.agenda;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
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

        cargarDatosDesdeXml();
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

                cargarDatosDesdeXml();
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

                cargarDatosDesdeXml();
                adapter.notifyDataSetChanged(); // Notifica al adapter sobre los cambios en los datos
            }
        });

        lvAct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Actividad actividadSeleccionada = actividades.get(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(agenda.this);
                builder.setTitle("Eliminar Actividad")
                        .setMessage("¿Estás seguro de que quieres eliminar esta actividad?")
                        .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                eliminarActividad(actividadSeleccionada);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // No hacer nada, simplemente cerrar el diálogo
                            }
                        })
                        .show();
            }
        });
    }
    @Override
    protected void onResume(){
        super.onResume();
        cargarDatosDesdeXml();
        adapter.notifyDataSetChanged();
    }

    private void eliminarActividad(Actividad actividad) {
        actividades.remove(actividad); // Remover del ArrayList

        // Eliminar la actividad del archivo XML
        try {
            File directory = getFilesDir();
            File file = new File(directory, "actividades.xml");
            FileInputStream fis = new FileInputStream(file);

            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(fis, null);

            // Creamos una nueva lista de actividades sin la actividad a eliminar
            ArrayList<Actividad> nuevasActividades = new ArrayList<>();

            int eventType = parser.getEventType();
            Actividad actividadActual = null;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    if ("actividad".equals(parser.getName())) {
                        actividadActual = new Actividad();
                    } else if ("titulo".equals(parser.getName()) && actividadActual != null) {
                        actividadActual.setTitulo(parser.nextText());
                    } else if ("descripcion".equals(parser.getName()) && actividadActual != null) {
                        actividadActual.setDescripcion(parser.nextText());
                    } else if ("fecha".equals(parser.getName()) && actividadActual != null) {
                        actividadActual.setFecha(parser.nextText());
                    } else if ("hora".equals(parser.getName()) && actividadActual != null) {
                        actividadActual.setHora(parser.nextText());
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    if ("actividad".equals(parser.getName()) && actividadActual != null) {
                        if (!actividadActual.equals(actividad)) {
                            // Agregar solo si no es la actividad a eliminar
                            nuevasActividades.add(actividadActual);
                        }
                    }
                }
                eventType = parser.next();
            }
            fis.close();

            // Sobrescribir el archivo XML con la nueva lista de actividades
            XmlSerializer xmlSerializer = Xml.newSerializer();
            StringWriter writer = new StringWriter();
            xmlSerializer.setOutput(writer);
            xmlSerializer.startDocument("UTF-8", true);
            xmlSerializer.startTag(null, "actividades");

            for (Actividad act : nuevasActividades) {
                xmlSerializer.startTag(null, "actividad");

                xmlSerializer.startTag(null, "titulo");
                xmlSerializer.text(act.getTitulo());
                xmlSerializer.endTag(null, "titulo");

                xmlSerializer.startTag(null, "descripcion");
                xmlSerializer.text(act.getDescripcion());
                xmlSerializer.endTag(null, "descripcion");

                xmlSerializer.startTag(null, "fecha");
                xmlSerializer.text(act.getFecha());
                xmlSerializer.endTag(null, "fecha");

                xmlSerializer.startTag(null, "hora");
                xmlSerializer.text(act.getHora());
                xmlSerializer.endTag(null, "hora");

                xmlSerializer.endTag(null, "actividad");
            }

            xmlSerializer.endTag(null, "actividades");
            xmlSerializer.endDocument();

            FileOutputStream fileos = new FileOutputStream(file);
            fileos.write(writer.toString().getBytes());
            fileos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        adapter.notifyDataSetChanged(); // Notifica al adapter sobre los cambios en los datos
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

                cargarDatosDesdeXml();
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



    private void cargarDatosDesdeXml() {
        actividades.clear(); // Limpia la lista actual de actividades

        try {
            File directory = getFilesDir();
            File file = new File(directory, "actividades.xml");
            FileInputStream fis = new FileInputStream(file);

            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(fis, null);

            int eventType = parser.getEventType();
            Actividad actividadActual = null;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    if ("actividad".equals(parser.getName())) {
                        actividadActual = new Actividad();
                    } else if ("titulo".equals(parser.getName()) && actividadActual != null) {
                        actividadActual.setTitulo(parser.nextText());
                    } else if ("descripcion".equals(parser.getName()) && actividadActual != null) {
                        actividadActual.setDescripcion(parser.nextText());
                    } else if ("fecha".equals(parser.getName()) && actividadActual != null) {
                        actividadActual.setFecha(parser.nextText());
                    } else if ("hora".equals(parser.getName()) && actividadActual != null) {
                        actividadActual.setHora(parser.nextText());
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    if ("actividad".equals(parser.getName()) && actividadActual != null) {
                        // Verifica si la actividad es del mes y año seleccionados antes de agregarla a la lista
                        if (esActividadDelMesYAnioSeleccionados(actividadActual.getFecha())) {
                            actividades.add(actividadActual);
                        }
                    }
                }
                eventType = parser.next();
            }
            fis.close();
            Collections.sort(actividades, new Comparator<Actividad>() {
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

                @Override
                public int compare(Actividad actividad1, Actividad actividad2) {
                    try {
                        Date fechaHora1 = dateFormat.parse(actividad1.getFecha() + " " + actividad1.getHora());
                        Date fechaHora2 = dateFormat.parse(actividad2.getFecha() + " " + actividad2.getHora());
                        return fechaHora1.compareTo(fechaHora2);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return 0;
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        adapter.notifyDataSetChanged(); // Notifica al adapter sobre los cambios en los datos
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
                convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
            }

            TextView text1 = convertView.findViewById(android.R.id.text1);
            TextView text2 = convertView.findViewById(android.R.id.text2);

            if (actividad != null) {
                String fechaHora = actividad.getFechaActividad();
                text1.setText(fechaHora + "- " + actividad.getTitulo() + "\n" + actividad.getHora());
                text2.setText(actividad.getDescripcion());
            }

            return convertView;
        }
    }


    public void nuevoAgenda(View v) {
        Intent intent = new Intent(this, nuevo_agenda.class);
        startActivity(intent);
    }
}
