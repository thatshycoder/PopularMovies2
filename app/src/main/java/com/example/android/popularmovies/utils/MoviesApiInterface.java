package com.example.android.popularmovies.utils;

import android.database.Observable;

import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.Reviews;
import com.example.android.popularmovies.model.Trailers;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MoviesApiInterface {

    @GET("/3/movie/popular")
    Call<Movie> getPopularMovies(@Query("api_key") String apiKey);

    @GET("/3/movie/top_rated")
    Call<Movie> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("/3/movie/{id}/videos")
    Call<Trailers> getTrailers(@Path("id") String id, @Query("api_key") String api_key);

    @GET("/3/movie/{id}/reviews")
    Call<Reviews> getReviews(@Path("id") String id, @Query("api_key") String api_key);
}
