package com.rondao.upopularmovies.details;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rondao.upopularmovies.R;
import com.rondao.upopularmovies.data.model.Review;

import java.util.ArrayList;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsAdapterViewHolder> {
    private ArrayList<Review> mReviewsData;

    public ReviewsAdapter() {
        mReviewsData = new ArrayList<>();
    }

    public class ReviewsAdapterViewHolder extends RecyclerView.ViewHolder {
        public final TextView mReviewNumberTextView;
        public final TextView mReviewAuthorTextView;
        public final TextView mReviewContentTextView;

        public ReviewsAdapterViewHolder(View view) {
            super(view);
            mReviewNumberTextView = view.findViewById(R.id.tv_review_number);
            mReviewAuthorTextView = view.findViewById(R.id.tv_review_author);
            mReviewContentTextView = view.findViewById(R.id.tv_review_content);
        }

        void bind(Review review, int position) {
            mReviewNumberTextView.setText(String.format("%02d", position + 1));
            mReviewAuthorTextView.setText(review.getAuthor());
            mReviewContentTextView.setText(review.getContent());
        }
    }

    @Override
    public ReviewsAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.movie_reviews, viewGroup, false);
        return new ReviewsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewsAdapterViewHolder moviesAdapterViewHolder, int position) {
        Review review = mReviewsData.get(position);
        moviesAdapterViewHolder.bind(review, position);
    }

    @Override
    public int getItemCount() {
        return mReviewsData.size();
    }

    public void setMovieReviewsData(ArrayList<Review> reviewsData) {
        mReviewsData = reviewsData;
        notifyDataSetChanged();
    }
}