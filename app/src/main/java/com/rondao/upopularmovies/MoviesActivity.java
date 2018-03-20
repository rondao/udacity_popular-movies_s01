package com.rondao.upopularmovies;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.os.PersistableBundle;
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

import com.rondao.upopularmovies.data.db.MovieContract;
import com.rondao.upopularmovies.data.db.MoviesDbHelper;
import com.rondao.upopularmovies.data.model.Movie;
import com.rondao.upopularmovies.data.source.MoviesAPI;
import com.rondao.upopularmovies.details.MovieDetailsActivity;

import java.util.ArrayList;

public class MoviesActivity extends AppCompatActivity implements MoviesAdapter.MovieItemClickListener {
    private RecyclerView mRecyclerView;
    private MoviesAdapter mMoviesAdapter;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessage;
    private TextView mEmptyFavorites;
    private ImageView mRefreshButton;

    private Parcelable mLayoutManagerSavedState;

    private MoviesDbHelper moviesDbHelper;

    private String currentSort = MoviesAPI.MOST_POPULAR;
    private final String FAVORITES = "Favorites";

    private static final String SAVED_LAYOUT_MANAGER = "RecyclerVIew_LayoutManager";
    private static final String SAVED_CURRENT_SORT = "CurrentSort";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        mLoadingIndicator = findViewById(R.id.progressBar);
        mErrorMessage = findViewById(R.id.tv_error_msg);
        mEmptyFavorites = findViewById(R.id.tv_empty_favorites);
        mRefreshButton = findViewById(R.id.iv_refresh);
        mRecyclerView = findViewById(R.id.recyclerview_thumbnails);

        moviesDbHelper = new MoviesDbHelper(this);

        mRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FetchMoviesTask().execute(currentSort);
            }
        });

        if (savedInstanceState != null) {
            String savedSort = savedInstanceState.getString(SAVED_CURRENT_SORT);
            if (savedSort != null) {
                currentSort = savedSort;
            }
        }

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.setHasFixedSize(true);

        mMoviesAdapter = new MoviesAdapter(this);
        mRecyclerView.setAdapter(mMoviesAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new FetchMoviesTask().execute(currentSort);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(SAVED_LAYOUT_MANAGER, mRecyclerView.getLayoutManager().onSaveInstanceState());
        outState.putString(SAVED_CURRENT_SORT, currentSort);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mLayoutManagerSavedState = savedInstanceState.getParcelable(SAVED_LAYOUT_MANAGER);
        super.onRestoreInstanceState(savedInstanceState);
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
            currentSort = FAVORITES;
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

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<Movie>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
            mErrorMessage.setVisibility(View.INVISIBLE);
            mRefreshButton.setVisibility(View.INVISIBLE);
            mEmptyFavorites.setVisibility(View.INVISIBLE);
        }

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {
            if (!isOnline()) {
                return null;
            }

            if (FAVORITES.equals(params[0])) {
                return fetchDbMovies();
            } else {
                return MoviesAPI.getMovies(params[0]);
            }
        }

        private ArrayList<Movie> fetchDbMovies() {
            Cursor cursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                    null, null, null, null);

            ArrayList<Movie> favoriteMovies = new ArrayList<>();
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    favoriteMovies.add(MoviesAPI.getMovie(cursor.getInt(
                            cursor.getColumnIndex(MovieContract.MovieEntry._ID))));
                }
                cursor.close();
            }

            return favoriteMovies;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            if (movies != null) {
                mMoviesAdapter.setMoviesData(movies);

                if (mLayoutManagerSavedState != null) {
                    mRecyclerView.getLayoutManager().onRestoreInstanceState(mLayoutManagerSavedState);
                    // Setting it to null so when changing currentSort the layout is not restored again.
                    mLayoutManagerSavedState = null;
                }

                if (movies.isEmpty() && FAVORITES.equals(currentSort)) {
                    mEmptyFavorites.setVisibility(View.VISIBLE);
                }
            } else {
                mMoviesAdapter.setMoviesData(new ArrayList<Movie>());

                mErrorMessage.setVisibility(View.VISIBLE);
                mRefreshButton.setVisibility(View.VISIBLE);
            }
            mLoadingIndicator.setVisibility(View.INVISIBLE);
        }
    }
}