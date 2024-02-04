package com.example.almohadascomodasademsbonitas;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import android.os.Environment;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Archivos que se deben verificar y copiar
        String[] archivosARevisar = {"comerciales.xml", "pedidos.xml", "partners.xml"};

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
}

