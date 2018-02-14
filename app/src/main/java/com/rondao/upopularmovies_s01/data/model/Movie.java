package com.rondao.upopularmovies_s01.data.model;

import com.google.gson.Gson;

public class Movie {
    private static final String BASE_POSTER_URL = "http://image.tmdb.org/t/p/w185";

    private String originalTitle;
    private String posterPath;
    private String overview;
    private float voteAverage;
    private String releaseDate;

    public String getPosterFullPath() {
        return BASE_POSTER_URL + getPosterPath();
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
