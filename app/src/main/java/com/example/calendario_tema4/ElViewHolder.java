package com.example.calendario_tema4;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ElViewHolder extends RecyclerView.ViewHolder {

    public TextView textoDia;
    public ImageView imagenPesa;

    public boolean[] seleccion;
    public ElViewHolder (@NonNull View itemView){
        super(itemView);
        textoDia=itemView.findViewById(R.id.textoDia);
        imagenPesa=itemView.findViewById(R.id.imagenPesa);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (seleccion[getAdapterPosition()]==true){
                    seleccion[getAdapterPosition()]=false;
                    imagenPesa.setColorFilter(null);
                }
                else{
                    seleccion[getAdapterPosition()]=true;
                    imagenPesa.setColorFilter(Color.RED);
                }
            }
        });

    }
}
