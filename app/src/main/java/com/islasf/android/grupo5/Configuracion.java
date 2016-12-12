package com.islasf.android.grupo5;

import java.io.Serializable;

/**
 * @author Carlos García Corpas y Javier Sánchez Gómez
 */

public class Configuracion implements Serializable{

    private int valorMax;
    private int x;
    private int y;

    private boolean vibracion;
    private boolean sonido;

    private String modo;

    public Configuracion(int valorMax, int x, int y, boolean vibracion, boolean sonido, String modo) {
        this.valorMax = valorMax;
        this.x = x;
        this.y = y;
        this.vibracion=vibracion;
        this.sonido=sonido;
        this.modo=modo;
    }

    public Configuracion(int valorMax, int x, int y){
        this.valorMax = valorMax;
        this.x = x;
        this.y = y;
    }

    public int getValorMax() {
        return valorMax;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isVibracion() {
        return vibracion;
    }

    public boolean isSonido() {
        return sonido;
    }

    public String getModo() {
        return modo;
    }
}
