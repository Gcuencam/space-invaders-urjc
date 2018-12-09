package com.example.gabrielcuenca.spaceinvaders.models;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.gabrielcuenca.spaceinvaders.EndActivity;
import com.example.gabrielcuenca.spaceinvaders.R;

import java.util.Random;

public class View extends SurfaceView implements Runnable {

    Context context;

    //Cronómetro
    private Chronometer time = new Chronometer();
    // Esta es nuestra secuencia
    private Thread gameThread = null;

    //Botón disparar
    Bitmap buttonShoot;
    Bitmap buttonShoot2;

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

    // Las balas del jugador
    private int nextMisileShip = 0;
    private int maxShipMisiles = 7;
    private Misile[] shipMisiles = new Misile[maxShipMisiles];


    private int maxInvaders = 60;

    // Las balas de los Invaders
    private int nextMisileInvader = 0;
    private int maxInvaderMisile = 7;
    private Misile[] invadersMisiles = new Misile[maxInvaderMisile];
    private Misile[] misilInvaderExtra = new Misile[maxInvaderMisile];


    // Hasta 60 Invaders
    Invader[] invaders = new Invader[maxInvaders];
    int numInvaders = 0;

    //Invader cada 10s y sus posicion inicial
    private Invader invaderExtra;
    private int xInicialEx = 0;
    private int yInicialEx = 0;

    // Las guaridas del jugador están construidas a base de ladrillos
    private Brick[] bricks = new Brick[400];
    private int numBricks;

    // La puntuación
    int score = 0;
    private final int VALUE_OF_INVADER = 100;
    private final int VALUE_OF_INVADER_EXTRA = 500;
    private int numInvadersVisibles;

    Activity gameActivity;

    Boolean adult;
    private boolean pro;

    private boolean win = true;
    public int getNumInvaders() {
        return this.numInvaders;
    }


    // Cuando inicializamos (call new()) en gameView
    // Este método especial de constructor se ejecuta
    public View(Context context, int x, int y, Activity gameActivity, Boolean adult, String username, boolean pro) {

        // La siguiente línea del código le pide a
        // la clase de SurfaceView que prepare nuestro objeto.
        // !Que amable¡.
        super(context);

        win = false;

        this.pro = pro;

        // Hace una copia del "context" disponible globalmete para que la usemos en otro método
        this.context = context;

        // Inicializa los objetos de ourHolder y paint
        ourHolder = getHolder();
        paint = new Paint();

        screenX = x;
        screenY = y;

        this.gameActivity = gameActivity;

        this.adult = adult;

        prepareLevel();
    }

    private void prepareLevel() {


        if (adult == true) {
            buttonShoot = BitmapFactory.decodeResource(context.getResources(), R.drawable.shoot_icon);
            buttonShoot = Bitmap.createScaledBitmap(buttonShoot,
                    (int) (screenX / 15),
                    (int) (screenY / 10),
                    false);
            buttonShoot2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.shoot_icon);
            buttonShoot2 = Bitmap.createScaledBitmap(buttonShoot,
                    (int) (screenX / 15),
                    (int) (screenY / 10),
                    false);
        }

        // Haz una nave espacial para un jugador nuevo
        playerShip = new Ship(context, screenX, screenY);

        // Preparar las balas del jugador
        for (int i = 0; i < maxShipMisiles; i++) {
            shipMisiles[i] = new Misile(screenY);
        }


        //Preparar el cronómetro
        time = new Chronometer();


