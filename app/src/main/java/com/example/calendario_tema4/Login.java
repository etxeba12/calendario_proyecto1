package com.example.calendario_tema4;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.calendario_tema4.BaseDeDatos.BD;
import com.example.calendario_tema4.calendarioReycler.MainActivity;

import java.util.Locale;

public class Login extends AppCompatActivity {

    private SQLiteDatabase db;
    private String idioma;

    private int tema = R.style.Calendario_tema1;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idioma = extras.getString("idioma");
            tema = extras.getInt("tema");
            setTheme(tema);
        }
        if(idioma != null){
            cambiarIdioma(idioma);
        }
        setContentView(R.layout.login);


        BD GestorDB = new BD(this, "Tabla", null, 1);
        db = GestorDB.getWritableDatabase();

        setSupportActionBar(findViewById(R.id.labarra));
        //db.delete("Calendario", "series > ?", new String[] {"0"});
        /*
        db.execSQL("INSERT INTO calendario (fecha, nombreEjercicio, series, repeticiones, RPE, kilos, cliente_nombre, kilosCliente, RPECliente) VALUES " +
                "('2024-03-19', 'Comp SQ', 4, 4, 5, 180, 'manuel', ' 0 0 0 0', ' 0 0 0 0'), " +
                "('2024-03-19', '320 BP', 3, 2, 6, 100, 'manuel', ' 0 0 0', ' 0 0 0'), " +
                "('2024-03-19', 'Empuje vertical', 4, 10, 9, 100, 'manuel', ' 0 0 0 0', ' 0 0 0 0'), " +
                "('2024-03-19', 'Extensión de triceps', 3, 12, 10, 100, 'manuel', ' 0 0 0', ' 0 0 0'), " +
                "('2024-03-14', 'Comp BP', 3, 3, 6, 110, 'manuel', ' 115 110 110', ' 5 5 6'), " +
                "('2024-03-14', '303 DL', 4, 5, 7, 200, 'manuel', ' 0 0 0 0', ' 0 0 0 0'), " +
                "('2024-03-14', 'Remo T', 4, 12, 8, 200, 'manuel', ' 0 0 0 0', ' 0 0 0 0'), " +
                "('2024-03-16', 'Biceps', 5, 12, 10, 200, 'manuel', ' 0 0 0 0 0', ' 0 0 0 0 0'), " +
                "('2024-03-16', 'PIN SQ', 1, 3, 5, 0, 'manuel', ' 100', ' 5'), " +
                "('2024-03-16', 'PIN SQ 2', 1, 3, 6, 0, 'manuel', ' 105', ' 5'), " +
                "('2024-03-16', 'PIN SQ 3', 1, 3, 7, 0, 'manuel', ' 110', ' 5'), " +
                "('2024-03-01', 'Larsen BP', 4, 4, 5, 0, 'manuel', NULL, NULL), " +
                "('2024-03-01', 'Deficit DL', 5, 5, 5, 140, 'manuel', NULL, NULL) ");

        db.execSQL("INSERT INTO Usuarios (nombreUsuarios, contrasena, entrenador, esEntrenador) VALUES " +
                "('eider', '1234', 'iker', 1), " +
                "('eufanasio', '1234', 'iker', 1), " +
                "('iker', '1234', '', 1), "+
                "('manuel', '1234', 'iker', 0), " +
                "('pedro', '1234', 'iker', 0)");
        */

        Button btLogin = findViewById(R.id.LoginBoton);
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText usuario = findViewById(R.id.NombreUsuarioMeter);
                EditText contra = findViewById(R.id.meterContra);
                String valido = GestorDB.comprobarUsuario(db,usuario.getText().toString());
                if (valido != null){
                    if (Integer.parseInt(valido)==1){ //comprobamos si es entrenador
                        Intent i = new Intent(Login.this, ListaAtletas.class);
                        i.putExtra("entrenador",usuario.getText().toString());
                        i.putExtra("tema",tema);
                        i.putExtra("idioma",idioma);
                        startActivity(i);
                        finish();
                    }else{
                        Intent i = new Intent(Login.this, MainActivity.class);
                        i.putExtra("atleta",usuario.getText().toString());
                        i.putExtra("tema",tema);
                        i.putExtra("idioma",idioma);
                        startActivity(i);
                        finish();
                    }
                }else{
                    usuario.setText("");
                    contra.setText("");
                }
            }
        });

        Button btRegistro = findViewById(R.id.Registro);
        btRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, Registro.class);
                i.putExtra("idioma",idioma);
                i.putExtra("tema",tema);
                startActivity(i);
                finish();
            }
        });
    }

    public void onDestroy() {
        super.onDestroy();
        db.close();
    }

    //Definir el fichero xml al toolbar
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
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

    @Override
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
