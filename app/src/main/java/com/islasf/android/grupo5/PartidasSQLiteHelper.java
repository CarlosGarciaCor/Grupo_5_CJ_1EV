package com.islasf.android.grupo5;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Clase PartidasSQLiteHelper. <br>
 *     A la hora de realizar este proyecto se tomó la decisión de trabajar con base de datos utilizando SQLite. Para ello tenemos esta clase
 *     que extiende de SQLiteOpenHelper. Esto nos permite la creación, apertura y actualización de nuestra base de datos sobrescribiendo ciertos métodos.
 *
 *     @author Carlos García y Javier Sánchez
 */
public class PartidasSQLiteHelper extends SQLiteOpenHelper {

    /**
     * Constructor por defecto de la clase SQLiteOpenHelper.
     * @param context el contexto de la aplicación.
     * @param name el nombre de la BBDD.
     * @param factory un factory para la creación de cursores.
     * @param version la versión de la base de datos.
     */
    public PartidasSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * Método sobrescrito de la clase SQLiteOpenHelper. <br>
     *     Se encarga de ejecutar la sentencia de creación de la base de datos.
     *
     * @param db la base de datos sobre la que trabajar.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Partida(" +
                "id integer auto_increment primary key," +
                "nombre varchar(20) not null," +
                "tiempo long not null," +
                "disposicion varchar(5) not null," +
                "numMax integer not null," +
                "pulsaciones integer not null)");
    }

    /**
     * Método sobrescrito de la clase SQLiteOpenHelper. <br>
     *     Se encarga de ejecutar las instrucciones especificadas a la hora de actualizar la base de datos. <br>
     *         Por lo general la idea es aquí tratar una posible migración de los datos para que no haya pérdida de los mismos a la hora de actualizar la versión de la base de datos. En este caso
     *         solo se borra la tabla y se vuelve a crear ya que el proyecto no requiere la complejidad de controlar versiones de base de datos.
     *
     * @param db la base de datos sobre la que trabajar.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Partida");
        onCreate(db);
    }
}
