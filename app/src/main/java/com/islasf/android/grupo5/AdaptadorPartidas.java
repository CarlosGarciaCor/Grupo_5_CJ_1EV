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
 * Clase AdaptadorPartida. <br>
 * Esta clase extiende ArrayAdapter y se utiliza para introducir los datos en una ListView.
 *
 * @author Carlos García y Javier Sánchez
 */
public class AdaptadorPartidas extends ArrayAdapter<Juego> {

    private ArrayList<Juego> datos;

    /**
     * Constructor a sobreescribir de la clase ArrayAdapter. Recibe el contexto de la aplicación y los datos que se van a introducir en el listView.
     *
     * @param context es el contexto de la aplicación.
     * @param datos   es la colección de datos. En este caso es un ArrayList de Juego.
     */
    public AdaptadorPartidas(Context context, ArrayList<Juego> datos) {
        super(context, R.layout.layout_lista_bbdd, datos);
        this.datos = datos;
    }

    /**
     * Método getView. Este está sobrescrito de la clase ArrayAdapter. La idea es que te devuelve una vista, que es un elemento del ListView. <br>
     * Recibe posicón, una posible view ya existente y un padre.
     *
     * @param position la posición de la lista.
     * @param convertView si una vista ha salido de la lista al hacer scroll esta se convierte en un convertView al aparecer de nuevo por pantalla. Usarla en vez de crear una nueva View optimiza la lista.
     * @param parent un posible padre de la ListView. En este caso no se trata.
     * @return la vista para la lista, es decir, un elemento de la lista.
     */
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View item = convertView;
        PartidaHolder holder;

        if (item == null) {
            LayoutInflater inflador = LayoutInflater.from(getContext());
            item = inflador.inflate(R.layout.layout_lista_bbdd, null);

            holder = new PartidaHolder();
            holder.tvTiempo = (TextView) item.findViewById(R.id.textTiempo);
            holder.tvNombre = (TextView) item.findViewById(R.id.textNombre);
            holder.tvDisposicion = (TextView) item.findViewById(R.id.textDispo);
            holder.tvIndice = (TextView) item.findViewById(R.id.textIndice);
            holder.tvPulsaciones = (TextView) item.findViewById(R.id.textPulsa);

            item.setTag(holder);
        } else {
            holder = (PartidaHolder) item.getTag();
        }

        holder.tvTiempo.setText(String.valueOf(datos.get(position).getTiempo()));
        holder.tvNombre.setText(datos.get(position).getUsuario());
        holder.tvDisposicion.setText(datos.get(position).getConfiguracion().getX() + "x" + datos.get(position).getConfiguracion().getY());
        holder.tvIndice.setText(String.valueOf(datos.get(position).getConfiguracion().getValorMax()));
        holder.tvPulsaciones.setText(String.valueOf(datos.get(position).getNumPulsaciones()));

        return item;
    }
}
