package com.example.calendario_tema4;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
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

    private String entrenador;
    private ArrayAdapter<String> eladaptador;

    private List<String> lista;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listausuarios);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String pUsuario= extras.getString("entrenador");
           this.entrenador = pUsuario;
        }

        setSupportActionBar(findViewById(R.id.labarra));

        BD GestorDB = new BD(this, "Tabla", null, 1);
        db = GestorDB.getWritableDatabase();

        lista = GestorDB.obtenerListaUsuarios(db,entrenador);
        eladaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,lista);
        ListView lalista = (ListView) findViewById(R.id.listView_atletas);
        lalista.setAdapter(eladaptador);

        lalista.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Log.i("etiqueta", ((TextView)view).getText().toString()+", "+position+", "+id);
                Intent i = new Intent(ListaAtletas.this, MainActivity.class);
                i.putExtra("atleta", ((TextView)view).getText().toString());
                i.putExtra("entrenador",entrenador);
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
                lista.clear();
                if (!pNombre.equals("")) {
                    lista.add(pNombre);
                }else{ //si el buscado esta vacio o no exite aparecen todos
                    List<String> usuarios = GestorDB.obtenerListaUsuarios(db,entrenador);
                    for (String usuario:usuarios){
                        lista.add(usuario);
                    }
                }

                eladaptador.notifyDataSetChanged();
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

}
