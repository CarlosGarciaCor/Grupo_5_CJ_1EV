package com.islasf.android.grupo5;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Actividad principal lanzada al abrir la aplicación (ver "AndroidManifest.xml").
 * Esta actividad se puede dividir en dos partes.
 * <br/>
 * Por un lado tiene la parte del juego. Generará un tablero gracias a la clase Juego en
 * función de una Configuración y se generarán los botones necesarios para representar ese tablero.
 * Los botones generados tendrán un listener correspondiente el cual permitirá pulsar una casilla y que la clase
 * Juego se encargue.
 * <br/>
 * Por otro lado, esta actividad ha de comunicarse con el resto de actividades de la aplicación. Es necesario
 * que pueda navegar hacia ellas. Para ello hemos desarrollado un menú lateral. Hay varias formas de realizar un
 * menú en Android, nosotros hemos utilizado el componente NavigationView.
 * <br/>
 * Esta clase extiende AppCompatActivity.
 */
public class ActividadPrincipal extends AppCompatActivity {

    /**
     * Atributo Juego que va a contener la partida que se esté jugando
     */
    private Juego juego;

    /**
     * Atributo Configuracion referido a la configuración actual
     */
    private Configuracion configuracion;

    /**
     * Layout de tipo DrawerLayout para el menú lateral
     */
    private DrawerLayout menuLateral;

    /**
     * Layout de tipo LinearLayout que va a ser el layout del tablero
     */
    private LinearLayout gameLayout;

    /**
     * Colección de botones que van a formar el tablero.
     */
    private ArrayList<Button> botones;

    /**
     * TextView de las pulsaciones.
     */
    private TextView tbPulsaciones;

    /**
     * Cronómetro a mostrar del tiempo que tarda el usuario en jugar.
     */
    private Chronometer chrono;

    /**
     * Atributo donde se va a guardar el tiempo del cronómetro cuando se pare.
     */
    private long timePaused=0;

    /*
     * Atributo de tipo Vibrator para establecer la vibración del móvil
     */
    private Vibrator vibrator;

    /*
     * Atributo de tipo SoundPool que va a permitir reproducir sonnidos.
     */
    private SoundPool poolSonidos;

    /**
     * Identificador del sonido utilizado.
     */
    private int sonidoPulsacion;

    /**
     * Colección de identificadores de drawables para el modo COLORES
     */
    private final int[] BTN_COLORDRAW ={R.drawable.draw_btncolor1,
            R.drawable.draw_btncolor2,
            R.drawable.draw_btncolor3,
            R.drawable.draw_btncolor4,
            R.drawable.draw_btncolor5};

    /**
     * Colección de identificadores de drawables para el modo NUMEROS
     */
    private final int[] BTN_NUMDRAW = {R.drawable.btnnum1,
            R.drawable.btnnum2,
            R.drawable.btnnum3,
            R.drawable.btnnum4,
            R.drawable.btnnum5};

