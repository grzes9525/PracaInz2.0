package com.pracainz20.Model;

import java.util.List;

/**
 * Created by Grzechu on 31.03.2018.
 */

public class Meal {

    private String pushId;
    private String name;
    private String date;
    private List<Product> products;

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
