package com.rondao.upopularmovies.data.db;

import android.provider.BaseColumns;

public class MovieContract {
    public static final class MovieEntry implements BaseColumns {
        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_NAME = "movieName";
    }
}
