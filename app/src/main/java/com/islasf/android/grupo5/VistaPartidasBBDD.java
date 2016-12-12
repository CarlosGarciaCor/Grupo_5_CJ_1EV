package com.islasf.android.grupo5;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class VistaPartidasBBDD extends AppCompatActivity{

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
}
