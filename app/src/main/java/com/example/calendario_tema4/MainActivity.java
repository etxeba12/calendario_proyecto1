package com.example.calendario_tema4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView laLista = findViewById(R.id.rv);
        int[] imagenes= {R.drawable.pesa, R.drawable.pesa, R.drawable.descanso, R.drawable.pesa, R.drawable.descanso,R.drawable.pesa, R.drawable.pesa, R.drawable.descanso, R.drawable.pesa, R.drawable.descanso,R.drawable.descanso};
        String[] diaMes={"1","2","3","4","5","6","7","8","9","10","11"};

        for (int i = 0; i < diaMes.length; i++) {
            Log.d("MainActivity", "Dia: " + diaMes[i] + ", Imagen: " + imagenes[i]);
        }
        adaptadorRecycler eladaptador = new adaptadorRecycler(diaMes,imagenes);
        laLista.setAdapter(eladaptador);

        RecyclerView.LayoutManager elLayoutRejillaIgual= new GridLayoutManager(this,7);
        laLista.setLayoutManager(elLayoutRejillaIgual);

    }
}