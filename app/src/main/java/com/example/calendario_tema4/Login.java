package com.example.calendario_tema4;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        BD GestorDB = new BD(this, "Usuarios", null, 1);
        SQLiteDatabase bd = GestorDB.getWritableDatabase();
        bd.execSQL("INSERT INTO Usuarios (nombreUsuarios, contrasena, entrenador) VALUES ('Iker', '1234', 1)");
        bd.execSQL("INSERT INTO Usuarios (nombreUsuarios, contrasena, entrenador) VALUES ('Ander', '1234', 0)");
        bd.execSQL("INSERT INTO Usuarios (nombreUsuarios, contrasena, entrenador) VALUES ('Eider', '1234', 0)");
    }
}
