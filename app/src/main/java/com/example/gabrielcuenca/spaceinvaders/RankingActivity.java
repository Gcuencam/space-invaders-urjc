package com.example.gabrielcuenca.spaceinvaders;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.gabrielcuenca.spaceinvaders.models.Ranking;

import java.util.Iterator;
import java.util.Set;

public class RankingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Ranking.load();

        Set keysRanking = Ranking.getRanking().keySet();
        TextView rankingList = (TextView) findViewById(R.id.listRankingView);

        for (Iterator i = keysRanking.iterator(); i.hasNext(); ) {
            String key = (String) i.next();
            String value = (String) Ranking.getRanking().get(key);
            rankingList.setText(key + " : " + value);
        }

        setContentView(R.layout.activity_ranking);
    }
}
