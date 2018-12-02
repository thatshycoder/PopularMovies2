package com.example.android.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.movie_cover)
    ImageView mMovieCover;

    @BindView(R.id.movie_poster)
    ImageView mMoviePoster;

    @BindView(R.id.title)
    TextView mMovieTitle;

    @BindView(R.id.released_year)
    TextView mReleasedYear;

    @BindView(R.id.average_rating)
    TextView mAverageRating;

    @BindView(R.id.overview)
    TextView mOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        String coverImage = "https://image.tmdb.org/t/p/w185" + getIntent().getStringExtra(getString(R.string.poster_path_extra));

        // bind the fetched data to corresponding views
        Picasso.get()
                .load(coverImage)
                .error(R.drawable.placeholder)
                .into(mMovieCover);

        Picasso.get()
                .load(coverImage)
                .error(R.drawable.placeholder)
                .into(mMoviePoster);

        String title = getIntent().getStringExtra(getString(R.string.title_extra));
        mMovieTitle.setText(title);

        String releasedYear = getIntent().getStringExtra(getString(R.string.released_date_extra));
        mReleasedYear.setText(releasedYear);

        String averageRating = getIntent().getStringExtra(getString(R.string.average_rating_extra));
        mAverageRating.setText(averageRating);

        String overview = getIntent().getStringExtra(getString(R.string.overview_extra));
        mOverview.setText(overview);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
