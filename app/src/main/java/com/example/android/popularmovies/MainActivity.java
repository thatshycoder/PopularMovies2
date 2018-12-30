package com.example.android.popularmovies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import com.example.android.popularmovies.adapters.MovieAdapter;
import com.example.android.popularmovies.loaders.MovieLoader;
import com.example.android.popularmovies.model.database.AppDatabase;
import com.example.android.popularmovies.model.database.MainViewModel;
import com.example.android.popularmovies.model.Movie;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Movie>, SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "MainActivity";

    private ArrayList<Movie> mMovieList;

    public @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    public @BindView(R.id.error)
    TextView mError;

    public @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    private MovieAdapter mMovieAdapter;

    private LoaderManager mLoaderManager;

    private String mSortOrder;

    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mDb = AppDatabase.getInstance(getApplicationContext());

        mMovieList = new ArrayList<>();

        // get the preference for sort order
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        mSortOrder = sharedPreferences.getString(getString(R.string.sort_key), getString(R.string.sort_popular));

        // check network connectivity and display error if offline
        ConnectivityManager connectivityManager =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        // phone is offline and sort order is not by favorites
        if (!isConnected && !mSortOrder.equals(getString(R.string.sort_favorite))) {
            showError();
            return;
        }

        // set the recyclerview layout to grid
        LinearLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);

        mMovieAdapter = new MovieAdapter(MainActivity.this, mMovieList);

        // display favorite movies if sort order is by favorite
        if (mSortOrder.equals(getString(R.string.sort_favorite))) {

            LiveData<List<Movie>> movies = mDb.favoriteDao().loadFavorites();
            movies.observe(this, new Observer<List<Movie>>() {
                @Override
                public void onChanged(@Nullable List<Movie> movies) {
                    Log.d(TAG, "Actively receiving database update");

                    if (movies != null && !movies.isEmpty()) {
                        mProgressBar.setVisibility(View.GONE);

                        mMovieList.addAll(movies);
                        setupViewModel();
                    } else {
                        showFaveError();
                    }
                }
            });

        } else {

            // initialize loader
            mLoaderManager = LoaderManager.getInstance(this);
            mLoaderManager.initLoader(0, null, this);
        }

        mRecyclerView.setAdapter(mMovieAdapter);
    }

    /*
     * Method fetches movie in the background from api
     */
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

    /*
     * Sets up ViewModel for Caching
     */
    private void setupViewModel() {

        MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        mainViewModel.getMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                Log.d(TAG, "Receiving database update from LiveData in ViewModel");
                mMovieAdapter.setMovieList(mMovieList);
            }
        });
    }

    /*
     * Method shows corresponding error messages when there's no internet connection
     */
    private void showError() {
        mError.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);
    }

    /*
     * Method shows corresponding error messages when there are no favorites
     */
    private void showFaveError() {
        mError.setVisibility(View.VISIBLE);
        mError.setText(R.string.no_fave);
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
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

        if (s.equals(getString(R.string.sort_key))) {

            mSortOrder = sharedPreferences.getString(getString(R.string.sort_key), getString(R.string.sort_popular));
            mMovieList.clear();

            mProgressBar.setVisibility(View.VISIBLE);

            // restart the loader
            LoaderManager.getInstance(this).restartLoader(0, null, this);

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
}
