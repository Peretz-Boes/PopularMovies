package com.example.android.popularmoviesstagetwo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;

import com.example.android.popularmoviesstagetwo.model.Movie;
import com.example.android.popularmoviesstagetwo.utils.Constants;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.BundleCallback{

    private static final String DETAIL_FRAGMENT_TAG="DFTAG";
    private boolean isATablet;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.movie_detail_container)!=null){
            isATablet=true;
            if (savedInstanceState==null){
                getSupportFragmentManager().beginTransaction().replace(R.id.movie_detail_container,new DetailActivityFragment(),DETAIL_FRAGMENT_TAG).commit();
            }
        }else {
            isATablet=false;
        }
    }

    @Override
    public void onItemSelected(Movie movie) {
        if(isATablet){
            Bundle bundle=new Bundle();
            bundle.putParcelable(Constants.MOVIE_TAG, (Parcelable) movie);
            DetailActivityFragment detailActivityFragment=new DetailActivityFragment();
            detailActivityFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.movie_detail_container,detailActivityFragment,DETAIL_FRAGMENT_TAG).commit();
        }else {
            Intent intent=new Intent(this,DetailActivity.class);
            intent.putExtra(Constants.MOVIE_TAG,movie);
            startActivity(intent);
        }
    }
}
