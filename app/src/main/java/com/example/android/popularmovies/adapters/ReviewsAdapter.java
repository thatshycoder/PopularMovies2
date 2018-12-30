package com.example.android.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.Reviews;
import com.example.android.popularmovies.model.Trailers;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ReviewsAdapter extends RecyclerView.Adapter<com.example.android.popularmovies.adapters.ReviewsAdapter.ReviewsViewHolder> {

    private Context mContext;
    private ArrayList<Reviews> mReviewsList;
    private Reviews mReviews;

    public ReviewsAdapter(Context mContext, ArrayList<Reviews> reviewsList) {
        this.mContext = mContext;
        mReviewsList = reviewsList;
    }

    public void setReviewsList(ArrayList<Reviews> reviewsList) {
        mReviewsList = reviewsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public com.example.android.popularmovies.adapters.ReviewsAdapter.ReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_reviewslist, parent, false);
        return new com.example.android.popularmovies.adapters.ReviewsAdapter.ReviewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull com.example.android.popularmovies.adapters.ReviewsAdapter.ReviewsViewHolder viewHolder, int position) {

        Object getrow = this.mReviewsList.get(position);
        final LinkedTreeMap<Object, Object> t = (LinkedTreeMap) getrow;

        mReviews = new Reviews(t.get("content").toString(), t.get("author").toString());

        viewHolder.mContent.setText(t.get("content").toString());
        viewHolder.mAuthor.setText(t.get("author").toString());
    }

    @Override
    public int getItemCount() {
        if (mReviewsList != null) {
            return mReviewsList.size();
        }

        return 0;
    }

    public class ReviewsViewHolder extends RecyclerView.ViewHolder {
        public TextView mContent;
        public TextView mAuthor;

        public ReviewsViewHolder(View itemView) {
            super(itemView);

            mContent = itemView.findViewById(R.id.review_content);
            mAuthor = itemView.findViewById(R.id.review_author);
        }
    }
}
