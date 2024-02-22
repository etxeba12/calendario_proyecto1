package com.example.calendario_tema4;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calendario_tema4.BaseDeDatos.BD;

public class ListaAtletas extends AppCompatActivity {
    private SQLiteDatabase db;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listausuarios);

        BD GestorDB = new BD(this, "Tabla", null, 1);
        db = GestorDB.getWritableDatabase();

        String[] arraydedatos={"alumno1","alumno2","alumno3"};
        ArrayAdapter eladaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,arraydedatos);
        ListView lalista = (ListView) findViewById(R.id.listView_atletas);
        lalista.setAdapter(eladaptador);

        lalista.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Log.i("etiqueta", ((TextView)view).getText().toString()+", "+position+", "+id);
            }
        });




    }

}
