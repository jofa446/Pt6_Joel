package com.example.pt6_joel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

public class MeuAdaptador extends RecyclerView.Adapter<MeuAdaptador.ExempleViewHolder> {

    private String[] dades; // Les dades del JSON

    public MeuAdaptador(String[] dades) {
        this.dades = dades;
    }

    @Override
    public ExempleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ExempleViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(ExempleViewHolder holder, int position) {
        String dadaActual = dades[position];
        holder.textView.setText(dadaActual);
    }

    @Override
    public int getItemCount() {
        return dades.length;
    }

    public static class ExempleViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public ExempleViewHolder(View vista) {
            super(vista);
            textView = vista.findViewById(R.id.textView); // Identificador del TextView a utilitzar
        }
    }
}
