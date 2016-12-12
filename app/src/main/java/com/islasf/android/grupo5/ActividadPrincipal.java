package com.islasf.android.grupo5;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
    private long timePaused=0;

    private Vibrator vibrator;
    private SoundPool poolSonidos;
    private int sonidoPulsacion;

    private final int[] BTN_COLORDRAW ={R.drawable.draw_btncolor1,
            R.drawable.draw_btncolor2,
            R.drawable.draw_btncolor3,
            R.drawable.draw_btncolor4,
            R.drawable.draw_btncolor5};

    private final int[] BTN_NUMDRAW = {R.drawable.btnnum1,
            R.drawable.btnnum2,
            R.drawable.btnnum3,
            R.drawable.btnnum4,
            R.drawable.btnnum5};

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
        getSupportActionBar().setDisplayShowCustomEnabled(true);

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
                                timePaused=chrono.getBase();
                                preferencias.putExtra("time", timePaused);
                                startActivityForResult(preferencias, 1234);
                                break;
                            case R.id.opt_ver_partidas:
                                Intent verPartidas = new Intent(getApplicationContext(), VistaPartidasBBDD.class);
                                timePaused = chrono.getBase();
                                startActivity(verPartidas);
                                break;
                            case R.id.opt_salir:
                                finish();
                        }

                        menuLateral.closeDrawers(); // Si se pulsa una opción se cierra el menú lateral.

                        return true;
                    }
                });

        gameLayout = (LinearLayout) findViewById(R.id.gameLayout);

        chrono = (Chronometer) findViewById(R.id.chrono);

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

        chrono.setBase(SystemClock.elapsedRealtime()); //Restart chrono
        tbPulsaciones.setText(Integer.toString(0));

        cargarConfiguracion();

        DisplayMetrics display=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(display);

        gameLayout.setWeightSum(configuracion.getY());

        juego=new Juego(configuracion);

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

        chrono.setBase(SystemClock.elapsedRealtime());
        chrono.start();
    }

    private void crearBoton(Casilla casilla, int id, LinearLayout linea){

        Button btn=new Button(this);
        btn.setId(id);

        if (configuracion.getModo().equals("COLORES"))
            btn.setBackground(ContextCompat.getDrawable(this, BTN_COLORDRAW[casilla.getValor() - 1]));
        else{
            btn.setBackground(ContextCompat.getDrawable(this, BTN_NUMDRAW[casilla.getValor() - 1]));
        }

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

        if (this.configuracion.isVibracion()){
            vibrator.vibrate(200);
        }

        Button btn=(Button)v;
        Casilla casilla=juego.getCasillas().get(btn.getId());

        juego.pulsarCasilla(casilla);
        juego.incrementarNumPulsaciones();
        this.actualizar();

        if (juego.condicionVictoria()){
            victoria();
        }
    }

    private void victoria() {
        chrono.stop();

        if (this.configuracion.isVibracion()){
            vibrator.vibrate(1000);
        }
        if (this.configuracion.isSonido()){
            poolSonidos.play(sonidoPulsacion,1,1,1,0,1);
        }

        DialogoWin win=new DialogoWin();
        win.show(getFragmentManager(), "dialogo");
    }

    public void onRespuesta(int result, boolean notSave, String user){
        //Guardamos resultado si no está marcado el CheckBox
        if (!notSave){
            AccesoBBDD acceso=new AccesoBBDD(this);
            juego.setTiempo(chrono.getBase());
            juego.setUsuario(user);
            acceso.insertarPartida(juego);
            //acceso.limpiarTabla();
        }

        if (result==0)
            nuevaPartida();
        else
            reiniciarPartida();
    }

    private void reiniciarPartida(){
        juego.reiniciarJuego();

        actualizar();
        chrono.setBase(SystemClock.elapsedRealtime());
    }

    private void actualizar(){
        for (int i=0;i<botones.size();i++){
            if (configuracion.getModo().equals("COLORES"))
                botones.get(i).setBackground(ContextCompat.getDrawable(this, BTN_COLORDRAW[juego.getCasillas().get(i).getValor()-1]));
            else {
                botones.get(i).setBackground(ContextCompat.getDrawable(this, BTN_NUMDRAW[juego.getCasillas().get(i).getValor() - 1]));
            }
        }
        tbPulsaciones.setText(Integer.toString(juego.getNumPulsaciones()));
    }

    private void cargarConfiguracion(){

        SharedPreferences prefs = getSharedPreferences("Configuracion", Context.MODE_PRIVATE);
        configuracion = new Configuracion(
                prefs.getInt("indiceMax", 3),
                prefs.getInt("indiceX", 3),
                prefs.getInt("indiceY", 4),
                prefs.getBoolean("vibracion", true),
                prefs.getBoolean("sonido",true),
                prefs.getString("modo", "COLORES")
        );

        ImageButton botonVibracion = (ImageButton)findViewById(R.id.btnVibracion);
        ImageButton botonSonido = (ImageButton)findViewById(R.id.btnSonido);

        if (prefs.getBoolean("vibracion", true)){
            botonVibracion.setImageResource(R.drawable.ic_vibracion);
            botonVibracion.setTag(true);
        } else {
            botonVibracion.setImageResource(R.drawable.ic_vibracion_no);
            botonVibracion.setTag(false);
        }

        if (prefs.getBoolean("sonido", true)){
            botonSonido.setImageResource(R.drawable.ic_sonido);
            botonSonido.setTag(true);
        } else {
            botonSonido.setImageResource(R.drawable.ic_sonido_no);
            botonSonido.setTag(false);
        }
    }

    public void pulsarBtnVibracion(View v){
        ImageButton btnPulsado = (ImageButton) v;

        SharedPreferences prefs = getSharedPreferences("Configuracion", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        if (btnPulsado.getTag() == (Object)true){
            btnPulsado.setImageResource(R.drawable.ic_vibracion_no);
            btnPulsado.setTag(false);

            editor.putBoolean("vibracion", false);
            editor.apply();
        } else {
            btnPulsado.setImageResource(R.drawable.ic_vibracion);
            btnPulsado.setTag(true);

            editor.putBoolean("vibracion", true);
            editor.apply();
        }

        cargarConfiguracion();
    }

    public void pulsarBtnSonido(View v){
        ImageButton btnPulsado = (ImageButton) v;

        SharedPreferences prefs = getSharedPreferences("Configuracion", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        if (btnPulsado.getTag() == (Object)true){
            btnPulsado.setImageResource(R.drawable.ic_sonido_no);
            btnPulsado.setTag(false);

            editor.putBoolean("sonido", false);
            editor.apply();
        } else {
            btnPulsado.setImageResource(R.drawable.ic_sonido);
            btnPulsado.setTag(true);

            editor.putBoolean("sonido", true);
            editor.apply();
        }

        cargarConfiguracion();
    }

    public void dialogClickado(View v){
        EditText cajaTexto = (EditText)v;
        cajaTexto.setTextColor(Color.BLACK);
        cajaTexto.setText("");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1234){
            if (resultCode == 1){
                // Si vuelve de la configuración habiendo cambiado el formato:
                nuevaPartida();
                Toast toast=Toast.makeText(this, R.string.changedConfig, Toast.LENGTH_SHORT);
                toast.show();
            } else if (resultCode == 2){
                // Si vuelve de la configuración sin cambiar nada definitorio:
                cargarConfiguracion();
                actualizar();
                chrono.setBase(chrono.getBase() - data.getLongExtra("time", SystemClock.elapsedRealtime()));
            }
        }
    }
}
