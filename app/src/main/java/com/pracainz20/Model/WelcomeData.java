package com.pracainz20.Model;

/**
 * Created by Grzechu on 23.10.2017.
 */

public class WelcomeData {
    String title;
    String value;
    String unity;
    String UserParameterId;

    public WelcomeData() {
    }

    public WelcomeData(String title, String value, String unity) {
        this.title = title;
        this.value = value;
        this.unity = unity;
    }

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


    public String getUserParameterId() {
        return UserParameterId;
    }

    public void setUserParameterId(String userParameterId) {
        UserParameterId = userParameterId;
    }
}
