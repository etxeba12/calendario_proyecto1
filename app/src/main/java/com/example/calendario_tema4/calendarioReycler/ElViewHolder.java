package com.example.calendario_tema4.calendarioReycler;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calendario_tema4.R;
import com.example.calendario_tema4.entrenamiento;

import org.w3c.dom.Text;

public class ElViewHolder extends RecyclerView.ViewHolder {

    public TextView textoDia;
    public ImageView imagenPesa;

    public boolean[] seleccion;

    private String mes;
    private String año;
    private String atleta;

    private String idioma;

    private final ActivityResultLauncher<Intent> activityLauncher;

    public ElViewHolder(@NonNull View itemView, ActivityResultLauncher<Intent> activityLauncher,String pMes,String pAño,String pAtleta,String pIdioma) {
        super(itemView);
        textoDia = itemView.findViewById(R.id.textoDia);
        imagenPesa = itemView.findViewById(R.id.imagenPesa);
        this.activityLauncher = activityLauncher;
        this.mes = pMes;
        this.año = pAño;
        this.atleta = pAtleta;
        this.idioma = pIdioma;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();


                Intent intent = new Intent(itemView.getContext(), entrenamiento.class);
                intent.putExtra("mes", mes);
                intent.putExtra("año", año);
                intent.putExtra("diaSeleccionado", String.format("%02d", position + 1));
                intent.putExtra("atleta", atleta);
                intent.putExtra("idioma",idioma);

                // Lanza la actividad con el ActivityResultLauncher
                activityLauncher.launch(intent);
            }
        });
    }
}
