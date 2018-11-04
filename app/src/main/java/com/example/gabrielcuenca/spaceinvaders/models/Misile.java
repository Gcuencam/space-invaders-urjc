package com.example.gabrielcuenca.spaceinvaders.models;

import android.graphics.RectF;

public class Misile {

    //region Atributos

    //coordenadas actuales de la bala
    private float x;
    private float y;

    //Rectángulo en el que dibujar
    private RectF rectf;

    //DIRECCION
    public int direccion = -1; //no se mueve
    public static int UP = 1;
    public static int DOWN = 0;
    private float speed = 300;

    //¿Está activate?
    private boolean activate = false;

    //Dimensiones
    private int width;
    private int height;

    //endregion

    //CONSTRUCTOR
    public Misile(int screenY){
        this.width = 9;
        this.height = screenY / 20;
        this.rectf = new RectF();
    }

    public RectF getRectf() {
        return rectf;
    }

    public void desactivar(){
        this.activate = false;
    }

    public void activar(){
        this.activate = true;
    }

    public boolean isActivated(){
       return this.activate;
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

    public void cambiarADireccionOpuesta(){
        if (direccion == DOWN){ //Dirección DOWN (=0)
            direccion = UP;
        }else{ //Dirección UP (=1)
            direccion = DOWN;
        }
    }

    //Te devuelve donde está el extremo de la bala que va a chocar
    public float extrem(){
        if(this.direccion==DOWN){
            return y + height;
        }else{
            return y;
        }
    }

    public boolean shoot(float coordenadaX, float coordenadaY, int dir){
        if(!this.activate){
            setX(coordenadaX);
            setY(coordenadaY);
            this.direccion=dir;
            activar();
        }
        return true;

    }

    public void update(long fps){
        if(direccion==UP){
            y = y - speed /fps;
        }else{
            y = y + speed /fps;
        }

        rectf.left = x;
        rectf.right = x + width;
        rectf.top = y;
        rectf.bottom = y + height;
    }

}
