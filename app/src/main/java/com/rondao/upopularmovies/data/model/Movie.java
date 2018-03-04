package com.rondao.upopularmovies.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

public class Movie implements Parcelable {
    private static final String BASE_POSTER_URL = "http://image.tmdb.org/t/p/w185";

    private int id;
    private String originalTitle;
    private String posterPath;
    private String overview;
    private float voteAverage;
    private String releaseDate;

    public Movie() {}

    private Movie(Parcel in) {
        id = in.readInt();
        originalTitle = in.readString();
        posterPath = in.readString();
        overview = in.readString();
        voteAverage = in.readFloat();
        releaseDate = in.readString();
    }

    public String getPosterFullPath() {
        return BASE_POSTER_URL + getPosterPath();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(id);
        out.writeString(originalTitle);
        out.writeString(posterPath);
        out.writeString(overview);
        out.writeFloat(voteAverage);
        out.writeString(releaseDate);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    private String getPosterPath() {
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

    @Override
    public int describeContents() {
        return 0;
    }
}
