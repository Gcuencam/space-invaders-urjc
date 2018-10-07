package com.example.gabrielcuenca.spaceinvaders.models;

import android.graphics.RectF;

public class Brick {
    private RectF rect;

    private boolean isVisible;

    private int numShelter;

    public Brick(int row, int column, int shelterNumber, int screenX, int screenY){

        int width = screenX / 90;
        int height = screenY / 40;

        this.numShelter=shelterNumber;

        isVisible = true;

        // A veces una bala atraviesa este relleno
        // Establece el relleno a 0 si te causa molestias
        int brickPadding = 1;

        // El número de guaridas
        int shelterPadding = screenX / 9;
        int startHeight = screenY - (screenY /6 * 2);

        rect = new RectF(column * width + brickPadding +
                (shelterPadding * shelterNumber) +
                shelterPadding + shelterPadding * shelterNumber,
                row * height + brickPadding + startHeight,
                column * width + width - brickPadding +
                        (shelterPadding * shelterNumber) +
                        shelterPadding + shelterPadding * shelterNumber,
                row * height + height - brickPadding + startHeight);
    }

    public RectF getRect(){
        return this.rect;
    }

    public void setInvisible(){
        isVisible = false;
    }

    public boolean getVisibility(){
        return isVisible;
    }

    public int getNumShelter() {
        return numShelter;
    }
}
