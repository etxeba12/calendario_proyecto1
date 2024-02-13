package com.example.calendario_tema4;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class adaptadorRecycler extends RecyclerView.Adapter<ElViewHolder> {
    private String[] losEntrenamientos;
    private int[] lasImagenes;

    public adaptadorRecycler(String[] entrenos, int[] imagenes){
        losEntrenamientos=entrenos;
        lasImagenes=imagenes;
    }
    public ElViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View elLayoutDeCadaItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.dia_entrenamiento,parent,false);
        ElViewHolder evh = new ElViewHolder(elLayoutDeCadaItem);
        return evh;
    }

    public void onBindViewHolder(@NonNull ElViewHolder holder, int position) {
        holder.textoDia.setText(losEntrenamientos[position]);
        holder.imagenPesa.setImageResource(lasImagenes[position]);
        Log.d("adaptadorRecycler", "Posición: " + position + ", Dia: " + losEntrenamientos[position] + ", Imagen: " + lasImagenes[position]);
    }
    @Override
    public int getItemCount() {
        return losEntrenamientos.length;
    }
}
