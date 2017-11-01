package com.pracainz20;

/**
 * Created by Grzechu on 23.10.2017.
 */

public class WelcomeData {
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

    public WelcomeData(String title, String value, Integer integer) {
        this.title = title;
        this.value = value;
    }

    String title;
    String value;


}
