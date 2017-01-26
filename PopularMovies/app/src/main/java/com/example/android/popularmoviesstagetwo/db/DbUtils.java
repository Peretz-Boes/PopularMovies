package com.example.android.popularmoviesstagetwo.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.android.popularmoviesstagetwo.model.Movie;

/**
 * Created by Peretz on 2017-01-15.
 */
public class DbUtils {

    public static ContentValues toContentValues(Movie movie){
        ContentValues contentValues=new ContentValues();
        contentValues.put(MovieContract.MovieEntry._ID,movie.getId());
        contentValues.put(MovieContract.MovieEntry.TITLE_COLUMN,movie.getOriginalTitle());
        contentValues.put(MovieContract.MovieEntry.OVERVIEW_COLUMN,movie.getOverview());
        contentValues.put(MovieContract.MovieEntry.POSTER_IMAGE_COLUMN,movie.getPosterPath());
        contentValues.put(MovieContract.MovieEntry.RELEASE_DATE_COLUMN,movie.getRelease_date());
        contentValues.put(MovieContract.MovieEntry.VOTE_AVERAGE_COLUMN,movie.getVoteAverage());
        return contentValues;
    }

    public static boolean isFavourite(Context context,String id){
        Cursor cursor=context.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,null,MovieContract.MovieEntry._ID+" = ?",new String[]{id},null);
        if (cursor!=null){
            int numRows=cursor.getCount();
            cursor.close();
            return (numRows>0);
        }
        return false;
    }

}
