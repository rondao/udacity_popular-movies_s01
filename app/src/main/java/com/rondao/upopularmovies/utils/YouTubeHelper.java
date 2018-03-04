package com.rondao.upopularmovies.utils;

import android.net.Uri;

public class YouTubeHelper {
    private static final String YOUTUBE_THUMBNAIL_URL = "https://img.youtube.com/vi/%s/0.jpg";
    private static final String YOUTUBE_APP = "vnd.youtube:";
    private static final String YOUTUBE_WATCH = "http://www.youtube.com/watch";
    private static final String YOUTUBE_VIDEO_PARAM = "v";

    public static String getThumbnail(String key){
        return String.format(YOUTUBE_THUMBNAIL_URL, key);
    }

    public static Uri getWebUri(String key) {
        return Uri.parse(YOUTUBE_WATCH).buildUpon()
                .appendQueryParameter(YOUTUBE_VIDEO_PARAM, key)
                .build();
    }

    public static Uri getAppUri(String key) {
        return Uri.parse(YOUTUBE_APP + key).buildUpon().build();
    }
}
