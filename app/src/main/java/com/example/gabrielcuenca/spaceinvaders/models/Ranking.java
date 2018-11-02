package com.example.gabrielcuenca.spaceinvaders.models;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

public class Ranking {

    private Map<Integer, String> ranking = new TreeMap<Integer, String>(java.util.Collections.reverseOrder());

    public Ranking() {

    }

    public Map<Integer, String> getRanking() {
        return ranking;
    }

    public void addScore(int score, String nick) {
        this.ranking.put(score, nick);
        this.save();
    }

    private void save() {
        Properties properties = new Properties();

        for (Map.Entry<Integer,String> entry : this.ranking.entrySet()) {
            properties.put(entry.getKey(), entry.getValue());
        }

        try {
            properties.store(new FileOutputStream("ranking.properties"), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("ranking.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String key : properties.stringPropertyNames()) {
            this.ranking.put(Integer.valueOf(key), properties.get(key).toString());
        }
    }
}
