package com.example.gabrielcuenca.spaceinvaders.models;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.gabrielcuenca.spaceinvaders.WelcomeActivity;
import com.example.gabrielcuenca.spaceinvaders.utils.BitmapCast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public final class Ranking {

    private static HashMap<String, Integer> ranking = new HashMap<String, Integer>();
    private static HashMap<String, Bitmap> images = new HashMap<String, Bitmap>();
    public static String userName;
    private static Context context;


    public static void setup() {
        context = WelcomeActivity.getContext();
        loadRanking();
        loadImages();
    }

    public static HashMap<String, Integer> getRanking() {
        return ranking;
    }

    public static HashMap<String, Bitmap> getImages() {
        return images;
    }

    public static void addScore(int score, Bitmap userPhoto) {
        ranking.put(Ranking.userName, score);
        images.put(Ranking.userName, userPhoto);
        saveRanking();
        saveImages();
    }

    private static void saveRanking() {
        Properties properties = new Properties();

        for (Map.Entry<String, Integer> entry : ranking.entrySet()) {
            properties.put(entry.getKey(), Integer.toString(entry.getValue()));
        }
        try {
            properties.store(new FileOutputStream(context.getFilesDir() + "/scores.properties"), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void saveImages() {
        Properties properties = new Properties();

        for (Map.Entry<String, Bitmap> entry : images.entrySet()) {
            properties.put(entry.getKey(), BitmapCast.BitMapToString(entry.getValue()));
        }
        try {
            properties.store(new FileOutputStream(context.getFilesDir() + "/images.properties"), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadImages() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(context.getFilesDir() + "/images.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String key : properties.stringPropertyNames()) {
            images.put(key, BitmapCast.StringToBitMap((String) properties.get(key)));
        }

    }

    public static void loadRanking() {
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
}