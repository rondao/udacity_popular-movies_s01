package com.rondao.upopularmovies.data.db;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

public class MoviesProvider extends ContentProvider {

    public static final int CODE_MOVIES = 100;
    public static final int CODE_MOVIES_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private MoviesDbHelper mMoviesDbHelper;

    public static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieContract.PATH_MOVIES, CODE_MOVIES);

        matcher.addURI(authority, MovieContract.PATH_MOVIES + "/#", CODE_MOVIES_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mMoviesDbHelper = new MoviesDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        Cursor cursor;

        switch (sUriMatcher.match(uri)) {

            case CODE_MOVIES: {
                cursor = mMoviesDbHelper.getReadableDatabase()
                        .query(MovieContract.MovieEntry.TABLE_NAME,
                                null, null, null, null, null, null);
                break;
            }

            case CODE_MOVIES_ID: {
                String id = uri.getPathSegments().get(1);
                cursor = mMoviesDbHelper.getReadableDatabase().query(MovieContract.MovieEntry.TABLE_NAME,
                        null, MovieContract.MovieEntry._ID + "=?", new String[]{id},
                        null, null, null);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int match = sUriMatcher.match(uri);
        int tasksDeleted;

        switch (match) {
            case CODE_MOVIES_ID:
                String id = uri.getPathSegments().get(1);
                tasksDeleted = mMoviesDbHelper.getWritableDatabase().delete(MovieContract.MovieEntry.TABLE_NAME, MovieContract.MovieEntry._ID+"=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (tasksDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return tasksDeleted;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not used");
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case CODE_MOVIES:
                long id = mMoviesDbHelper.getWritableDatabase().insert(MovieContract.MovieEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Not used");
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        mMoviesDbHelper.close();
        super.shutdown();
    }
}