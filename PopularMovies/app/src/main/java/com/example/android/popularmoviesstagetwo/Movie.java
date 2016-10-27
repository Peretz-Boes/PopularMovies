package com.example.android.popularmoviesstagetwo;

import android.database.Cursor;

/**
 * Created by Peretz on 2016-10-13.
 */
public class Movie {
    String mTitle;
    int mThumbnail;
    String mOverview;
    String mUserRating;
    String mReleaseDate;

    public Movie(String title,int thumbnail,String overview,String userRating,String releaseDate) {
        mTitle=title;
        mThumbnail=thumbnail;
        mOverview=overview;
        mUserRating=userRating;
        mReleaseDate=releaseDate;
    }

    public String getTitle(){
        return mTitle;
    }

    public int getThumbnail(){
        return mThumbnail;
    }

    public String getReleaseDate(){
        return mReleaseDate;
    }

    public String getOverview(){
        return mOverview;
    }

    public String getUserRating(){
        return mUserRating;
    }

    public static Movie fromCursor(Cursor cursor){
        Movie movie=new Movie();
        return movie;
    }
}
