package com.example.calendario_tema4;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.DialogFragment;

import com.example.calendario_tema4.BaseDeDatos.BD;
import com.example.calendario_tema4.calendarioReycler.MainActivity;
import com.example.calendario_tema4.dialogos.dialogoAlerta;
import com.example.calendario_tema4.dialogos.salirAplicacion;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Locale;

public class entrenamiento extends AppCompatActivity {

    private SQLiteDatabase db;

    private String nombreAtleta;
    private String nombreEntrenador;
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
            String pEntrenador = extras.getString("entrenador");
            String pDia= extras.getString("diaSeleccionado");
            String pAtleta= extras.getString("atleta");
            tema = extras.getInt("tema");
            setTheme(tema);
            mes = pMes;
            fecha = pAño+"-"+pMes+"-"+pDia;
            nombreAtleta = pAtleta;
            nombreEntrenador =pEntrenador;
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

        //para borrar atletas al clickar mucho en uno
        lalista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                // cogemos el nombre del atleta
                String nombreEjer = (String) adapterView.getItemAtPosition(i);
                lista.clear();
                GestorDB.borrarEjercicio(db,fecha,nombreAtleta,nombreEjer);
                List<String> ejercicios = GestorDB.conseguirNombreEjer(db,fecha,nombreAtleta);
                for (String ejercicio:ejercicios){
                    lista.add(ejercicio);
                }
                eladaptador.notifyDataSetChanged();
                int tiempo= Toast.LENGTH_SHORT;
                Toast aviso = Toast.makeText(entrenamiento.this, "Se ha eliminado el ejercicio", tiempo);
                aviso.show();
                return true;
            }
        });

        ImageButton btAgregar = findViewById(R.id.btAgregar); //boton para agregarAtletas
        btAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et = findViewById(R.id.NombreAgregarEjer);
                if(!et.getText().toString().equals("")){
                    lista.clear();
                    GestorDB.agregarEjercicio(db,fecha,nombreAtleta,et.getText().toString());
                    List<String> ejercicios = GestorDB.conseguirNombreEjer(db,fecha,nombreAtleta);
                    for (String ejercicio:ejercicios){
                        Log.d("ejer",ejercicio);
                        lista.add(ejercicio);
                    }
                    eladaptador.notifyDataSetChanged();
                    crearNotificacion();
                }else{
                    dialogoAlerta dialogo = new dialogoAlerta();
                    dialogo.setMensaje("El ejercicio no puede estar vacio");
                    dialogo.show(getSupportFragmentManager(), "etiqueta2");
                }

            }
        });
    }

    private void crearNotificacion(){
        NotificationManager elManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder elBuilder = new NotificationCompat.Builder(this, "1");
        elBuilder.setSmallIcon(android.R.drawable.stat_sys_warning);
        elBuilder.setContentTitle("Entrenamiento añadido");
        elBuilder.setAutoCancel(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel elCanal = new NotificationChannel("1", "NombreCanal", NotificationManager.IMPORTANCE_DEFAULT);
            elCanal.enableLights(true);
            elCanal.setLightColor(Color.RED);
            elCanal.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            elCanal.enableVibration(true);
            elManager.createNotificationChannel(elCanal);
        }
        elManager.notify(1, elBuilder.build());
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
