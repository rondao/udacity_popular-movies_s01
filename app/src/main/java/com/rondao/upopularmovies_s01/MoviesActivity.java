package com.rondao.upopularmovies_s01;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.rondao.upopularmovies_s01.data.model.Movie;
import com.rondao.upopularmovies_s01.data.source.MoviesAPI;

import java.util.ArrayList;

public class MoviesActivity extends AppCompatActivity {
    private MoviesAdapter mMoviesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

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