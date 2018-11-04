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
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.gabrielcuenca.spaceinvaders.EndActivity;
import com.example.gabrielcuenca.spaceinvaders.R;

public class View extends SurfaceView implements Runnable {

    Context context;

    //Cronómetro
    private Chronometer time = new Chronometer();

    //Ranking
    public Ranking ranking;

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
    private int nextMisilJugador =0;
    private int maxJugadorMisiles = 7;
    private Misile[] jugadorMisiles = new Misile[maxJugadorMisiles];


    private int maxInvaders=60;

    // Las balas de los Invaders
    private int nextMisile =0;
    private int maxInvaderMisile = 10;
    private Misile[] invadersMisiles = new Misile[maxInvaderMisile];
    private Misile[] misilInvaderExtra = new Misile[maxInvaderMisile];


    // Hasta 60 Invaders
    Invader[] invaders = new Invader[maxInvaders];
    int numInvaders = 0;

    //Invader cada 10s y sus posicion inicial
    private Invader invaderExtra;
    private int xInicialEx=0;
    private int yInicialEx=0;

    // Las guaridas del jugador están construidas a base de ladrillos
    private Brick[] bricks = new Brick[400];
    private int numBricks;

    // La puntuación
    int score = 0;
    private final int VALUE_OF_INVADER = 100;
    private final int VALUE_OF_INVADER_EXTRA = 500;

    Activity gameActivity;

    Boolean adult;

    private boolean win=true;

    public int getNumInvaders(){
        return this.numInvaders;
    }



    // Cuando inicializamos (call new()) en gameView
    // Este método especial de constructor se ejecuta
    public View(Context context, int x, int y, Activity gameActivity, Boolean adult, String username) {

        // La siguiente línea del código le pide a
        // la clase de SurfaceView que prepare nuestro objeto.
        // !Que amable¡.
        super(context);

        win=false;


        ranking = new Ranking(context,username);

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



        if(adult==true){
            buttonShoot = BitmapFactory.decodeResource(context.getResources(), R.drawable.shoot_icon);
            buttonShoot = Bitmap.createScaledBitmap(buttonShoot,
                    (int) (screenX/15),
                    (int) (screenY/10),
                    false);
            buttonShoot2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.shoot_icon);
            buttonShoot2 = Bitmap.createScaledBitmap(buttonShoot,
                    (int) (screenX/15),
                    (int) (screenY/10),
                    false);
        }

        // Haz una nave espacial para un jugador nuevo
        playerShip = new Ship(context, screenX, screenY);

        // Preparar las balas del jugador
        for (int i = 0; i < maxJugadorMisiles; i++) {
            jugadorMisiles[i]=new Misile(screenY);
        }


        //Preparar el cronómetro
        time=new Chronometer();


        // Construir un ejercito de invaders
        numInvaders = 0;
        for (int i = 0; i < 6 ; i++) {
            for (int j = 0; j < 3 ; j++) {
                invaders[numInvaders] = new Invader(context,j,i,screenY,screenX);
                numInvaders++;
            }
        }


        invaderExtra=new Invader(context,xInicialEx,yInicialEx,screenX,screenY,true);
        invaderExtra.makeInvisible();


        // Inicializar la formación de invadersMisiles
        for (int i = 0; i < maxInvaderMisile; i++) {
            invadersMisiles[i]=new Misile(screenY);
        }

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
        for (int i = 0; i <numInvaders ; i++) {
            if(invaders[i].isVisible()){
                invaders[i].update(fps,screenX);
                if(invaders[i].getXleft()==0 || invaders[i].getXleft() + invaders[i].getHeight()==screenX){
                    bumped=true;
                }
            }
            if(invaders[i].shoot() && this.adult){
                //Si la bala 'invadersMisiles[nextMisile]' no ha sido disparada se dispara
                if(invadersMisiles[nextMisile].shoot(invaders[i].getXleft()*2,
                        invaders[i].getY() + invaders[i].getHeight()*3,Misile.DOWN)){

                    if(nextMisile == maxInvaderMisile-1){
                        nextMisile=0;
                    }else{
                        nextMisile++;
                    }
                }


            }

            //Colisión
            if (invaders[i].getXleft() > screenX - invaders[i].getWidth()
                    || invaders[i].getXleft() < 0) {

                bumped = true;
            }

        }
        /**INVADER EXTRA UPDATE**/
        invaderExtra.update(fps,screenX);
        if(invaderExtra.getXleft()>=screenX){
            invaderExtra=new Invader(context,xInicialEx,yInicialEx,screenX,screenY,true);
            invaderExtra.makeInvisible();
        }

