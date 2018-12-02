package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.model.Movie;
import com.google.gson.internal.LinkedTreeMap;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private Context mContext;
    private ArrayList<Movie> mMovieList;

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

        Object getrow = this.mMovieList.get(position);
        final LinkedTreeMap<Object, Object> t = (LinkedTreeMap) getrow;

        Picasso.get()
                .load("https://image.tmdb.org/t/p/w185" + t.get("poster_path"))
                .error(R.drawable.placeholder)
                .into(viewHolder.moviePoster);

        viewHolder.moviePoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DetailActivity.class);

                intent.putExtra(mContext.getString(R.string.poster_path_extra), t.get("poster_path").toString());
                intent.putExtra(mContext.getString(R.string.title_extra), t.get("original_title").toString());
                intent.putExtra(mContext.getString(R.string.released_date_extra), t.get("release_date").toString());
                intent.putExtra(mContext.getString(R.string.overview_extra), t.get("overview").toString());
                intent.putExtra(mContext.getString(R.string.average_rating_extra), t.get("vote_average").toString());

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
