package com.rondao.upopularmovies.details;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
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
import com.rondao.upopularmovies.data.db.MoviesDbHelper;
import com.rondao.upopularmovies.data.model.Movie;
import com.rondao.upopularmovies.data.model.Review;
import com.rondao.upopularmovies.data.model.Trailer;
import com.rondao.upopularmovies.data.source.MoviesAPI;
import com.rondao.upopularmovies.utils.YouTubeHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieDetailsActivity extends AppCompatActivity implements TrailersAdapter.TrailerItemClickListener {

    private MoviesDbHelper moviesDbHelper;
    private Movie currentMovie;

    private ReviewsAdapter mReviewsAdapter;
    private TrailersAdapter mTrailersAdapter;

    private boolean isFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        currentMovie = getIntent().getParcelableExtra("movie");

        Picasso.with(this)
                .load(currentMovie.getPosterFullPath())
                .into((ImageView) findViewById(R.id.iv_movie_poster));

        ((TextView) findViewById(R.id.tv_movie_original_title))
                .setText(currentMovie.getOriginalTitle());
        ((TextView) findViewById(R.id.tv_movie_average_rating))
                .setText(String.valueOf(currentMovie.getVoteAverage()));
        ((TextView) findViewById(R.id.tv_movie_release_date))
                .setText(currentMovie.getReleaseDate());
        ((TextView) findViewById(R.id.tv_movie_synopsis))
                .setText(currentMovie.getOverview());

        moviesDbHelper = new MoviesDbHelper(this);

        mReviewsAdapter = new ReviewsAdapter();
        mTrailersAdapter = new TrailersAdapter(this);

        RecyclerView rcReviews = findViewById(R.id.recyclerview_reviews);
        rcReviews.setLayoutManager(new LinearLayoutManager(this));
        rcReviews.setHasFixedSize(true);
        rcReviews.setAdapter(mReviewsAdapter);

        RecyclerView rcTrailers = findViewById(R.id.recyclerview_trailers);
        rcTrailers.setLayoutManager(new GridLayoutManager(this, 2));
        rcTrailers.setHasFixedSize(true);
        rcTrailers.setAdapter(mTrailersAdapter);

        new FetchMovieReviewsTask().execute(currentMovie.getId());
        new FetchMovieTrailersTask().execute(currentMovie.getId());
    }

    @Override
    protected void onDestroy() {
        currentMovie = null;
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_details_menu, menu);

        Cursor cursor = moviesDbHelper.queryMovie(currentMovie);
        try {
            if (cursor != null && cursor.getCount() == 1) {
                isFavorite = true;
                menu.findItem(R.id.action_favorite_movie)
                        .setIcon(R.drawable.ic_favorite_true);
            }
        } finally {
            cursor.close();
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_favorite_movie) {
            if (isFavorite) {
                if (moviesDbHelper.delete(currentMovie)) {
                    isFavorite = false;
                    item.setIcon(R.drawable.ic_favorite_false);
                }
            } else {
                if (moviesDbHelper.insert(currentMovie)) {
                    isFavorite = true;
                    item.setIcon(R.drawable.ic_favorite_true);
                }
            }
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
