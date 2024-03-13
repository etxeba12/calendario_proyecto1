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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calendario_tema4.BaseDeDatos.BD;
import com.example.calendario_tema4.calendarioReycler.MainActivity;

import java.util.List;
import java.util.Locale;

public class ListaAtletas extends AppCompatActivity {
    private SQLiteDatabase db;

    private String entrenador;
    private ArrayAdapter<String> eladaptador;

    private String idioma;

    private List<String> lista;
    private int tema;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String pUsuario= extras.getString("entrenador");
           this.entrenador = pUsuario;
           idioma = extras.getString("idioma");
           tema = extras.getInt("tema");
           setTheme(tema);

        }
        if(idioma != null){
            cambiarIdioma(idioma);
        }
        setContentView(R.layout.listausuarios);
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
                i.putExtra("idioma",idioma);
                i.putExtra("tema",tema);
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

    public void onChangeThemeClick(MenuItem item) {
        int currentTheme = tema; // tema actual

        if (currentTheme == R.style.Calendario_tema1) {
            currentTheme = R.style.Calendario_tema2;

        } else {
            currentTheme = R.style.Calendario_tema1;
        }
        getIntent().putExtra("idioma",idioma);
        getIntent().putExtra("tema",currentTheme);
        finish();
        startActivity(getIntent());
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.castellano){
            idioma = "es";
            getIntent().putExtra("idioma",idioma);
            getIntent().putExtra("tema",tema);
            finish();
            startActivity(getIntent());
            return true;
        }

        else if(id == R.id.euskera){
            idioma = "eu";
            getIntent().putExtra("idioma",idioma);
            getIntent().putExtra("tema",tema);
            finish();
            startActivity(getIntent());
            return true;
        }
        else if(id == R.id.ingles){
            idioma = "en";
            getIntent().putExtra("idioma",idioma);
            getIntent().putExtra("tema",tema);
            finish();
            startActivity(getIntent());
            return true;
        }
        return super.onOptionsItemSelected(item);

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
