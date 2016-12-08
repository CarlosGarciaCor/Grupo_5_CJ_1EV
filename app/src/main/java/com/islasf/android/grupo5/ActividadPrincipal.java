package com.islasf.android.grupo5;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ActividadPrincipal extends AppCompatActivity {

    private Juego juego;
    private Configuracion configuracion;
    private Preferencias preferencias;
    private GridLayout layout;
    private ArrayList<Button> botones;
    private TextView tbTiempo;
    private TextView tbPulsaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_principal);

        layout = (GridLayout) findViewById(R.id.gridLayout);

        tbTiempo=(TextView)findViewById(R.id.tbTiempo);
        tbTiempo.setText("00:00");
        tbPulsaciones=(TextView)findViewById(R.id.tbPulsaciones);
        tbPulsaciones.setText("0");
        botones=new ArrayList<>();
        nuevaPartida();
    }

    private void nuevaPartida(){

        if (botones.size()>0){
            layout.removeAllViews();
            botones.clear();
        }

        configuracion=new Configuracion(2, 3, 3, 0);
        preferencias=new Preferencias();

        layout.setRowCount(configuracion.getY());
        layout.setColumnCount(configuracion.getX());

        juego=new Juego(configuracion, preferencias);

        int i=0;
        for (int y=0; y<configuracion.getY(); y++){
            for (int x=0; x<configuracion.getX(); x++){
                crearBoton(juego.getCasillas().get(i), i);
                i++;
            }
        }
    }

    private void crearBoton(Casilla casilla, int id){

        Button btn=new Button(this);
        btn.setId(id); //Si no le pasas una ID te tira excepciones a la cara

        int btnHeigth=layout.getHeight()/configuracion.getY();
        int btnWidth=layout.getWidth()/configuracion.getX();
        btn.setHeight(btnHeigth);
        btn.setWidth(btnWidth);

        btn.setText(Integer.toString(casilla.getValor()));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pulsarCasilla(v);
            }
        });

        layout.addView(btn);
        botones.add(btn);
    }

    private void pulsarCasilla(View v){
        Button btn=(Button)v;
        Casilla casilla=juego.getCasillas().get(btn.getId());

        juego.pulsarCasilla(casilla);
        juego.incrementarNumPulsaciones();
        this.actualizar();
        if (juego.condicionVictoria()){
            Toast t=Toast.makeText(this, "BRAVO", Toast.LENGTH_SHORT);
            t.show();
            nuevaPartida();
        }
    }

    private void actualizar(){
        for (int i=0;i<botones.size();i++){
            botones.get(i).setText(Integer.toString(juego.getCasillas().get(i).getValor()));
        }
        tbPulsaciones.setText(Integer.toString(juego.getNumPulsaciones()));
    }

}
