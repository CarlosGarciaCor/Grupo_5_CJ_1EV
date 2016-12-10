package com.islasf.android.grupo5;

/**
 * @author Carlos García Corpas y Javier Sánchez Gómez
 */

public class Preferencias {

    private boolean vibracion;
    private boolean sonido;

    private String modo;

    public Preferencias(boolean vibracion, boolean sonido, String modo) {
        this.vibracion = vibracion;
        this.sonido = sonido;
        this.modo = modo;
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
