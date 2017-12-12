package com.pracainz20.Data;

/**
 * Created by Grzechu on 24.10.2017.
 */

public class WelcomeDB {


    static String[] titles = {"Imię","Nazwisko","Numer telefonu","Wzrost","Wiek"};
    static String[] values = {"Adam", "Adamecki", "123456789","175","25"};
    static String[] units = {"kg", "kg", "kg na tydz"," "," "};
    static String[] unityCardView = {"","","","cm","lat"};
    static Integer[] id_ = {0, 1, 2, 3,4};



    public static String[] getTitles() {
        return titles;
    }

    public static void setTitles(String[] titles) {
        WelcomeDB.titles = titles;
    }

    public static String[] getValues() {
        return values;
    }

    public static void setValues(String[] values) {
        WelcomeDB.values = values;
    }

    public static String[] getUnits() {
        return units;
    }

    public static void setUnits(String[] units) {
        WelcomeDB.units = units;
    }

    public static Integer[] getId_() {
        return id_;
    }

    public static void setId_(Integer[] id_) {
        WelcomeDB.id_ = id_;
    }


    public static String[] getUnityCardView() {
        return unityCardView;
    }

    public static void setUnityCardView(String[] unityCardView) {
        WelcomeDB.unityCardView = unityCardView;
    }

}
