package com.example.almohadascomodasademsbonitas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class info extends AppCompatActivity implements OnMapReadyCallback{

    private GoogleMap mapa;
    private Button btEmail, btTlf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        btEmail = findViewById(R.id.buttonEmail);
        btTlf = findViewById(R.id.buttonTelefono);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarEmail(v);
            }
        });

        btTlf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llamarNumero(v);
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mapa = googleMap;

        LatLng cebanc = new LatLng(43.3046912,-2.0168937);
        mapa.addMarker(new MarkerOptions().position(cebanc).title("Cebanc"));
        mapa.moveCamera(CameraUpdateFactory.newLatLng(cebanc));
    }
    public void enviarEmail(View view) {
        String destinatario = "almohadas@acab.com"; // Dirección de correo a la que quieres enviar
        String asunto = "Asunto del correo";

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");

        // Dirección de correo y asunto prellenados
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { destinatario });
        intent.putExtra(Intent.EXTRA_SUBJECT, asunto);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(intent, "Enviar correo usando:"));
        } else {
            // Si no hay ninguna aplicación para manejar correos
            Toast.makeText(this, "No hay aplicación de correo disponible", Toast.LENGTH_SHORT).show();
        }
    }

    public void llamarNumero(View view) {
        String numeroTelefono = "1312"; // Número al que quieres llamar

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + numeroTelefono));
        startActivity(intent);
    }
}



