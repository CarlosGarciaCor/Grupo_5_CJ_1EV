package com.islasf.android.grupo5;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * Clase principal de negocio de la aplicación. En esta clase están programados los mecanismos internos
 * que van a definir el juego flip.
 * <br/>
 * Las funcionalidades que otorga en cuanto al juego son básicamente cuatro:
 * <ul>
 *  <li>Generar el tablero en función de la configuración</li>
 *  <li>Reaccionar al pulsar en una casilla, incrementando los valores de las casillas correspondientes</li>
 *  <li>Comprobar la condición de victoria, que todas las casillas tengan el mismo valor</li>
 *  <li>Reiniciar la partida</li>
 * </ul>
 * <br/>
 * También permite llevar un contador que sea el número de pulsaciones que va realizando el usuario.
 *
 * Esta clase implementa la interfaz Serializable para poder meter un objeto de la misma en un Bundle.
 * @author Carlos García y Javier Sánchez
 */

public class Juego implements Serializable {

    /**
     * Configuración a partir de la cual se va a construir el juego.
     */
    private Configuracion configuracion;

    /**
     * Colección de casillas que va a componer el tablero.
     */
    private ArrayList<Casilla> listCasillas;

    /**
     * Colección de casillas original, copia del tablero generado para poder reiniciar la partida.
     */
    private ArrayList<Casilla> listReinicio;

    /**
     * Contador que lleva el número de pulsaciones que realiza el usuario
     */
    private int numPulsaciones;

    /**
     * Tiempo que tarda el usuario en ganar la partida (sólo para guardar en BBDD)
     */
    private long tiempo;

    /**
     * Nombre del usuario que ha ganado la partida (sólo para guardar en BBDD)
     */
    private String usuario;

    /**
     * Constructor de Juego a partir de una Configuracion. Para facilitar el uso de esta clase, en el constructor
     * se genera directamente el tablero y se guardan las casillas generadas para poder reinicar la partida en
     * cualquier momento.
     *
     * A la hora de hacer esto último hay que tener en cuenta que la asignación de objetos en Java es por
     * referencia, por lo que si lo planetas como this.listReinicio=this.listCasillas, la lista de reinicio
     * va a apuntar siempre a la lista de casillas, y esta varía. Por esto se explica la fontanería realizada
     * al final del método.
     *
     * @param configuracion
     */
    public Juego(Configuracion configuracion) {
        this.configuracion = configuracion;
        this.listCasillas=new ArrayList<>();
        this.numPulsaciones=0;

        while (condicionVictoria()){
            this.generarCasillas();
        }

        //Como el paso de objetos es por referencia, si haces listReinicio=listCasillas simplemente,
        //la listReinicio va a estar apuntando a la otra, por lo que se va a ir actualizando.
        this.listReinicio=new ArrayList<>();
        for (Casilla item: listCasillas){
            int x=item.getX();
            int y=item.getY();
            int valor=item.getValor();

            listReinicio.add(new Casilla(x, y, valor));
        }
    }

    /**
     * Método que genera el tablero en función de la configuración. Va recorriendo por cada X e Y y va
     * añadiendo a la colección un nuevo objeto Casilla generado con un valor aleatorio (utilizando Random)
     */
    private void generarCasillas(){
        Random random=new Random();
        for (int y=1; y<=this.configuracion.getY(); y++){
            for (int x=1; x<=this.configuracion.getX(); x++){
                listCasillas.add(new Casilla(x, y, random.nextInt(this.configuracion.getValorMax())+1));
            }
        }
    }

    /**
     * Método privado para obtener el índice en el que se encuentra una Casilla dentro de la colección.
     * @param casilla Casilla a buscar dentro de la colección.
     * @return Indice en el que se encuentra la Casilla buscada en la colección.
     */
    private int getIndex(Casilla casilla){
        for (Casilla item: listCasillas){
            if (casilla.getX()==item.getX() && casilla.getY()==item.getY())
                return listCasillas.indexOf(item);
        }
        return -1;
    }

