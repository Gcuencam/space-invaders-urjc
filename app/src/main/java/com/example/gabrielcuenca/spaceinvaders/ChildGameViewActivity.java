package com.example.gabrielcuenca.spaceinvaders;

import android.app.Activity;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;

import com.example.gabrielcuenca.spaceinvaders.models.Chronometer;
import com.example.gabrielcuenca.spaceinvaders.models.View;

import java.util.Random;

public class ChildGameViewActivity extends Activity
{
    // spaceInvadersView será la visualización del juego
    // También tendrá la lógica del juego
    // y responderá a los toques a la pantalla
    View spaceInvadersView;
    private MediaPlayer musica = new MediaPlayer();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Obtener un objeto de Display para accesar a los detalles de la pantalla
        Display display = getWindowManager().getDefaultDisplay();
        // Cargar la resolución a un objeto de Point
        Point size = new Point();
        display.getSize(size);

        // Inicializar gameView y establecerlo como la visualización
        spaceInvadersView = new View(this, size.x, size.y, this, false,"child",false);
        setContentView(spaceInvadersView);

        //Comienza la primera cancion.
        musica = MediaPlayer.create(this, R.raw.fleeting_dream);


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
