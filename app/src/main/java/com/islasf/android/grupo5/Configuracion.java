package com.islasf.android.grupo5;

/**
 * @author Carlos García Corpas y Javier Sánchez Gómez
 */

public class Configuracion {

    private int valorMax;
    private int x;
    private int y;
    private int maxPulsaciones;

    private boolean vibracion;
    private boolean sonido;

    private String modo;

    public Configuracion(int valorMax, int x, int y, int maxPulsaciones, boolean vibracion, boolean sonido, String modo) {
        this.valorMax = valorMax;
        this.x = x;
        this.y = y;
        this.maxPulsaciones=maxPulsaciones;
        this.vibracion=vibracion;
        this.sonido=sonido;
        this.modo=modo;
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
