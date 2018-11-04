package com.example.gabrielcuenca.spaceinvaders;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.gabrielcuenca.spaceinvaders.models.Ranking;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.Set;

public class RankingActivity extends AppCompatActivity {

    public Ranking ranking;
    public String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        this.userName =getIntent().getStringExtra("user");
        ranking = new Ranking(this,userName);




        final String[] valores = ranking.load();


        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_filas, valores);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                System.out.println(valores[position]);
            }
        });

        setContentView(R.layout.activity_ranking);
    }
}
