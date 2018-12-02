package com.example.android.popularmovies.utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MoviesApiClient {

    public static String BASE_URL = "https://api.themoviedb.org";

    private static Retrofit retrofit;

    public static MoviesApiInterface getClient() {
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit.create(MoviesApiInterface.class);
    }
}
