package com.example.gabrielcuenca.spaceinvaders.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

import com.example.gabrielcuenca.spaceinvaders.R;

import java.util.Random;

public class Ship {
    RectF rect;


    // La nave espacial del jugador será representada por un Bitmap
    private Bitmap bitmap;

    // Que tan ancho y alto puede llegar nuestra nave espacial
    private float length;
    private float height;

    // X es la parte extrema a la izquierda del rectángulo el cual forma nuestra nave espacial
    private float x;

    // Y es la coordenada de a mero arriba
    private float y;

    // Esto va a mantener la rapidez de los pixeles por segundo a la que la nave espacial se moverá
    private float shipSpeed;

    // En qué direcciones se puede mover la nave espacial
    public final int STOPPED = 0;
    public final int LEFT = 1;
    public final int RIGHT = 2;
    public final int UP = 3;
    public final int DOWN = 4;

    // Se esta moviendo la nave espacial y en que dirección
    private int shipMoving = STOPPED;

    //La nave tiene el color inicial
    private boolean colorInicial=true;

    // Cuando creamos un objeto de esta clase daremos
    // la anchura y la altura de la pantalla
    public Ship(Context context, int screenX, int screenY){

        // Inicializa un RectF vacío
        rect = new RectF();

        length = screenX/10;
        height = screenY/10;

        // Inicia la nave en el centro de la pantalla aproximadamente
        x = screenX / 2;
        y = screenY - 20;

        // Inicializa el bitmap
        bitmap = BitmapFactory.decodeResource(
                context.getResources(),
                R.drawable.player_ship);

        // ajusta el bitmap a un tamaño proporcionado a la resolución de la pantalla
        bitmap = Bitmap.createScaledBitmap(bitmap,
                (int) (length),
                (int) (height),
                false);

        // Qué tan rápido va la nave espacial en pixeles por segundo
        shipSpeed = 300;
    }

    public float getHeight() {
        return height;
    }

    public RectF getRect(){
        return rect;
    }

    // Este es un método de "get" para hacer el rectángulo que
    // define nuestra nave espacial disponible en la clase de SpaceInvadersView
    public Bitmap getBitmap(){
        return bitmap;
    }

    public float getX(){
        return x;
    }

    public float getY() {
        return y;
    }

    public float getLength(){
        return length;
    }

    // Este método será usado para cambiar/establecer si la nave
    // espacial va a la izquierda, la derecha o no se mueve

    public void setShipMoving(int shipMoving) {
        this.shipMoving = shipMoving;
    }

    // Este método de update será llamado desde el update en SpaceInvadersView
    // Determina si la nave espacial del jugador necesita moverse y cambiar las coordenadas
    // que están en x si es necesario

    public void update(long fps){
        if(shipMoving == LEFT){
            if(x>0){
                x = x - shipSpeed / fps;
            }
        }

        if(shipMoving == RIGHT){
            if(x<length*9){
                x = x + shipSpeed / fps;
            }
        }
        if (shipMoving == UP){
            if (y>0){
                y = y - shipSpeed / fps;


            }
        }
        if (shipMoving == DOWN){

                y = y + shipSpeed / fps;

            
        }


        // Actualiza rect el cual es usado para detectar impactos
        rect.top = y;
        rect.bottom = y + height;
        rect.left = x+5;
        rect.right = x + length-5;

    }

    public void desaparecer (){
        x = -300;
    }

    public void aparecer(int screenX){
        x = (int) (Math.random() * screenX)- this.length;
    }



    public void setImagen(Context context){
        if(colorInicial){
            bitmap = BitmapFactory.decodeResource(
                    context.getResources(),
                    R.drawable.ship_cambio);
            colorInicial=false;
        }else{
            bitmap = BitmapFactory.decodeResource(
                    context.getResources(),
                    R.drawable.player_ship);
            colorInicial=true;
        }
        bitmap = Bitmap.createScaledBitmap(bitmap,
                (int) (length),
                (int) (height),
                false);
    }


    public void serImagenAleatoria(Context context){
        int i= (int) (Math.random()*4);

        switch (i){
            case 0:
                bitmap = BitmapFactory.decodeResource(
                        context.getResources(),
                        R.drawable.player_ship);
                break;
            case 1:

                bitmap = BitmapFactory.decodeResource(
                        context.getResources(),
                        R.drawable.ship_cambiogris);
                break;
            case 2:

                bitmap = BitmapFactory.decodeResource(
                        context.getResources(),
                        R.drawable.ship_cambio);
                break;
            case 3:

                bitmap = BitmapFactory.decodeResource(
                        context.getResources(),
                        R.drawable.ship_cambioverde);
                break;
        }

        bitmap = Bitmap.createScaledBitmap(bitmap,
                (int) (length),
                (int) (height),
                false);
    }

}
