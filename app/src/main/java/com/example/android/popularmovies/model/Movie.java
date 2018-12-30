package com.example.android.popularmovies.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Property;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

@Entity(tableName = "favorites")
public class Movie implements Parcelable {

    @SerializedName("mid")
    @PrimaryKey(autoGenerate = true)
    private int mid;

    @SerializedName("id")
    private int id;

    @Ignore
    @SerializedName("results")
    private ArrayList results;

    public String getOriginal_title() {
        return original_title;
    }

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

    @Ignore
    public Movie() {

    }

    @Ignore
    public Movie(Parcel parcel) {
        id = parcel.readInt();
        original_title = parcel.readString();
        release_date = parcel.readString();
        vote_average = parcel.readFloat();
        poster_path = parcel.readString();
        overview = parcel.readString();
    }

    public Movie(int mid, int id, String original_title, String release_date, float vote_average, String poster_path, String overview) {

        this.mid = mid;
        this.id = id;
        this.original_title = original_title;
        this.release_date = release_date;
        this.vote_average = vote_average;
        this.poster_path = poster_path;
        this.overview = overview;
    }

    @Ignore
    public Movie(int id, String original_title, String release_date, float vote_average, String poster_path, String overview) {

        this.id = id;
        this.original_title = original_title;
        this.release_date = release_date;
        this.vote_average = vote_average;
        this.poster_path = poster_path;
        this.overview = overview;
    }

    public int describeContents() {
        return hashCode();
    }

    public void writeToParcel(Parcel dest, int i) {

        dest.writeInt(id);
        dest.writeString(original_title);
        dest.writeString(release_date);
        dest.writeFloat(vote_average);
        dest.writeString(poster_path);
        dest.writeString(overview);
    }

    // creator - used when un-parceling our parcle (creating the object)
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {

        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[0];
        }
    };

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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
