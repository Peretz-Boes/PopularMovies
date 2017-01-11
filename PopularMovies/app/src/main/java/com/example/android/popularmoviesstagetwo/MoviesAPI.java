package com.example.android.popularmoviesstagetwo;

import java.util.List;

import retrofit2.Callback;
import retrofit2.http.GET;

/**
 * Created by Peretz on 2017-01-10.
 */
public interface MoviesAPI {
    @GET("movie/popular?api_key=281ad0257e71bca17a21b42c9fee7304")
    void getPopularMovies(Callback<List<Movie>> popularMoviesResponse);
    @GET("movie/top_rated?api_key=281ad0257e71bca17a21b42c9fee7304")
    void getTopRatedMovies(Callback<List<Movie>> topRatedMoviesResponse);
}