        if(time.getSegundos()==10){
            //Que aparezca un nuevo marciano
            invaderExtra.makeVisible();
            time.reset();
        }

        if(lost){
            pause();
        }

        // Actualiza a todas las balas del jugador si están activas
        for(int i=0; i < maxJugadorMisiles; i++){
            if(jugadorMisiles[i].isActivated()){
            jugadorMisiles[i].update(fps);
            }
        }

        int countInvaders=0;

        for (int i = 0; i <numInvaders ; i++) {
            if(invaders[i].isVisible()){
                countInvaders++;
                if(invaders[i].getY() >= screenY - playerShip.getLength()){
                    finDePartida();
                }
            }
        }

        if(countInvaders==0){
            win=true;
            finDePartida();
        }

        // Actualiza a todas las balas de los invaders si están activas
        for(int i = 0; i < maxInvaderMisile; i++){
            if(invadersMisiles[i].isActivated()) {
                invadersMisiles[i].update(fps);
            }
        }


        // ¿Chocó algún invader en el extremo de la pantalla?
        if (bumped) {
            for (int i = 0; i <numInvaders ; i++) {
                invaders[i].automaticMove();
                if(invaders[i].getY() > playerShip.getY()){
                    lost = true;
                }
            }
        }


        //Las balas tocan el tope superior de la pantalla
        for(int i = 0; i < maxJugadorMisiles; i++) {
            if (jugadorMisiles[i].isActivated() && jugadorMisiles[i].extrem() <= 0) {
                //bala.desactivar();
                jugadorMisiles[i].cambiarADireccionOpuesta();
            }
        }
        for (int i = 0; i <maxInvaderMisile ; i++) {
            if(invadersMisiles[i].extrem() <= 0 && invadersMisiles[i].isActivated()){
                //invadersMisiles[i].desactivar();
                invadersMisiles[i].cambiarADireccionOpuesta();
            }
        }

        //Las balas tocan el limite de abajo
        for(int i = 0; i < maxJugadorMisiles; i++) {
            if (jugadorMisiles[i].isActivated() && jugadorMisiles[i].extrem() >= screenY) {
                //bala.desactivar();
                jugadorMisiles[i].cambiarADireccionOpuesta();
            }
        }
        for (int i = 0; i <maxInvaderMisile ; i++) {
            if(invadersMisiles[i].extrem()>= screenY && invadersMisiles[i].isActivated()){
                //invadersMisiles[i].desactivar();
                invadersMisiles[i].cambiarADireccionOpuesta();
            }
        }

        // Ha tocado alguna bala a algún invader
        for (int i = 0; i < maxJugadorMisiles ; i++) {
            if (jugadorMisiles[i].isActivated()) {
                for (int j = 0; j < numInvaders; j++) {
                    if (invaders[j].isVisible() && RectF.intersects(invaders[j].getRectf(), jugadorMisiles[i].getRectf())) {
                        invaders[j].makeInvisible();
                        jugadorMisiles[i].desactivar();
                        score = score + VALUE_OF_INVADER;
                    }
                }
            }
        }
        for (int i = 0; i < maxInvaderMisile ; i++) {
            for (int j = 0; j < numInvaders; j++) {
                if (invaders[j].isVisible() && RectF.intersects(invaders[j].getRectf(), invadersMisiles[i].getRectf())) {
                    if(invadersMisiles[i].direccion==Misile.UP){

                        invaders[j].makeInvisible();
                        invadersMisiles[i].desactivar();
                        score = score + VALUE_OF_INVADER;
                    }
                }
            }
        }

