package com.islasf.android.grupo5;

import java.io.Serializable;

/**
 * Clase de negocio Casilla. Representa cada celda del tablero de juego.
 * Las casillas van a tener unas coordenadas en el tablero (la posición en la que están)
 * y un valor al que en la parte gráfica se le asignará un Drawable.
 *
 * Implementa la interfaz Serializable para poder meter un objeto Juego en un Bundle
 * con el método putSerializable(...).
 * @author Carlos García y Javier Sánchez
 */
public class Casilla implements Serializable{

    /**
     * Coordenada X de la casilla en el tablero.
     */
    private int x;
    /**
     * Coordenada Y de la casilla en el tablero.
     */
    private int y;
    /**
     * Valor de la casilla.
     */
    private int valor;

    /**
     * Constructor para instanciar casillas a partir de sus tres atributos.
     * @param x Coordenada X
     * @param y Coordenada Y
     * @param valor Valor de la casilla
     */
    public Casilla(int x, int y, int valor) {
        this.x = x;
        this.y = y;
        this.valor = valor;
    }

    /**
     * Método que incrementa el valor de la casilla en función del valor máximo permitido, teniendo
     * en cuenta que si al incrementar el valor lo sobrepasa, volverá al valor inicial (1).
     * @param valorMax Parámetro referente a la Configuracion del Juego que indica el valor máximo que puede
     *                 tener la casilla.
     */
    public void incrementarValor(int valorMax){
        if (this.valor<valorMax)
            this.valor++;
        else
            this.valor=1;
    }

    /**
     * Getter para la coordenada X
     * @return Coordenada X
     */
    public int getX() {
        return x;
    }

    /**
     * Getter para la coordenada Y
     * @return Coordenada Y
     */
    public int getY() {
        return y;
    }

    /**
     * Getter para el valor de la casilla
     * @return Valor de la casilla
     */
    public int getValor() {
        return valor;
    }
}
