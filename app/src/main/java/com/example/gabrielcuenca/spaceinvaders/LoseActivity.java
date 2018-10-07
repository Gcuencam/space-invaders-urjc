package com.example.gabrielcuenca.spaceinvaders;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class LoseActivity extends AppCompatActivity {

    String score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lose);

        Intent intent = getIntent(); // gets the previously created intent
        this.score = intent.getStringExtra("score");

        TextView textView = (TextView) findViewById(R.id.scoreView);
        textView.setText(this.score);
    }
}
