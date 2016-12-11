package com.islasf.android.grupo5;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author Carlos García y Javier Sánchez
 */

public class AccesoBBDD {

    private Context contexto;

    public AccesoBBDD(Context contexto) {
        this.contexto = contexto;
    }

    public void insertarPartida(Juego juego) {
        // 1. Abrir bbdd en mood escritura.
        PartidasSQLiteHelper partidas = new PartidasSQLiteHelper(contexto, "BDPartidas", null, 1);
        SQLiteDatabase db = partidas.getWritableDatabase();

        if (db != null) { //Si la base de datos se abre correctamente:
            ContentValues contenido = new ContentValues();
            // TODO terminar por aquí
            //contenido.put("nombre", "nombre"); //OJETE! Hay que sacar el nombre y eso.
            //contenido.put("tiempo" juego.getTiempo); // Ojete, más de lo mismo.
            contenido.put("disposicion", juego.getConfiguracion().getX()+"x"+juego.getConfiguracion().getY());
            contenido.put("numMax", juego.getConfiguracion().getValorMax());
            contenido.put("pulsaciones", juego.getNumPulsaciones());
            db.insert("Partida", "id", contenido);

            db.close();
        }
    }

    public void eliminarPartida(int id) {

        PartidasSQLiteHelper partidas = new PartidasSQLiteHelper(contexto, "BDPartidas", null, 1);
        SQLiteDatabase db = partidas.getWritableDatabase();

        if (db != null){
            db.delete("Partida", "id="+id, null);

            db.close();
        }
    }


}
