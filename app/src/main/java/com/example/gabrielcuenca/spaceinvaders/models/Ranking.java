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
import java.util.Collections;

public class Ranking {

    public ArrayList<Score> puntuaciones = new ArrayList<>();
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
                                    context.openFileInput("points.txt")));
            lineaAleer = fin.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {

            OutputStreamWriter fout =
                    new OutputStreamWriter(
                            context.openFileOutput("points.txt", Context.MODE_PRIVATE));

            if (lineaAleer != null)
                fout.write(lineaAleer + "" + userName + ":" + score + "pts");
            else
                fout.write(userName + ": " + score + "pts");
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
                                    context.openFileInput("points.txt")));
            String lineaActual;
            while ((lineaActual = fin.readLine()) != null) {
                System.out.println(lineaActual);
                String[] puntuacioneGuardadas = lineaActual.split("pts");
                for (int i = 0; i < puntuacioneGuardadas.length; i++) {
                    String[] datosPuntuacion = puntuacioneGuardadas[i].split(":");
                    System.out.println(datosPuntuacion[0] + "-" + datosPuntuacion[1]);
                    puntuaciones.add(new Score(Integer.parseInt(datosPuntuacion[1].trim()),datosPuntuacion[0]));
                }

            }
            fin.close();

            Log.i("Ficheros", "Todo bien, todo correcto");
        } catch (FileNotFoundException e) {
            Log.i("Ficheros", "El fichero no está");
        } catch (IOException e) {
            Log.i("Ficheros", "RIP");
        }
        String[] array = new String[puntuaciones.size()];
        Collections.sort(puntuaciones);
        for (int i = 0; i < puntuaciones.size(); i++) {
            array[i] = puntuaciones.get(i).getName() + ": " +puntuaciones.get(i).getScore();
        }


        return array;
    }
}
