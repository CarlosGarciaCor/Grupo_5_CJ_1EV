package com.islasf.android.grupo5;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by CarlosG on 10/12/2016.
 */

public class PartidasSQLiteHelper extends SQLiteOpenHelper {

    public PartidasSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Partida(" +
                "id integer auto_increment primary key," +
                "nombre varchar(20) not null," +
                "tiempo varchar(5) not null," +
                "disposicion varchar(5) not null," +
                "numMax integer not null," +
                "pulsaciones integer not null)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Partida");

        onCreate(db);
    }
}
