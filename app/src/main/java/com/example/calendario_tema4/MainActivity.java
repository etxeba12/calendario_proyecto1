package com.example.calendario_tema4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        solicitarPermisosNotificaciones(); //Pedimos permisos, si todavia no tiene

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String pMes= extras.getString("mes");
            String pAño= extras.getString("año");
            mes =pMes;
            año=pAño;
            fecha = "00/" + mes + "/"+ año;
        }
        RecyclerView laLista = findViewById(R.id.rv);
        int[] imagenes= {R.drawable.pesa, R.drawable.pesa, R.drawable.descanso, R.drawable.pesa, R.drawable.descanso,R.drawable.pesa, R.drawable.pesa, R.drawable.descanso, R.drawable.pesa, R.drawable.descanso,R.drawable.descanso,
                R.drawable.pesa, R.drawable.pesa, R.drawable.descanso, R.drawable.pesa, R.drawable.descanso,R.drawable.pesa, R.drawable.pesa, R.drawable.descanso, R.drawable.pesa, R.drawable.descanso,R.drawable.descanso,
                R.drawable.pesa, R.drawable.pesa, R.drawable.descanso, R.drawable.pesa, R.drawable.descanso,R.drawable.pesa, R.drawable.pesa, R.drawable.descanso, R.drawable.pesa};

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
        //Conseguir y poner fecha

        //Botones navegación
        Button botonAnt = findViewById(R.id.bt_ant);
        botonAnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int MesNum = Integer.parseInt(mes) - 1;
                Intent i = getIntent();
                i.putExtra("mes",Integer.toString(MesNum));
                i.putExtra("año",año);
                finish();
                startActivity(i);
            }
        });
        Button botonSig = findViewById(R.id.bt_sig);
        botonSig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int MesNum = Integer.parseInt(mes) + 1;
                Intent i = getIntent();
                i.putExtra("mes",Integer.toString(MesNum));
                i.putExtra("año",año);
                finish();
                startActivity(i);

            }
        });
        //Botones navegación

        adaptadorRecycler eladaptador = new adaptadorRecycler(diaMes,imagenes);
        laLista.setAdapter(eladaptador);

        RecyclerView.LayoutManager elLayoutRejillaIgual= new GridLayoutManager(this,7,GridLayoutManager.VERTICAL,false);
        laLista.setLayoutManager(elLayoutRejillaIgual);

        crearNotificacion();
    }
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
        DialogFragment dialogoAlerta = new salirAplicacion();
        dialogoAlerta.show(getSupportFragmentManager(), "etiqueta");
    }
}