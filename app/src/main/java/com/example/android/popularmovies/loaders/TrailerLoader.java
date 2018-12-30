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

public class TrailerLoader extends AsyncTaskLoader<Trailers> {

    private static final String TAG = "TrailersLoader";
    private static final String API_KEY = "";
    private Response<Trailers> mTrailers;
    private Trailers mData;
    private String mId;

    public TrailerLoader(Context context, String id) {
        super(context);
        mId = id;
    }


    @Override
    public Trailers loadInBackground() {
        getTrailers(mId);

        if (mTrailers != null) {
            mData = mTrailers.body();
            return mData;

        } else
            return null;
    }

    @Override
    protected void onStartLoading() {

        if (mTrailers != null) {
            deliverResult(mTrailers.body());
            return;
        }

        forceLoad();
    }

    @Override
    public void deliverResult(Trailers data) {
        mData = data;
        super.deliverResult(data);
    }

    /*
     * Method shows corresponding error messages when there are no trailers
     */
    private void getTrailers(String id) {

        MoviesApiInterface moviesApiInterface = MoviesApiClient.getClient();
        Call<Trailers> callBackend;

        callBackend = moviesApiInterface.getTrailers(id, API_KEY);

        try {
            mTrailers = callBackend.execute();

        } catch (IOException e) {
            Log.d(TAG, "IOException.." + e);
        }

    }
}
