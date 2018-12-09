package com.example.gabrielcuenca.spaceinvaders.models;


import android.os.health.SystemHealthManager;

public class Chronometer extends Thread {

    public int milesimas;
    public boolean stopped; // estado del contador
    private int seconds;

    // clase interna que representa una tarea, se puede crear varias tareas y asignarle al timer luego

    public void run() {
        milesimas++;
        if (stopped != true) {
            update();
        }
    }

    public void update() {
        if (this.milesimas == 100) {
            seconds++;
            this.milesimas = 0;
        }

    }

    public void start() {
        stopped = false;
        seconds = 0;
        milesimas = 0;
    }



    public void reset() {
        seconds = 0;
        milesimas = 0;

    }// end Reset

    public int getSeconds() {
        return this.seconds;
    }
}
