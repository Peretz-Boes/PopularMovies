package com.example.android.popularmoviesstagetwo;

/**
 * Created by Peretz on 2016-12-22.
 */
public class Movie {

    String overview;
    String title;
    String date;
    String posterPath;
    String rating;

    public Movie(String date, String overview, String posterPath, String rating, String title) {
        this.date = date;
        this.overview = overview;
        this.posterPath = posterPath;
        this.rating = rating;
        this.title = title;
    }
}
