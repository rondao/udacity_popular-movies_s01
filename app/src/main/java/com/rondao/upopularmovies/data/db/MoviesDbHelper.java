package com.rondao.upopularmovies.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.rondao.upopularmovies.data.db.MovieContract.*;
import com.rondao.upopularmovies.data.model.Movie;

public class MoviesDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 2;

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                MovieEntry._ID + " INTEGER PRIMARY KEY, " +
                MovieEntry.COLUMN_NAME + " TEXT NOT NULL" +
                "); ";

        db.execSQL(SQL_CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(db);
    }

    public Cursor queryAll() {
        return getReadableDatabase().query(MovieEntry.TABLE_NAME,
                null,null,null,null,null,null);
    }

    public Cursor queryMovie(Movie movie) {
        return getReadableDatabase().query(MovieEntry.TABLE_NAME,
                null, MovieEntry._ID+"=?", new String[]{String.valueOf(movie.getId())},
                null,null,null);
    }

    public boolean insert(Movie movie) {
        ContentValues cv = new ContentValues();
        cv.put(MovieEntry._ID, movie.getId());
        cv.put(MovieEntry.COLUMN_NAME, movie.getOriginalTitle());

        return (getWritableDatabase().insert(MovieEntry.TABLE_NAME, null, cv) > 0);
    }

    public boolean delete(Movie movie) {
        return (getWritableDatabase().delete(MovieEntry.TABLE_NAME,
                MovieEntry._ID+"=?",
                new String[]{String.valueOf(movie.getId())}) > 0);
    }
}