        // Construir un ejercito de invaders
        numInvaders = 0;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 3; j++) {
                invaders[numInvaders] = new Invader(context, j, i, screenY, screenX);
                numInvaders++;
            }
        }
        numInvadersVisibles = numInvaders;


        invaderExtra = new Invader(context, xInicialEx, yInicialEx, screenX, screenY, true);
        invaderExtra.makeInvisible();


        // Inicializar la formación de invadersMisiles
        for (int i = 0; i < maxInvaderMisile; i++) {
            invadersMisiles[i] = new Misile(screenY);
        }

        // Construir las guaridas
        numBricks = 0;
        for (int shelterNumber = 0; shelterNumber < 4; shelterNumber++) {
            for (int column = 0; column < 10; column++) {
                for (int row = 0; row < 5; row++) {
                    bricks[numBricks] = new Brick(row, column, shelterNumber, screenX, screenY);
                    numBricks++;
                }
            }
        }
        time.start();
    }

    @Override
    public void run() {
        while (playing) {

            // Captura el tiempo actual en milisegundos en startFrameTime
            long startFrameTime = System.currentTimeMillis();

            // Actualiza el cuadro
            if (!paused) {
                update();
                this.time.run();
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
            time = new Chronometer();

            //Comienza la primera cancion.

        }

    }


    private void update() {

        // ¿Chocó el invader contra el lado de la pantalla?
        boolean bumped = false;

        // ¿Ha perdido el jugador?
        boolean lost = false;

        //¿Quedan invaders?
        if (numInvadersVisibles == 0) {
            win = true;
            finDePartida();
        }

        /** **** UPDATE DEL MOVIMIENTO DEL JUGADOR ****** **/
        playerShip.update(fps);

        /** ***** COLISIONES INVADER EXTRA/BARRERA CON EL JUGADOR ******* **/
        if (invaderExtra.isVisible() && RectF.intersects(playerShip.getRect(), invaderExtra.getRectf())) {
            finDePartida();
        }
        for (int i = 0; i < numBricks; i++) {
            if (bricks[i].getVisibility() && RectF.intersects(playerShip.getRect(), bricks[i].getRect())) {
                finDePartida();
            }
        }

        /** ***** UPDATE DEL MOVIMIENTO DE LOS INVADERS ****** **/
        for (int i = 0; i < numInvaders; i++) {
            if (invaders[i].isVisible()) {
                invaders[i].update(fps, screenX);
            }
            if (invaders[i].shoot() && this.adult && invaders[i].isVisible()) {
                //Si la bala 'invadersMisiles[nextMisileInvader]' no ha sido disparada se dispara
                if (invadersMisiles[nextMisileInvader].shoot(invaders[i].getXleft() + invaders[i].getWidth(),
                        invaders[i].getY() + invaders[i].getHeight(), Misile.DOWN)) {

                    if (nextMisileInvader == maxInvaderMisile - 1) {
                        nextMisileInvader = 0;
                    } else {
                        nextMisileInvader++;
                    }
                }

            }
            //Choca con el limite de la pantalla
            if (invaders[i].getXleft() > screenX - invaders[i].getWidth()
                    || invaders[i].getXleft() < 0) {

                bumped = true;
            }
        }
        //Si choca con el limite de la pantalla se baja el ejercito de invaders
        if (bumped) {
            for (int i = 0; i < numInvaders; i++) {
                invaders[i].automaticMove();
                if (invaders[i].isVisible() && RectF.intersects(invaders[i].getRectf(), playerShip.getRect())) {
                    finDePartida();
                }
            }
        }


        /** UPDATE DEL MOVIMIENTO DEL INVADER EXTRA **/
        invaderExtra.update(fps, screenX);
        if (invaderExtra.getXleft() >= screenX) { //ha llegado al final de la pantalla
            invaderExtra = new Invader(context, xInicialEx, yInicialEx, screenX, screenY, true);
            invaderExtra.makeInvisible();
        }

        if (time.getSeconds() == 10) {
            //Que aparezca un nuevo marciano
            invaderExtra.makeVisible();
            time.reset();
        }



        /** UPDATE DE LAS BALAS MOVIMIENTO & COLISIONES **/
        updateMoveMisiles();
        updateColisionesMisiles();


        //Ha impactado un marciano con un bloque o con el jugador
        int numBloque = -1;
        for (int i = 0; i < numInvaders; i++) {
            if (invaders[i].isVisible()) {
                for (int j = 0; j < numBricks; j++) {
                    if (bricks[j].getVisibility() && RectF.intersects(invaders[i].getRectf(), bricks[j].getRect())) {
                        this.bricks[j].setInvisible();
                        numBloque = this.bricks[j].getNumShelter();
                    }
                    if (bricks[j].getNumShelter() == numBloque) {
                        this.bricks[j].setInvisible();
                    }
                }
                if (RectF.intersects(playerShip.getRect(), invaders[i].getRectf())) {
                    finDePartida();
                }
            }
        }

        //Tiempo aleatorio
        Random r = new Random();
        int t = r.nextInt(350);


        //Desaparecer nave
        if (t==2){
            System.out.println(t);
            playerShip.desaparecer();
            //Aparecer nave
            playerShip.aparecer(screenX);
        }
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
            canvas.drawBitmap(playerShip.getBitmap(), playerShip.getX() + 50, playerShip.getY() - playerShip.getHeight(), paint);
            // Dibuja a los invaders


            // Escoje el color de la brocha para dibujar
            paint.setColor(Color.argb(255, 255, 191, 0));


            for (int i = 0; i < numInvaders; i++) {
                if (invaders[i].isVisible()) {
                    canvas.drawBitmap(invaders[i].getBitmap(), invaders[i].getXleft(), invaders[i].getY(), paint);
                }
            }
            if (invaderExtra.isVisible()) {
                canvas.drawBitmap(invaderExtra.getBitmap(), invaderExtra.getXleft(), invaderExtra.getY(), paint);
            }
            // Dibuja los ladrillos si están visibles
            for (int i = 0; i < numBricks; i++) {
                if (bricks[i].getVisibility()) {
                    canvas.drawRect(bricks[i].getRect(), paint);
                }
            }
            if (adult) {
                canvas.drawBitmap(buttonShoot, 0, (screenY / 2) - 100, paint);
                canvas.drawBitmap(buttonShoot2, screenX - screenX / 15, (screenY / 2) - 100, paint);
            }


            // Dibuja a las balas del jugador si están activas

            for (int i = 0; i < maxShipMisiles; i++) {
                if (shipMisiles[i].isActivated()) {
                    RectF rect = shipMisiles[i].getRectf();
                    rect.right -= 13;
                    rect.left -= 13;
                    canvas.drawRect(rect, paint);
                }
            }

            // Actualiza todas las balas de los invaders si están activas
            for (int i = 0; i < maxInvaderMisile; i++) {
                if (invadersMisiles[i].isActivated()) {
                    canvas.drawRect(invadersMisiles[i].getRectf(), paint);
                }
            }

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
                if (motionEvent.getY() <= screenY / 2 && (motionEvent.getX() <= screenX / 15 || motionEvent.getX() >= screenX - screenX / 15) &&
                        motionEvent.getY() >= screenY / 2 - screenY / 10) {
                    if (adult) {

                        if (shipMisiles[nextMisileShip].shoot(playerShip.getX() + playerShip.getLength(),
                                playerShip.getY() - playerShip.getHeight() / 2, Misile.UP)) {

                            if (nextMisileShip == maxShipMisiles - 1) {
                                nextMisileShip = 0;
                            } else {
                                nextMisileShip++;
                            }
                        }
                    }
                } else if (motionEvent.getY() >= screenY / 2) {
                    //laterales de la pantalla
                    if (motionEvent.getX() <= (screenX / 3)) {
                        //se mueve a la izq
                        playerShip.setShipMoving(playerShip.LEFT);
                    } else if (((screenX / 3) * 2) < motionEvent.getX()) {
                        //se mueve a la dcha
                        playerShip.setShipMoving(playerShip.RIGHT);
                    } else if (((screenX / 3) < motionEvent.getX()) && (motionEvent.getX() <= ((screenX / 3) * 2))) {
                        if (motionEvent.getY() <= (screenY * 3 / 4)) {
                            //se mueve hacia arriba
                            playerShip.setShipMoving(playerShip.UP);
                        } else if (motionEvent.getY() > (screenY * 3 / 4)) {
                            //se mueve hacia abajo
                            playerShip.setShipMoving(playerShip.DOWN);
                        }
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

    private void finDePartida() {
        paused = true;
        Intent intent = new Intent(this.gameActivity, EndActivity.class);
        intent.putExtra("score", Integer.toString(this.score));
        intent.putExtra("pro", pro);
        intent.putExtra("adult", adult);
        String winner = "loser";
        if (win) {
            winner = "winner";
        }
        intent.putExtra("win", winner);
        //intent.putExtra("user", ranking.userName);
        //ranking.save(score);
        this.gameActivity.startActivity(intent);

    }

    private void changeColour(int impactos) {
        if(impactos==1) {
            playerShip.setImagen(context);
            for (int i = 0; i < numInvaders; i++) {
                invaders[i].setImagen(context);
            }
            invaderExtra.setImagen(context);
        }else if(impactos>1){
            playerShip.serImagenAleatoria(context);
            for (int i = 0; i < numInvaders; i++) {
                invaders[i].setImagenAleatoria(context);
            }
            invaderExtra.setImagenAleatoria(context);
        }
    }



    private void updateMoveMisiles(){
        /** ****** UPDATE BALAS DEL JUGADOR ****** **/
        for (int i = 0; i < maxShipMisiles; i++) {
            if (shipMisiles[i].isActivated()) {
                shipMisiles[i].update(fps);
                if (shipMisiles[i].extrem() >= screenY || shipMisiles[i].extrem() <= 0) {
                    if (pro) {
                        shipMisiles[i].cambiarADireccionOpuesta();
                    } else {
                        shipMisiles[i].desactivar();
                    }
                }
            }
        }

        /** ***** UPDATE BALAS DE LOS INVADERS **** **/
        for (int i = 0; i < maxInvaderMisile; i++) {
            if (invadersMisiles[i].isActivated()) {
                invadersMisiles[i].update(fps);
                if (invadersMisiles[i].extrem() >= screenY || invadersMisiles[i].extrem() <= 0) {
                    if (pro) {
                        invadersMisiles[i].cambiarADireccionOpuesta();
                    } else {
                        invadersMisiles[i].desactivar();
                    }
                }
            }
        }
    }

    private void updateColisionesMisiles(){
        /** *********COLISIONES DE LAS BALAS DEL JUGADOR********** **/

        int contadorImpactos = 0; //Cuenta los impactos para saber que tipo de cambio de color debe realizar

        for (int i = 0; i < maxShipMisiles; i++) {
            //Colision con la nave del jugador
            if (shipMisiles[i].isActivated() && pro) {
                if (RectF.intersects(shipMisiles[i].getRectf(), playerShip.getRect())) {
                    shipMisiles[i].desactivar();
                    finDePartida();
                }
            }
            //Colision con algún invader
            if (shipMisiles[i].isActivated()) {
                for (int j = 0; j < numInvaders; j++) {
                    if (invaders[j].isVisible() && RectF.intersects(invaders[j].getRectf(), shipMisiles[i].getRectf())) {
                        invaders[j].makeInvisible();
                        numInvadersVisibles--;
                        shipMisiles[i].desactivar();
                        score = score + VALUE_OF_INVADER;
                    }
                }
            }
            //Colision con el invader extra
            if (shipMisiles[i].isActivated() &&
                    invaderExtra.isVisible() && RectF.intersects(invaderExtra.getRectf(), shipMisiles[i].getRectf())) {
                invaderExtra = new Invader(context, xInicialEx, yInicialEx, screenX, screenY, true);
                invaderExtra.makeInvisible();
                shipMisiles[i].desactivar();
                score = score + VALUE_OF_INVADER_EXTRA;
            }
            //Colision con la barrera
            if (shipMisiles[i].isActivated()) {

                for (int j = 0; j < numBricks; j++) {
                    if (bricks[j].getVisibility() && RectF.intersects(bricks[j].getRect(), shipMisiles[i].getRectf())) {
                        contadorImpactos++;
                        shipMisiles[i].desactivar();
                    }
                }
            }
        }

        /** **********COLISIONES DE LAS BALAS DE LOS INVADERS*********** **/
        for (int i = 0; i < maxInvaderMisile; i++) {
            if (invadersMisiles[i].isActivated()) {
                //Colision con la nave del jugador
                if (invadersMisiles[i].isActivated() && RectF.intersects(invadersMisiles[i].getRectf(), playerShip.getRect())) {
                    finDePartida();
                }
            }
            //Colision con la con algún invader
            if (invadersMisiles[i].isActivated() && pro) {
                for (int j = 0; j < numInvaders; j++) {
                    if (invaders[j].isVisible() && RectF.intersects(invaders[j].getRectf(), invadersMisiles[i].getRectf())) {
                        if (invadersMisiles[i].direccion == Misile.UP) {
                            invaders[j].makeInvisible();
                            numInvadersVisibles--;
                            invadersMisiles[i].desactivar();
                            score = score + VALUE_OF_INVADER;
                        }
                    }
                }
            }
            //Colision con el invader Extra
            if (invadersMisiles[i].isActivated() &&
                    invaderExtra.isVisible() && RectF.intersects(invaderExtra.getRectf(), invadersMisiles[i].getRectf())) {
                invaderExtra = new Invader(context, xInicialEx, yInicialEx, screenX, screenY, true);
                invaderExtra.makeInvisible();
                invadersMisiles[i].desactivar();
                score = score + VALUE_OF_INVADER_EXTRA;
            }
            //Colision con la barrera
            if (invadersMisiles[i].isActivated()) {
                for (int j = 0; j < numBricks; j++) {
                    if (RectF.intersects(bricks[j].getRect(), invadersMisiles[i].getRectf()) && bricks[j].getVisibility()) {
                        contadorImpactos++;
                        bricks[j].setInvisible();
                        invadersMisiles[i].desactivar();

                    }
                }
            }
        }

        /** Cambio de color si es necesario **/
        changeColour(contadorImpactos);
    }


}