package com.islasf.android.grupo5;

/**
 * Created by sanch on 07/12/2016.
 */

public class Configuracion {

    private int valorMax;
    private int x;
    private int y;
    private int maxPulsaciones;

    public Configuracion(int valorMax, int x, int y, int maxPulsaciones) {
        this.valorMax = valorMax;
        this.x = x;
        this.y = y;
        this.maxPulsaciones=maxPulsaciones;
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

    public int getMaxPulsaciones() {
        return maxPulsaciones;
    }
}
