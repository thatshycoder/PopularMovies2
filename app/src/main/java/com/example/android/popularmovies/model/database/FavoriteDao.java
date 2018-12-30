package com.example.android.popularmovies.model.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.android.popularmovies.model.Movie;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface FavoriteDao {

    @Query("SELECT * FROM favorites ORDER BY id DESC")
    LiveData<List<Movie>> loadFavorites();

    @Query("SELECT * FROM favorites WHERE id == :movie_id")
    LiveData<Movie> isFavorite(int movie_id);

    @Insert
    void insertFavorite(Movie favoriteMovies);

    @Query("DELETE FROM favorites WHERE id == :movie_id")
    void deleteFavorite(String movie_id);
}
