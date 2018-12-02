package com.example.android.popularmovies.utils;

import com.example.android.popularmovies.model.Movie;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MoviesApiInterface {

    @GET("/3/movie/popular")
    Call<Movie> getPopularMovies(@Query("api_key") String apiKey);

    @GET("/3/movie/top_rated")
    Call<Movie> getTopRatedMovies(@Query("api_key") String apiKey);

}
