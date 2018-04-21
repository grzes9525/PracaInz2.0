package com.pracainz20.Model;

import java.io.Serializable;
import java.util.List;

public class Exercise implements Serializable {

    private String pushId;
    private String name;
    private String kindOfExercise;
    private String kindOfExerciseInDetail;
    private Integer serires;
    private Integer repetitions;
    private Double volume;
    private Double weight;
    private List<Serie> series;
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Serie> getSeries() {
        return series;
    }

    public void setSeries(List<Serie> series) {
        this.series = series;
    }

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

    public String getKindOfExercise() {
        return kindOfExercise;
    }

    public void setKindOfExercise(String kindOfExercise) {
        this.kindOfExercise = kindOfExercise;
    }

    public String getKindOfExerciseInDetail() {
        return kindOfExerciseInDetail;
    }

    public void setKindOfExerciseInDetail(String kindOfExerciseInDetail) {
        this.kindOfExerciseInDetail = kindOfExerciseInDetail;
    }

    public Integer getSerires() {
        return serires;
    }

    public void setSerires(Integer serires) {
        this.serires = serires;
    }

    public Integer getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(Integer repetitions) {
        this.repetitions = repetitions;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }
}
