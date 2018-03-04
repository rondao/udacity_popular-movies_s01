package com.rondao.upopularmovies.details;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rondao.upopularmovies.R;
import com.rondao.upopularmovies.data.model.MovieReview;

import java.util.ArrayList;

public class MovieReviewsAdapter extends RecyclerView.Adapter<MovieReviewsAdapter.MovieReviewsAdapterViewHolder> {
    private ArrayList<MovieReview> mMovieReviewsData;

    public MovieReviewsAdapter() {
        mMovieReviewsData = new ArrayList<>();
    }

    public class MovieReviewsAdapterViewHolder extends RecyclerView.ViewHolder {
        public final TextView mReviewNumberTextView;
        public final TextView mReviewAuthorTextView;
        public final TextView mReviewContentTextView;

        public MovieReviewsAdapterViewHolder(View view) {
            super(view);
            mReviewNumberTextView = view.findViewById(R.id.tv_review_number);
            mReviewAuthorTextView = view.findViewById(R.id.tv_review_author);
            mReviewContentTextView = view.findViewById(R.id.tv_review_content);
        }

        void bind(MovieReview movieReview, int position) {
            mReviewNumberTextView.setText(String.format("%02d", position + 1));
            mReviewAuthorTextView.setText(movieReview.getAuthor());
            mReviewContentTextView.setText(movieReview.getContent());
        }
    }

    @Override
    public MovieReviewsAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.movie_reviews, viewGroup, false);
        return new MovieReviewsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieReviewsAdapterViewHolder moviesAdapterViewHolder, int position) {
        MovieReview movieReview = mMovieReviewsData.get(position);
        moviesAdapterViewHolder.bind(movieReview, position);
    }

    @Override
    public int getItemCount() {
        return mMovieReviewsData.size();
    }

    public void setMovieReviewsData(ArrayList<MovieReview> movieReviewsData) {
        mMovieReviewsData = movieReviewsData;
        notifyDataSetChanged();
    }
}