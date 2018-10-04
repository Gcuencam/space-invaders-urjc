package com.example.gabrielcuenca.spaceinvaders.models;

import android.graphics.RectF;

public class Bala {

    //region Atributos

    //coordenadas actuales de la bala
    private float x;
    private float y;

    //Rectángulo en el que dibujar
    private RectF rectf;

    //DIRECCION
    private int direccion = -1; //no se mueve
    public final int UP = 1;
    public final int DOWN = 0;
    private float velocidad = 300;

    //¿Está activado?
    private boolean activado = false;

    //Dimensiones
    private int width;
    private int height;

    //endregion

    //CONSTRUCTOR
    public Bala (int screenY){
        this.width = 1;
        this.height = screenY / 20;
        this.rectf = new RectF();
    }

    public RectF getRectf() {
        return rectf;
    }

    public void desactivar(){
        this.activado = false;
    }

    public void activar(){
        this.activado = true;
    }

    public boolean estaActivado(){
       return this.activado;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    //Te devuelve donde está el extremo de la bala que va a chocar
    public float extremo(){
        if(this.direccion==DOWN){
            return y + height;
        }else{
            return y;
        }
    }

    public boolean disparo(float coordenadaX, float coordenadaY, int dir){
        if(!this.activado){
            setX(coordenadaX);
            setY(coordenadaY);
            activar();
            return true;
        }else{
            return false;
            //ya está disparando
        }
    }

    public void update(long fps){
        if(direccion==UP){
            y = y - velocidad/fps;
        }else{
            y = y + velocidad/fps;
        }

        rectf.left = x;
        rectf.right = x + width;
        rectf.top = y;
        rectf.bottom = y + height;
    }

}
