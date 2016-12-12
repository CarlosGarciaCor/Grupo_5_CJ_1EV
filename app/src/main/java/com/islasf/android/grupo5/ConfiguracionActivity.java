package com.islasf.android.grupo5;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Clase ConfiguracionActivity. <br>
 *     La clase ConfiguracionActivity es la asociada con la actividad de ver y modificar la configuración del juego.
 *
 *     La actividad funciona de la siguiente manera:
 *     <ul>
 *         <li>1. Si existe una configuración previa carga los elementos con esos datos, si no, se cargan con unos datos por defecto.</li>
 *         <li>2. Según el atributo vaya modificando datos se le va mostrando el resultado de estos, como son la disposicion NxM</li>
 *         <li>3. Cuando el usuario le de a guardar la aplicación valora lo que se ha modificado. Si se han modificado datos relevantes a la partida se inicia una nueva. Si no,
 *         se reanuda la que estaba con el estilo modificado.</li>
 *         <li>4. Si el usuario le da a volver o utiliza el botón back del móvil volverá a la partida en la que estaba sin aplicar ningún cambio.</li>
 *     </ul>
 */
public class ConfiguracionActivity extends AppCompatActivity {

    private SeekBar seekBarX;
    private TextView textoX;
    private SeekBar seekBarY;
    private TextView textoY;
    private SeekBar seekBarMax;
    private TextView textoMax;

    private RadioGroup radioGroup;
    private RadioButton rbColor;
    private RadioButton rbNumero;

    private CheckBox vibracion;
    private CheckBox sonido;

    private long tiempoCrono;

    // Elementos de inicio:
    private int indiceX;
    private int indiceY;
    private int numeroIndiceMax;


    /**
     * Método sobrescrito onCreate. Se encarga de cargar una posible configuración previa para el mostrado de los botones y recoge los valores definitorios para ver si estos se han modificado
     * cuando el usuario le de a aceptar.
     * @param savedInstanceState un posible bundle con una instancia anterior de la actividad (se ha reiniciado la aplicación).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        // Damos valor a todos los componentes:
        seekBarX = (SeekBar)findViewById(R.id.barraElementosX);
        textoX = (TextView)findViewById(R.id.textoEjeX);
        seekBarY = (SeekBar)findViewById(R.id.barraElementosY);
        textoY = (TextView)findViewById(R.id.textoEjeY);
        seekBarMax = (SeekBar)findViewById(R.id.barraElementosIndice);
        textoMax = (TextView)findViewById(R.id.textoMax);

        radioGroup = (RadioGroup)findViewById(R.id.rGroup);
        rbColor = (RadioButton)findViewById(R.id.rButtColores);
        rbNumero = (RadioButton)findViewById(R.id.rButtNumeros);

        vibracion = (CheckBox)findViewById(R.id.cbVibr);
        sonido = (CheckBox)findViewById(R.id.cbSoni);
        // /componentes

        // Cargamos una posible configuracion previa:
        cargarConfiguracion();
        //

        // Recogemos los valores definitorios (si estos se modifican la partida se reinicia, si no no.
        indiceX = seekBarX.getProgress()+3;
        indiceY = seekBarY.getProgress()+3;
        numeroIndiceMax = seekBarMax.getProgress()+2;

        //Recogemos el tiempo del crono del juego
        Intent recibido = getIntent();
        tiempoCrono = recibido.getExtras().getLong("time");
        //

        // Ponemos un valor de inicio a los textos de las seekbar:
        textoX.setText(getResources().getString(R.string.seekbarX)+" " + (seekBarX.getProgress()+3));
        textoY.setText(getResources().getString(R.string.seekbarY)+" " + (seekBarY.getProgress()+3));
        textoMax.setText(getResources().getString(R.string.numMax)+" " + (seekBarMax.getProgress()+2));
        // /textos

        // Los listeners para cambiar el texto según cambian las barras:
        seekBarX.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textoX.setText(getResources().getString(R.string.seekbarX)+" " + (progress+3));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        seekBarY.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textoY.setText(getResources().getString(R.string.seekbarY) + " " + (progress+3));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        seekBarMax.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textoMax.setText(getResources().getString(R.string.numMax)+" "+(progress+2));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        // /listeners barras

    }

    /**
     * Se encarga de cargar una posible configuración en uso. Si no hay ninguna configuración en uso (es el primer inicio de la app) entonces carga los valores que toma la configuración por defecto en el
     * primer inicio.
     */
    private void cargarConfiguracion(){
        SharedPreferences prefs = getSharedPreferences("Configuracion", Context.MODE_PRIVATE);
        seekBarX.setProgress(prefs.getInt("indiceX", 3)-3);
        seekBarY.setProgress(prefs.getInt("indiceY", 4)-3);
        seekBarMax.setProgress(prefs.getInt("indiceMax", 3)-2);

        switch (prefs.getString("modo", "COLORES")){
            case "COLORES":
                rbColor.setChecked(true);
                rbNumero.setChecked(false);
                break;
            case "NUMEROS":
                rbNumero.setChecked(true);
                rbColor.setChecked(false);
                break;
        }

        vibracion.setChecked(prefs.getBoolean("vibracion", true));
        sonido.setChecked(prefs.getBoolean("sonido", true));

    }

    /**
     * Recoge los valores que ha modificado el usuario, verifica que alguno de estos sea definitorio para la partida y manda un resultado u otro a la actividad principal dependiendo de esto.
     * @param v el botón desde el que es llamado, el botón de aceptar.
     */
    public void aceptarConfiguracion(View v){
        int elementosX = seekBarX.getProgress()+3;
        int elementosY = seekBarY.getProgress()+3;
        int numMax = seekBarMax.getProgress()+2;
        String modo = "COLORES";
        boolean isVibracion = vibracion.isChecked();
        boolean isSonido = sonido.isChecked();

        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.rButtColores:
                modo = "COLORES";
                break;
            case R.id.rButtNumeros:
                modo = "NUMEROS";
                break;
        }

        // Guardamos la configuración en el fichero de preferencias:
        SharedPreferences prefs = getSharedPreferences("Configuracion", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt("indiceX", elementosX);
        editor.putInt("indiceY", elementosY);
        editor.putInt("indiceMax", numMax);
        editor.putString("modo", modo);
        editor.putBoolean("vibracion", isVibracion);
        editor.putBoolean("sonido", isSonido);

        editor.apply();


        /*
            Si se ha modificado algún parametro definitorio se inicia una nueva partida, si no, solo se actualiza el diseño.
         */
        if (elementosX != indiceX || elementosY != indiceY || numMax != numeroIndiceMax){
            setResult(1);
            this.finish();
        } else {
            Intent i = new Intent();
            i.putExtra("time", tiempoCrono);
            setResult(2, i);
            this.finish();
        }

    }

    /**
     * El método llamado cuando el usuario pulsa Volver. Se encarga de devolverle a la actividad el tiempo del crono (beta) y de mandarle un codigo de resultado para que la actividad principal
     * identifique lo que el usuario ha decidido (volver sin aceptar los cambios).
     * @param v el botón volver.
     */
    public void volver(View v){
        Intent i = new Intent();
        i.putExtra("time", tiempoCrono);
        setResult(0, i);
        this.finish();
    }
}
