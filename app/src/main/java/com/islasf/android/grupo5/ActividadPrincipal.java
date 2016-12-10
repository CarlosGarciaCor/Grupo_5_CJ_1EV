package com.islasf.android.grupo5;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ActividadPrincipal extends AppCompatActivity {

    private Juego juego;
    private Configuracion configuracion;
    private DrawerLayout menuLateral;
    private LinearLayout gameLayout;
    private ArrayList<Button> botones;
    private TextView tbPulsaciones;

    private Chronometer chrono;
    private Vibrator vibrator;
    private SoundPool poolSonidos;
    private int sonidoPulsacion;

    private final int[] BTN_DRAW={R.drawable.draw_btn1,
            R.drawable.draw_btn2,
            R.drawable.draw_btn3,
            R.drawable.draw_btn4,
            R.drawable.draw_btn5};

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

        //Pulsación de botones del NavView y demás.
        NavigationView navView = (NavigationView)findViewById(R.id.navview);
        navView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        switch (menuItem.getItemId()) {
                            case R.id.juego_opcion_1:
                                nuevaPartida();
                                break;
                            case R.id.juego_opcion_2:
                                reiniciarPartida();
                                break;
                            case R.id.opts_opcion_1:
                                Intent preferencias = new Intent(getApplicationContext(), ConfiguracionActivity.class);
                                startActivity(preferencias);
                                break;
                            case R.id.opt_salir:
                                finish();
                        }

                        menuLateral.closeDrawers(); // Si se pulsa una opción se cierra el menú lateral.

                        return true;
                    }
                });

        gameLayout = (LinearLayout) findViewById(R.id.gameLayout);

        chrono= (Chronometer) findViewById(R.id.chrono);

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

    private void nuevaPartida(){

        chrono.setBase(SystemClock.elapsedRealtime()-0); //Restart chrono
        tbPulsaciones.setText(Integer.toString(0));

        configuracion=new Configuracion(2, 3, 3, 0, true, true, "NUMEROS");

        DisplayMetrics display=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(display);

        gameLayout.setWeightSum(configuracion.getY());

        juego=new Juego(configuracion);

        for (Casilla item: juego.getCasillas()){
            Log.i("tag", ""+item.getValor());
        }
        for (Casilla item: juego.getCasillasReinicio()){
            Log.i("reinicio", ""+item.getValor());
        }

        if (botones.size()!=juego.getCasillas().size()) {
            gameLayout.removeAllViews();
            botones.clear();

            int i = 0;
            for (int y = 0; y < configuracion.getY(); y++) {
                LinearLayout linea = new LinearLayout(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);

                linea.setLayoutParams(params);
                linea.setOrientation(LinearLayout.HORIZONTAL);
                linea.setWeightSum(configuracion.getX());

                for (int x = 0; x < configuracion.getX(); x++) {
                    crearBoton(juego.getCasillas().get(i), i, linea);
                    i++;
                }
                gameLayout.addView(linea);
            }
        }
        else
            actualizar();

        chrono.start();
    }

    private void crearBoton(Casilla casilla, int id, LinearLayout linea){

        Button btn=new Button(this);
        btn.setId(id);

        btn.setBackground(getResources().getDrawable(BTN_DRAW[casilla.getValor() - 1]));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);

        btn.setLayoutParams(params);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pulsarCasilla(v);
            }
        });

        linea.addView(btn);
        botones.add(btn);
    }

    private void pulsarCasilla(View v){

        if (this.configuracion.isSonido()){
            poolSonidos.play(sonidoPulsacion,1,1,1,0,1);
        }

        Button btn=(Button)v;
        Casilla casilla=juego.getCasillas().get(btn.getId());

        juego.pulsarCasilla(casilla);
        juego.incrementarNumPulsaciones();
        this.actualizar();

        for (Casilla item: juego.getCasillas()){
            Log.i("tag", ""+item.getValor());
        }
        for (Casilla item: juego.getCasillasReinicio()){
            Log.i("reinicio", ""+item.getValor());
        }

        if (juego.condicionVictoria()){
            victoria();
        }
    }

    private void victoria() {
        if (this.configuracion.isVibracion()){
            vibrator.vibrate(200);
        }
        if (this.configuracion.isSonido()){
            poolSonidos.play(sonidoPulsacion,1,1,1,0,1);
        }

        //TODO guardar resultado

        DialogoWin win=new DialogoWin();
        win.show(getFragmentManager(), "dialogo");
    }

    public void onRespuesta(int result){
        if (result==0)
            nuevaPartida();
        else
            reiniciarPartida();
    }

    private void reiniciarPartida(){
        juego.reiniciarJuego();

        actualizar();
        chrono.setBase(SystemClock.elapsedRealtime()-0);
    }

    private void actualizar(){
        for (int i=0;i<botones.size();i++){
            botones.get(i).setBackground(getResources().getDrawable(BTN_DRAW[juego.getCasillas().get(i).getValor()-1]));
        }
        tbPulsaciones.setText(Integer.toString(juego.getNumPulsaciones()));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable("juego", juego);
        outState.putLong("time", chrono.getBase());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        juego=(Juego)savedInstanceState.getSerializable("juego");
        tbPulsaciones.setText(Integer.toString(juego.getNumPulsaciones()));
        chrono.setBase(savedInstanceState.getLong("time"));
        actualizar();
    }
}
