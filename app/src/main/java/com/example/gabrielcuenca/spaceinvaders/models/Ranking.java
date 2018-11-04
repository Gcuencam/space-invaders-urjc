package com.example.gabrielcuenca.spaceinvaders.models;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

public class Ranking {

    public ArrayList<Integer> puntuaciones = new ArrayList<>();
    public  String userName;
    public Context context;

    public void setUserName(String s){
        userName=s;
    }

    public Ranking(Context context, String username) {
        this.context = context;
        this.userName=username;
    }


    public void save(int score) {
        String lineaAleer = "";
        try {
            BufferedReader fin =
                    new BufferedReader(
                            new InputStreamReader(
                                    context.openFileInput("nueva_puntuacion2.txt")));
            lineaAleer = fin.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {

            OutputStreamWriter fout =
                    new OutputStreamWriter(
                            context.openFileOutput("nueva_puntuacion2.txt", Context.MODE_PRIVATE));

            if (lineaAleer != null)
                fout.write(lineaAleer + "" + userName + "¬" + score + "#");
            else
                fout.write(userName + "¬" + score + "#");
            fout.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String[] load() {
        try {
            BufferedReader fin =
                    new BufferedReader(
                            new InputStreamReader(
                                    context.openFileInput("nueva_puntuacion2.txt")));
            String lineaActual;
            while ((lineaActual = fin.readLine()) != null) {
                System.out.println(lineaActual);
                String[] puntuacioneGuardadas = lineaActual.split("#");
                for (int i = 0; i < puntuacioneGuardadas.length; i++) {
                    String[] datosPuntuacion = puntuacioneGuardadas[i].split("¬");
                    System.out.println(datosPuntuacion[0] + "-" + datosPuntuacion[1]);
                    puntuaciones.add(Integer.parseInt(datosPuntuacion[1]));
                }
            }
            fin.close();

            Log.i("Ficheros", "Fichero leido!");
        } catch (FileNotFoundException e) {
            Log.i("Ficheros", "Fichero no leido!");
        } catch (IOException e) {
            Log.i("Ficheros", "ALGO PASA!");
        }
        String[] array = new String[puntuaciones.size()];
        int contador = 0;
        for (int i = 0; i < puntuaciones.size(); i++) {
            array[i] = userName+": "+puntuaciones.get(i);
        }
        System.out.println(array.toString());

        return array;
    }
}
