package com.example.calendario_tema4;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.calendario_tema4.BaseDeDatos.BD;
import com.example.calendario_tema4.calendarioReycler.MainActivity;

import java.util.List;

public class entrenamiento extends AppCompatActivity {

    private SQLiteDatabase db;

    private String nombreAtleta;

    private String fecha;
    private List<String> lista;
    private ArrayAdapter<String> eladaptador;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ver_entrenamiento);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String pMes= extras.getString("mes");
            String pAño= extras.getString("año");
            String pDia= extras.getString("diaSeleccionado");
            String pAtleta= extras.getString("atleta");
            Log.d("nombreAt",pAtleta);
            fecha = pAño+"-"+pMes+"-"+pDia;
            Log.d("lista",fecha);
            nombreAtleta = pAtleta;
            Log.d("lista",nombreAtleta);
        }

        setSupportActionBar(findViewById(R.id.labarra));

        BD GestorDB = new BD(this, "Tabla", null, 1);
        db = GestorDB.getWritableDatabase();

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

    //Definir el fichero xml al toolbar
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

}
