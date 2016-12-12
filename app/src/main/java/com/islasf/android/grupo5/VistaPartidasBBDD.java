package com.islasf.android.grupo5;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Clase VistaPartidasBBDD. <br>
 *     Esta clase extiende de AppCompatActivity ya que trata la actividad correspondiente a la visualización de las partidas de la base de datos.
 *     En la clase, dentro del método sobrescrito onCreate nos encargamos de llamar a los métodos necesarios de clases como AccesoBBDD y AdaptadorPartidas para
 *     poder visualizar en un ListView, correctamente y de forma óptima el listado de partidas que existe dentro de la BBDD.
 *
 * @see AdaptadorPartidas
 * @see AccesoBBDD
 * @author Carlos García y Javier Sánchez
 *
 */
public class VistaPartidasBBDD extends AppCompatActivity{

    /**
     * Método onCreate. <br>
     *     En el método onCreate se ha programado lo necesario para cargar esa ListView de partidas.
     *     Se recogen las partidas de la base de datos en una ArrayList de la clase Juego y luego el adaptador se encarga de transformar los datos
     *     de esa colección a una serie de views.
     *     Finalmente este adaptador se le pasa a esa ListView.
     *
     * @param savedInstanceState un bundle con la instancia guardada (reinicio de actividad).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_partidas_bbdd);


        ListView listadoPartidas = (ListView)findViewById(R.id.listaPartidas);

        AccesoBBDD accesoBBDD = new AccesoBBDD(this.getApplicationContext());
        ArrayList<Juego> partidasBBDD = accesoBBDD.recogerPartidas();

        AdaptadorPartidas adaptador = new AdaptadorPartidas(this.getApplicationContext(), partidasBBDD);

        listadoPartidas.setAdapter(adaptador);
    }

    /**
     * Método que controla el hacer click sobre el botón volver.
     * @param v una View que hace referencia al botón volver.
     */
    public void botonVolver(View v){
        this.finish();
    }
}
