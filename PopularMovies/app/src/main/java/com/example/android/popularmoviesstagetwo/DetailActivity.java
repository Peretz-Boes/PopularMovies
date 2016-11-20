package com.example.android.popularmoviesstagetwo;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class DetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        if (savedInstanceState==null){
            getSupportFragmentManager().beginTransaction().add(R.id.container,new DetailActivityFragment()).commit();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void favouriteMovie(View view){
        Button button=(Button)findViewById(R.id.favourite_movie_button);
        if(button.getText().equals("Favourite movie")){
            button.setText("Unfavourite movie");
            button.getBackground().setColorFilter(Color.CYAN, PorterDuff.Mode.MULTIPLY);
            ContentValues contentValues=new ContentValues();
            contentValues.put(MovieContract.MovieEntry.TITLES,DetailActivityFragment.title);
            contentValues.put(MovieContract.MovieEntry.THUMBNAIL_IMAGE,DetailActivityFragment.poster);
            contentValues.put(MovieContract.MovieEntry.USER_RATING,DetailActivityFragment.rating);
            contentValues.put(MovieContract.MovieEntry.RELEASE_DATE,DetailActivityFragment.date);
            contentValues.put(MovieContract.MovieEntry.USER_COMMENTS,DetailActivityFragment.comments);
            contentValues.put(MovieContract.MovieEntry.YOUTUBE_LINKS_1,DetailActivityFragment.youtubeLinks1);
            contentValues.put(MovieContract.MovieEntry.YOUTUBE_LINKS_2,DetailActivityFragment.youtubeLinks2);
            getContentResolver().insert(MovieContract.BASE_CONTENT_URI,contentValues);
        }else {
            button.setText("Favourite movie");
            button.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
            getContentResolver().delete(Uri.parse("com.example.android.popularmoviesstagetwo.MovieProvider"),"title=?",new String[]{DetailActivityFragment.title});
        }
    }
    public void playTrailer1(View view){
        Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v="+MainActivityFragment.youtubeLinks1));
        startActivity(intent);
    }
    public void playTrailer2(View view){
        Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v="+MainActivityFragment.youtubeLinks2));
        startActivity(intent);
    }
}
