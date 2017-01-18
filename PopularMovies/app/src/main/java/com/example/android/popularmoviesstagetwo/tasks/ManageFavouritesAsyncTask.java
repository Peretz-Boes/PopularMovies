package com.example.android.popularmoviesstagetwo.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Button;

import com.example.android.popularmoviesstagetwo.R;
import com.example.android.popularmoviesstagetwo.db.DbUtils;
import com.example.android.popularmoviesstagetwo.model.Movie;

/**
 * Created by Peretz on 2017-01-17.
 */
public class ManageFavouritesAsyncTask extends AsyncTask<Void,Void,Boolean> {

    private Context context;
    private Movie movie;
    private Boolean performAction;
    private Button favouriteButton;

    public ManageFavouritesAsyncTask(Context context, Button favouriteButton, Movie movie, Boolean performAction) {
        this.context = context;
        this.favouriteButton = favouriteButton;
        this.movie = movie;
        this.performAction = performAction;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        return DbUtils.isFavourite(context,movie.getId());
    }

    @Override
    protected void onPostExecute(Boolean isFavourited) {
        if (performAction){
            new UpdateFavouritesAsyncTask(context,favouriteButton,isFavourited,movie).execute();
        }else {
            if (isFavourited){
                favouriteButton.setText(context.getString(R.string.unfavourite_movie_message));
            }else {
                favouriteButton.setText(context.getString(R.string.favourite_movie_message));
            }
        }
    }
}
