package com.example.runit;


import android.app.Application;

public class Pinin extends Application {

    private static String pinin;


    public static void setPinin(String pinin) {
        pinin = pinin;
    }

    public static String getPinin() {
        return pinin;
    }
}