package com.example.android.popularmoviesstagetwo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Peretz on 2017-01-15.
 */
public class MovieDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION=1;
    public static final String DATABASE_NAME="favourited_movies.db";

    public MovieDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_MOVIE_TABLE="CREATE TABLE"+ MovieContract.MovieEntry.TABLE_NAME+"("+MovieContract.MovieEntry._ID+"INTEGER PRIMARY KEY"+MovieContract.MovieEntry.TITLE_COLUMN+"TEXT NOT NULL"+MovieContract.MovieEntry.OVERVIEW_COLUMN+MovieContract.MovieEntry.POSTER_IMAGE_COLUMN+MovieContract.MovieEntry.RELEASE_DATE_COLUMN+MovieContract.MovieEntry.VOTE_AVERAGE_COLUMN+")";
        sqLiteDatabase.execSQL(CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS"+MovieContract.MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
