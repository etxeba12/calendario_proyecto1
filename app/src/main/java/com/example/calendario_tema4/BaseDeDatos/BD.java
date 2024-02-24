package com.example.calendario_tema4.BaseDeDatos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BD extends SQLiteOpenHelper {
    public BD(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Usuarios ('nombreUsuarios' VARCHAR(255) PRIMARY KEY NOT NULL, 'contrasena' VARCHAR(255) NOT NULL, 'entrenador' VARCHAR(255), 'esEntrenador' INTEGER(1) NOT NULL)");
        db.execSQL("CREATE TABLE calendario('fecha' date NOT NULL, 'nombreEjercicio' varchar(50) DEFAULT NULL,'series' int(11) DEFAULT NULL,'repeticiones' int(11) DEFAULT NULL,'RPE' int(11) DEFAULT NULL, 'kilos' float DEFAULT NULL,'cliente_nombre' varchar(50) DEFAULT NULL,'kilosCliente' varchar(100) DEFAULT NULL,'RPECliente' varchar(100) DEFAULT NULL,FOREIGN KEY ('cliente_nombre') REFERENCES Usuarios ('nombreUsuarios') ON DELETE CASCADE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Usuarios");
        db.execSQL("DROP TABLE IF EXISTS Calendario");
        onCreate(db);
    }

    public String comprobarUsuario(SQLiteDatabase db,String usu) {
        String query = "SELECT * FROM Usuarios WHERE nombreUsuarios = ?";
        String[] selectionArgs = {usu};
        String esEntrenador = null;
        Cursor c = db.rawQuery(query, selectionArgs);
        if (c.getCount() != 0) {
            if (c.moveToFirst()) {
                //si no esta vacio, cogemos la columna "esEntrenador"
                esEntrenador = c.getString(3);
            }
        }
        c.close(); // Cerramos el cursor después de usarlo
        return esEntrenador;
    }

    public void meterUsuario(SQLiteDatabase db,String nombreUsuario, String pContraseña, String pEntrenador, Integer pEsEntrenador){
        ContentValues values = new ContentValues();
        values.put("nombreUsuarios", nombreUsuario);
        values.put("contrasena", pContraseña);
        values.put("entrenador", pEntrenador);
        values.put("esEntrenador", pEsEntrenador);

        // Ejecutar la consulta parametrizada
        db.insert("Usuarios", null, values);
    }

    public List<String> obtenerListaUsuarios(SQLiteDatabase db, String nombreEntrenador) {
        List<String> usuarios = new ArrayList<>(); // la lista donde guardar los usuarios

        String query = "SELECT nombreUsuarios FROM Usuarios WHERE entrenador = ?";
        String[] selectionArgs = {nombreEntrenador};
        Cursor cursor = db.rawQuery(query, selectionArgs);

        if (cursor.moveToFirst()) {
            do {
                String nombreUsuario = cursor.getString(cursor.getColumnIndexOrThrow("nombreUsuarios"));
                usuarios.add(nombreUsuario);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return usuarios;
    }
    public String obtenerUsuarioEntrenador(SQLiteDatabase db,String pNombre) {
        String usuario = "";// la lista donde guardar los usuarios

        String query = "SELECT nombreUsuarios FROM Usuarios WHERE nombreUsuarios = ?";
        String[] selectionArgs = {pNombre};
        Cursor cursor = db.rawQuery(query, selectionArgs);

        if (cursor.moveToFirst()) {
            usuario = cursor.getString(cursor.getColumnIndexOrThrow("nombreUsuarios"));
        }
        cursor.close();

        return usuario;
    }





}
