package com.example.gabrielcuenca.spaceinvaders;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.gabrielcuenca.spaceinvaders.models.Ranking;
import android.view.View;
import java.lang.Process;

public class EndActivity extends AppCompatActivity {

    String score;
    String win;
    String userName;
    boolean pro;
    boolean adult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);

        Intent intent = getIntent(); // gets the previously created intent
        this.score = intent.getStringExtra("score");
        this.win= intent.getStringExtra("win");
        this.userName = intent.getStringExtra("user");

        this.pro = intent.getExtras().getBoolean("pro");
        this.adult = intent.getExtras().getBoolean("adult");

        int n = Integer.parseInt(score);

        //Creamos la referencia del boton de reinicio
        Button bRestart = (Button)findViewById(R.id.buttonRestart);

        if(win.equals("winner")){
            TextView ganar = findViewById(R.id.estadoText);
            ganar.setText("WIN");
        }
        TextView textView = (TextView) findViewById(R.id.scoreView);
        textView.setText(this.score);

        //Si la puntuacion es menor de 500 puntos no se podra reiniciar la partida
        if(n < 500){
            bRestart.setVisibility(View.GONE);
        }
    }

    public void selectRanking (View view){
        Intent intent = new Intent(EndActivity.this, RankingActivity.class);
        intent.putExtra("user",userName);
        startActivity(intent);
    }

    //Metodo para reiniciar el juego
    public void restartGame(View view) {

            if(adult){
                //si se ha jugado el modo para adultos...
                Intent intent = new Intent(EndActivity.this, GameViewActivity.class);
                intent.putExtra("userName", userName);
                intent.putExtra("proMode", pro);
                startActivity(intent);
            }else {
                //si se ha jugado el modo para niños...
                Intent intent2 = new Intent(EndActivity.this, ChildGameViewActivity.class);
                startActivity(intent2);
            }
    }

    //Método para salir del juego
    public void exitGame(View view) {

        Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);

    }
}
