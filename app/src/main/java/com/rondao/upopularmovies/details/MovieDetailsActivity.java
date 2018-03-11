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
import com.rondao.upopularmovies.data.model.Review;
import com.rondao.upopularmovies.data.model.Trailer;
import com.rondao.upopularmovies.data.source.MoviesAPI;
import com.rondao.upopularmovies.utils.YouTubeHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieDetailsActivity extends AppCompatActivity implements TrailersAdapter.TrailerItemClickListener {

    private RecyclerView mRecyclerViewReviews;
    private ReviewsAdapter mReviewsAdapter;

    private RecyclerView mRecyclerViewTrailers;
    private TrailersAdapter mTrailersAdapter;

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

        mReviewsAdapter = new ReviewsAdapter();
        mTrailersAdapter = new TrailersAdapter(this);

        mRecyclerViewReviews = findViewById(R.id.recyclerview_reviews);
        mRecyclerViewReviews.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerViewReviews.setHasFixedSize(true);
        mRecyclerViewReviews.setAdapter(mReviewsAdapter);

        mRecyclerViewTrailers = findViewById(R.id.recyclerview_trailers);
        mRecyclerViewTrailers.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerViewTrailers.setHasFixedSize(true);
        mRecyclerViewTrailers.setAdapter(mTrailersAdapter);

        new FetchMovieReviewsTask().execute(movie.getId());
        new FetchMovieTrailersTask().execute(movie.getId());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_details_menu, menu);
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
    public void onMovieTrailerItemClick(Trailer trailer) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, YouTubeHelper.getAppUri(trailer.getKey()));
        Intent webIntent = new Intent(Intent.ACTION_VIEW, YouTubeHelper.getWebUri(trailer.getKey()));

        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

    class FetchMovieReviewsTask extends AsyncTask<Integer, Void, ArrayList<Review>> {
        @Override
        protected ArrayList<Review> doInBackground(Integer... params) {
            return MoviesAPI.getMovieReviews(params[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<Review> reviews) {
            if (reviews != null) {
                mReviewsAdapter.setMovieReviewsData(reviews);
            }
        }
    }

    class FetchMovieTrailersTask extends AsyncTask<Integer, Void, ArrayList<Trailer>> {
        @Override
        protected ArrayList<Trailer> doInBackground(Integer... params) {
            return MoviesAPI.getMovieTrailers(params[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<Trailer> trailers) {
            if (trailers != null) {
                mTrailersAdapter.setMovieTrailersData(trailers);
            }
        }
    }
}
