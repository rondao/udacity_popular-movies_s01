package com.rondao.upopularmovies.utils;

public class YouTubeHelper {
    private static final String YOUTUBE_THUMBNAIL_URL = "https://img.youtube.com/vi/%s/0.jpg";

    public static String getThumbnail(String key){
        return String.format(YOUTUBE_THUMBNAIL_URL, key);
    }
}