        //Ha tocado alguna bala al invader extra
        for (int i = 0; i < maxJugadorMisiles ; i++) {
            if (jugadorMisiles[i].isActivated()) {
                if (invaderExtra.isVisible() && RectF.intersects(invaderExtra.getRectf(), jugadorMisiles[i].getRectf())) {
                    invaderExtra = new Invader(context, xInicialEx, yInicialEx, screenX, screenY, true);
                    invaderExtra.makeInvisible();
                    jugadorMisiles[i].desactivar();
                    score = score + VALUE_OF_INVADER_EXTRA;
                }
            }
        }
        for (int i = 0; i < maxInvaderMisile ; i++) {
            if (invadersMisiles[i].isActivated()) {
                if (invaderExtra.isVisible() && RectF.intersects(invaderExtra.getRectf(), invadersMisiles[i].getRectf())) {
                    invaderExtra = new Invader(context, xInicialEx, yInicialEx, screenX, screenY, true);
                    invaderExtra.makeInvisible();
                    invadersMisiles[i].desactivar();
                    score = score + VALUE_OF_INVADER_EXTRA;
                }
            }
        }


        // Ha impactado una bala alienígena a un ladrillo de la guarida

        int contadorImpactos=0;
        for (int i = 0; i <maxInvaderMisile ; i++) {
            if(invadersMisiles[i].isActivated()){
                for (int j = 0; j <numBricks ; j++) {
                    if(RectF.intersects(bricks[j].getRect(),invadersMisiles[i].getRectf()) && bricks[j].getVisibility()){
                        contadorImpactos++;
                        bricks[j].setInvisible();
                        invadersMisiles[i].desactivar();
                    }
                }
            }
        }


        // Ha impactado una bala del jugador a un ladrillo de la guarida
        for (int i = 0; i < maxJugadorMisiles ; i++) {
            if (jugadorMisiles[i].isActivated()) {
                for (int j = 0; j < numBricks; j++) {
                    if (bricks[j].getVisibility() && RectF.intersects(bricks[j].getRect(), jugadorMisiles[i].getRectf())) {
                        contadorImpactos++;
                        jugadorMisiles[i].desactivar();
                    }
                }
            }
        }

        if(contadorImpactos==1){
            cambioColor();
        }else if(contadorImpactos>1){
            cambioColorAutomatico();
        }



        // Ha impactado una bala a la nave espacial del jugador

