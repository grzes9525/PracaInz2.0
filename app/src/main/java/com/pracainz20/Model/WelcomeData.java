package com.pracainz20.Model;

/**
 * Created by Grzechu on 23.10.2017.
 */

public class WelcomeData {
    String title;
    String value;
    String unity;
    String unitDialog;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }

    public String getUnity() {
        return unity;
    }

    public void setUnity(String unity) {
        this.unity = unity;
    }

    public String getUnitDialog() {
        return unitDialog;
    }

    public void setUnitDialog(String unitDialog) {
        this.unitDialog = unitDialog;
    }

    public WelcomeData(String title, String value, Integer integer, String unitDialog, String unityCardView) {
        this.title = title;
        this.value = value;
        this.unitDialog = unitDialog;
        this.unity = unityCardView;
    }





}
