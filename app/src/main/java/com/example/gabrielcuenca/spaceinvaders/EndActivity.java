package com.example.gabrielcuenca.spaceinvaders;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.gabrielcuenca.spaceinvaders.models.Ranking;
import android.view.View;

public class EndActivity extends AppCompatActivity {

    String score;
    String win;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);

        Intent intent = getIntent(); // gets the previously created intent
        this.score = intent.getStringExtra("score");
        this.win= intent.getStringExtra("win");

        int n=Integer.parseInt(score);

        if(win.equals("winner")){
            TextView ganar = findViewById(R.id.estadoText);
            ganar.setText("WIN");
        }
        TextView textView = (TextView) findViewById(R.id.scoreView);
        textView.setText(this.score);
    }

    public void selectRanking (View view){
        Intent intent = new Intent(EndActivity.this, RankingActivity.class);
        startActivity(intent);
    }

}
