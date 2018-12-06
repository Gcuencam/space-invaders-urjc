package com.example.gabrielcuenca.spaceinvaders;

import android.app.Activity;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Display;

import com.example.gabrielcuenca.spaceinvaders.models.Chronometer;
import com.example.gabrielcuenca.spaceinvaders.models.View;

import java.util.Random;


// SpaceInvadersActivity es el punto de entrada al juego.
// Se va a encargar del ciclo de vida del juego al llamar
// los métodos de spaceInvadersView cuando sean solicitados por el OS.

public class GameViewActivity extends Activity {

    // spaceInvadersView será la visualización del juego
    // También tendrá la lógica del juego
    // y responderá a los toques a la pantalla
    View spaceInvadersView;
    MediaPlayer musica = new MediaPlayer();
    Chronometer time = new Chronometer();
    Random rand= new Random();
    int numeroRand1 = rand.nextInt(2);
    int numeroRand2 = rand.nextInt(1);
    int trackN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Obtener un objeto de Display para accesar a los detalles de la pantalla
        Display display = getWindowManager().getDefaultDisplay();
        // Cargar la resolución a un objeto de Point
        Point size = new Point();
        display.getSize(size);

        // Inicializar gameView y establecerlo como la visualización
        String name = getIntent().getExtras().getString("userName");

        boolean proMode = getIntent().getExtras().getBoolean("proMode");
        spaceInvadersView = new View(this, size.x, size.y, this, true,name,proMode);
        setContentView(spaceInvadersView);

        //Comienza la primera cancion.
        if ( !musica.isPlaying()) {
            switch (numeroRand1) {
                case 0:
                    musica = MediaPlayer.create(this, R.raw.kyojin);
                    musica.start();
                    trackN = 0;
                    break;
                case 1:
                    musica = MediaPlayer.create(this, R.raw.boss_battle);
                    musica.start();
                    trackN = 1;
                    break;
                case 2:
                    musica = MediaPlayer.create(this, R.raw.real_emotion);
                    musica.start();
                    trackN = 2;
                    break;
            }
        }

        // loop para cambio de canciones. Controlamos que una cancion no pueda repetirse dos veces seguidas.

        time.start();
        if (time.getSeconds() == 20){
            musica.release();
            switch (trackN){
                case 0 :
                    switch (numeroRand2){
                        case 0 :
                            musica = MediaPlayer.create(this, R.raw.boss_battle);
                            musica.start();
                            trackN = 1;
                            break;

                        case 1 :
                            musica = MediaPlayer.create(this, R.raw.real_emotion);
                            musica.start();
                            trackN = 2;
                            break;
                    }
                    break;

                case 1 :
                    switch (numeroRand2){
                        case 0 :
                            musica = MediaPlayer.create(this, R.raw.kyojin);
                            musica.start();
                            trackN = 0;
                            break;
                        case 1 :
                            musica = MediaPlayer.create(this, R.raw.real_emotion);
                            musica.start();
                            trackN = 2;
                            break;
                    }
                case 2 :
                    switch (numeroRand2){
                        case 0 :
                            musica = MediaPlayer.create(this, R.raw.kyojin);
                            musica.start();
                            trackN = 0;
                            break;
                        case 1:
                            musica = MediaPlayer.create(this, R.raw.boss_battle);
                            musica.start();
                            trackN = 1;
                            break;
                    }
            }
        }



    }

    // Este método se ejecuta cuando el jugador empieza el juego
    @Override
    protected void onResume() {
        super.onResume();

        // Le dice al método de reanudar del gameView que se ejecute
        spaceInvadersView.resume();
        // Reanuda la cancion
        musica.start();
    }

    // Este método se ejecuta cuando el jugador se sale del juego
    @Override
    protected void onPause() {
        super.onPause();
        // Le dice al método de pausa del gameView que se ejecute
        spaceInvadersView.pause();
        // Pausa la cancion
        musica.pause();
    }
}