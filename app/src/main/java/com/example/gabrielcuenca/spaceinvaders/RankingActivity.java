package com.example.gabrielcuenca.spaceinvaders;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.gabrielcuenca.spaceinvaders.models.Ranking;

public class RankingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ranking_activity);
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_filas, Ranking.toArray());
        ListView listView = (ListView) findViewById(R.id.Dinamismo);
        listView.setAdapter(adapter);
    }
}
