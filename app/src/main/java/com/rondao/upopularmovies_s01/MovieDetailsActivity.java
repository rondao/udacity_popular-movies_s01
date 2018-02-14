package com.rondao.upopularmovies_s01;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.rondao.upopularmovies_s01.data.model.Movie;
import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Movie movie = getIntent().getParcelableExtra("movie");

        Picasso.with(this)
                .load(movie.getPosterFullPath())
                .into((ImageView) findViewById(R.id.iv_movie_poster));

        ((TextView) findViewById(R.id.tv_movie_original_title))
                .setText(movie.getOriginalTitle());
        ((TextView) findViewById(R.id.tv_movie_average_rating))
                .setText(String.valueOf(movie.getVoteAverage()));
        ((TextView) findViewById(R.id.tv_movie_release_date))
                .setText(movie.getReleaseDate());
        ((TextView) findViewById(R.id.tv_movie_synopsis))
                .setText(movie.getOverview());
    }
}
