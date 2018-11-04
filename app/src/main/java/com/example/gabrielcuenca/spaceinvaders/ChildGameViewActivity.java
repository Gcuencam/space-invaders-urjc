package com.example.gabrielcuenca.spaceinvaders;

import android.app.Activity;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;

import com.example.gabrielcuenca.spaceinvaders.models.View;

public class ChildGameViewActivity extends Activity
{
    // spaceInvadersView será la visualización del juego
    // También tendrá la lógica del juego
    // y responderá a los toques a la pantalla
    View spaceInvadersView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Obtener un objeto de Display para accesar a los detalles de la pantalla
        Display display = getWindowManager().getDefaultDisplay();
        // Cargar la resolución a un objeto de Point
        Point size = new Point();
        display.getSize(size);

        // Inicializar gameView y establecerlo como la visualización
        spaceInvadersView = new View(this, size.x, size.y, this, false,"child");
        setContentView(spaceInvadersView);

    }

    // Este método se ejecuta cuando el jugador empieza el juego
    @Override
    protected void onResume() {
        super.onResume();

        // Le dice al método de reanudar del gameView que se ejecute
        spaceInvadersView.resume();
    }

    // Este método se ejecuta cuando el jugador se sale del juego
    @Override
    protected void onPause() {
        super.onPause();

        // Le dice al método de pausa del gameView que se ejecute
        spaceInvadersView.pause();
    }
}
