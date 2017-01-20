package com.example.android.popularmoviesstagetwo.network;

import com.example.android.popularmoviesstagetwo.model.MoviesListResponse;
import com.example.android.popularmoviesstagetwo.model.ReviewListResponse;
import com.example.android.popularmoviesstagetwo.model.TrailersList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Peretz on 2017-01-11.
 */
public interface MovieRetrofitInterface {
    @GET("3/discover/movie")
    Call<MoviesListResponse> getMovies(@Query("sort_by")String sortBy);
    @GET("3/movie/{id}/reviews?")
    Call<ReviewListResponse> getMovieReviews(@Path("id") String id);
    @GET("3/movie/{id}/videos?")
    Call<TrailersList> getMovieTrailers(@Path("id") String id);
}
