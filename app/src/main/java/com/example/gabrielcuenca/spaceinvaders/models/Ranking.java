package com.example.gabrielcuenca.spaceinvaders.models;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

public final class Ranking {

    private static Map<Integer, String> ranking = new TreeMap<Integer, String>(java.util.Collections.reverseOrder());
    public static String userName;

    public Ranking() {

    }

    public static Map<Integer, String> getRanking() {
        return ranking;
    }

    public void addScore(int score) {
        this.ranking.put(score, Ranking.userName);
        this.save();
    }

    private static void save() {
        Properties properties = new Properties();

        for (Map.Entry<Integer,String> entry : ranking.entrySet()) {
            properties.put(entry.getKey(), entry.getValue());
        }

        try {
            properties.store(new FileOutputStream("ranking.properties"), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void load() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("ranking.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String key : properties.stringPropertyNames()) {
            ranking.put(Integer.valueOf(key), properties.get(key).toString());
        }
    }
}
