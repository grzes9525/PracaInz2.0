package com.pracainz20.Data;

/**
 * Created by Grzechu on 11.01.2018.
 */

public class WelcomeDB {

    static String[] titles = {"Waga", "Obwód szyi", "Obwód bioder", "Obwód tali" ," Obwód klatki piersiowej","Obwód lewego bicepsa","Obwód prawego bicepsa","Zdjęcie sylwetki"};
    static String[] units = {"kg", "cm", "cm", "cm","cm","cm","cm",""};
    static Integer[] id_ = {0, 1, 2, 3,4,5,6,7};

    public static String[] getTitles() {
        return titles;
    }

    public static void setTitles(String[] titles) {
        WelcomeDB.titles = titles;
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
}
