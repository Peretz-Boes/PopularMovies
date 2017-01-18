package com.example.android.popularmoviesstagetwo.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Peretz on 2017-01-15.
 */
public class MovieProvider extends ContentProvider {

    private static final UriMatcher uriMatcher=buildUriMatcher();
    private MovieDbHelper movieDbHelper;
    static final int MOVIE=100;
    static final int MOVIE_WITH_ID=101;

    static UriMatcher buildUriMatcher(){
        final UriMatcher uriMatcher1=new UriMatcher(UriMatcher.NO_MATCH);
        final String contentAuthority=MovieContract.CONTENT_AUTHORITY;
        uriMatcher1.addURI(contentAuthority,MovieContract.MovieEntry.TABLE_NAME,MOVIE);
        uriMatcher1.addURI(contentAuthority,MovieContract.MovieEntry.TABLE_NAME+"/#",MOVIE_WITH_ID);
        return uriMatcher1;
    }

    @Override
    public boolean onCreate() {
        movieDbHelper=new MovieDbHelper(getContext());
        return false;
    }

    @Override
    public void shutdown() {
        super.shutdown();
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        int match=uriMatcher.match(uri);
        switch (match) {
            case MOVIE:
            retCursor = movieDbHelper.getReadableDatabase().query(MovieContract.MovieEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
            break;
            case MOVIE_WITH_ID:
                String movieIdSelection=MovieContract.MovieEntry.TABLE_NAME+"."+MovieContract.MovieEntry._ID+"=?";
                String[] movieSelectionArgs=new String[]{MovieContract.MovieEntry.getMovieIdFromUri(uri)};
                retCursor=movieDbHelper.getReadableDatabase().query(MovieContract.MovieEntry.TABLE_NAME,projection,movieIdSelection,movieSelectionArgs,null,null,sortOrder);
                break;
            default:
                throw new UnsupportedOperationException();
        }

        if (getContext()!=null&&getContext().getContentResolver()!=null){
            retCursor.setNotificationUri(getContext().getContentResolver(),uri);
        }
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match=uriMatcher.match(uri);
        String type;
        switch (match){
            case MOVIE:
                type=MovieContract.MovieEntry.CONTENT_TYPE;
                break;
            case MOVIE_WITH_ID:
                type=MovieContract.MovieEntry.CONTENT_ITEM_TYPE;
                break;
            default:
                throw new UnsupportedOperationException();
        }
        return type;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {
        Uri returnUri;
        int match=uriMatcher.match(uri);
        SQLiteDatabase sqLiteDatabase=movieDbHelper.getWritableDatabase();
        switch (match){
            case MOVIE:
                long movieId=sqLiteDatabase.insert(MovieContract.MovieEntry.TABLE_NAME,null,contentValues);
                if (movieId!=-1){
                    returnUri=MovieContract.MovieEntry.buildMovieUri(movieId);
                }else {
                    returnUri=null;
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri"+uri);
        }
        sqLiteDatabase.close();
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int rowsDeleted;
        int match=uriMatcher.match(uri);
        SQLiteDatabase sqLiteDatabase=movieDbHelper.getWritableDatabase();
        if (selection==null){
            selection="1";
        }

        switch (match){
            case MOVIE:
                rowsDeleted=sqLiteDatabase.delete(MovieContract.MovieEntry.TABLE_NAME,selection,selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri"+uri);
        }

        if (rowsDeleted!=0){
            if (getContext()!=null&&getContext().getContentResolver()!=null){
                getContext().getContentResolver().notifyChange(uri,null);
            }
        }
        sqLiteDatabase.close();
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int rowsUpdated;
        int match=uriMatcher.match(uri);
        SQLiteDatabase sqLiteDatabase=movieDbHelper.getWritableDatabase();
        switch (match){
            case MOVIE:
                rowsUpdated=sqLiteDatabase.update(MovieContract.MovieEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri"+uri);
        }
        if (rowsUpdated!=0){
            if (getContext()!=null&&getContext().getContentResolver()!=null){
                getContext().getContentResolver().notifyChange(uri,null);
            }
        }
        sqLiteDatabase.close();
        return rowsUpdated;
    }
}
