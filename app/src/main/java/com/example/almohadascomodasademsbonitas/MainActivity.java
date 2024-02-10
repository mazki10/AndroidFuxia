package com.example.almohadascomodasademsbonitas;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.almohadascomodasademsbonitas.BBDD.DBconexion;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private DBconexion dbHelper;
    private SQLiteDatabase db;
    private EditText Usuario, Contras;
    private Button Inicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Inicio = findViewById(R.id.bTiN);
        Usuario = findViewById(R.id.edTxUs);
        Contras = findViewById(R.id.eDtxCn);

        dbHelper = new DBconexion(this, "ACAB2.db", null, 1);
        db = dbHelper.getWritableDatabase();


        // Crea la base de datos ACAB
        crearBaseDatosACAB();
        String query = "SELECT * FROM LOGIN WHERE SESION = 1";
        Cursor cursor = db.rawQuery(query, null);

        // Si hay algún resultado, significa que existe un comercial con login igual a 1
        if (cursor.moveToFirst()) {
            // Si existe, lanzar la nueva actividad
            lanzarNuevaActividad();
            // Cerrar el cursor después de usarlo
            cursor.close();
            return; // Salir del método onCreate() ya que hemos lanzado la nueva actividad
        }


        Inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuarioText = Usuario.getText().toString();
                String contrasenaText = Contras.getText().toString();

                // Realizar la consulta SELECT
                String query = "SELECT ID_COMERCIAL, SESION FROM LOGIN WHERE USER = ? AND PASSWORD = ?";
                Cursor cursor = db.rawQuery(query, new String[]{usuarioText, contrasenaText});

                if (cursor.moveToFirst()) {
                    int idComercialColumnIndex = cursor.getColumnIndex("ID_COMERCIAL");
                    int sesionColumnIndex = cursor.getColumnIndex("SESION");

                    if (idComercialColumnIndex >= 0 && sesionColumnIndex >= 0) {
                        String idComercial = cursor.getString(idComercialColumnIndex);
                        boolean sesionActual = cursor.getInt(sesionColumnIndex) == 1;

                        if (!sesionActual) {
                            // Inicio de sesión exitoso
                            Toast.makeText(MainActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();

                            // Actualizar la columna SESION a TRUE
                            ContentValues values = new ContentValues();
                            values.put("SESION", 1);

                            // Actualizar la SESION a TRUE solo para el usuario actual
                            db.update("LOGIN", values, "USER = ? AND PASSWORD = ?", new String[]{usuarioText, contrasenaText});

                            // Lanzar la nueva actividad
                            lanzarNuevaActividad();
                        } else {
                            // Sesión ya iniciada
                            Toast.makeText(MainActivity.this, "Sesión ya iniciada", Toast.LENGTH_SHORT).show();
                            lanzarNuevaActividad();
                        }
                    } else {
                        // Alguna de las columnas no existe
                        Toast.makeText(MainActivity.this, "Error en la base de datos", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Usuario o contraseña incorrectos
                    Toast.makeText(MainActivity.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                }

                // Cerrar el cursor después de usarlo
                cursor.close();
            }
        });


    }


    private void crearBaseDatosACAB() {
        try {
            // Ruta donde deseas almacenar la base de datos
            String dbPath = getFilesDir().getParent() + File.separator + "BBDD" + File.separator + "ACAB2";

            // Crea la base de datos ACAB utilizando la clase DBConexion
            DBconexion dbConexion = new DBconexion(this, dbPath);
            SQLiteDatabase db = dbConexion.getWritableDatabase();
            // Cierra la conexión a la base de datos
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MainActivity", "Error al crear la base de datos: " + e.getMessage());
        }
    }
    public void lanzarNuevaActividad() {
        Intent intent = new Intent(this, ActividadCicurlarLayout.class);//declarar la nueva actividad

        startActivity(intent);//lanzar la actividad
    }
    protected void onRestart(){
        super.onRestart();
        finish();
    }
}
