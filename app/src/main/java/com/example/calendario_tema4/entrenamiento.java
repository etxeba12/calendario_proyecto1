package com.example.calendario_tema4;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.calendario_tema4.BaseDeDatos.BD;
import com.example.calendario_tema4.calendarioReycler.MainActivity;

import java.util.List;
import java.util.Locale;

public class entrenamiento extends AppCompatActivity {

    private SQLiteDatabase db;

    private String nombreAtleta;

    private String idioma;

    private String fecha;
    private List<String> lista;
    private ArrayAdapter<String> eladaptador;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idioma = extras.getString("idioma");
            String pMes= extras.getString("mes");
            String pAño= extras.getString("año");
            String pDia= extras.getString("diaSeleccionado");
            String pAtleta= extras.getString("atleta");
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

        lista = GestorDB.conseguirNombreEjer(db,fecha,nombreAtleta);
        for(String i:lista){
            Log.d("lista",i);
        }
        eladaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,lista);
        ListView lalista = (ListView) findViewById(R.id.listaEjercicios);
        lalista.setAdapter(eladaptador);

        lalista.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Log.i("etiqueta", ((TextView)view).getText().toString()+", "+position+", "+id);

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
