package com.islasf.android.grupo5;

import android.widget.TextView;

/**
 * Clase PartidaHolder. <br>
 *     La clase contiene todas las variables que contiene una View de la ListView de partidas. <br>
 *         El propósito de esta es aligerar la carga de datos para no tener que recoger los valores siempre desde la clase R.id.
 *
 *     @see AdaptadorPartidas
 *     @author Carlos García y Javier Sánchez
 */

public class PartidaHolder {
    TextView tvTiempo;
    TextView tvNombre;
    TextView tvDisposicion;
    TextView tvIndice;
    TextView tvPulsaciones;
}
