package com.islasf.android.grupo5;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

public class ConfiguracionActivity extends AppCompatActivity {

    private SeekBar seekBarX;
    private TextView textoX;
    private SeekBar seekBarY;
    private TextView textoY;
    private SeekBar seekBarMax;
    private TextView textoMax;

    private RadioGroup radioGroup;

    private CheckBox vibracion;
    private CheckBox sonido;


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

        vibracion = (CheckBox)findViewById(R.id.cbVibr);
        sonido = (CheckBox)findViewById(R.id.cbSoni);
        // /componentes

        // Ponemos un valor de inicio a los textos de las seekbar:
        textoX.setText(getResources().getString(R.string.seekbarX)+" " + (seekBarX.getProgress()+3));
        textoY.setText(getResources().getString(R.string.seekbarY)+" " + (seekBarY.getProgress()+3));
        textoMax.setText(getResources().getString(R.string.numMax)+" " + (seekBarMax.getProgress()+3));
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

    public void aceptarConfiguracion(View v){
        int elementosX = seekBarX.getProgress();
        int elementosY = seekBarY.getProgress();
        int numMax = seekBarMax.getProgress();
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

        Configuracion conf = new Configuracion(numMax+2, elementosX+3, elementosY+3, isVibracion, isSonido, modo);
    }

    public void volver(View v){
        this.finish();
    }
}
