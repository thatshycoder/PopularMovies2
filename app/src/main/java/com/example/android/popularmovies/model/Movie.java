package com.example.android.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Movie {

    @SerializedName("results")
    private ArrayList results;

    @SerializedName("original_title")
    private String original_title;

    @SerializedName("release_date")
    private String release_date;

    @SerializedName("vote_average")
    private float vote_average;

    @SerializedName("poster_path")
    private String poster_path;

    @SerializedName("overview")
    private String overview;

    public ArrayList getResults() {
        return results;
    }

    public void setResults(ArrayList results) {
        this.results = results;
    }

    public String getTitle() {
        return original_title;
    }

    public void setTitle(String title) {
        this.original_title = title;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public float getVote_average() {
        return vote_average;
    }

    public void setVote_average(float vote_average) {
        this.vote_average = vote_average;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

}
