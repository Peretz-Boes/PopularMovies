package com.example.android.popularmoviesstagetwo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android.popularmoviesstagetwo.model.Movie;
import com.example.android.popularmoviesstagetwo.utils.Constants;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState==null){
            Movie selectedMovie=(Movie)getIntent().getSerializableExtra(Constants.MOVIE_TAG);
            Bundle arguments=new Bundle();
            arguments.putSerializable(Constants.MOVIE_TAG,selectedMovie);
            DetailActivityFragment detailActivityFragment=new DetailActivityFragment();
            detailActivityFragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().add(R.id.movie_detail_container,detailActivityFragment).commit();
        }
    }

}
