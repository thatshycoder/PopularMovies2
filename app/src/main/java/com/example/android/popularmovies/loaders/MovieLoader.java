package com.example.android.popularmovies.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.Reviews;
import com.example.android.popularmovies.model.Trailers;
import com.example.android.popularmovies.utils.MoviesApiClient;
import com.example.android.popularmovies.utils.MoviesApiInterface;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class MovieLoader extends AsyncTaskLoader<Movie> {

    private static final String TAG = "MoviesLoader";
    private static final String API_KEY = "";
    private Response<Movie> mMovies;
    private Response<Reviews> mReviews;
    private Movie mData;
    private String mSortOrder;
    private Context mContext;

    public MovieLoader(Context context) {
        super(context);

        mContext = context;
    }
    public MovieLoader(Context context, String sort_order) {
        super(context);

        mContext = context;
        mSortOrder = sort_order;
    }


    @Override
    public Movie loadInBackground() {
        getMovies();

        if (mMovies != null) {
            mData = mMovies.body();
            return mData;

        } else
            return null;
    }

    @Override
    protected void onStartLoading() {

        if (mMovies != null) {
            deliverResult(mMovies.body());
            return;
        }

        forceLoad();
    }

    @Override
    public void deliverResult(Movie data) {
        mData = data;
        super.deliverResult(data);
    }

    /*
     * Method fetches movie from api
     */
    private void getMovies() {
        String sortPopular = mContext.getString(R.string.sort_popular);
        String sortTopRated = mContext.getString(R.string.sort_top_rated);

        MoviesApiInterface moviesApiInterface = MoviesApiClient.getClient();
        Call<Movie> callBackend;

        if (mSortOrder.equals(sortPopular)) {
            callBackend = moviesApiInterface.getMovies(sortPopular, API_KEY);

        } else {
            callBackend = moviesApiInterface.getMovies(sortTopRated, API_KEY);
        }

        try {
            mMovies = callBackend.execute();

        } catch (IOException e) {
            Log.d(TAG, "IOException.." + e);
        }

    }
}