        for (int i = 0; i < maxInvaderMisile ; i++) {
            if(invadersMisiles[i].isActivated() && RectF.intersects(invadersMisiles[i].getRectf(), playerShip.getRect())){
                finDePartida();

            }
        }
        for (int i = 0; i < maxJugadorMisiles ; i++) {
            if(jugadorMisiles[i].isActivated() && RectF.intersects(jugadorMisiles[i].getRectf(), playerShip.getRect())){
                finDePartida();
            }
        }
        for(int i = 0; i<numInvaders;i++) {
            if (invaders[i].isVisible()) {

            }
        }
        //Ha impactado un marciano con un bloque o con el jugador
        int numBloque=-1;
        for (int i = 0; i <numInvaders ; i++) {
            if(invaders[i].isVisible()){
                for (int j = 0; j <numBricks ; j++) {
                    if(bricks[j].getVisibility() && RectF.intersects(invaders[i].getRectf(),bricks[j].getRect())){
                        this.bricks[j].setInvisible();
                        numBloque=this.bricks[j].getNumShelter();
                    }
                    if(bricks[j].getNumShelter()==numBloque){
                        this.bricks[j].setInvisible();
                    }
                }
                if (RectF.intersects(playerShip.getRect(),invaders[i].getRectf())) {
                    finDePartida();
                }
            }
        }
        //Ha impactado la nave con la barrera
        for (int i = 0; i<numBricks; i++){
            if(bricks[i].getVisibility() && RectF.intersects(playerShip.getRect(),bricks[i].getRect())){
                finDePartida();
            }
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
            canvas.drawBitmap(playerShip.getBitmap(), playerShip.getX()+50, playerShip.getY() - playerShip.getHeight(), paint);
            // Dibuja a los invaders


            // Escoje el color de la brocha para dibujar
            paint.setColor(Color.argb(255, 255, 191, 0));


            for(int i = 0; i < numInvaders; i++){
                if(invaders[i].isVisible()) {
                    canvas.drawBitmap(invaders[i].getBitmap(), invaders[i].getXleft(), invaders[i].getY(), paint);
                }
            }
            if(invaderExtra.isVisible()) {
                canvas.drawBitmap(invaderExtra.getBitmap(), invaderExtra.getXleft(), invaderExtra.getY(), paint);
            }
            // Dibuja los ladrillos si están visibles
            for(int i = 0; i < numBricks; i++){
                if(bricks[i].getVisibility()) {
                    canvas.drawRect(bricks[i].getRect(), paint);
                }
            }
            if(adult){
                canvas.drawBitmap(buttonShoot,0,(screenY/2)-100 , paint);
                canvas.drawBitmap(buttonShoot2,screenX - screenX/15,(screenY/2)- 100, paint);
            }


            // Dibuja a las balas del jugador si están activas

            for (int i=0; i < maxJugadorMisiles; i++ ) {
                if (jugadorMisiles[i].isActivated()) {
                    RectF rect = jugadorMisiles[i].getRectf();
                    rect.right -= 13;
                    rect.left -= 13;
                    canvas.drawRect(rect, paint);
                }
            }

            // Actualiza todas las balas de los invaders si están activas
            for (int i = 0; i <maxInvaderMisile ; i++) {
                if(invadersMisiles[i].isActivated()){
                    canvas.drawRect(invadersMisiles[i].getRectf(),paint);
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
                if(motionEvent.getY()<=screenY/2 && (motionEvent.getX()<=screenX/15 || motionEvent.getX()>=screenX-screenX/15) &&
                        motionEvent.getY()>=screenY/2 - screenY/10 ) {
                    System.out.println("pimpam tructrucu");
                    if(adult){

                        if(jugadorMisiles[nextMisilJugador].shoot(playerShip.getX()+playerShip.getLength(),
                                playerShip.getY()-playerShip.getHeight()/2,Misile.UP)){

                            if(nextMisilJugador == maxJugadorMisiles-1){
                                nextMisilJugador=0;
                            }else{
                                nextMisilJugador++;
                            }
                        }
                    }
                }else if(motionEvent.getY()>=screenY/2){
                    //laterales de la pantalla
                    if (motionEvent.getX() <= (screenX / 3)) {
                        //se mueve a la izq
                        playerShip.setShipMoving(playerShip.LEFT);
                    } else if (((screenX / 3) * 2) < motionEvent.getX()) {
                        //se mueve a la dcha
                        playerShip.setShipMoving(playerShip.RIGHT);
                    } else if (((screenX/3)<motionEvent.getX())&& (motionEvent.getX()<=((screenX/3)*2))){
                        if (motionEvent.getY()<= (screenY*3/4)){
                            //se mueve hacia arriba
                            playerShip.setShipMoving(playerShip.UP);
                        }else if (motionEvent.getY()>(screenY*3/4)){
                            System.out.println("wsedrft mhbgyfv vtygvbhurtfyuh");
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

    private void finDePartida(){
        paused=true;
        Intent intent = new Intent(this.gameActivity, EndActivity.class);
        intent.putExtra("score",  Integer.toString(this.score));
        String winner="loser";
        if(win){
            winner="winner";
        }
        intent.putExtra("win",winner );
        intent.putExtra("user", ranking.userName);
        ranking.save(score);
        this.gameActivity.startActivity(intent);
    }

    private void cambioColor(){
        playerShip.setImagen(context);
        for (int i = 0; i <numInvaders; i++) {
            invaders[i].setImagen(context);
        }
        invaderExtra.setImagen(context);
    }

    private void cambioColorAutomatico(){
        playerShip.serImagenAleatoria(context);
        for (int i = 0; i <numInvaders ; i++) {
            invaders[i].setImagenAleatoria(context);
        }
        invaderExtra.setImagenAleatoria(context);
    }


}