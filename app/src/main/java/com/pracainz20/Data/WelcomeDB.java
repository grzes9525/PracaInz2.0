package com.pracainz20.Data;

/**
 * Created by Grzechu on 24.10.2017.
 */

public class WelcomeDB {
    private String[] titles = {"Imię","Nazwisko","Numer telefonu","Wzrost","Wiek"};
    private String[] values = {"Adam", "Adamecki", "123456789","175","25"};
    private String[] units = {"kg", "kg", "kg na tydz"," "," "};
    private String[] unityCardView = {"","","","cm","lat"};
    private Integer[] id_ = {0, 1, 2, 3,4};

    public String[] getTitles() {
        return titles;
    }

    public void setTitles(String[] titles) {
        this.titles = titles;
    }

    public String[] getValues() {
        return values;
    }

    public void setValues(String[] values) {
        this.values = values;
    }

    public String[] getUnits() {
        return units;
    }

    public void setUnits(String[] units) {
        this.units = units;
    }

    public String[] getUnityCardView() {
        return unityCardView;
    }

    public void setUnityCardView(String[] unityCardView) {
        this.unityCardView = unityCardView;
    }

    public Integer[] getId_() {
        return id_;
    }

    public void setId_(Integer[] id_) {
        this.id_ = id_;
    }





}
