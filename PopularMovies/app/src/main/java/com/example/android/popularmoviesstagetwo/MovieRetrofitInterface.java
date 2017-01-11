package com.example.android.popularmoviesstagetwo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Peretz on 2017-01-11.
 */
public interface MovieRetrofitInterface {
    @GET("3/discover/movie")
    Call<MoviesListResponse> getMovies(@Query("sort_by")String sortBy);
}
