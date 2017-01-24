package com.example.android.popularmoviesstagetwo.tasks;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.example.android.popularmoviesstagetwo.adapter.MovieAdapter;
import com.example.android.popularmoviesstagetwo.db.MovieContract;
import com.example.android.popularmoviesstagetwo.model.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peretz on 2017-01-17.
 */
public class FetchFavouritesAsyncTask extends AsyncTask<Void,Void,List<Movie>> {

    private static final String[] MOVIE_COLUMNS={MovieContract.MovieEntry._ID,MovieContract.MovieEntry.TITLE_COLUMN,MovieContract.MovieEntry.OVERVIEW_COLUMN,MovieContract.MovieEntry.POSTER_IMAGE_COLUMN,MovieContract.MovieEntry.VOTE_AVERAGE_COLUMN,MovieContract.MovieEntry.RELEASE_DATE_COLUMN};
    private MovieAdapter movieAdapter;
    public static final int COL_ID=0;
    public static final int COL_TITLE=1;
    public static final int COL_OVERVIEW=2;
    public static final int COL_POSTER_IMAGE=3;
    public static final int COL_VOTE_AVERAGE=4;
    public static final int COL_RELEASE_DATE=5;
    private Context context;
    private List<Movie> movies;

    public FetchFavouritesAsyncTask(Context context, MovieAdapter movieAdapter, List<Movie> movies) {
        this.context = context;
        this.movieAdapter = movieAdapter;
        this.movies = movies;
    }

    private List<Movie> getFavouritedMovieDataFromCursor(Cursor cursor){
        List<Movie> results=new ArrayList<>();
        if (cursor!=null&&cursor.moveToFirst()){
            do {
                Movie movie=new Movie(cursor.getString(COL_ID),cursor.getString(COL_TITLE),cursor.getString(COL_OVERVIEW),cursor.getString(COL_POSTER_IMAGE),cursor.getFloat(COL_VOTE_AVERAGE),cursor.getString(COL_RELEASE_DATE));
                results.add(movie);
            }while (cursor.moveToNext());
            cursor.close();
        }
        return results;
    }

    @Override
    protected List<Movie> doInBackground(Void... voids) {
        Cursor cursor=context.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,MOVIE_COLUMNS,null,null,null);
        return getFavouritedMovieDataFromCursor(cursor);
    }

    @Override
    protected void onPostExecute(List<Movie> fetchedMovies) {
        if (movies!=null){
            if (movieAdapter!=null){
                movieAdapter.clear();
                movies=new ArrayList<>();
                movies.addAll(fetchedMovies);
                for (Movie movie:movies){
                    movieAdapter.add(movie);
                }
            }
        }
    }
}