    /**
     * Método principal del juego que se llamará al pulsar en una casilla.
     * Hay que tener en cuenta que las casillas afectadas varían en función de que la casilla pulsada
     * sea una esquina o no.
     * <ul>
     *     <li>Si es una esquina: se incrementa la propia casilla y todas las de su alrededor</li>
     *     <li>Si no es una esquina: se incrementa la propia casilla y las que compartan lado con ella</li>
     * </ul>
     *
     * Lo que hace en los dos casos es recorrer la colección de casillas e incrementar el valor de
     * las casillas que encuentra que cumplan las condiciones anteriores.
     * @param casilla Casilla pulsada
     */
    public void pulsarCasilla(Casilla casilla){
        casilla=listCasillas.get(this.getIndex(casilla)); //Para coger el objeto que está dentro de la lista.
        casilla.incrementarValor(this.configuracion.getValorMax());

        //Si es una esquina:
        if (casilla.getX()==1 || casilla.getX()==this.configuracion.getX()){
            //Recorremos la colección e incrementamos las casillas que tenga a los lados y
            //la que comparta arista

            for (Casilla item: listCasillas){
                if (casilla.getY()==item.getY()){
                    if (item.getX()==casilla.getX()+1 || item.getX()==casilla.getX()-1)
                        item.incrementarValor(this.configuracion.getValorMax());
                }
                else if (casilla.getX()==item.getX()){
                    if (item.getY()==casilla.getY()+1 || item.getY()==casilla.getY()-1)
                        item.incrementarValor(this.configuracion.getValorMax());
                }
                if (compartenArista(casilla, item))
                    item.incrementarValor(this.configuracion.getValorMax());
            }
        }

        else {
            //Si no es una esquina, recorremos la lista de casillas y incrementamos el valor de
            //las que tengan la misma x o la misma y (las que estén a los lados)

            for (Casilla item: listCasillas){
                if (casilla.getY()==item.getY()){
                    if (item.getX()==casilla.getX()+1 || item.getX()==casilla.getX()-1)
                        item.incrementarValor(this.configuracion.getValorMax());
                }
                else if (casilla.getX()==item.getX()){
                    if (item.getY()==casilla.getY()+1 || item.getY()==casilla.getY()-1)
                        item.incrementarValor(this.configuracion.getValorMax());
                }
            }
        }
    }

    /**
     * Método privado para determinar si una casilla comparte arista con otra. Como el tablero es cuadrado,
     * hay cuatro esquinas, por lo que éste método descubre de qué esquina se trata y si la otra casilla comparte
     * arista o no con ella.
     * @param esquina Casilla esquina.
     * @param arista Casilla de la cual se busca saber si comparte arista con la Casilla esquina.
     * @return
     */
    private boolean compartenArista(Casilla esquina, Casilla arista){

        //Esquina superior izquierda
        if (esquina.getX()==1 && esquina.getY()==1)
            if (arista.getX()==esquina.getX()+1 && arista.getY()==esquina.getY()+1)
                return true;
        //Equina inferior izquierda
        if (esquina.getX()==1 && esquina.getY()==this.configuracion.getY())
            if (arista.getX()==esquina.getX()+1 && arista.getY()==esquina.getY()-1)
                return true;

        //Esquina superior derecha
        if (esquina.getX()==this.configuracion.getX() && esquina.getY()==1)
            if (arista.getX()==esquina.getX()-1 && arista.getY()==esquina.getY()+1)
                return true;

        //Esquina inferior derecha
        if (esquina.getX()==this.configuracion.getX() && esquina.getY()==this.configuracion.getY())
            if (arista.getX()==esquina.getX()-1 && arista.getY()==esquina.getY()-1)
                return true;

        return false;
    }

    /**
     * Método que comprueba si el juego ha sido completado o no. Recorre la colección de casillas y usando
     * una Casilla auxiliar las va comparando para que tengan el mismo valor. Si pasa el bucle devuelve true, si
     * encuentra que dos casillas no tienen el mismo valor devuelve false.
     * @return True si todas las casillas tienen el mismo valor.
     */
    public boolean condicionVictoria(){
        Casilla aux=null;
        for (Casilla item: listCasillas){
            if (aux!=null){
                if (aux.getValor()!=item.getValor())
                    return false;
            }
            aux=item;
        }
        return true;
    }

    public void reiniciarJuego(){
        this.listCasillas=new ArrayList<>();
        for (Casilla item: listReinicio){
            int x=item.getX();
            int y=item.getY();
            int valor=item.getValor();

            listCasillas.add(new Casilla(x, y, valor));
        }
        this.numPulsaciones=0;
    }

    /**
     * Método que incrementa en uno el contador del número de pulsaciones.
     */
    public void incrementarNumPulsaciones() {
        this.numPulsaciones++;
    }

    /**
     * Getter para la Configuracion
     * @return Configuracion del juego
     */
    public Configuracion getConfiguracion() {
        return configuracion;
    }

    /**
     * Getter para la colección de casillas del tablero.
     * @return Lista de casillas
     */
    public ArrayList<Casilla> getCasillas() {
        return listCasillas;
    }

    /**
     * Getter para la lista de casillas de reinicio
     * @return Lista de casillas de reinicio
     */
    public ArrayList<Casilla> getCasillasReinicio() {
        return listReinicio;
    }

    /**
     * Getter para el número de pulsaciones.
     * @return Número de pulsaciones.
     */
    public int getNumPulsaciones() {
        return numPulsaciones;
    }

    /**
     * Setter para el número de pulsaciones.
     * @param numPulsaciones Número de pulsaciones.
     */
    public void setNumPulsaciones(int numPulsaciones) {
        this.numPulsaciones = numPulsaciones;
    }

    /**
     * Getter para el tiempo
     * @return Tiempo
     */
    public long getTiempo() {

        return tiempo;
    }

    /**
     * Setter para el tiempo
     * @param tiempo Tiempo
     */
    public void setTiempo(long tiempo) {
        this.tiempo = tiempo;
    }

    /**
     * Getter para el nombre de usuario
     * @return Nombre de usuario
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * Setter para el nombre de usuario
     * @param usuario Nombre de usuario
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}
