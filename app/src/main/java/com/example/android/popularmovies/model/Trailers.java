package com.example.android.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Trailers {

    @SerializedName("results")
    private ArrayList results;

    @SerializedName("name")
    private String name;

    @SerializedName("key")
    private String key;

    public Trailers(String name, String video_key) {
        this.name = name;
        this.key = video_key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String video_key) {
        this.key = video_key;
    }

    public ArrayList getResults() {
        return results;
    }

    public void setResults(ArrayList results) {
        this.results = results;
    }
}
