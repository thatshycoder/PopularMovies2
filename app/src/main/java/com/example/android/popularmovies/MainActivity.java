package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.model.Movie;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Movie>, SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "MainActivity";

    ArrayList<Movie> mMovieList;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.error)
    TextView mError;

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    MovieAdapter mMovieAdapter;

    private LoaderManager mLoaderManager;

    private String mSortOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mMovieList = new ArrayList<>();

        // set the recyclerview layout to grid
        LinearLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);

        mMovieAdapter = new MovieAdapter(getApplicationContext(), mMovieList);
        mRecyclerView.setAdapter(mMovieAdapter);

        // check network connectivity and display error if offline
        ConnectivityManager connectivityManager =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        // phone is offline
        if (!isConnected) {
            showError();
            return;
        }

        // get the preference for sort order
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        mSortOrder = sharedPreferences.getString(getString(R.string.sort_key), getString(R.string.sort_popular));

        // initialize loader
        mLoaderManager = LoaderManager.getInstance(this);
        mLoaderManager.initLoader(0, null, this);
    }

    @NonNull
    @Override
    public Loader<Movie> onCreateLoader(int i, Bundle bundle) {
        return new MovieLoader(this, mSortOrder);
    }

    @Override
    public void onLoaderReset(Loader<Movie> loader) {
        Log.e(TAG, "onLoaderReset");
    }

    @Override
    public void onLoadFinished(Loader<Movie> loader, Movie movies) {
        mProgressBar.setVisibility(View.GONE);

        if (movies != null) {

            mMovieList.addAll(movies.getResults());


            if (mMovieList != null) {
                mMovieAdapter.setMovieList(mMovieList);
                mMovieAdapter.notifyDataSetChanged();
            }

        } else {
            showError();
        }
    }

    private void showError() {
        mError.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);

        if (mLoaderManager != null) {
            mLoaderManager.destroyLoader(0);
        }

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

        if (s.equals(getString(R.string.sort_key))) {

            mSortOrder = sharedPreferences.getString(getString(R.string.sort_key), getString(R.string.sort_popular));
            mMovieList.clear();

            mProgressBar.setVisibility(View.VISIBLE);

            // restart the loader
            LoaderManager.getInstance(this).restartLoader(0, null, this);

        }
    }
}
