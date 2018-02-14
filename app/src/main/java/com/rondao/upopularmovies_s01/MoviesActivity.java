package com.rondao.upopularmovies_s01;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.rondao.upopularmovies_s01.data.model.Movie;
import com.rondao.upopularmovies_s01.data.source.MoviesAPI;

import java.util.ArrayList;

public class MoviesActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private MoviesAdapter mMoviesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_thumbnails);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.setHasFixedSize(true);

        mMoviesAdapter = new MoviesAdapter();
        mRecyclerView.setAdapter(mMoviesAdapter);

        new FetchMoviesTask().execute(MoviesAPI.MOST_POPULAR);
    }

    class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<Movie>> {
        @Override
        protected ArrayList<Movie> doInBackground(String... params) {
            return MoviesAPI.getMovies(params[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            mMoviesAdapter.seMoviesData(movies);
        }
    }
}