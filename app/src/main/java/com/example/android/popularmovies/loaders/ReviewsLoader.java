package com.example.android.popularmovies.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.Reviews;
import com.example.android.popularmovies.model.Trailers;
import com.example.android.popularmovies.utils.MoviesApiClient;
import com.example.android.popularmovies.utils.MoviesApiInterface;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class ReviewsLoader extends AsyncTaskLoader<Reviews> {

    private static final String TAG = "TrailersLoader";
    private static final String API_KEY = "";
    private Response<Reviews> mReviews;
    private Reviews mData;
    private String mId;

    public ReviewsLoader(Context context, String id) {
        super(context);
        mId = id;
    }


    @Override
    public Reviews loadInBackground() {
        getReviews(mId);

        if (mReviews != null) {
            mData = mReviews.body();
            return mData;

        } else
            return null;
    }

    @Override
    protected void onStartLoading() {

        if (mReviews != null) {
            deliverResult(mReviews.body());
            return;
        }

        forceLoad();
    }

    @Override
    public void deliverResult(Reviews data) {
        mData = data;
        super.deliverResult(data);
    }

    /*
     * Method fetches reviews from api
     */
    private void getReviews(String id) {

        MoviesApiInterface moviesApiInterface = MoviesApiClient.getClient();
        Call<Reviews> callBackend;

        callBackend = moviesApiInterface.getReviews(id, API_KEY);

        try {
            mReviews = callBackend.execute();

        } catch (IOException e) {
            Log.d(TAG, "IOException.." + e);
        }

    }
}
