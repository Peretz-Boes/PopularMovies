package com.example.android.popularmoviesstagetwo;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private List<Movie>movies;
    private MovieAdapter mMovieAdapter;
    TextView noMoviesTextView;
    private MovieRetrofitInterface movieRetrofitInterface;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        View rootView=inflater.inflate(R.layout.fragment_main,container,false);
        GridView movieGrid=(GridView)rootView.findViewById(R.id.movie_grid);
        noMoviesTextView=(TextView)rootView.findViewById(R.id.noMovieMessageTextView);
        movies=new ArrayList<>();
        movieRetrofitInterface=MovieClient.createService(MovieRetrofitInterface.class);
            mMovieAdapter = new MovieAdapter(getActivity(), movies);
            movieGrid.setAdapter(mMovieAdapter);
        if (isOnline()){
            getMovies(Constants.APIConstants.SORT_BY_POPULARITY);
            noMoviesTextView.setVisibility(View.GONE);
        }else {
            noMoviesTextView.setVisibility(View.VISIBLE);
        }
        return rootView;
    }

    public boolean isOnline(){
        ConnectivityManager connectivityManager=(ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if (networkInfo==null||!networkInfo.isConnected()||!networkInfo.isAvailable()){
            Toast.makeText(getContext(),getString(R.string.internet_connection_message),Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void getMovies(String sortOrder){
        Call<MoviesListResponse>moviesListResponseCall=movieRetrofitInterface.getMovies(sortOrder);
        moviesListResponseCall.enqueue(new Callback<MoviesListResponse>() {
            @Override
            public void onResponse(Call<MoviesListResponse> call, Response<MoviesListResponse> response) {
                if (response!=null&&response.body()!=null){
                    List<Movie> movieList=response.body().getResults();
                    mMovieAdapter.clear();
                    for (Movie movie:movieList){
                        mMovieAdapter.add(movie);
                    }
                }else {
                    Toast.makeText(getContext(),"Cannot access movies list",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MoviesListResponse> call, Throwable t) {
                Toast.makeText(getContext(),getString(R.string.internet_connection_message),Toast.LENGTH_SHORT).show();
            }
        });
    }

}
