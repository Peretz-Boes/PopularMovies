package com.example.android.popularmoviesstagetwo;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Peretz on 2016-11-15.
 */
public class MovieContract {
    public static final String CONTENT_AUTHORITY="com.example.android.popularmoviesstagetwo.MovieProvider";
    public static final Uri BASE_CONTENT_URI=Uri.parse("content://"+CONTENT_AUTHORITY);

    public static final class MovieEntry implements BaseColumns{
        public static final String FAVOURITED_MOVIES="favourited_movies";
        public static final String _ID="_id";
        public static final String THUMBNAIL_IMAGE="thumbnail_image";
        public static final String USER_RATING="rating";
        public static final String YOUTUBE_LINKS_1="youtube_links_1";
        public static final String YOUTUBE_LINKS_2="youtube_links_2";
        public static final String USER_COMMENTS="comments";
        public static final String OVERVIEW="overview";
        public static final String TITLES="title";
        public static final String RELEASE_DATE="release date";

        public static Uri CONTENT_URI=BASE_CONTENT_URI.buildUpon().appendPath(FAVOURITED_MOVIES).build();
        public static final String CONTENT_DIR_TYPE= ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+FAVOURITED_MOVIES;
        public static final String CONTENT_ITEM_TYPE=ContentResolver.CURSOR_ITEM_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+FAVOURITED_MOVIES;

        public static Uri buildMoviesUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }

    }
}
