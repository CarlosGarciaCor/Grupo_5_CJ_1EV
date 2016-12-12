package com.islasf.android.grupo5;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by CarlosG on 12/12/2016.
 */

public class AdaptadorPartidas extends ArrayAdapter<Juego>{

    private ArrayList<Juego> datos;

    public AdaptadorPartidas(Context context, ArrayList<Juego> datos){
        super(context, R.layout.layout_lista_bbdd, datos);
        this.datos = datos;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View item = convertView;
        PartidaHolder holder;

        if(item == null){
            LayoutInflater inflador = LayoutInflater.from(getContext());
            item = inflador.inflate(R.layout.layout_lista_bbdd, null);

            holder = new PartidaHolder();
            holder.tvTiempo = (TextView)item.findViewById(R.id.textTiempo);
            holder.tvNombre = (TextView)item.findViewById(R.id.textNombre);
            holder.tvDisposicion = (TextView)item.findViewById(R.id.textDispo);
            holder.tvIndice = (TextView)item.findViewById(R.id.textIndice);
            holder.tvPulsaciones = (TextView)item.findViewById(R.id.textPulsa);

            item.setTag(holder);
        } else {
            holder = (PartidaHolder) item.getTag();
        }

        holder.tvTiempo.setText(String.valueOf(datos.get(position).getTiempo()));
        holder.tvNombre.setText(datos.get(position).getUsuario());
        holder.tvDisposicion.setText(datos.get(position).getConfiguracion().getX()+"x"+datos.get(position).getConfiguracion().getY());
        holder.tvIndice.setText(String.valueOf(datos.get(position).getConfiguracion().getValorMax()));
        holder.tvPulsaciones.setText(String.valueOf(datos.get(position).getNumPulsaciones()));

        return item;
    }
}
