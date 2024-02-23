package com.example.calendario_tema4.calendarioReycler;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calendario_tema4.R;

public class adaptadorRecycler extends RecyclerView.Adapter<ElViewHolder> {
    private String[] losEntrenamientos;
    private int[] lasImagenes;
    private boolean[] seleccionados;
    private final ActivityResultLauncher<Intent> activityLauncher;

    private String mes;
    private String año;


    public adaptadorRecycler(String[] entrenos, int[] imagenes, ActivityResultLauncher<Intent> activityLauncher,String pMes,String pAño){
        losEntrenamientos=entrenos;
        lasImagenes=imagenes;
        seleccionados = new boolean[entrenos.length];
        this.activityLauncher = activityLauncher;
        this.mes = pMes;
        this.año = pAño;
    }
    public ElViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View elLayoutDeCadaItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.dia_entrenamiento,parent,false);
        ElViewHolder evh = new ElViewHolder(elLayoutDeCadaItem,activityLauncher,mes,año);
        evh.seleccion = seleccionados;
        return evh;
    }

    public void onBindViewHolder(@NonNull ElViewHolder holder, int position) {
        holder.textoDia.setText(losEntrenamientos[position]);
        holder.imagenPesa.setImageResource(lasImagenes[position]);
    }
    @Override
    public int getItemCount() {
        return losEntrenamientos.length;
    }

    public String getDiaSeleccionado(int position) {
        if (position >= 0 && position < losEntrenamientos.length) {
            return losEntrenamientos[position];
        }
        return null;
    }


}
