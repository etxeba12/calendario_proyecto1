package com.example.calendario_tema4;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class entrenamiento extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ver_entrenamiento);

        String fecha = "";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String pMes= extras.getString("mes");
            Log.d("fecha",pMes);
            String pAño= extras.getString("año");
            Log.d("fecha",pAño);
            String pDia= extras.getString("diaSeleccionado");
            Log.d("fecha",pDia);
            fecha = pDia+"/"+pMes+"/"+pAño;
        }
        EditText et = findViewById(R.id.editTextText);
        et.setText(fecha);
    }
}
