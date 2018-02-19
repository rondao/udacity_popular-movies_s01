package com.rondao.upopularmovies_s01;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rondao.upopularmovies_s01.data.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder> {
    private ArrayList<Movie> mMoviesData;
    private final MovieItemClickListener mOnClickListener;

    public MoviesAdapter(MovieItemClickListener onClickListener) {
        mMoviesData = new ArrayList<>();
        mOnClickListener = onClickListener;
    }

    public interface MovieItemClickListener {
        void onMovieItemClick(Movie movie);
    }

    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder
            implements OnClickListener {
        public final ImageView mMovieImageView;

        public MoviesAdapterViewHolder(View view) {
            super(view);
            mMovieImageView = view.findViewById(R.id.iv_movie_thumbnail);

            itemView.setOnClickListener(this);
        }

        void bind(Movie movie) {
            Picasso.with(mMovieImageView.getContext())
                    .load(movie.getPosterFullPath())
                    .into(mMovieImageView);
        }

        @Override
        public void onClick(View view) {
            mOnClickListener.onMovieItemClick(
                    mMoviesData.get(getAdapterPosition()));
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

    public void setMoviesData(ArrayList<Movie> moviesData) {
        mMoviesData = moviesData;
        notifyDataSetChanged();
    }
}