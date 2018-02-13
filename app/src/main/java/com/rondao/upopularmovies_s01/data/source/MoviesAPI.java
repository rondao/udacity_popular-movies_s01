package com.rondao.upopularmovies_s01.data.source;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class MoviesAPI {
    private static final String TAG = MoviesAPI.class.getSimpleName();

    private static final String BASE_API_URL = "http://api.themoviedb.org/3";

    public static final String MOST_POPULAR = "/movie/popular";
    public static final String TOP_RATED = "/movie/top_rated";

    private static final String API_KEY_PARAM = "api_key";

    public static String queryMovies(String query) {
        String jsonResult = "";

        Uri queryUri = Uri.parse(BASE_API_URL + query).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, TMDB.getKey())
                .build();

        URL queryUrl = null;
        try {
            queryUrl = new URL(queryUri.toString());
        } catch (MalformedURLException e) {
            return jsonResult;
        }

        try {
            jsonResult = getResponseFromHttpUrl(queryUrl);
        } catch (IOException e) {
            return jsonResult;
        }

        return jsonResult;
    }

    private static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
