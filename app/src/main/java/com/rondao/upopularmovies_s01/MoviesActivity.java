package com.rondao.upopularmovies_s01;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.rondao.upopularmovies_s01.data.model.Movie;
import com.rondao.upopularmovies_s01.data.source.MoviesAPI;

import java.util.ArrayList;

public class MoviesActivity extends AppCompatActivity implements MoviesAdapter.MovietItemClickListener {
    private RecyclerView mRecyclerView;
    private MoviesAdapter mMoviesAdapter;
    private ProgressBar mLoadingIndicator;

    private String currentSort = MoviesAPI.MOST_POPULAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.progressBar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_thumbnails);

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
        if (item.getItemId() == R.id.action_toggle_sort) {
            if (currentSort.equals(MoviesAPI.MOST_POPULAR)) {
                currentSort = MoviesAPI.TOP_RATED;
                item.setIcon(R.drawable.ic_top_rated);
            } else {
                currentSort = MoviesAPI.MOST_POPULAR;
                item.setIcon(R.drawable.ic_most_popular);
            }

            mRecyclerView.getLayoutManager().scrollToPosition(0);
            new FetchMoviesTask().execute(currentSort);
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        }

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {
            return MoviesAPI.getMovies(params[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            mMoviesAdapter.seMoviesData(movies);
            mLoadingIndicator.setVisibility(View.INVISIBLE);
        }
    }
}