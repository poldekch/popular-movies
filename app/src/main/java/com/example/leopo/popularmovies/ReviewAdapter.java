package com.example.leopo.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private ArrayList<Review> mReviews;
    private Context mContext;

    public Review getReview(int i) {
        return mReviews.get(i);
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();

        int layoutIdForListItem = R.layout.review_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review pos = mReviews.get(position);
        holder.bind(pos);
    }

    public void setReviewsData(ArrayList<Review> reviewData) {
        mReviews = reviewData;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (null == mReviews) return 0;
        return mReviews.size();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView reviewAuthorView;
        TextView reviewContentView;

        public ReviewViewHolder(View itemView) {
            super(itemView);

            reviewContentView = (TextView) itemView.findViewById(R.id.tv_review_content);
            reviewAuthorView = (TextView) itemView.findViewById(R.id.tv_review_author);
        }

        void bind(Review review) {
            reviewContentView.setText(review.getContent());
            reviewAuthorView.setText(review.getAuthor() + ":");
        }
    }
}
