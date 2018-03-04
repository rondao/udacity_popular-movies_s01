package com.rondao.upopularmovies.details;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.rondao.upopularmovies.R;
import com.rondao.upopularmovies.data.model.Movie;
import com.rondao.upopularmovies.data.model.MovieReview;
import com.rondao.upopularmovies.data.model.MovieTrailer;
import com.rondao.upopularmovies.data.source.MoviesAPI;
import com.rondao.upopularmovies.utils.YouTubeHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieDetailsActivity extends AppCompatActivity implements MovieTrailersAdapter.MovieTrailerItemClickListener {

    private RecyclerView mRecyclerViewReviews;
    private MovieReviewsAdapter mMovieReviewsAdapter;

    private RecyclerView mRecyclerViewTrailers;
    private MovieTrailersAdapter mMovieTrailersAdapter;

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

        mMovieReviewsAdapter = new MovieReviewsAdapter();
        mMovieTrailersAdapter = new MovieTrailersAdapter(this);

        mRecyclerViewReviews = findViewById(R.id.recyclerview_reviews);
        mRecyclerViewReviews.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerViewReviews.setHasFixedSize(true);
        mRecyclerViewReviews.setAdapter(mMovieReviewsAdapter);

        mRecyclerViewTrailers = findViewById(R.id.recyclerview_trailers);
        mRecyclerViewTrailers.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerViewTrailers.setHasFixedSize(true);
        mRecyclerViewTrailers.setAdapter(mMovieTrailersAdapter);

        new FetchMovieReviewsTask().execute(movie.getId());
        new FetchMovieTrailersTask().execute(movie.getId());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movies_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_favorite_movie) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMovieTrailerItemClick(MovieTrailer movieTrailer) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, YouTubeHelper.getAppUri(movieTrailer.getKey()));
        Intent webIntent = new Intent(Intent.ACTION_VIEW, YouTubeHelper.getWebUri(movieTrailer.getKey()));

        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

    class FetchMovieReviewsTask extends AsyncTask<Integer, Void, ArrayList<MovieReview>> {
        @Override
        protected ArrayList<MovieReview> doInBackground(Integer... params) {
            return MoviesAPI.getMovieReviews(params[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<MovieReview> movieReviews) {
            if (movieReviews != null) {
                mMovieReviewsAdapter.setMovieReviewsData(movieReviews);
            }
        }
    }

    class FetchMovieTrailersTask extends AsyncTask<Integer, Void, ArrayList<MovieTrailer>> {
        @Override
        protected ArrayList<MovieTrailer> doInBackground(Integer... params) {
            return MoviesAPI.getMovieTrailers(params[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<MovieTrailer> movieTrailers) {
            if (movieTrailers != null) {
                mMovieTrailersAdapter.setMovieTrailersData(movieTrailers);
            }
        }
    }
}
