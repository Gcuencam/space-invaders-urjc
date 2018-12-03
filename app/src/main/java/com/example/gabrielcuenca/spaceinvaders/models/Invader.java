package com.example.gabrielcuenca.spaceinvaders.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

import com.example.gabrielcuenca.spaceinvaders.R;

import java.util.Random;

public class Invader {

    //region atributos

    private boolean extra=false;

    private RectF rectf;

    private Bitmap bitmap;

        //dimensiones
    private float width;
    private float height;

    //Tiene color inicial?
    private boolean colorIncial=true;

        //coordenada
    private float xleft; //esquina izquiera
    private float yup; //esquina superior

        //velocidad
    private float velocidad;

        //direccion
    private final int LEFT = 1;
    private final int RIGT = 2;
    private int direccion = 2;

    private boolean isVisible;
    //endregion

    //CONSTRUCTOR
    public Invader(Context context, int columna, int fila, int pantallaY, int pantallaX){
        rectf = new RectF();
        width = pantallaX/15;
        height = pantallaY/15;

        isVisible = true;

        int margen = pantallaX/120;

        yup = columna * (width + margen);

        xleft = fila * (width + margen);

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.space_invader);

        velocidad = 100;
    }

    public Invader(Context context, int x, int y, int pantallaY, int pantallaX, boolean extra){
        rectf = new RectF();
        width = pantallaX/15;
        height = pantallaY/15;

        isVisible = true;
        yup = y;
        xleft = x;

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.space_invader);

        velocidad = 300;

    }

    public void makeVisible(){ this.isVisible = true;}

    public void makeInvisible(){
        this.isVisible = false;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getXleft() {
        return xleft;
    }

    public float getY() {
        return yup;
    }

    public RectF getRectf() {
        return rectf;
    }

    public void update(long fps, int screenX){
        if(isVisible) {

            if (direccion == LEFT) {
                xleft = xleft - velocidad / fps;
            } else {
                xleft = xleft + velocidad / fps;
            }

            rectf.left = xleft;
            rectf.right = xleft + height;

            rectf.top = yup;
            rectf.bottom = yup + width;
        }

    }

    public void automaticMove(){
        if(direccion==LEFT){
            direccion=RIGT;
        }else{
            direccion=LEFT;
        }
        yup = height + yup;
    }

    public boolean shoot(){
        Random generator = new Random();
        int random = generator.nextInt(20);
        if(random==1){
            return true;
        }else{
            return false;
        }

    }

    public void setImagen(Context context){
        if(colorIncial){
            bitmap = BitmapFactory.decodeResource(
                    context.getResources(),
                    R.drawable.invader_cambio);
            colorIncial=false;
        }else{

            bitmap = BitmapFactory.decodeResource(
                    context.getResources(),
                    R.drawable.space_invader);
            colorIncial=true;
        }
    }

    public void setImagenAleatoria(Context context){
        int i = (int) (Math.random()*4);
        switch (i){
            case 0:
                bitmap = BitmapFactory.decodeResource(
                        context.getResources(),
                        R.drawable.invader_cambio);
                break;
            case 1:
                bitmap = BitmapFactory.decodeResource(
                        context.getResources(),
                        R.drawable.space_invader);
                break;

            case 2:
                bitmap = BitmapFactory.decodeResource(
                        context.getResources(),
                        R.drawable.space_invaderazul);
                break;
            case 3:
                bitmap = BitmapFactory.decodeResource(
                        context.getResources(),
                        R.drawable.space_invadernaranja);
                break;

        }
    }
}
