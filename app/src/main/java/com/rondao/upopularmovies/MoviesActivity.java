package com.rondao.upopularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rondao.upopularmovies.data.model.Movie;
import com.rondao.upopularmovies.data.source.MoviesAPI;
import com.rondao.upopularmovies.details.MovieDetailsActivity;

import java.util.ArrayList;

public class MoviesActivity extends AppCompatActivity implements MoviesAdapter.MovieItemClickListener {
    private RecyclerView mRecyclerView;
    private MoviesAdapter mMoviesAdapter;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessage;
    private ImageView mRefreshButton;

    private String currentSort = MoviesAPI.MOST_POPULAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        mLoadingIndicator = findViewById(R.id.progressBar);
        mErrorMessage = findViewById(R.id.tv_error_msg);
        mRefreshButton = findViewById(R.id.iv_refresh);
        mRecyclerView = findViewById(R.id.recyclerview_thumbnails);

        mRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FetchMoviesTask().execute(currentSort);
            }
        });

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.setHasFixedSize(true);

        mMoviesAdapter = new MoviesAdapter(this);
        mRecyclerView.setAdapter(mMoviesAdapter);

        new FetchMoviesTask().execute(currentSort);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movies_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_most_popular) {
            currentSort = MoviesAPI.MOST_POPULAR;
        } else if (item.getItemId() == R.id.action_top_rated) {
            currentSort = MoviesAPI.TOP_RATED;
        } else if (item.getItemId() == R.id.action_favorites) {
        }

        mRecyclerView.getLayoutManager().scrollToPosition(0);
        new FetchMoviesTask().execute(currentSort);
        return true;
    }

    @Override
    public void onMovieItemClick(Movie movie) {
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra("movie", movie);

        startActivity(intent);
    }

    class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<Movie>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
            mErrorMessage.setVisibility(View.INVISIBLE);
            mRefreshButton.setVisibility(View.INVISIBLE);
        }

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {
            return MoviesAPI.getMovies(params[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            if (movies != null) {
                mMoviesAdapter.setMoviesData(movies);
            } else {
                mErrorMessage.setVisibility(View.VISIBLE);
                mRefreshButton.setVisibility(View.VISIBLE);
            }
            mLoadingIndicator.setVisibility(View.INVISIBLE);
        }
    }
}