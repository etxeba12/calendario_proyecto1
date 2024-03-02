package com.example.calendario_tema4;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.calendario_tema4.BaseDeDatos.BD;
import com.example.calendario_tema4.calendarioReycler.MainActivity;

public class Login extends AppCompatActivity {

    private SQLiteDatabase db;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        BD GestorDB = new BD(this, "Tabla", null, 1);
        db = GestorDB.getWritableDatabase();
        /*
        db.execSQL("INSERT INTO calendario (fecha, nombreEjercicio, series, repeticiones, RPE, kilos, cliente_nombre, kilosCliente, RPECliente) VALUES " +
                "('2023-11-19', 'Comp SQ', 4, 4, 5, 180, 'manuel', ' 0 0 0 0', ' 0 0 0 0'), " +
                "('2023-11-19', '320 BP', 3, 2, 6, 100, 'manuel', ' 0 0 0', ' 0 0 0'), " +
                "('2023-11-19', 'Empuje vertical', 4, 10, 9, 100, 'manuel', ' 0 0 0 0', ' 0 0 0 0'), " +
                "('2023-11-19', 'Extensión de triceps', 3, 12, 10, 100, 'manuel', ' 0 0 0', ' 0 0 0'), " +
                "('2023-11-21', 'Comp BP', 3, 3, 6, 110, 'manuel', ' 115 110 110', ' 5 5 6'), " +
                "('2023-11-21', '303 DL', 4, 5, 7, 200, 'manuel', ' 0 0 0 0', ' 0 0 0 0'), " +
                "('2023-11-21', 'Remo T', 4, 12, 8, 200, 'manuel', ' 0 0 0 0', ' 0 0 0 0'), " +
                "('2023-11-21', 'Biceps', 5, 12, 10, 200, 'manuel', ' 0 0 0 0 0', ' 0 0 0 0 0'), " +
                "('2023-11-23', 'PIN SQ', 1, 3, 5, 0, 'manuel', ' 100', ' 5'), " +
                "('2023-11-23', 'PIN SQ 2', 1, 3, 6, 0, 'manuel', ' 105', ' 5'), " +
                "('2023-11-23', 'PIN SQ 3', 1, 3, 7, 0, 'manuel', ' 110', ' 5'), " +
                "('2023-11-23', 'Larsen BP', 4, 4, 5, 0, 'manuel', NULL, NULL), " +
                "('2023-11-23', 'Deficit DL', 5, 5, 5, 140, 'manuel', NULL, NULL), " +
                "('2023-11-14', 'BP', 5, 5, 4, 80, 'pedro', NULL, NULL), " +
                "('2023-11-07', 'Doble pausa DL', 4, 5, 6, 180, 'eufanasio', NULL, NULL), " +
                "('2023-11-07', '320 BP', 6, 1, 4, 105, 'eufanasio', NULL, NULL), " +
                "('2023-11-07', 'Tracción vertical', 4, 10, 8, 0, 'eufanasio', NULL, NULL), " +
                "('2023-11-07', 'Biceps', 3, 15, 10, 0, 'eufanasio', NULL, NULL), " +
                "('2023-11-09', '300 SQ', 3, 3, 5, 0, 'eufanasio', NULL, NULL), " +
                "('2023-11-09', 'kodama BP', 5, 5, 4, 95, 'eufanasio', NULL, NULL), " +
                "('2023-11-09', 'Empuje vertical', 3, 8, 9, 0, 'eufanasio', NULL, NULL), " +
                "('2023-11-09', 'Extensión de cuadiceps', 4, 15, 10, 0, 'eufanasio', NULL, NULL), " +
                "('2023-11-10', 'Comp DL', 3, 2, 6, 200, 'eufanasio', NULL, NULL), " +
                "('2023-11-10', 'Comp BP', 5, 5, 7, 0, 'eufanasio', NULL, NULL), " +
                "('2023-11-10', 'Tracción horizontal', 4, 12, 9, 0, 'eufanasio', NULL, NULL), " +
                "('2023-11-10', 'Biceps', 3, 15, 10, 0, 'eufanasio', NULL, NULL), " +
                "('2023-11-14', 'Prensa', 4, 15, 9, 0, 'eufanasio', NULL, NULL), " +
                "('2023-11-14', 'Femoral sentado', 3, 12, 8, 0, 'eufanasio', NULL, NULL), " +
                "('2023-11-14', 'Gemelo', 5, 10, 10, 0, 'eufanasio', NULL, NULL), " +
                "('2023-11-17', 'Comp SQ', 3, 3, 9, 0, 'eufanasio', NULL, NULL), " +
                "('2023-11-17', 'Comp BP', 3, 3, 9, 0, 'eufanasio', NULL, NULL), " +
                "('2023-11-17', 'Comp DL', 3, 3, 9, 0, 'eufanasio', NULL, NULL), " +
                "('2023-11-20', 'larsen BP', 6, 6, 5, 60, 'eider', NULL, NULL), " +
                "('2023-11-20', '303 DL', 4, 3, 6, 95, 'eider', NULL, NULL), " +
                "('2023-11-20', 'Hip thrust', 2, 12, 9, 0, 'eider', NULL, NULL), " +
                "('2023-11-20', 'Jalon al pecho', 3, 10, 8, 0, 'eider', NULL, NULL), " +
                "('2023-11-20', 'Biceps', 4, 15, 5, 0, 'eider', NULL, NULL), " +
                "('2023-11-20', 'Biceps', 4, 15, 10, 0, 'eider', NULL, NULL), " +
                "('2023-11-22', 'PIN SQ', 3, 3, 3, 90, 'eider', NULL, NULL), " +
                "('2023-11-22', 'PIN BP', 5, 5, 5, 55, 'eider', NULL, NULL), " +
                "('2023-11-22', 'Hombro', 4, 8, 6, 0, 'eider', NULL, NULL), " +
                "('2023-11-22', 'Triceps', 3, 10, 10, 0, 'eider', NULL, NULL), " +
                "('2023-11-22', 'Abs', 2, 15, 10, 0, 'eider', NULL, NULL), " +
                "('2023-11-23', 'Abductor', 4, 10, 8, 0, 'eider', NULL, NULL), " +
                "('2023-11-23', 'Remo libre', 3, 8, 9, 0, 'eider', NULL, NULL), " +
                "('2023-11-23', 'Hombro posterior', 2, 15, 10, 0, 'eider', NULL, NULL), " +
                "('2023-11-23', 'Gemelo', 2, 15, 10, 0, 'eider', NULL, NULL), " +
                "('2023-11-23', 'Banca', 3, 10, 10, 0, 'eider', NULL, NULL)");
        */
        /*
        db.execSQL("INSERT INTO Usuarios (nombreUsuarios, contrasena, entrenador, esEntrenador) VALUES " +
                "('eider', '1234', 'iker', 1), " +
                "('eufanasio', '1234=', 'iker', 1), " +
                "('manuel', '1234=', 'iker', 0), " +
                "('pedro', '1234=', 'iker', 0)");
        */
        Button btLogin = findViewById(R.id.LoginBoton);
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText usuario = findViewById(R.id.NombreUsuarioMeter);
                EditText contra = findViewById(R.id.meterContra);
                String valido = GestorDB.comprobarUsuario(db,usuario.getText().toString());
                if (valido != null){
                    if (Integer.parseInt(valido)==1){ //comprobamos si es entrenador
                        Intent i = new Intent(Login.this, ListaAtletas.class);
                        i.putExtra("entrenador",usuario.getText().toString());
                        startActivity(i);
                        finish();
                    }else{
                        Intent i = new Intent(Login.this, MainActivity.class);
                        i.putExtra("atleta",usuario.getText().toString());
                        startActivity(i);
                        finish();
                    }
                }else{
                    usuario.setText("");
                    contra.setText("");
                }
            }
        });

        Button btRegistro = findViewById(R.id.Registro);
        btRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, Registro.class);
                startActivity(i);
                finish();
            }
        });
    }


    public void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
