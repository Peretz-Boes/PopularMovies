package com.example.android.popularmoviesstagetwo.db;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Peretz on 2017-01-15.
 */
public class MovieContract {
    public static final String CONTENT_AUTHORITY="com.example.android.popularmoviesstagetwo.db.MovieProvider";
    public static final Uri BASE_CONTENT_URI=Uri.parse("content://"+CONTENT_AUTHORITY);

    public static final class MovieEntry implements BaseColumns {
        public static final String TABLE_NAME="favourited_movies";
        public static final Uri CONTENT_URI=BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        public static Uri buildMovieUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static final String CONTENT_TYPE= ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE=ContentResolver.CURSOR_ITEM_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+TABLE_NAME;
        public static final String TITLE_COLUMN="title";
        public static final String OVERVIEW_COLUMN="overview";
        public static final String POSTER_IMAGE_COLUMN="poster_image";
        public static final String RELEASE_DATE_COLUMN="release_date";
        public static final String VOTE_AVERAGE_COLUMN="vote_average";

        public static String getMovieIdFromUri(Uri uri){
            return uri.getPathSegments().get(1);
        }
    }
}
