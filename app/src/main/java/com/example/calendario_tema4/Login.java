package com.example.calendario_tema4;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    private SQLiteDatabase db;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        BD GestorDB = new BD(this, "Usuarios", null, 1);
        db = GestorDB.getWritableDatabase();

        Button btLogin = findViewById(R.id.LoginBoton);
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText usuario = findViewById(R.id.NombreUsuarioMeter);
                EditText contra = findViewById(R.id.meterContra);
                boolean valido = comprobarUsuario(usuario.getText().toString(),contra.getText().toString());
                if (valido){
                    Intent i = new Intent(Login.this,MainActivity.class);
                    i.putExtra("usuario",usuario.getText().toString());
                    startActivity(i);
                    finish();
                }else{
                    usuario.setText("");
                    contra.setText("");
                }
            }
        });
    }

    private boolean comprobarUsuario(String usu, String Contra) {
        String query = "SELECT * FROM Usuarios WHERE nombreUsuarios = ? AND contrasena = ?";
        String[] selectionArgs = {usu, Contra};
        Cursor c = db.rawQuery(query, selectionArgs);
        if (c.getCount() != 0) {
            c.close(); // Cerramos el cursor después de usarlo
            return true;
        }
        c.close(); // Cerramos el cursor después de usarlo
        return false;
    }

    public void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
