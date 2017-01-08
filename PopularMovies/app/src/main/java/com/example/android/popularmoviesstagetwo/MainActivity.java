package com.example.android.popularmoviesstagetwo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.BundleCallback {

    private static final String DETAIL_FRAGMENT_TAG="DFTAG";
    private boolean isATablet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(findViewById(R.id.movie_details)!=null){
            isATablet=true;
            if(savedInstanceState==null){
                getSupportFragmentManager().beginTransaction().replace(R.id.movie_details,new DetailActivityFragment(),DETAIL_FRAGMENT_TAG).commit();
            }else {
                isATablet=false;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(Movie movie){
        if(isATablet){
            Bundle bundle=new Bundle();
            bundle.putParcelable(ConstantVariables.MOVIE_TAG, (Parcelable) movie);
            DetailActivityFragment detailActivityFragment=new DetailActivityFragment();
            detailActivityFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.movie_details,detailActivityFragment,DETAIL_FRAGMENT_TAG).commit();
        }else {
            Intent intent=new Intent(MainActivity.this,DetailActivity.class).putExtra(ConstantVariables.MOVIE_TAG, (Parcelable) movie);
            startActivity(intent);
        }
    }

}
