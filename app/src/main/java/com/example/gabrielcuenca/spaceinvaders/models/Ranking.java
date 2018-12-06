package com.example.gabrielcuenca.spaceinvaders.models;

import android.content.Context;
import android.util.Log;

import com.example.gabrielcuenca.spaceinvaders.WelcomeActivity;
import com.example.gabrielcuenca.spaceinvaders.utils.MapSorter;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public final class Ranking {

    private static HashMap<String, Integer> ranking = new HashMap<String, Integer>();
    public static String userName;
    private static Context context;


    public static void setup() {
        context = WelcomeActivity.getContext();
        load();
    }

    public static Map<String, Integer> getRanking() {
        return ranking;
    }

    public static void addScore(int score) {
        ranking.put(Ranking.userName, score);
        save();
        Log.w("myApp", String.valueOf(context.getFilesDir()));

    }

    private static void save() {
        Properties properties = new Properties();

        for (Map.Entry<String, Integer> entry : ranking.entrySet()) {
            properties.put(entry.getKey(), Integer.toString(entry.getValue()));
        }
        Log.w("myApp", String.valueOf(context.getFilesDir()));
        try {
            properties.store(new FileOutputStream(context.getFilesDir() + "/scores.properties"), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void load() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(context.getFilesDir() + "/scores.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String key : properties.stringPropertyNames()) {
            ranking.put(key, Integer.parseInt((String) properties.get(key)));
        }

    }

    public static String[] toArray() {
        HashMap<String, Integer> sortedRanking = MapSorter.sortByValues(ranking);
        String[] a = new String[sortedRanking.size() > 10 ? 10 : sortedRanking.size()];
        int i = 0;
        for (Map.Entry<String, Integer> entry : sortedRanking.entrySet()) {
            a[i] = entry.getKey() + " - " + Integer.toString((entry.getValue()));
            i++;
            if (i == 10) {
                break;
            }
        }
        return a;
    }
}