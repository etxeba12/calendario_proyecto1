package com.example.calendario_tema4.BaseDeDatos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.sql.ResultSet;
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

    public boolean comprobarExisteUsuario(SQLiteDatabase db,String usu) {
        String query = "SELECT * FROM Usuarios WHERE nombreUsuarios = ?";
        String[] selectionArgs = {usu};
        boolean existe = false;
        Cursor c = db.rawQuery(query, selectionArgs);
        if (c.getCount() != 0) {
                existe = true;
        }
        c.close(); // Cerramos el cursor después de usarlo
        return existe;
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
    public void borrarEntrenadorDeAtleta(SQLiteDatabase db, String pAtleta) {
        // actualizar la columna entrenador a ''
        String query = "UPDATE Usuarios SET entrenador = '' WHERE nombreUsuarios = ?";
        String[] whereArgs = {pAtleta};

        // Ejecuta la consulta de actualización
        db.execSQL(query, whereArgs);
    }
    public boolean atletaTieneEntrenador(SQLiteDatabase db, String pAtleta) {
        // Define la consulta SQL para verificar si el atleta tiene un entrenador
        String query = "SELECT entrenador FROM Usuarios WHERE nombreUsuarios=? AND entrenador != ''";
        String[] selectionArgs = {pAtleta};
        Cursor cursor = db.rawQuery(query, selectionArgs);

        // Verifica si se encontró algún resultado
        boolean tieneEntrenador = false;
        if (cursor != null && cursor.moveToFirst()) {
            tieneEntrenador = true;
        }
        if (cursor != null) {
            cursor.close();
        }

        return tieneEntrenador;
    }


    public void meterUsuario(SQLiteDatabase db,String nombreUsuario, String pContraseña, String pEntrenador, Integer pEsEntrenador){
        //Agregar un usuario a la base datos
        ContentValues values = new ContentValues();
        values.put("nombreUsuarios", nombreUsuario);
        values.put("contrasena", pContraseña);
        values.put("entrenador", pEntrenador);
        values.put("esEntrenador", pEsEntrenador);

        // Ejecutar la consulta parametrizada
        db.insert("Usuarios", null, values);
    }

    public boolean hayEntreno(SQLiteDatabase db,String pFecha, String pNombre){
        String query = "SELECT * FROM calendario WHERE fecha = ? AND cliente_nombre = ? ";
        String[] selectionArgs = {pFecha,pNombre};
        Cursor cursor = db.rawQuery(query, selectionArgs);

        if (cursor.getCount()!=0) {
            return true;
        }
        cursor.close();

        return false;
    }

    public void meterAtletaAEntrenador(SQLiteDatabase db,String pEntrenador, String pAtleta){
        //Agregar un Entrenador a un atleta
        ContentValues values = new ContentValues();
        values.put("entrenador", pEntrenador);

        String whereClause = "nombreUsuarios=?";
        String[] whereArgs = {pAtleta};

        // Ejecuta la consulta para actualizar el registro del usuario
        int rowsAffected = db.update("Usuarios", values, whereClause, whereArgs);
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
    public String obtenerUsuarioEntrenador(SQLiteDatabase db,String pNombre,String pEntrenador) {
        String usuario = "";// la lista donde guardar los usuarios

        String query = "SELECT nombreUsuarios FROM Usuarios WHERE nombreUsuarios = ? AND entrenador=?";
        String[] selectionArgs = {pNombre,pEntrenador};
        Cursor cursor = db.rawQuery(query, selectionArgs);

        if (cursor.moveToFirst()) {
            usuario = cursor.getString(cursor.getColumnIndexOrThrow("nombreUsuarios"));
        }
        cursor.close();

        return usuario;
    }

    public void borrarEjercicio(SQLiteDatabase db, String pFecha, String pNombreUsuario, String nombreEjercicioABorrar) {
        String tabla = "calendario";
        String whereClause = "fecha = ? AND cliente_nombre = ? AND nombreEjercicio = ?";
        String[] whereArgs = {pFecha, pNombreUsuario, nombreEjercicioABorrar};

        db.delete(tabla, whereClause, whereArgs);
    }

    public void agregarEjercicio(SQLiteDatabase db, String pFecha, String pNombreUsuario, String nombreEjercicio) {
        ContentValues values = new ContentValues();
        values.put("fecha", pFecha);
        values.put("cliente_nombre", pNombreUsuario);
        values.put("nombreEjercicio", nombreEjercicio);

        db.insert("calendario", null, values);
    }


    public List<String> conseguirNombreEjer(SQLiteDatabase db,String pFecha,String pNombreUsuario) {
        List<String> nombreEjercicios = new ArrayList<>(); // la lista donde guardar los usuarios

        String query = "SELECT * FROM calendario WHERE fecha = ? AND cliente_nombre = ?";
        String[] selectionArgs = {pFecha,pNombreUsuario};
        Cursor cursor = db.rawQuery(query, selectionArgs);

        if (cursor.moveToFirst()) {
            do {
                String nombreEjer = cursor.getString(cursor.getColumnIndexOrThrow("nombreEjercicio"));
                nombreEjercicios.add(nombreEjer);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return nombreEjercicios;
    }

    public Cursor conseguirEntrenoMes(SQLiteDatabase db,String pMes, String pNombreAtleta){
        String query = "SELECT * FROM calendario WHERE strftime('%m', fecha) = ? AND cliente_nombre = ?";
        String[] selectionArgs = {pMes,pNombreAtleta};
        return db.rawQuery(query,selectionArgs);
    }





}
