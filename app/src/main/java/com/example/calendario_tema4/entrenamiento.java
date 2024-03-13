package com.example.calendario_tema4;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.calendario_tema4.BaseDeDatos.BD;
import com.example.calendario_tema4.calendarioReycler.MainActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Locale;

public class entrenamiento extends AppCompatActivity {

    private SQLiteDatabase db;

    private String nombreAtleta;

    private String idioma;

    private String fecha;

    private String mes;
    private List<String> lista;
    private ArrayAdapter<String> eladaptador;
    private int tema;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idioma = extras.getString("idioma");
            String pMes= extras.getString("mes");
            String pAño= extras.getString("año");
            String pDia= extras.getString("diaSeleccionado");
            String pAtleta= extras.getString("atleta");
            tema = extras.getInt("tema");
            setTheme(tema);
            mes = pMes;
            fecha = pAño+"-"+pMes+"-"+pDia;
            nombreAtleta = pAtleta;
        }
        if(idioma != null){
            cambiarIdioma(idioma);
        }
        setContentView(R.layout.ver_entrenamiento);

        BD GestorDB = new BD(this, "Tabla", null, 1);
        db = GestorDB.getWritableDatabase();


        TextView tv = findViewById(R.id.nombreAtleta);
        tv.setText(nombreAtleta);

       Button guardar = findViewById(R.id.btGuardar);
       guardar.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               // Verifica si la memoria externa está disponible
               String estadoMemoriaExterna = Environment.getExternalStorageState();

               if (Environment.MEDIA_MOUNTED.equals(estadoMemoriaExterna) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(estadoMemoriaExterna)) {
                   // Obtiene el directorio de la memoria externa
                   File directorioExterno = getExternalFilesDir(null);
                   Log.d("Directorio externo", directorioExterno.getAbsolutePath());

                   // Crea el archivo en la memoria externa
                   File archivoExterno = new File(directorioExterno, "nombrefichero.txt");
                   Cursor cursor = GestorDB.conseguirEntrenoMes(db, mes, nombreAtleta);
                   try {
                       // Crear un archivo de texto para  escribir los datos
                       OutputStreamWriter fichero = new OutputStreamWriter(new FileOutputStream(archivoExterno));

                       String fechaAnterior = "";

                       //  escribimos en el archivo
                       while (cursor.moveToNext()) {

                           String fecha = cursor.getString(0);
                           String ejercicio = cursor.getString(1);
                           int series = cursor.getInt(2);
                           int repeticiones = cursor.getInt(3);
                           int rpe = cursor.getInt(4);

                           if (!fecha.equals(fechaAnterior)) {
                               fichero.write("\n" + fecha + "\n");
                               fechaAnterior = fecha;
                           }

                           String linea = ejercicio + " " + series + " x " + repeticiones + " @" + rpe;
                           fichero.write(linea + "\n");
                           Log.d("fila", linea);

                       }
                       // Cerrams el archivo
                       fichero.close();
                   } catch (IOException e) {
                       e.printStackTrace();
                   } finally {
                       // Cerrar el cursor
                       cursor.close();
                   }
               }
           }
       });

        lista = GestorDB.conseguirNombreEjer(db,fecha,nombreAtleta);
        eladaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,lista);
        ListView lalista = (ListView) findViewById(R.id.listaEjercicios);
        lalista.setAdapter(eladaptador);

        lalista.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){

            }
        });
    }

    protected void cambiarIdioma(String idioma){
        Locale nuevaloc = new Locale(idioma);
        Locale.setDefault(nuevaloc);

        Configuration configuration = getBaseContext().getResources().getConfiguration();
        configuration.setLocale(nuevaloc);
        configuration.setLayoutDirection(nuevaloc);

        Context context = getBaseContext().createConfigurationContext(configuration);
        getBaseContext().getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());
    }

}
