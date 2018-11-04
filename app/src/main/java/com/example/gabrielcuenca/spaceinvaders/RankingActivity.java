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
        setContentView(R.layout.activity_ranking);

        Ranking.load();

        Set keysRanking = Ranking.getRanking().keySet();
        TextView rankingList = (TextView) findViewById(R.id.listRankingView);

        for (Iterator i = keysRanking.iterator(); i.hasNext(); ) {
            String key = i.next().toString();
            System.out.println("KEy: " + key);
            String value = Ranking.getRanking().get(Integer.parseInt(key));
            System.out.println("valor: " + value +  " key:" + key);
            String cadena = value + " : " + key;
            rankingList.setText(cadena);
        }

        setContentView(R.layout.activity_ranking);
    }
}
