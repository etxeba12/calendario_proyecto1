package com.example.calendario_tema4.calendarioReycler;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.calendario_tema4.BaseDeDatos.BD;
import com.example.calendario_tema4.ListaAtletas;
import com.example.calendario_tema4.R;
import com.example.calendario_tema4.calendarioReycler.adaptadorRecycler;
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

    private SQLiteDatabase db;
    private String nombreAtleta;
    private String nombreEntrenador;
    private BD GestorDB = new BD(this, "Tabla", null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        solicitarPermisosNotificaciones(); //Pedimos permisos, si todavia no tiene

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String pMes= extras.getString("mes");
            String pAño= extras.getString("año");
            String pNombre = extras.getString("atleta");
            String pEntrenador = extras.getString("entrenador");
            if(pMes != null && pAño != null){
                if(Integer.parseInt(pMes)<10){
                    mes = "0"+pMes;
                }else{
                    mes =pMes;
                }
                año=pAño;
            }
            nombreAtleta = pNombre;
            nombreEntrenador = pEntrenador;
            fecha = "00/" + mes + "/"+ año;
        }
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
                i.putExtra("mes",Integer.toString(MesNum));
                i.putExtra("año",Integer.toString(añoNum));
                i.putExtra("atleta",nombreAtleta);
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
                i.putExtra("mes",Integer.toString(MesNum));
                i.putExtra("año",Integer.toString(añoNum));
                i.putExtra("atleta",nombreAtleta);
                finish();
                startActivity(i);

            }
        });
        //Botones navegación

        adaptadorRecycler eladaptador = new adaptadorRecycler(diaMes,imagenes,activityLauncher,mes,año,nombreAtleta);
        laLista.setAdapter(eladaptador);

        RecyclerView.LayoutManager elLayoutRejillaIgual= new GridLayoutManager(this,7,GridLayoutManager.VERTICAL,false);
        laLista.setLayoutManager(elLayoutRejillaIgual);

        //crearNotificacion();
    }

    private final ActivityResultLauncher<Intent> activityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // Aquí puedes manejar el resultado de la actividad si es necesario
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Puedes realizar acciones adicionales si es necesario
                    }
                }
            });
    private void solicitarPermisosNotificaciones(){
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)!= PackageManager.PERMISSION_GRANTED) {
            //PEDIR EL PERMISO
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 11);
        }
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
            startActivity(i);
            finish();
        }else{
            DialogFragment dialogoAlerta = new salirAplicacion();
            dialogoAlerta.show(getSupportFragmentManager(), "etiqueta");
        }
    }

    //Definir el fichero xml al toolbar
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }


    public String getMes(){
        return this.mes;
    }

    public String getAño(){
        return this.año;
    }
}