package com.example.gabrielcuenca.spaceinvaders;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.gabrielcuenca.spaceinvaders.models.Ranking;

public class WelcomeActivity extends AppCompatActivity {

    EditText nameInput;
    static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        this.nameInput = (EditText) findViewById(R.id.textInputName);
        this.context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }

    public void setActivityBackgroundColor(int color) {
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(color);
    }

    public void initGame(View view) {
        String name = this.nameInput.getText().toString();

        CheckBox age = findViewById(R.id.checkEdad);
        CheckBox pro = findViewById(R.id.checkPro);
        if (age.isChecked()) {
            //opcion para adultos
            Ranking.userName = name;
            Intent intent = new Intent(WelcomeActivity.this, GameViewActivity.class);
            intent.putExtra("userName", name);
            intent.putExtra("proMode", pro.isChecked());
            startActivity(intent);
        } else {
            //opcion para niños
            Intent intent2 = new Intent(WelcomeActivity.this, ChildGameViewActivity.class);
            startActivity(intent2);
        }
    }
}
