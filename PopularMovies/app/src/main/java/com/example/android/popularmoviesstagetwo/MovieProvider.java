package com.example.android.popularmoviesstagetwo;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Peretz on 2016-10-16.
 */
public final class MovieProvider extends ContentProvider {
    private static final String LOG_TAG=MovieProvider.class.getSimpleName();
    private static final UriMatcher sUriMatcher=buildUriMatcher();
    private MoviesDatabaseHelper moviesDatabaseHelper;

    private static final int MOVIE=100;
    private static final int MOVIE_WITH_ID=200;
    public static final SQLiteQueryBuilder sqLiteQueryBuilder;

    static {
        sqLiteQueryBuilder=new SQLiteQueryBuilder();
        sqLiteQueryBuilder.setTables(MovieContract.MovieEntry.FAVOURITED_MOVIES);
    }

    private static UriMatcher buildUriMatcher(){
        final UriMatcher matcher=new UriMatcher(UriMatcher.NO_MATCH);
        final String authority=MovieContract.CONTENT_AUTHORITY;
        matcher.addURI(authority,MovieContract.MovieEntry.FAVOURITED_MOVIES,MOVIE);
        matcher.addURI(authority,MovieContract.MovieEntry.FAVOURITED_MOVIES+"/#",MOVIE_WITH_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        moviesDatabaseHelper=new MoviesDatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.v(LOG_TAG,"Attempting to query for uri"+uri);
        final String movieQuerySelection= MovieContract.MovieEntry.FAVOURITED_MOVIES+"."+MovieContract.MovieEntry._ID+"="+MovieContract.MovieEntry.getMovieUri(uri);
        Cursor retCursor;
        switch (sUriMatcher.match(uri)){
            case MOVIE :
                Log.v(LOG_TAG,"Successfully got query for all movies");
                return sqLiteQueryBuilder.query(moviesDatabaseHelper.getReadableDatabase(),null,null,null,null,null,null);
            case MOVIE_WITH_ID:
                Log.v(LOG_TAG,"Successfully got query for movie with id "+getMovieId(uri));
                return sqLiteQueryBuilder.query(moviesDatabaseHelper.getReadableDatabase(),null,movieQuerySelection,new String[]{},null,null,null);
            default:
                Log.w(LOG_TAG,"No match found for uri"+uri);
                return null;
        }

    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match=sUriMatcher.match(uri);
        switch (match){
            case MOVIE:
                return MovieContract.MovieEntry.CONTENT_DIR_TYPE;
            case MOVIE_WITH_ID:
                return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri"+uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase sqLiteDatabase=moviesDatabaseHelper.getWritableDatabase();
        Uri returnUri;
        switch (sUriMatcher.match(uri)){
            case MOVIE:
                long _id=sqLiteDatabase.insert(MovieContract.MovieEntry.FAVOURITED_MOVIES,null,values);
                if(_id>0){
                    returnUri=MovieContract.MovieEntry.buildMoviesUri(_id);
                }else {
                    throw new android.database.SQLException("Failed to insert row into"+uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri"+uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase sqLiteDatabase=moviesDatabaseHelper.getWritableDatabase();
        final int match=sUriMatcher.match(uri);
        int numberDeleted;
        switch (match){
            case MOVIE:
                numberDeleted=sqLiteDatabase.delete(MovieContract.MovieEntry.FAVOURITED_MOVIES,selection,selectionArgs);
                break;
            case MOVIE_WITH_ID:
                numberDeleted=sqLiteDatabase.delete(MovieContract.MovieEntry.FAVOURITED_MOVIES,MovieContract.MovieEntry._ID+"=?",new String[]{String.valueOf(ContentUris.parseId(uri))});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri"+uri);
        }
        return numberDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        SQLiteDatabase sqLiteDatabase=moviesDatabaseHelper.getWritableDatabase();
        int numberUpdated;
        if (contentValues==null){
            throw new IllegalArgumentException("Cannot have null content values");
        }
        switch (sUriMatcher.match(uri)){
            case MOVIE:
                numberUpdated=sqLiteDatabase.update(MovieContract.MovieEntry.FAVOURITED_MOVIES,contentValues,selection,selectionArgs);
                break;
            case MOVIE_WITH_ID:
                numberUpdated=sqLiteDatabase.update(MovieContract.MovieEntry.FAVOURITED_MOVIES,contentValues,MovieContract.MovieEntry._ID+"=?",new String[]{String.valueOf(ContentUris.parseId(uri))});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri"+uri);
        }
        if (numberUpdated>0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return numberUpdated;
    }

    private String getMovieId(Uri uri){
        return uri.getLastPathSegment();
    }

}
