package com.example.almohadascomodasademsbonitas;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.almohadascomodasademsbonitas.BBDD.DBconexion;
import com.example.almohadascomodasademsbonitas.BBDD.DBconexion;
import com.example.almohadascomodasademsbonitas.LogIn.LogIn;
import com.example.almohadascomodasademsbonitas.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

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
        // Archivos que se deben verificar y copiar
        String[] archivosARevisar = {"pedidos.xml", "partners.xml"};

        for (String nombreArchivo : archivosARevisar) {
            // Ruta del archivo en el directorio de Descargas
            File archivoDescargas = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), nombreArchivo);

            if (archivoDescargas.exists()) {
                Log.d("MainActivity", "Copiando " + nombreArchivo);
                try {
                    copiarArchivo(archivoDescargas, new File(getFilesDir(), nombreArchivo));
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("MainActivity", "Error al copiar " + nombreArchivo + ": " + e.getMessage());
                }
            }
        }

        // Crea la base de datos ACAB
        crearBaseDatosACAB();
        Inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String usuarioText = Usuario.getText().toString();
                String contrasenaText = Contras.getText().toString();

                // Realizar la consulta SELECT
                String query = "SELECT * FROM LOGIN WHERE USER = ? AND PASSWORD = ?";
                Cursor cursor = db.rawQuery(query, new String[]{usuarioText, contrasenaText});

                if (cursor.moveToFirst()) {
                    int usuarioColumnIndex = cursor.getColumnIndex("USER");
                    int contrasenaColumnIndex = cursor.getColumnIndex("PASSWORD");

                    if (usuarioColumnIndex >= 0 && contrasenaColumnIndex >= 0) {
                        String usuarioEncontrado = cursor.getString(usuarioColumnIndex);
                        String contrasenaEncontrada = cursor.getString(contrasenaColumnIndex);

                        if (usuarioText.equals(usuarioEncontrado) && contrasenaText.equals(contrasenaEncontrada)) {
                            // Inicio de sesión exitoso
                            Toast.makeText(MainActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                            lanzarNuevaActividad(v);
                        } else {
                            // Usuario o contraseña incorrectos
                            Toast.makeText(MainActivity.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
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

    private void copiarArchivo(File sourceFile, File destFile) throws IOException {
        FileChannel sourceChannel = null;
        FileChannel destChannel = null;

        try {
            sourceChannel = new FileInputStream(sourceFile).getChannel();
            destChannel = new FileOutputStream(destFile).getChannel();
            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        } finally {
            if (sourceChannel != null) {
                sourceChannel.close();
            }
            if (destChannel != null) {
                destChannel.close();
            }
        }
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
    public void lanzarNuevaActividad(View v) {
        Intent intent = new Intent(this,ActividadCicurlarLayout .class);//declarar la nueva actividad

        startActivity(intent);//lanzar la actividad
    }
}
