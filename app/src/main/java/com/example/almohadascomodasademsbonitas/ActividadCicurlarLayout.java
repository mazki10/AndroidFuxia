package com.example.almohadascomodasademsbonitas;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.almohadascomodasademsbonitas.BBDD.DBconexion;

public class ActividadCicurlarLayout extends AppCompatActivity {
    private DBconexion dbHelper;
    private SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dbHelper = new DBconexion(this, "ACAB2.db", null, 1);
        db = dbHelper.getWritableDatabase();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_cicurlar_layout);

        Toolbar toolbar = findViewById(R.id.toBr);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(view -> {
            // Acciones cuando se hace clic en el botón de navegación
            // Puedes abrir el cajón de navegación u otras acciones
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.opcion1) {
            //importar
        } else if (itemId == R.id.opcion2) {
            cerrarSesion();
        } else {
            // Otros casos si es necesario
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void cerrarSesion() {
        // Lógica para cerrar sesión
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("SESION", 0);  // 0 indica FALSE

        // Actualiza el campo SESION a FALSE para todos los usuarios
        db.update("LOGIN", values, null, null);

        // Cierra la base de datos
        db.close();

        // Limpia preferencias u otros datos de sesión si es necesario
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        preferences.edit().clear().apply();

        // Reinicia la actividad de inicio de sesión (o cualquier actividad de inicio según tu aplicación)
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();  // Cierra la actividad actual
    }
}
