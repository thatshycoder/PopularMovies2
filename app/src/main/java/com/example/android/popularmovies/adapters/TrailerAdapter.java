package com.example.android.popularmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.example.android.popularmovies.model.Trailers;
import com.google.gson.internal.LinkedTreeMap;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class TrailerAdapter extends RecyclerView.Adapter<com.example.android.popularmovies.adapters.TrailerAdapter.TrailerViewHolder> {

    private Context mContext;
    private ArrayList<Trailers> mTrailerList;
    private Trailers mTrailer;

    public TrailerAdapter(Context mContext, ArrayList<Trailers> trailerList) {
        this.mContext = mContext;
        mTrailerList = trailerList;
    }

    public void setTrailerList(ArrayList<Trailers> trailerList) {
        mTrailerList = trailerList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public com.example.android.popularmovies.adapters.TrailerAdapter.TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_trailergrid, parent, false);
        return new com.example.android.popularmovies.adapters.TrailerAdapter.TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull com.example.android.popularmovies.adapters.TrailerAdapter.TrailerViewHolder viewHolder, int position) {

        Object getrow = this.mTrailerList.get(position);
        final LinkedTreeMap<Object, Object> t = (LinkedTreeMap) getrow;

        final String videoKey = t.get("key").toString();

        Picasso.get()
                .load("https://img.youtube.com/vi/" + videoKey + "/0.jpg")
                .error(R.drawable.placeholder)
                .into(viewHolder.mTrailerThumbail);

        mTrailer = new Trailers(t.get("name").toString(), t.get("key").toString());

        viewHolder.mPlayButton.setImageResource(R.drawable.ic_play);

        viewHolder.mTrailerThumbail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // start intent to youtube

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + videoKey));
                Intent chooser = Intent.createChooser(intent, "Open With");

                if (intent.resolveActivity(mContext.getPackageManager()) != null) {
                    mContext.startActivity(chooser);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        if (mTrailerList != null) {
            return mTrailerList.size();
        }

        return 0;
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder {
        private ImageView mTrailerThumbail;
        private ImageView mPlayButton;

        public TrailerViewHolder(View itemView) {
            super(itemView);

            mTrailerThumbail = itemView.findViewById(R.id.trailer_thumbnail);
            mPlayButton = itemView.findViewById(R.id.ic_play);
        }
    }
}
