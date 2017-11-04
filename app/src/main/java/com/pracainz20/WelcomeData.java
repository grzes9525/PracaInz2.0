package com.pracainz20;

/**
 * Created by Grzechu on 23.10.2017.
 */

public class WelcomeData {
    String title;
    String value;

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

    public WelcomeData(String title, String value, Integer integer, String unit) {
        this.title = title;
        this.value = value;
    }





}
