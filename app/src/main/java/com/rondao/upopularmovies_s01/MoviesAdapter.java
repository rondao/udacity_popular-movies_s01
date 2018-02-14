package com.rondao.upopularmovies_s01;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rondao.upopularmovies_s01.data.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder> {
    private ArrayList<Movie> mMoviesData;

    public MoviesAdapter() {
        mMoviesData = new ArrayList<Movie>();
    }

    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder {
        public final ImageView mMovieImageView;

        public MoviesAdapterViewHolder(View view) {
            super(view);
            mMovieImageView = (ImageView) view.findViewById(R.id.iv_movie_thumbnail);
        }

        protected void bind(Movie movie) {
            Picasso.with(mMovieImageView.getContext())
                    .load(movie.getPosterFullPath())
                    .into(mMovieImageView);
        }
    }

    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.movies_thumbnail, viewGroup, false);
        return new MoviesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesAdapterViewHolder moviesAdapterViewHolder, int position) {
        Movie movie = mMoviesData.get(position);
        moviesAdapterViewHolder.bind(movie);
    }

    @Override
    public int getItemCount() {
        return mMoviesData.size();
    }

    public void seMoviesData(ArrayList<Movie> moviesData) {
        mMoviesData = moviesData;
        notifyDataSetChanged();
    }
}