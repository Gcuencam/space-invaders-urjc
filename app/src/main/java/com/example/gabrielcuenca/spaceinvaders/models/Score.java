package com.example.gabrielcuenca.spaceinvaders.models;

import android.support.annotation.NonNull;

public class Score implements Comparable {

    private int score;
    private String name;

    public Score(int s, String n){
        this.name = n;
        this.score = s;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        if(this.score>((Score) o).score){
            return -1;
        }else if(this.score<((Score) o).score){
            return 1;
        }
        return 0;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
