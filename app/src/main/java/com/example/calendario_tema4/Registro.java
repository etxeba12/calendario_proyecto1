package com.example.calendario_tema4;

import static android.icu.text.ListFormatter.Type.AND;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class Registro extends AppCompatActivity {
    private SQLiteDatabase db;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);

        BD GestorDB = new BD(this, "Usuarios", null, 1);
        db = GestorDB.getWritableDatabase();

        Button registrobt = findViewById(R.id.Registro);

        registrobt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nombreUsu = findViewById(R.id.NombreUsuarioMeter);
                EditText contra1 = findViewById(R.id.meterContra);
                EditText contra2 = findViewById(R.id.meterContra2);
                CheckBox entrenador = findViewById(R.id.esEntrenador);
                if(nombreUsu.getText().toString() != ""){
                    if(contra1.getText().toString() != "" && contra2.getText().toString() != ""){
                        if(contra1.getText().toString().equals(contra2.getText().toString())){
                            if(entrenador.isChecked()){
                                meterUsuario(nombreUsu.getText().toString(),contra1.getText().toString(),1);
                            }else{
                                meterUsuario(nombreUsu.getText().toString(),contra1.getText().toString(),0);
                            }
                            Intent i = new Intent(Registro.this, Login.class);
                            startActivity(i);
                            finish();
                        }else{
                            //las contraseñas no coinciden
                        }
                    }else{
                        //hacer que salga una pantallita diciendo que las contraseñas no puede estar vacias
                    }
                }else{
                    //hacer que salga una pantallita diciendo que el nombre usuario no puede estar vacio
                }
            }
        });

        Button loginbt = findViewById(R.id.LoginBoton);

        loginbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Registro.this,Login.class);
                startActivity(i);
                finish();
            }
        });

    }

    private void meterUsuario(String nombreUsuario, String pContraseña, Integer pEntrenador){
        ContentValues values = new ContentValues();
        values.put("nombreUsuarios", nombreUsuario);
        values.put("contrasena", pContraseña);
        values.put("entrenador", pEntrenador);

        // Ejecutar la consulta parametrizada
        db.insert("Usuarios", null, values);

    }

    public void onDestroy() {
        super.onDestroy();
        db.close();
    }

}
