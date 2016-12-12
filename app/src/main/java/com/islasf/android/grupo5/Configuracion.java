package com.islasf.android.grupo5;

import java.io.Serializable;

/**
 * Clase Configuración. <br>
 *     Es la encargada de gestionar la configuración, con parámetros como el número de elementos en el eje X, el número máximo de índice, etc. <br>
 *         Implementa la interfaz Serializable para que se pueda meter junto con la clase Juego en un Bundle.
 *
 * @author Carlos García y Javier Sánchez
 */

public class Configuracion implements Serializable{

    private int valorMax;
    private int x;
    private int y;

    private boolean vibracion;
    private boolean sonido;

    private String modo;

    /**
     * Constructor completo de la clase. Recibe todos los parámetros para que la configuración pueda funcionar.
     *
     * @param valorMax índice máximo de valores de los botones.
     * @param x número de elementos en el eje x.
     * @param y número de elementos en el eje y.
     * @param vibracion booleano que indica si hay vibración o no.
     * @param sonido si hay sonido o no.
     * @param modo el estilo del juego. "COLORES" para jugar con colores y "NUMEROS" para jugar con números.
     */
    public Configuracion(int valorMax, int x, int y, boolean vibracion, boolean sonido, String modo) {
        this.valorMax = valorMax;
        this.x = x;
        this.y = y;
        this.vibracion=vibracion;
        this.sonido=sonido;
        this.modo=modo;
    }

    /**
     * Constructor de Configuración utilizado para recoger datos de la base de datos. <br>
     *     Solamente recibe lo esencial.
     *
     * @param valorMax índice máximo de valores de los botones.
     * @param x número de elementos en el eje x.
     * @param y número de elementos en el eje y.
     */
    public Configuracion(int valorMax, int x, int y){
        this.valorMax = valorMax;
        this.x = x;
        this.y = y;
    }

    /**
     * Devuelve el índice máximo de valores.
     * @return el índice máximo de valores.
     */
    public int getValorMax() {
        return valorMax;
    }

    /**
     * Método que devuelve el número de elementos en el eje x.
     * @return el número de elementos en el eje x.
     */
    public int getX() {
        return x;
    }

    /**
     * Método que devuelve el número de elementos en el eje y.
     * @return el número de elementos en el eje y.
     */
    public int getY() {
        return y;
    }

    /**
     * Método que devuelve si la configuración tiene la vibración activada o no.
     * @return un boolean indicando si hay vibración o no.
     */
    public boolean isVibracion() {
        return vibracion;
    }

    /**
     * Método que devuelve si la configuración tiene el sonido activado o no.
     * @return un boolean indicando si hay sonido o no.
     */
    public boolean isSonido() {
        return sonido;
    }

    /**
     * Método que devuelve el estilo de la partida.
     * @return "COLORES" si el estilo son cuadrados de colores o "NUMEROS" si se está jugando con números.
     */
    public String getModo() {
        return modo;
    }
}
