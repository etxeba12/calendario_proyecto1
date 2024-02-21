package com.example.calendario_tema4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class BD extends SQLiteOpenHelper {
    public BD(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Usuarios ('nombreUsuarios' VARCHAR(255) PRIMARY KEY NOT NULL, 'contrasena' VARCHAR(255) NOT NULL, 'entrenador' INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Usuarios");
        onCreate(db);

    }

    public boolean comprobarUsuario(SQLiteDatabase db,String usu) {
        String query = "SELECT * FROM Usuarios WHERE nombreUsuarios = ?";
        String[] selectionArgs = {usu};
        Cursor c = db.rawQuery(query, selectionArgs);
        if (c.getCount() != 0) {
            c.close(); // Cerramos el cursor después de usarlo
            return true;
        }
        c.close(); // Cerramos el cursor después de usarlo
        return false;
    }

    public void meterUsuario(SQLiteDatabase db,String nombreUsuario, String pContraseña, Integer pEntrenador){
        ContentValues values = new ContentValues();
        values.put("nombreUsuarios", nombreUsuario);
        values.put("contrasena", pContraseña);
        values.put("entrenador", pEntrenador);

        // Ejecutar la consulta parametrizada
        db.insert("Usuarios", null, values);
    }

}
