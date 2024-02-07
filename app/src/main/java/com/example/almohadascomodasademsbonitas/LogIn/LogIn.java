package com.example.almohadascomodasademsbonitas.LogIn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.almohadascomodasademsbonitas.ActividadCicurlarLayout;
import com.example.almohadascomodasademsbonitas.BBDD.DBconexion;
import com.example.almohadascomodasademsbonitas.R;

public class LogIn extends AppCompatActivity {
    private DBconexion dbHelper;
    private SQLiteDatabase db;
    private EditText Usuario, Contras;
    private Button Inicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        Usuario = findViewById(R.id.edTxUs);
        Contras = findViewById(R.id.eDtxCn);
        Inicio = findViewById(R.id.bTiN);

        dbHelper = new DBconexion(this, "ACAB2.db", null, 1);
        db = dbHelper.getWritableDatabase();
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
                            Toast.makeText(LogIn.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                            lanzarNuevaActividad(v);
                        } else {
                            // Usuario o contraseña incorrectos
                            Toast.makeText(LogIn.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Alguna de las columnas no existe
                        Toast.makeText(LogIn.this, "Error en la base de datos", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Usuario o contraseña incorrectos
                    Toast.makeText(LogIn.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                }

                // Cerrar el cursor después de usarlo
                cursor.close();
            }
        });


    }

    public void lanzarNuevaActividad(View v) {
        Intent intent = new Intent(this,ActividadCicurlarLayout .class);//declarar la nueva actividad

        startActivity(intent);//lanzar la actividad
    }
}
