package com.example.android.popularmoviesstagetwo.tasks;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Button;

import com.example.android.popularmoviesstagetwo.R;
import com.example.android.popularmoviesstagetwo.db.DbUtils;
import com.example.android.popularmoviesstagetwo.db.MovieContract;
import com.example.android.popularmoviesstagetwo.model.Movie;

/**
 * Created by Peretz on 2017-01-17.
 */
public class UpdateFavouritesAsyncTask extends AsyncTask<Void,Void,Void> {

    private Context context;
    private Movie movie;
    private Boolean isFavourited;
    private Button favouritedButton;

    public UpdateFavouritesAsyncTask(Context context, Button favouritedButton, Boolean isFavourited, Movie movie) {
        this.context = context;
        this.favouritedButton = favouritedButton;
        this.isFavourited = isFavourited;
        this.movie = movie;
    }

    @Override
    protected Void doInBackground(Void... params) {
        if (isFavourited){
            ContentValues contentValues= DbUtils.toContentValues(movie);
            context.getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI,contentValues);
        }else {
            context.getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI,MovieContract.MovieEntry._ID+"=?",new String[]{movie.getId()});
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        int stringResourceToast;
        if (isFavourited){
            stringResourceToast= R.string.favourite_movie_message;
            favouritedButton.setText(context.getString(R.string.unfavourite_movie_message));
        }else {
            stringResourceToast=R.string.unfavourite_movie_message;
            favouritedButton.setText(context.getString(R.string.favourite_movie_message));
        }
    }
}
