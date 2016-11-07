package com.example.android.popularmoviesstagetwo;

/**
 * Created by Peretz on 2016-10-13.
 */
public class Movie{
    String mTitle;
    int mThumbnail;
    String mOverview;
    String mUserRating;
    String mReleaseDate;
    String mYoutubeVideos;

    public Movie(String title,int thumbnail,String overview,String userRating,String releaseDate,String youtubeVideos) {
        mTitle=title;
        mThumbnail=thumbnail;
        mOverview=overview;
        mUserRating=userRating;
        mReleaseDate=releaseDate;
        mYoutubeVideos=youtubeVideos;
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
    public String getYoutubeVideos(){
        return mYoutubeVideos;
    }
}
