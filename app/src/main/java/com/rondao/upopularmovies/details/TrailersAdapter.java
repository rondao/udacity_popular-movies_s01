package com.rondao.upopularmovies.details;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rondao.upopularmovies.R;
import com.rondao.upopularmovies.data.model.Trailer;
import com.rondao.upopularmovies.utils.YouTubeHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailersAdapterViewHolder> {
    private ArrayList<Trailer> mTrailersData;
    private final TrailerItemClickListener mOnClickListener;

    public TrailersAdapter(TrailerItemClickListener onClickListener) {
        mTrailersData = new ArrayList<>();
        mOnClickListener = onClickListener;
    }

    public interface TrailerItemClickListener {
        void onMovieTrailerItemClick(Trailer trailer);
    }

    public class TrailersAdapterViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        public final ImageView mMovieImageView;

        public TrailersAdapterViewHolder(View view) {
            super(view);
            mMovieImageView = view.findViewById(R.id.iv_trailers_thumbnail);

            itemView.setOnClickListener(this);
        }

        void bind(Trailer trailer) {
            Picasso.with(mMovieImageView.getContext())
                    .load(YouTubeHelper.getThumbnail(trailer.getKey()))
                    .into(mMovieImageView);
        }

        @Override
        public void onClick(View v) {
            mOnClickListener.onMovieTrailerItemClick(
                    mTrailersData.get(getAdapterPosition()));
        }
    }

    @Override
    public TrailersAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.trailers_thumbnail, viewGroup, false);
        return new TrailersAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailersAdapterViewHolder moviesAdapterViewHolder, int position) {
        Trailer trailer = mTrailersData.get(position);
        moviesAdapterViewHolder.bind(trailer);
    }

    @Override
    public int getItemCount() {
        return mTrailersData.size();
    }

    public void setMovieTrailersData(ArrayList<Trailer> trailersData) {
        mTrailersData = trailersData;
        notifyDataSetChanged();
    }
}