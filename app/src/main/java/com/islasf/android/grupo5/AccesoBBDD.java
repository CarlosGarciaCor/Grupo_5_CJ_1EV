package com.islasf.android.grupo5;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.StringTokenizer;

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
            contenido.put("nombre", juego.getUsuario());
            contenido.put("tiempo", juego.getTiempo());
            String pene = juego.getConfiguracion().getX()+"x"+juego.getConfiguracion().getY();
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

    public void limpiarTabla(){
        PartidasSQLiteHelper partidas = new PartidasSQLiteHelper(contexto, "BDPartidas", null, 1);
        SQLiteDatabase db = partidas.getWritableDatabase();

        if (db != null){
            db.delete("Partida", null, null);

            db.close();
        }
    }

    public ArrayList<Juego> recogerPartidas(){
        ArrayList<Juego> partidas = new ArrayList<>();

        PartidasSQLiteHelper partidasbbdd = new PartidasSQLiteHelper(contexto, "BDPartidas", null, 1);
        SQLiteDatabase db = partidasbbdd.getReadableDatabase();

        if (db != null){
            Cursor c = db.rawQuery("select * from Partida", null);

            if (c.moveToFirst()){
                do{
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
