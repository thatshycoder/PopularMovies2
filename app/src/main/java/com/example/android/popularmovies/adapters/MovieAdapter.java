package com.example.android.popularmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.DetailActivity;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.Movie;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private Context mContext;
    private ArrayList<Movie> mMovieList;
    private Movie mMovie;

    public MovieAdapter(Context mContext, ArrayList<Movie> movieList) {
        this.mContext = mContext;
        mMovieList = movieList;
    }

    public void setMovieList(ArrayList<Movie> movieList) {
        mMovieList = movieList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieAdapter.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_griditem, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.MovieViewHolder viewHolder, int position) {

        Gson gs = new Gson();
        String js = gs.toJson(mMovieList.get(position));
        final Movie movieModel = gs.fromJson(js, Movie.class);

        Picasso.get()
                .load("https://image.tmdb.org/t/p/w185" + movieModel.getPoster_path())
                .error(R.drawable.placeholder)
                .into(viewHolder.moviePoster);

        viewHolder.moviePoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // add details to intent
                mMovie = new Movie(movieModel.getId(), movieModel.getTitle(),
                        movieModel.getRelease_date(),
                        movieModel.getVote_average(),
                        movieModel.getPoster_path(),
                        movieModel.getOverview());

                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("Movie", mMovie);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (mMovieList != null) {
            return mMovieList.size();
        }

        return 0;
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView moviePoster;

        public MovieViewHolder(View itemView) {
            super(itemView);

            moviePoster = itemView.findViewById(R.id.movie_poster);
        }
    }
}
