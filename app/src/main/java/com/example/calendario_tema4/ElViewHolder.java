package com.example.calendario_tema4;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ElViewHolder extends RecyclerView.ViewHolder {

    public TextView textoDia;
    public ImageView imagenPesa;
    public ElViewHolder (@NonNull View itemView){
        super(itemView);
        textoDia=itemView.findViewById(R.id.textoDia);
        imagenPesa=itemView.findViewById(R.id.imagenPesa);

        Log.d("ElViewHolder", "ViewHolder creado para posición: " + getAdapterPosition());
    }
}
