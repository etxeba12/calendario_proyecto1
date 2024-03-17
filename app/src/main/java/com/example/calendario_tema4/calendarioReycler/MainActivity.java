package com.example.calendario_tema4.calendarioReycler;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.util.AttributeSet;
import android.view.MenuItem;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calendario_tema4.BaseDeDatos.BD;
import com.example.calendario_tema4.ListaAtletas;
import com.example.calendario_tema4.R;
import com.example.calendario_tema4.calendarioReycler.adaptadorRecycler;
import com.example.calendario_tema4.dialogos.dialogoAlerta;
import com.example.calendario_tema4.dialogos.salirAplicacion;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private String dia ;
    private String mes;
    private String año;
    private String fecha;

    private String idioma;

    private int tema;

    private SQLiteDatabase db;
    private String nombreAtleta;
    private String nombreEntrenador;
    private BD GestorDB = new BD(this, "Tabla", null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idioma = extras.getString("idioma");
            String pMes= extras.getString("mes");
            String pAño= extras.getString("año");
            String pNombre = extras.getString("atleta");
            String pEntrenador = extras.getString("entrenador");
            tema = extras.getInt("tema");

            setTheme(tema);
            if(pMes != null && pAño != null){
                if (pMes.length() == 2) { // Verificar si pMes tiene dos dígitos
                    mes = pMes;
                } else if (pMes.length() == 1) { // Si pMes tiene un solo dígito, agregar un 0 al principio
                    mes = "0" + pMes;
                }
                año=pAño;
            }
            nombreAtleta = pNombre;
            nombreEntrenador = pEntrenador;
            fecha = "00/" + mes + "/"+ año;
        }
        if(idioma != null){
            cambiarIdioma(idioma);
        }
        setContentView(R.layout.activity_main);
        solicitarPermisosNotificaciones(); //Pedimos permisos, si todavia no tiene
        setSupportActionBar(findViewById(R.id.labarra));
        db = GestorDB.getWritableDatabase();

        RecyclerView laLista = findViewById(R.id.rv);
        //Conseguir y poner fecha
        if(mes==null || año==null) {
            fecha =consigueFecha();
        }
        String[] fechaLista = fecha.split("/");
        mes = fechaLista[1];
        año = fechaLista[2];
        TextView tv = findViewById(R.id.fecha);
        tv.setText(obtenerNombreMes(Integer.parseInt(mes)) + " "+ año);
        String[] diaMes = new String[obtenerDiasMes(Integer.parseInt(mes),Integer.parseInt(año))];
        for (int i = 0; i < diaMes.length ; i++) {
            diaMes[i] = String.valueOf(i + 1);
        }
        int[] imagenes = new int[diaMes.length];
        
        for (int i = 1; i < diaMes.length + 1 ; i++){
            Log.d("entreno",año+mes+i);
            String d;
            if (i <10){
                d = "0"+i;
            }else{
                d = Integer.toString(i);
            }
            if(GestorDB.hayEntreno(db,año+"-"+mes+"-"+d,nombreAtleta)){
                imagenes[i-1] = R.drawable.pesa;
            }else{
                imagenes[i-1] = R.drawable.descanso;
            }
        }
        //Conseguir y poner fecha

        ImageButton btAbrirNav = findViewById(R.id.btAbrirNavegador);
        btAbrirNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?client=firefox-b-d&q=gimnasios+cerca+de+mi"));
                startActivity(i);
            }
        });

        //Botones navegación
        Button botonAnt = findViewById(R.id.bt_ant);
        botonAnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int MesNum = Integer.parseInt(mes) - 1;
                int añoNum = Integer.parseInt(año);
                if(MesNum <= 0){
                    añoNum --;
                    MesNum = 12;

                }
                Intent i = getIntent();
                i.putExtra("idioma",idioma);
                i.putExtra("mes",Integer.toString(MesNum));
                i.putExtra("año",Integer.toString(añoNum));
                i.putExtra("atleta",nombreAtleta);
                i.putExtra("tema",tema);
                finish();
                startActivity(i);
            }
        });
        Button botonSig = findViewById(R.id.bt_sig);
        botonSig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int MesNum = Integer.parseInt(mes) + 1;
                int añoNum = Integer.parseInt(año);
                if(MesNum >= 13){
                    añoNum ++;
                    MesNum = 1;

                }
                Intent i = getIntent();
                i.putExtra("idioma",idioma);
                i.putExtra("mes",Integer.toString(MesNum));
                i.putExtra("año",Integer.toString(añoNum));
                i.putExtra("atleta",nombreAtleta);
                i.putExtra("tema",tema);
                finish();
                startActivity(i);

            }
        });
        //Botones navegación

        adaptadorRecycler eladaptador = new adaptadorRecycler(diaMes,imagenes,activityLauncher,mes,año,nombreAtleta,idioma,tema,nombreEntrenador);
        laLista.setAdapter(eladaptador);

        RecyclerView.LayoutManager elLayoutRejillaIgual= new GridLayoutManager(this,7,GridLayoutManager.VERTICAL,false);
        laLista.setLayoutManager(elLayoutRejillaIgual);


    }

    private final ActivityResultLauncher<Intent> activityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // para recrear la actividad al agregar ejercicios en un dia en concreto
                    recreate();
                }
            });
    private void solicitarPermisosNotificaciones(){
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)!= PackageManager.PERMISSION_GRANTED) {
            //PEDIR EL PERMISO
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 11);
        }
    }
    private String consigueFecha(){
        Date fechaActual = new Date();
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        String fechaFormateada = formatoFecha.format(fechaActual);
        return fechaFormateada;
    }

    private String obtenerNombreMes(int numeroMes) {
        Locale locale = new Locale("es", "ES");
        DateFormatSymbols symbols = new DateFormatSymbols(locale);
        String[] nombresMeses = symbols.getMonths();
        return nombresMeses[numeroMes - 1];
    }

    private int obtenerDiasMes(int mes, int año) {
        YearMonth añoMes = YearMonth.of(año, mes);
        return añoMes.lengthOfMonth();
    }


    public void onBackPressed() {
        if(nombreEntrenador!=null){ //miramos si es entrenador
            Intent i = new Intent(MainActivity.this, ListaAtletas.class);
            i.putExtra("atleta",nombreAtleta);
            i.putExtra("entrenador",nombreEntrenador);
            i.putExtra("idioma",idioma);
            i.putExtra("tema",tema);
            startActivity(i);
            finish();
        }else{
            DialogFragment dialogoAlerta = new salirAplicacion();
            dialogoAlerta.show(getSupportFragmentManager(), "etiqueta");
        }
    }

    //Definir el fichero xml al toolbar
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Log.d("MainActivity", "onOptionsItemSelected: " + item.getItemId());
        if(id == R.id.castellano){
            Log.d("entro","he entrado");
            idioma = "es";
            Intent i =   new Intent(MainActivity.this,MainActivity.class);
            i.putExtra("idioma",idioma);
            i.putExtra("mes",mes);
            i.putExtra("año",año);
            i.putExtra("atleta",nombreAtleta);
            i.putExtra("entrenador",nombreEntrenador);
            i.putExtra("tema",tema);
            finish();
            startActivity(i);
            return true;
        }

        else if(id == R.id.euskera){
            idioma = "eu";
            Intent i =   new Intent(MainActivity.this,MainActivity.class);
            i.putExtra("idioma",idioma);
            i.putExtra("mes",mes);
            i.putExtra("año",año);
            i.putExtra("atleta",nombreAtleta);
            i.putExtra("entrenador",nombreEntrenador);
            i.putExtra("tema",tema);
            startActivity(i);
            finish();
            return true;
        }
        else if(id == R.id.ingles){
            idioma = "en";
            Intent i =   new Intent(MainActivity.this,MainActivity.class);
            i.putExtra("idioma",idioma);
            i.putExtra("mes",mes);
            i.putExtra("año",año);
            i.putExtra("atleta",nombreAtleta);
            i.putExtra("entrenador",nombreEntrenador);
            i.putExtra("tema",tema);
            startActivity(i);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);

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

    protected void cambiarIdioma(String idioma){
        Locale nuevaloc = new Locale(idioma);
        Locale.setDefault(nuevaloc);

        Configuration configuration = getBaseContext().getResources().getConfiguration();
        configuration.setLocale(nuevaloc);
        configuration.setLayoutDirection(nuevaloc);

        Context context = getBaseContext().createConfigurationContext(configuration);
        getBaseContext().getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());
    }



    public String getMes(){
        return this.mes;
    }

    public String getAño(){
        return this.año;
    }
}