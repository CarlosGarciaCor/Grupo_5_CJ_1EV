package com.islasf.android.grupo5;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Clase AccesoBBDD. <br>
 * Esta clase contiene métodos que nos permiten hacer las instrucciones necesarias en la base de datos. <br>
 * En este caso van a ser dos, el insertar una partida y el recoger todas las partidas existentes.
 *
 * @author Carlos García y Javier Sánchez
 */

public class AccesoBBDD {

    private Context contexto;

    /**
     * Constructor por defecto. Recibe el contexto de la aplicación.
     *
     * @param contexto Contexto de la aplicación. Necesario para recoger la base de datos.
     */
    public AccesoBBDD(Context contexto) {
        this.contexto = contexto;
    }

    /**
     * Método encargado de insertar partidas en la BBDD.
     * @param juego Recibe por parámetro un juego con los datos a insertar en la BBDD.
     */
    public void insertarPartida(Juego juego) {
        // 1. Abrir bbdd en modo escritura.
        PartidasSQLiteHelper partidas = new PartidasSQLiteHelper(contexto, "BDPartidas", null, 1);
        SQLiteDatabase db = partidas.getWritableDatabase();

        if (db != null) { //Si la base de datos se abre correctamente:
            ContentValues contenido = new ContentValues();
            contenido.put("nombre", juego.getUsuario());
            contenido.put("tiempo", juego.getTiempo());
            contenido.put("disposicion", juego.getConfiguracion().getX() + "x" + juego.getConfiguracion().getY());
            contenido.put("numMax", juego.getConfiguracion().getValorMax());
            contenido.put("pulsaciones", juego.getNumPulsaciones());
            db.insert("Partida", "id", contenido);

            db.close();
        }
    }

    /**
     * Método encargado de realizar la query y recoger los datos de la misma a la hora de querer recoger todas las partidas que existen en la BBDD.
     * @return un ArrayList de la clase Juego.
     */
    public ArrayList<Juego> recogerPartidas() {
        ArrayList<Juego> partidas = new ArrayList<>();

        PartidasSQLiteHelper partidasbbdd = new PartidasSQLiteHelper(contexto, "BDPartidas", null, 1);
        SQLiteDatabase db = partidasbbdd.getReadableDatabase();

        if (db != null) {
            Cursor c = db.rawQuery("select * from Partida", null);

            if (c.moveToFirst()) {
                do {
                    String disposicion = c.getString(3);
                    StringTokenizer tokenizer = new StringTokenizer(disposicion, "x");
                    int x = Integer.valueOf(tokenizer.nextToken());
                    int y = Integer.valueOf(tokenizer.nextToken());
                    Juego juego = new Juego(new Configuracion(c.getInt(4), c.getInt(x), c.getInt(y)));
                    juego.setTiempo(c.getLong(2));
                    juego.setUsuario(c.getString(1));
                    juego.setNumPulsaciones(c.getInt(5));
                    partidas.add(juego);
                } while (c.moveToNext());
            }
        }

        return partidas;
    }


}
