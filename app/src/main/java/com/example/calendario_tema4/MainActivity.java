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
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        solicitarPermisosNotificaciones(); //Pedimos permisos, si todavia no tiene

        RecyclerView laLista = findViewById(R.id.rv);
        int[] imagenes= {R.drawable.pesa, R.drawable.pesa, R.drawable.descanso, R.drawable.pesa, R.drawable.descanso,R.drawable.pesa, R.drawable.pesa, R.drawable.descanso, R.drawable.pesa, R.drawable.descanso,R.drawable.descanso,
                R.drawable.pesa, R.drawable.pesa, R.drawable.descanso, R.drawable.pesa, R.drawable.descanso,R.drawable.pesa, R.drawable.pesa, R.drawable.descanso, R.drawable.pesa, R.drawable.descanso,R.drawable.descanso,
                R.drawable.pesa, R.drawable.pesa, R.drawable.descanso, R.drawable.pesa, R.drawable.descanso,R.drawable.pesa, R.drawable.pesa, R.drawable.descanso, R.drawable.pesa};
        String[] diaMes = new String[31];
        for (int i = 0; i < 31; i++) {
            diaMes[i] = String.valueOf(i + 1);
        }

        for (int i = 0; i < diaMes.length; i++) {
            Log.d("MainActivity", "Dia: " + diaMes[i] + ", Imagen: " + imagenes[i]);
        }
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
    public void onBackPressed() {
        DialogFragment dialogoAlerta = new salirAplicacion();
        dialogoAlerta.show(getSupportFragmentManager(), "etiqueta");
    }
}