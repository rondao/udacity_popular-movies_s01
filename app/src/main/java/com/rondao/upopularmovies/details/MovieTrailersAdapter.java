package com.rondao.upopularmovies.details;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rondao.upopularmovies.R;
import com.rondao.upopularmovies.data.model.MovieTrailer;
import com.rondao.upopularmovies.utils.YouTubeHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieTrailersAdapter extends RecyclerView.Adapter<MovieTrailersAdapter.MovieTrailersAdapterViewHolder> {
    private ArrayList<MovieTrailer> mMovieTrailersData;

    public MovieTrailersAdapter() {
        mMovieTrailersData = new ArrayList<>();
    }

    public class MovieTrailersAdapterViewHolder extends RecyclerView.ViewHolder {
        public final ImageView mMovieImageView;

        public MovieTrailersAdapterViewHolder(View view) {
            super(view);
            mMovieImageView = view.findViewById(R.id.iv_trailers_thumbnail);
        }

        void bind(MovieTrailer movieTrailer) {
            Picasso.with(mMovieImageView.getContext())
                    .load(YouTubeHelper.getThumbnail(movieTrailer.getKey()))
                    .into(mMovieImageView);
        }
    }

    @Override
    public MovieTrailersAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.trailers_thumbnail, viewGroup, false);
        return new MovieTrailersAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieTrailersAdapterViewHolder moviesAdapterViewHolder, int position) {
        MovieTrailer movieTrailer = mMovieTrailersData.get(position);
        moviesAdapterViewHolder.bind(movieTrailer);
    }

    @Override
    public int getItemCount() {
        return mMovieTrailersData.size();
    }

    public void setMovieTrailersData(ArrayList<MovieTrailer> movieTrailersData) {
        mMovieTrailersData = movieTrailersData;
        notifyDataSetChanged();
    }
}