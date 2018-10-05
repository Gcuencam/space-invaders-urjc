package com.example.gabrielcuenca.spaceinvaders;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    public void setActivityBackgroundColor(int color) {
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(color);
    }

    public void initGame(View view) {
        CheckBox age = findViewById(R.id.checkEdad);
        if(age.isChecked()){
            //opcion para adultos
            Intent intent = new Intent(WelcomeActivity.this, GameViewActivity.class);
            startActivity(intent);
        }else{
            //opcion para niños
        }
    }
}
