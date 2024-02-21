package com.example.calendario_tema4;

import static android.icu.text.ListFormatter.Type.AND;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

public class Registro extends AppCompatActivity {
    private SQLiteDatabase db; //base de datos de la tabla usuarios

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);

        BD GestorDB = new BD(this, "Tablas", null, 1);
        db = GestorDB.getWritableDatabase();

        Button registrobt = findViewById(R.id.Registro);

        registrobt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nombreUsu = findViewById(R.id.NombreUsuarioMeter);
                EditText contra1 = findViewById(R.id.meterContra);
                EditText contra2 = findViewById(R.id.meterContra2);
                CheckBox entrenador = findViewById(R.id.esEntrenador);
                if(!nombreUsu.getText().toString().equals("")){
                    if(!contra1.getText().toString().equals("") && !contra2.getText().toString().equals("")){
                        if(contra1.getText().toString().equals(contra2.getText().toString())){
                            if(entrenador.isChecked()){
                                if(!GestorDB.comprobarUsuario(db,nombreUsu.getText().toString(),true)) {
                                    GestorDB.meterEntrenador(db,nombreUsu.getText().toString(),contra1.getText().toString());
                                    Intent i = new Intent(Registro.this, Login.class);
                                    startActivity(i);
                                    finish();
                                }else{
                                    Log.d("hola","hola");
                                    dialogoAlerta dialogo = new dialogoAlerta();
                                    dialogo.setMensaje("Usuario existente, pruebe otro");
                                    dialogo.show(getSupportFragmentManager(), "etiqueta0");
                                }
                            }else{
                                if(!GestorDB.comprobarUsuario(db,nombreUsu.getText().toString(),false)) {
                                    GestorDB.meterUsuario(db,nombreUsu.getText().toString(),contra1.getText().toString(),"");
                                    Intent i = new Intent(Registro.this, Login.class);
                                    startActivity(i);
                                    finish();
                                }else{
                                    Log.d("hola","hola1");
                                    dialogoAlerta dialogo = new dialogoAlerta();
                                    dialogo.setMensaje("Usuario existente, pruebe otro");
                                    dialogo.show(getSupportFragmentManager(), "etiqueta1");
                                }
                            }
                        }else{
                            //las contraseñas no coinciden
                            dialogoAlerta dialogo = new dialogoAlerta();
                            dialogo.setMensaje("Las contraseñas no coinciden");
                            dialogo.show(getSupportFragmentManager(), "etiqueta2");
                        }
                    }else{
                        //hacer que salga una pantallita diciendo que las contraseñas no puede estar vacias
                        dialogoAlerta dialogo = new dialogoAlerta();
                        dialogo.setMensaje("Las contraseñas no pueden estar vacias");
                        dialogo.show(getSupportFragmentManager(), "etiqueta3");
                    }
                }else{
                    //hacer que salga una pantallita diciendo que el nombre usuario no puede estar vacio
                    dialogoAlerta dialogo = new dialogoAlerta();
                    dialogo.setMensaje("El nombre de usuario no puede estar vacio");
                    dialogo.show(getSupportFragmentManager(), "etiqueta4");
                }
                nombreUsu.setText("");
                contra1.setText("");
                contra2.setText("");
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

    public void onDestroy() {
        super.onDestroy();

    }

}
