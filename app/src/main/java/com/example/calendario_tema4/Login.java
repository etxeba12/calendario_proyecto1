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
                        i.putExtra("usuario",usuario.getText().toString());
                        startActivity(i);
                        finish();
                    }else{
                        Intent i = new Intent(Login.this, MainActivity.class);
                        i.putExtra("usuario",usuario.getText().toString());
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
