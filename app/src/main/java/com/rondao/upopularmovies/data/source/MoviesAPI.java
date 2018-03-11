package com.rondao.upopularmovies.data.source;

import android.net.Uri;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.rondao.upopularmovies.data.model.Movie;
import com.rondao.upopularmovies.data.model.Review;
import com.rondao.upopularmovies.data.model.Trailer;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class MoviesAPI {
    private static final String TAG = MoviesAPI.class.getSimpleName();

    private static final String BASE_API_URL = "http://api.themoviedb.org/3";

    public static final String MOST_POPULAR = "/movie/popular";
    public static final String TOP_RATED = "/movie/top_rated";

    private static final String MOVIE = "/movie/%d";
    private static final String MOVIE_REVIEWS = "/movie/%d/reviews";
    private static final String MOVIE_TRAILERS = "/movie/%d/videos";

    private static final String API_KEY_PARAM = "api_key";

    public static ArrayList<Movie> getMovies(String query) {
        return jsonToType(queryMovies(query), new TypeToken<ArrayList<Movie>>(){});
    }

    public static Movie getMovie(int movieId) {
        return jsonToType(queryMovie(movieId), new TypeToken<Movie>(){});
    }

    public static ArrayList<Review> getMovieReviews(int movieId) {
        return jsonToType(queryMovieReviews(movieId), new TypeToken<ArrayList<Review>>(){});
    }

    public static ArrayList<Trailer> getMovieTrailers(int movieId) {
        return jsonToType(queryMovieTrailers(movieId), new TypeToken<ArrayList<Trailer>>(){});
    }

    private static <T> T jsonToType(String json, TypeToken<T> typeToken) {
        JsonObject jsonRoot = new JsonParser().parse(json).getAsJsonObject();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        if (typeToken.getRawType().isAssignableFrom(ArrayList.class)) {
            return gson.fromJson(jsonRoot.getAsJsonArray("results"), typeToken.getType());
        } else {
            return gson.fromJson(jsonRoot, typeToken.getType());
        }
    }

    private static String queryMovies(String query) {
        Uri queryUri = Uri.parse(BASE_API_URL + query).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, TMDB.getKey())
                .build();

        return queryUri(queryUri);
    }

    private static String queryMovie(int movieId) {
        Uri queryUri = Uri.parse(BASE_API_URL + String.format(MOVIE, movieId)).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, TMDB.getKey())
                .build();

        return queryUri(queryUri);
    }

    private static String queryMovieReviews(int movieId) {
        Uri queryUri = Uri.parse(BASE_API_URL + String.format(MOVIE_REVIEWS, movieId)).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, TMDB.getKey())
                .build();

        return queryUri(queryUri);
    }

    private static String queryMovieTrailers(int movieId) {
        Uri queryUri = Uri.parse(BASE_API_URL + String.format(MOVIE_TRAILERS, movieId)).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, TMDB.getKey())
                .build();

        return queryUri(queryUri);
    }

    private static String queryUri(Uri queryUri) {
        String jsonResult = "{}";

        URL queryUrl;
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
