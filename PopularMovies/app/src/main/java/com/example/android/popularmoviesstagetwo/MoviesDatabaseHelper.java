package com.example.android.popularmoviesstagetwo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by Peretz on 2016-11-15.
 */
public class MoviesDatabaseHelper extends SQLiteOpenHelper{
    public static final String LOG_TAG=MoviesDatabaseHelper.class.getSimpleName();

    public static final String DATABASE_NAME="favourited_movies.db";
    public static final int DATABASE_VERSION=12;

    public interface Tables{
        String MOVIES="movies";
    }

    public MoviesDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_MOVIES_TABLE="CREATE TABLE "+MovieContract.MovieEntry.FAVOURITED_MOVIES+"("
                + BaseColumns._ID+" INTEGER NOT NULL PRIMARY KEY,"
                +MovieContract.MovieEntry.TITLES+" TEXT NOT NULL,"
                +MovieContract.MovieEntry.OVERVIEW+" TEXT,"
                +MovieContract.MovieEntry.RELEASE_DATE+" TEXT,"
                +MovieContract.MovieEntry.THUMBNAIL_IMAGE+" TEXT,"
                +MovieContract.MovieEntry.USER_COMMENTS+" TEXT,"
                +MovieContract.MovieEntry.USER_RATING+" REAL,"
                +MovieContract.MovieEntry.YOUTUBE_LINKS_1+" TEXT,"
                +MovieContract.MovieEntry.YOUTUBE_LINKS_2+" TEXT"
                +")";
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.w(LOG_TAG, "Upgrading database from " + oldVersion + "to" + newVersion + ".OLD DATA WILL BE DESTROYED");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS" + MovieContract.MovieEntry.FAVOURITED_MOVIES);
        sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME='" + MovieContract.MovieEntry.FAVOURITED_MOVIES + "'");
        onCreate(sqLiteDatabase);
    }

}
