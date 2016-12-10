package com.islasf.android.grupo5;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Vibrator;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ActividadPrincipal extends AppCompatActivity {

    private Juego juego;
    private Configuracion configuracion;
    private Preferencias preferencias;
    private DrawerLayout menuLateral;
    private GridLayout layout;
    private ArrayList<Button> botones;
    private TextView tbTiempo;
    private TextView tbPulsaciones;
    private int btnHeigth;
    private int btnWidth;

    private Vibrator vibrator;
    private SoundPool poolSonidos;
    private int sonidoPulsacion;

    private int alturaGrid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_principal);

        vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE); //Instanciamos vibrador y soundpool.
        poolSonidos = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);

        this.setVolumeControlStream(AudioManager.STREAM_MUSIC); // Para que si el usuairo sube o baja el volumen lo haga del de la música.
        // Le damos un valor a la variable sonidoPulsacion:
        sonidoPulsacion = poolSonidos.load(this, R.raw.touch, 1);

        // Recogemos el menú lateral:
        menuLateral = (DrawerLayout)findViewById(R.id.activity_actividad_principal);
        // Toolbar:
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); // Le ponemos a esta actividad la toolbar como actionBar

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_action_list); // Le indicamos cual es el icono.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);// Habilitamos la función del botón.

        layout = (GridLayout) findViewById(R.id.gridLayout);

        tbTiempo=(TextView)findViewById(R.id.tbTiempo);
        tbTiempo.setText("00:00");
        tbPulsaciones=(TextView)findViewById(R.id.tbPulsaciones);
        tbPulsaciones.setText("0");
        botones=new ArrayList<>();
        nuevaPartida();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                menuLateral.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        RelativeLayout rLayout=(RelativeLayout) findViewById(R.id.relativeLayout);
        int rLayoutHeight= rLayout.getHeight();
        Log.i("relativeLayout H", rLayoutHeight+"");

        GridLayout gLayout = (GridLayout) findViewById(R.id.gridLayout);
        alturaGrid = gLayout.getHeight();
        Log.i("gLayout", alturaGrid+"");
    }

    private void nuevaPartida(){

        if (botones.size()>0){
            layout.removeAllViews();
            botones.clear();
        }

        configuracion=new Configuracion(2, 3, 3, 0);
        preferencias=new Preferencias(true, true, "NUMEROS");

        layout.setRowCount(configuracion.getY());
        layout.setColumnCount(configuracion.getX());

        DisplayMetrics display=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(display);
        Log.i("pepe", display.heightPixels+"");

        /*
        RelativeLayout rLayout=(RelativeLayout) findViewById(R.id.relativeLayout);
        int rLayoutHeight=rLayout.getHeight();
        Log.i("relativeLayout H", rLayoutHeight+"");
*/

        btnHeigth=(alturaGrid)/configuracion.getY();
        btnWidth=display.widthPixels/configuracion.getX();

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

        btn.setHeight(btnHeigth);
        btn.setWidth(btnWidth);

        btn.setText(Integer.toString(casilla.getValor()));
        btn.setTextSize(18);

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
        if (this.preferencias.isVibracion()){
            vibrator.vibrate(200);
        }
        if (this.preferencias.isSonido()){
            poolSonidos.play(sonidoPulsacion,1,1,1,0,1);
        }
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
