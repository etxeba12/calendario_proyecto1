package com.example.calendario_tema4;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calendario_tema4.BaseDeDatos.BD;
import com.example.calendario_tema4.calendarioReycler.MainActivity;

import java.util.List;

public class ListaAtletas extends AppCompatActivity {
    private SQLiteDatabase db;

    private String nombre ="Iker";
    private ArrayAdapter<String> eladaptador;

    private List<String> lista;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listausuarios);

        BD GestorDB = new BD(this, "Tabla", null, 1);
        db = GestorDB.getWritableDatabase();

        lista = GestorDB.obtenerListaUsuarios(db,nombre);
            eladaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,lista);
        ListView lalista = (ListView) findViewById(R.id.listView_atletas);
        lalista.setAdapter(eladaptador);

        lalista.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Log.i("etiqueta", ((TextView)view).getText().toString()+", "+position+", "+id);
                Intent i = new Intent(ListaAtletas.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        Button btBuscar = findViewById(R.id.btBuscar);
        btBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et = findViewById(R.id.NombreAtleta);
                String pNombre = GestorDB.obtenerUsuarioEntrenador(db,et.getText().toString());
                Log.d("nombre",pNombre);
                lista.clear();
                if (!pNombre.equals("")) {
                    lista.add(pNombre);
                }

                eladaptador.notifyDataSetChanged();
            }
        });



    }

}
