package com.example.almohadascomodasademsbonitas;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.almohadascomodasademsbonitas.BBDD.DBconexion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActividadCicurlarLayout extends AppCompatActivity {
    private DBconexion dbHelper;
    private SQLiteDatabase db;
    private static final int PICK_FILES_REQUEST_CODE = 1;
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
            openFilePicker();
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

    private void openFilePicker() {
        // Verificar si alguno de los archivos ya existe en el directorio de archivos internos
        if (checkIfFileExists("partners.xml") || checkIfFileExists("articulos.xml")) {
            // Si alguno de los archivos existe, preguntar al usuario si quiere sobrescribirlos
            askUserForOverwriteConfirmation();
        } else {
            // Si ninguno de los archivos existe, abrir el selector de archivos directamente
            launchFilePicker();
        }
    }

    private boolean checkIfFileExists(String fileName) {
        File file = new File(getFilesDir(), fileName);
        // Verificar si el archivo ya existe
        return file.exists();
    }

    private void askUserForOverwriteConfirmation() {
        // Mostrar un diálogo de confirmación al usuario
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Archivo existente");
        builder.setMessage("Al menos uno de los archivos ya existe. ¿Quieres sobrescribirlo?");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Si el usuario elige sobrescribir, abrir el selector de archivos
                launchFilePicker();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Si el usuario elige no sobrescribir, cancelar la operación
                Toast.makeText(getApplicationContext(), "Operación cancelada", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

    private void launchFilePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("text/xml"); // Cambia esto según el tipo de archivo que estés intentando abrir
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // Permitir la selección múltiple
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, PICK_FILES_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILES_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            boolean foundAllRequiredFiles = true;
            List<String> selectedFileNames = new ArrayList<>();
            String[] requiredFiles = {"partners.xml", "articulos.xml"};

            // Verifica si se seleccionaron varios archivos
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri uri = data.getClipData().getItemAt(i).getUri();
                    String fileName = getFileNameFromUri(this, uri);

                    // Verifica si el archivo seleccionado es uno de los archivos requeridos
                    if (!Arrays.asList(requiredFiles).contains(fileName)) {
                        foundAllRequiredFiles = false;
                        Toast.makeText(this, "El archivo '" + fileName + "' no es requerido", Toast.LENGTH_SHORT).show();
                    } else {
                        selectedFileNames.add(fileName);
                        // Lee el contenido del archivo y crea un archivo interno con el mismo nombre y escribe el contenido en él
                        try {
                            String fileContent = readFileContent(uri);
                            writeFileToInternalStorage(fileName, fileContent);
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(this, "Error al leer o escribir el archivo " + fileName, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            } else if (data.getData() != null) {
                // Si solo se seleccionó un archivo
                Uri uri = data.getData();
                if (uri != null) { // Verifica si uri no es nulo
                    String fileName = getFileNameFromUri(this, uri);

                    // Verifica si el archivo seleccionado es uno de los archivos requeridos
                    if (!Arrays.asList(requiredFiles).contains(fileName)) {
                        foundAllRequiredFiles = false;
                        Toast.makeText(this, "El archivo '" + fileName + "' no es requerido", Toast.LENGTH_SHORT).show();
                    } else {
                        selectedFileNames.add(fileName);
                        // Lee el contenido del archivo y crea un archivo interno con el mismo nombre y escribe el contenido en él
                        try {
                            String fileContent = readFileContent(uri);
                            writeFileToInternalStorage(fileName, fileContent);
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(this, "Error al leer o escribir el archivo " + fileName, Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    // Manejar caso donde uri es null
                    Toast.makeText(this, "No se ha seleccionado ningún archivo", Toast.LENGTH_SHORT).show();
                }
            }

            // Si no se encontraron todos los archivos requeridos, muestra un mensaje indicando que se deben seleccionar todos los archivos requeridos
            if (!foundAllRequiredFiles || selectedFileNames.size() != requiredFiles.length) {
                Toast.makeText(this, "Por favor, selecciona los xml partners y/o articulos", Toast.LENGTH_LONG).show();
            } else {
                // Si se encontraron todos los archivos requeridos, muestra los nombres de los archivos seleccionados
                StringBuilder message = new StringBuilder("Archivos seleccionados:\n");
                for (String fileName : selectedFileNames) {
                    message.append(fileName).append("\n");
                }
                Toast.makeText(this, message.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }




    private String readFileContent(Uri uri) throws IOException {
        StringBuilder content = new StringBuilder();
        if (uri != null) { // Verifica si uri no es nulo
            InputStream inputStream = getContentResolver().openInputStream(uri);
            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
                inputStream.close();
            }
        } else {
            // Manejar caso donde uri es null, por ejemplo, lanzar una excepción o mostrar un mensaje de error
            throw new IllegalArgumentException("Uri es nulo");
        }
        return content.toString();
    }


    private void writeFileToInternalStorage(String fileName, String content) throws IOException {
        File outputFile = new File(getFilesDir(), fileName);
        FileOutputStream outputStream = new FileOutputStream(outputFile);
        outputStream.write(content.getBytes());
        outputStream.close();
        Toast.makeText(this, "Archivo " + fileName + " creado correctamente", Toast.LENGTH_SHORT).show();
    }




    private String getFileNameFromUri(Context context, Uri uri) {
        String fileName = null;
        if (uri.getScheme() != null && uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver contentResolver = context.getContentResolver();
            Cursor cursor = contentResolver.query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    int displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (displayNameIndex != -1) {
                        fileName = cursor.getString(displayNameIndex);
                    }
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (fileName == null) {
            fileName = uri.getLastPathSegment();
        }
        return fileName;
    }



}