    /**
     * El método onCreate de las actividades de Android se ejecuta al lanzar la actividad por primera vez
     * (ver ciclo de vida de Activities).
     * En esta aplicación el onCreate hará tres cosas:
     * <ul>
     *     <li>Llamar al constructor del padre</li>
     *     <li>Iniciar los componentes de la actividad, incluido el menú</li>
     *     <li>Iniciar una nueva partida</li>
     * </ul>
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initComponents();
        generarMenu();
        nuevaPartida();
    }

    /**
     * Método privado en el cuál se "inician los componentes" de la actividad. Es decir se instancian los objetos que
     * se van a necesitar en esta clase (vibración, sonido, layouts, etc).
     *
     * También se da un valor inicial al número de pulsaciones (0) y se instancia la colección de botones (vacía)
     */
    private void initComponents(){
        setContentView(R.layout.activity_actividad_principal);

        //Iniciar componentes de preferencias
        vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE); //Instanciamos vibrador y soundpool.
        poolSonidos = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);

        this.setVolumeControlStream(AudioManager.STREAM_MUSIC); // Para que si el usuairo sube o baja el volumen lo haga del de la música.
        // Le damos un valor a la variable sonidoPulsacion:
        sonidoPulsacion = poolSonidos.load(this, R.raw.touch, 1);

        //Iniciar componentes del juego.
        gameLayout = (LinearLayout) findViewById(R.id.gameLayout);

        chrono = (Chronometer) findViewById(R.id.chrono);

        tbPulsaciones=(TextView)findViewById(R.id.tbPulsaciones);
        tbPulsaciones.setText("0");
        botones=new ArrayList<>();
    }

    /**
     * Método privado en el cual se va a generar el menú lateral utilizando
     * el componente NavigationView ya realizado por layout(ver "activity_actividad_principal.xml").
     * En éste método se añade una ToolBar al menú y se habilitan los botones, añadiendoles un listener
     * en el cual está programado un switch según el botón pulsado. Dentro de cada case del switch
     * se llamará a la opción correspondiente o se creará un Intent para lanzar una segunda actividad.
     */
    private void generarMenu(){
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

    /**
     * Método privado que crea una nueva partida. Para mejorar el rendimiento en este método se comprueba
     * que no haya ya un tablero generado (una colección de botones ya hechos) si el número de estos botones
     * coincide con el número de casillas a pintar. De esta forma sólo se generarán botones una vez, cada
     * vez que se cambie la configuración.
     *
     * Para generar los botones lo que hace es instanciar un LinearLayout por cada fila y un botón por cada celda.
     * Con la propiedad Weight Sum centramos todos los layouts, centrando el tablero en la pantalla.
     *
     * Antes de generar los botones, carga la configuración.
     *
     * Al terminar, inicia el crono.
     */
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

    /**
     * Método privado que sirve para crear un botón a partir de una casilla.
     * En este método se instancia un Button y se le asignan las propiedades necesarias, principalmente
     * se le asocia un drawable en función del modo y del valor que tenga la casilla.
     *
     * El botón se guardará en la colección de botones, para no tener que volver a generarlo
     * si se empieza una nueva partida con el mismo tamaño de tablero.
     *
     * @param casilla Casilla asignada al botón creado.
     * @param id Id del botón, que va a coincidir con la posición de la casilla en la colección.
     * @param linea LinarLayout que va a ser la línea a la que se añade el botón creado.
     */
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

    /**
     * Método que se llama desde el listener de cada botón que forma el tablero.
     * Al pulsar una casilla se reproduce un sonido y una vibración si esto está activado en las
     * preferencias.
     * Después se busca la casilla asignada a ese botón y se llama con ella al método pulsarCasilla() de Juego.
     * Tras esto, se actualiza el tablero y se comprueba si se ha ganado o no la partida. Si se ha ganado, se
     * llama al método privado victoria()
     * @param v Vista pulsada, es decir, el botón pulsado.
     */
    private void pulsarCasilla(View v){

        if (this.configuracion.isSonido()){
            poolSonidos.play(sonidoPulsacion,1,1,1,0,1);
        }

        if (this.configuracion.isVibracion()){
            vibrator.vibrate(300);
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

    /**
     * Éste método se ejecuta cuando el usuario consigue pasarse la partida. Lo primero que hacer es parar el crono
     * y reproducir el sonido y vibrar si así lo permite la configuración.
     *
     * Tras esto, muestra el diálogo de victoria de tipo DialogoWin (ver esta clase).
     */
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

    /**
     * Método llamado desde el DialogoWin lanzado al ganar una partida.
     * Éste método hace dos cosas. La primera es guardar en la base de datos el resultado
     * de la partida (si el usuario ha querido guardarlo). Para guardar usamos la clase AccesoBBDD (ver clase).
     * La segunda es iniciar una nueva partida o restablecer la misma (también en función de lo que
     * haya decidido el usuario).
     * @param result Entero para designar la opción del usuario (si nueva partida o reiniciar)
     * @param notSave Boolean que designa si se ha de guardar la partida o no.
     * @param user Nombre de usuario para guardar la partida
     */
    public void onRespuesta(int result, boolean notSave, String user){
        //Guardamos resultado si no está marcado el CheckBox
        if (!notSave){
            AccesoBBDD acceso=new AccesoBBDD(this);
            juego.setTiempo(chrono.getBase());
            juego.setUsuario(user);
            acceso.insertarPartida(juego);
        }

        if (result==0)
            nuevaPartida();
        else
            reiniciarPartida();
    }

    /**
     * Método que reinicia el juego y el crono y actualiza el tablero.
     */
    private void reiniciarPartida(){
        juego.reiniciarJuego();

        actualizar();
        chrono.setBase(SystemClock.elapsedRealtime());
    }

    /**
     * Este método actualiza los botones del tablero, es decir, les asigna el valor (en forma de drawable)
     * que corresponde al valor que tiene cada Casilla en el objeto Juego.
     * Actualiza también el número de pulsaciones.
     */
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

    /**
     * Éste método carga la configuración desde el fichero de preferencias utilizando la clase
     * SharedPreferences.
     *
     * También cambia el drawable asignado a los botones de sonido y vibración de la cabecera en función
     * de la configuración leída.
     */
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

    /**
     * Método que define el comportamiento del botón de vibración de la cabecera.
     * Básicamente escribe en la configuración el nuevo valor al cambiarlo y cambiar el drawable
     * en consecuencia.
     *
     * Para saber en qué estado está el botón (vibración activada o desactivada) usamos un Tag, una etiqueta.
     * Para escribir en el fichero de preferencias usamos un Editor obtenido de la clase SharedPreferences.
     *
     * Tras escribir, carga de nuevo la configuración.
     * @param v Botón pulsado
     */
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

    /**
     * Método equivalente a pulsarBtnVibracion pero para el botón de sonido.
     * @param v Botón pulsado
     */
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

    /**
     * Éste método es una aportación de Android que sirve para poder guardar el estado de un actividad
     * o una aplicación al cerrarla o destruirla.
     *
     * Aquí programamos por tanto que queremos que se guarde al rotar la pantalla, y esto es el objeto Juego, la partida
     * y el tiempo por el que va el crono.
     *
     * Es necesario que el Juego y todos los objetos que lo forman sean Serializables para poder incluirlo en
     * el Bundle.
     * @param outState Bundle para guardar el estado de la actividad
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable("juego", juego);
        outState.putLong("time", chrono.getBase());
    }

    /**
     * Método que proporciona Android para recuperar datos del bundle guardado en el método anterior
     * al destruir la actividad. Por tanto aquí lo que hacemos es recuperar el objeto Juego y el tiempo
     * en el que se quedó y actualizar la pantalla en consecuencia.
     * @param savedInstanceState Bundle donde está guardado el estado de la actividad.
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        juego=(Juego)savedInstanceState.getSerializable("juego");
        tbPulsaciones.setText(Integer.toString(juego.getNumPulsaciones()));
        chrono.setBase(savedInstanceState.getLong("time"));
        actualizar();
    }

    /**
     * Al lanzar la actividad ConfigurationActivity (ver clase) es necesario crear un canal de comunicación, es decir,
     * poder pasarle datos y poder recibir datos de ella.
     * En éste método se reciben dichos datos y se actúa en consecuencia.
     * Consideramos dos modos de actuación:
     * <ul>
     *     <li>Si el usuario cambia la configuración del tablero: se generá una nueva partida y se le lanza un mensaje
     *     comunicándoselo</li>
     *     <li>Si no cambia el tablero si no sólo preferencias (sonido, vibración, modo), en cuyo caso
     *     el juego seguirá siendo el que era y tan sólo se cargará la configuración nueva y actualizará
     *     la pantalla</li>
     * </ul>
     * @param requestCode Código del Intent lanzado.
     * @param resultCode Código de resultado que da la Activity lanzada
     * @param data Intent que devuelve la Activity lanzada (donde van los datos de vuelta)
     */
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
