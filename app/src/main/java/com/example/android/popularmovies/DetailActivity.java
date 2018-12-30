package com.example.android.popularmovies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.adapters.ReviewsAdapter;
import com.example.android.popularmovies.adapters.TrailerAdapter;
import com.example.android.popularmovies.loaders.ReviewsLoader;
import com.example.android.popularmovies.loaders.TrailerLoader;
import com.example.android.popularmovies.model.database.AppDatabase;
import com.example.android.popularmovies.model.database.AppExecutor;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.Reviews;
import com.example.android.popularmovies.model.Trailers;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.MaskTransformation;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = "DetailActivity";

    public @BindView(R.id.btn_fave)
    ImageView mFave;

    public @BindView(R.id.btn_unfave)
    ImageView mUnFave;

    public @BindView(R.id.movie_cover)
    ImageView mMovieCover;

    public @BindView(R.id.movie_poster)
    ImageView mMoviePoster;

    public @BindView(R.id.title)
    TextView mMovieTitle;

    public @BindView(R.id.released_year)
    TextView mReleasedYear;

    public @BindView(R.id.average_rating)
    TextView mAverageRating;

    public @BindView(R.id.overview)
    TextView mOverview;

    public @BindView(R.id.reviews_error)
    TextView mReviewsError;

    public @BindView(R.id.trailer_error)
    TextView mTrailerError;

    public @BindView(R.id.trailer_rv)
    RecyclerView mTrailerRecyclerView;

    public @BindView(R.id.reviews_rv)
    RecyclerView mReviewsRecyclerView;

    private LoaderManager mLoaderManager;

    private static final int TRAILER_LOADER_ID = 1;

    private static final int REVIEWS_LOADER_ID = 2;

    private ArrayList<Trailers> mTrailerList;

    private ArrayList<Reviews> mReviewsList;

    private TrailerAdapter mTrailerAdapter;

    private ReviewsAdapter mReviewsAdapter;

    Intent intent;

    private String mId;

    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        intent = getIntent();

        final Movie movie = intent.getParcelableExtra("Movie");
        mId = String.valueOf(movie.getId());

        // check network connectivity and display error if offline
        ConnectivityManager connectivityManager =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        // phone is offline and sort order is not by favorites
        if (isConnected) {
            // initialize loader
            mLoaderManager = LoaderManager.getInstance(this);

            mLoaderManager.initLoader(TRAILER_LOADER_ID, null, trailerLoaderListener);
            mLoaderManager.initLoader(REVIEWS_LOADER_ID, null, reviewsLoaderListener);

        } else {
            showNoConnError();
        }

        mDb = AppDatabase.getInstance(getApplicationContext());

        mTrailerList = new ArrayList<>();
        mReviewsList = new ArrayList<>();

        // setup recyclerview
        mTrailerRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
        mReviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mTrailerAdapter = new TrailerAdapter(getApplicationContext(), mTrailerList);
        mTrailerRecyclerView.setAdapter(mTrailerAdapter);

        mReviewsAdapter = new ReviewsAdapter(getApplicationContext(), mReviewsList);
        mReviewsRecyclerView.setAdapter(mReviewsAdapter);

        final String poster = movie.getPoster_path();
        String coverImage = "https://image.tmdb.org/t/p/w185" + poster;

        // bind the fetched data to corresponding views
        Picasso.get()
                .load(coverImage)
                .error(R.drawable.placeholder)
                .into(mMovieCover);

        Transformation transformation = new MaskTransformation(getApplicationContext(), R.drawable.poster_border);

        Picasso.get()
                .load(coverImage)
                .error(R.drawable.placeholder)
                .transform(transformation)
                .into(mMoviePoster);

        final String title = movie.getTitle();
        mMovieTitle.setText(title);

        final String releasedYear = movie.getRelease_date();
        mReleasedYear.setText(releasedYear);

        final String averageRating = Float.toString(movie.getVote_average());
        mAverageRating.setText(averageRating);

        final String overview = movie.getOverview();
        mOverview.setText(overview);

        final int movieId = Integer.valueOf(mId);

        // Listen to changes in adding and removing favorite and update UI accordingly
        final LiveData<Movie> faveMovie = mDb.favoriteDao().isFavorite(movieId);
        faveMovie.observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(@Nullable Movie movies) {
                faveMovie.removeObserver(this);

                Log.d(TAG, "Actively receiving database update");

                if (movies != null) {
                    if (movies.getMid() > 0) {
                        // movie is a favorite
                        favorited();
                    } else {
                        // movie is a favorite
                        unFavorited();
                    }
                }
            }
        });

        // Remove a movie from favorites
        mUnFave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppExecutor.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {

                        mDb.favoriteDao().deleteFavorite(mId);
                    }
                });
                unFavorited();
            }

        });

        final Movie favoriteMovies = new Movie(Integer.parseInt(mId), title, releasedYear, Float.valueOf(averageRating), poster, overview);

        // Add a new movie as favorite
        mFave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppExecutor.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {

                        mDb.favoriteDao().insertFavorite(favoriteMovies);

                    }
                });
                favorited();
            }
        });
    }

    /*
     * Method updates the UI when a movie is added as favorite
     */
    private void favorited() {
        mFave.setVisibility(View.GONE);
        mUnFave.setVisibility(View.VISIBLE);
    }

    /*
     * Method updates the UI when a movie is removed as favorite
     */
    private void unFavorited() {
        mFave.setVisibility(View.VISIBLE);
        mUnFave.setVisibility(View.GONE);
    }

    /*
     * Method shows corresponding error messages when there's no internet connection
     */
    private void showNoConnError() {
        mReviewsError.setText(R.string.no_conn_reviews);
        mReviewsError.setVisibility(View.VISIBLE);

        mTrailerError.setText(R.string.no_conn_trailer);
        mTrailerError.setVisibility(View.VISIBLE);

        mTrailerRecyclerView.setVisibility(View.GONE);
        mReviewsRecyclerView.setVisibility(View.GONE);
    }

    /**
     * Fetch trailers from api in background
     */
    private LoaderManager.LoaderCallbacks<Trailers> trailerLoaderListener
            = new LoaderManager.LoaderCallbacks<Trailers>() {

        @NonNull
        @Override
        public Loader<Trailers> onCreateLoader(int i, Bundle bundle) {

            return new TrailerLoader(getApplicationContext(), mId);
        }

        @Override
        public void onLoaderReset(Loader<Trailers> loader) {
            Log.e(TAG, "onLoaderReset");
        }

        @Override
        public void onLoadFinished(Loader<Trailers> loader, Trailers trailers) {
            if (trailers != null) {

                mTrailerList.addAll(trailers.getResults());

                if (mTrailerList != null) {
                    mTrailerAdapter.setTrailerList(mTrailerList);
                    mTrailerAdapter.notifyDataSetChanged();
                }

            } else {
                showTrailerError();
            }
        }

    };

    /**
     * Fetch reviews from api in background
     */
    private LoaderManager.LoaderCallbacks<Reviews> reviewsLoaderListener
            = new LoaderManager.LoaderCallbacks<Reviews>() {
        @NonNull
        @Override
        public Loader<Reviews> onCreateLoader(int i, Bundle bundle) {
            return new ReviewsLoader(getApplicationContext(), mId);
        }

        @Override
        public void onLoaderReset(Loader<Reviews> loader) {
            Log.e(TAG, "onLoaderReset");
        }

        @Override
        public void onLoadFinished(Loader<Reviews> loader, Reviews reviews) {

            if (!reviews.getResults().isEmpty()) {

                mReviewsList.addAll(reviews.getResults());

                if (mReviewsList != null) {
                    mReviewsAdapter.setReviewsList(mReviewsList);
                    mReviewsAdapter.notifyDataSetChanged();
                }

            } else {
                showReviewError();
            }
        }

    };

    /*
     * Method shows corresponding error messages when there are no reviews
     */
    private void showReviewError() {
        mReviewsRecyclerView.setVisibility(View.GONE);
        mReviewsError.setVisibility(View.VISIBLE);
    }

    /*
     * Method shows corresponding error messages when there are no trailers
     */
    private void showTrailerError() {
        mTrailerRecyclerView.setVisibility(View.GONE);
        mTrailerError.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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

        if (mLoaderManager != null) {
            mLoaderManager.destroyLoader(0);
        }

    }

}
