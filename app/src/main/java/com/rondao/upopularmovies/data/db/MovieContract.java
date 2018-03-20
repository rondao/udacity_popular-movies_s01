package com.rondao.upopularmovies.data.db;

import android.net.Uri;
import android.provider.BaseColumns;

import com.rondao.upopularmovies.data.model.Movie;

public class MovieContract {
    public static final String CONTENT_AUTHORITY = "com.rondao.upopularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIES = "movies";

    public static final class MovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIES)
                .build();

        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_NAME = "movieName";

        public static Uri buildMoviesUriWithId(Movie movie) {
            return CONTENT_URI.buildUpon()
                    .appendPath(String.valueOf(movie.getId()))
                    .build();
        }
    }
}
