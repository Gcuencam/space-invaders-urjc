package com.example.gabrielcuenca.spaceinvaders.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.gabrielcuenca.spaceinvaders.R;

public class View extends SurfaceView implements Runnable {

    Context context;

    // Esta es nuestra secuencia
    private Thread gameThread = null;

    // Nuestro SurfaceHolder para bloquear la superficie antes de que dibujemos nuestros gráficos
    private SurfaceHolder ourHolder;

    // Un booleano el cual vamos a activar y desactivar
    // cuando el juego este activo- o no.
    private volatile boolean playing;

    // El juego esta pausado al iniciar
    private boolean paused = true;

    // Un objeto de lienzo (Canvas) y de pintar (Paint)
    private Canvas canvas;
    private Paint paint;

    // Esta variable rastrea los cuadros por segundo del juego
    private long fps;

    // Esto se utiliza para ayudar a calcular los cuadros por segundo
    private long timeThisFrame;

    // El tamaño de la pantalla en pixeles
    private int screenX;
    private int screenY;

    // La nave del jugador
    private Ship playerShip;

    // La bala del jugador
    private Misile bala;

    // Las balas de los Invaders
    //private int nextBullet=0;
    private int maxInvaderBullets = 10;
    private Misile[] invadersBullets = new Misile[maxInvaderBullets];

    // Hasta 60 Invaders
    // Invader[] invaders = new Invader[60];
    int numInvaders = 0;

    // Las guaridas del jugador están construidas a base de ladrillos
    private Brick[] bricks = new Brick[400];
    private int numBricks;

    // La puntuación
    int score = 0;




    // Cuando inicializamos (call new()) en gameView
    // Este método especial de constructor se ejecuta
    public View(Context context, int x, int y) {

        // La siguiente línea del código le pide a
        // la clase de SurfaceView que prepare nuestro objeto.
        // !Que amable¡.
        super(context);

        // Hace una copia del "context" disponible globalmete para que la usemos en otro método
        this.context = context;

        // Inicializa los objetos de ourHolder y paint
        ourHolder = getHolder();
        paint = new Paint();

        screenX = x;
        screenY = y;

        prepareLevel();
    }

    private void prepareLevel() {

        // Aquí vamos a inicializar todos los objetos del juego

        // Haz una nave espacial para un jugador nuevo
        playerShip = new Ship(context, screenX, screenY);

        // Preparar las balas del jugador
        bala = new Misile(screenY);

        // Inicializar la formación de invadersBullets
        for (int i = 0; i <maxInvaderBullets ; i++) {
            invadersBullets[i]=new Misile(screenY);
        }
        // Construir un ejercito de invaders

        // Construir las guaridas
        numBricks = 0;
        for(int shelterNumber = 0; shelterNumber < 4; shelterNumber++){
            for(int column = 0; column < 10; column ++ ) {
                for (int row = 0; row < 5; row++) {
                    bricks[numBricks] = new Brick(row, column, shelterNumber, screenX, screenY);
                    numBricks++;
                }
            }
        }

    }

    @Override
    public void run() {

        while (playing) {

            // Captura el tiempo actual en milisegundos en startFrameTime
            long startFrameTime = System.currentTimeMillis();

            // Actualiza el cuadro
            if (!paused) {
                update();
            }

            // Dibuja el cuadro
            draw();

            // Calcula los cuadros por segundo de este cuadro
            // Ahora podemos usar los resultados para
            // medir el tiempo de animaciones y otras cosas más.
            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame;
            }

            // Vamos a hacer algo nuevo aquí hacia el final de proyecto

        }
    }

    private void update() {

        // ¿Chocó el invader contra el lado de la pantalla?
        boolean bumped = false;

        // ¿Ha perdido el jugador?
        boolean lost = false;

        // Mueve la nave espacial del jugador
        playerShip.update(fps);

        // Actualiza a los invaders si se ven


        // Actualiza a todas las balas de los invaders si están activas

        // ¿Chocó algún invader en el extremo de la pantalla?
        if (bumped) {

            // Mueve a todos los invaders hacia abajo y cambia la dirección


            // Incrementa el nivel de amenaza
            // al hacer los sonidos más frecuentes
        }

        if (lost) {
            prepareLevel();
        }

        // Actualiza las balas del jugador


        // Actualiza todas las balas de los invaders si están activas


        // Ha tocado la parte alta de la pantalla la bala del jugador


        // Ha tocado la parte baja de la pantalla la bala del invader

        // Ha tocado la bala del jugador a algún invader


        // Ha impactado una bala alienígena a un ladrillo de la guarida


        // Ha impactado una bala del jugador a un ladrillo de la guarida

        // Ha impactado una bala de un invader a la nave espacial del jugador


    }

    private void draw() {

        // Asegurate de que la superficie del dibujo sea valida o tronamos
        if (ourHolder.getSurface().isValid()) {
            // Bloquea el lienzo para que este listo para dibujar
            canvas = ourHolder.lockCanvas();

            // Dibuja el color del fondo
            canvas.drawColor(Color.argb(255, 0, 0, 0));

            // Escoje el color de la brocha para dibujar
            paint.setColor(Color.argb(255, 255, 255, 255));

            // Dibuja a la nave espacial del jugador
            canvas.drawBitmap(playerShip.getBitmap(), playerShip.getX()+50, screenY - 150, paint);
            // Dibuja a los invaders

            // Escoje el color de la brocha para dibujar
            paint.setColor(Color.argb(255, 255, 191, 0));

            // Dibuja los ladrillos si están visibles
            for(int i = 0; i < numBricks; i++){
                if(bricks[i].getVisibility()) {
                    canvas.drawRect(bricks[i].getRect(), paint);
                }
            }


            // Dibuja a las balas del jugador si están activas

            // Actualiza todas las balas de los invaders si están activas

            // Dibuja la puntuación y las vidas restantes
            // Cambia el color de la brocha
            paint.setColor(Color.argb(255, 255, 255, 255));
            paint.setTextSize(60);
            canvas.drawText("Score: " + score, 10, 50, paint);

            // Extrae todo a la pantalla
            ourHolder.unlockCanvasAndPost(canvas);
        }
    }


    // Si SpaceInvadersActivity es pausado/detenido
    // apaga nuestra secuencia.
    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }

    }

    // Si SpaceInvadersActivity es iniciado entonces
    // inicia nuestra secuencia.
    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    // La clase de SurfaceView implementa a onTouchListener
    // Así es que podemos anular este método y detectar toques a la pantalla.
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {



        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {

            // El jugador ha tocado la pantalla
            case MotionEvent.ACTION_DOWN:

                paused = false;
                if(motionEvent.getY() <= screenY/2) {
                    if ((screenX / 3 < motionEvent.getX()) && (motionEvent.getX() <= (screenX / 3) * 2)) {
                        //parte central de la pantalla
                        System.out.println("pimpam trucu trucu");

                    }
                }else {
                    //laterales de la pantalla
                    if (motionEvent.getX() <= (screenX / 3)) {
                        //se mueve a la izq
                        playerShip.setShipMoving(playerShip.LEFT);
                    } else if (((screenX / 3) * 2) < motionEvent.getX()) {
                        //se mueve a la dcha
                        playerShip.setShipMoving(playerShip.RIGHT);
                    }
                }
                break;

            // El jugador a retirado el dedo de la pantalla
            case MotionEvent.ACTION_UP:
                //se para
                playerShip.setShipMoving(playerShip.STOPPED);
                break;
        }

        return true;
    }

}