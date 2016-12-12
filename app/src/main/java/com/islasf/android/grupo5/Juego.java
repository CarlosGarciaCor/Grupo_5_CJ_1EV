package com.islasf.android.grupo5;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * @author Carlos García y Javier Sánchez
 */

public class Juego implements Serializable {

    private Configuracion configuracion;
    private ArrayList<Casilla> listCasillas;
    private ArrayList<Casilla> listReinicio;
    private int numPulsaciones;

    private long tiempo;
    private String usuario;

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

    private void generarCasillas(){
        Random random=new Random();
        for (int y=1; y<=this.configuracion.getY(); y++){
            for (int x=1; x<=this.configuracion.getX(); x++){
                listCasillas.add(new Casilla(x, y, random.nextInt(this.configuracion.getValorMax())+1));
            }
        }
    }

    private int getIndex(Casilla casilla){
        for (Casilla item: listCasillas){
            if (casilla.getX()==item.getX() && casilla.getY()==item.getY())
                return listCasillas.indexOf(item);
        }
        return -1;
    }

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

    public Configuracion getConfiguracion() {
        return configuracion;
    }

    public ArrayList<Casilla> getCasillas() {
        return listCasillas;
    }

    public ArrayList<Casilla> getCasillasReinicio() {
        return listReinicio;
    }

    public int getNumPulsaciones() {
        return numPulsaciones;
    }

    public void incrementarNumPulsaciones() {
        this.numPulsaciones++;
    }

    public void setNumPulsaciones(int numPulsaciones) {
        this.numPulsaciones = numPulsaciones;
    }

    public long getTiempo() {

        return tiempo;
    }

    public void setTiempo(long tiempo) {
        this.tiempo = tiempo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}
