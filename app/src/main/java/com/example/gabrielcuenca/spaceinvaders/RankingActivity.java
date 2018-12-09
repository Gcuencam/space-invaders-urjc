package com.example.gabrielcuenca.spaceinvaders;

import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gabrielcuenca.spaceinvaders.models.Ranking;
import com.example.gabrielcuenca.spaceinvaders.utils.MapSorter;

import java.util.HashMap;
import java.util.Map;


public class RankingActivity extends AppCompatActivity {

    MediaPlayer musica = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ranking_activity);
        musica = MediaPlayer.create(this, R.raw.danza_kuduro);
        musica.start();
        try {
            HashMap<String, Integer> sortedRanking = MapSorter.sortByValues(Ranking.getRanking());
            HashMap<String, Bitmap> images = Ranking.getImages();
            int numToShow = sortedRanking.size() < 10 ? sortedRanking.size() : 10;

            int i = 0;
            for (Map.Entry<String, Integer> entry : sortedRanking.entrySet()) {
                TextView userName = (TextView) findViewById(getResources().getIdentifier("user" + i, "id", getPackageName()));
                TextView userScore = (TextView) findViewById(getResources().getIdentifier("score" + i, "id", getPackageName()));
                ImageView userImage = (ImageView) findViewById(getResources().getIdentifier("image" + i, "id", getPackageName()));

                userName.setText(entry.getKey());
                userScore.setText(Integer.toString((entry.getValue())));
                userImage.setImageBitmap(images.get(entry.getKey()));

                i++;
                if (i == numToShow) {
                    break;
                }
            }
        } catch (Exception e) {
            Log.w("myApp", e);
        }
    }
}
