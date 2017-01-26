package com.example.android.popularmoviesstagetwo;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmoviesstagetwo.adapter.MovieAdapter;
import com.example.android.popularmoviesstagetwo.model.Movie;
import com.example.android.popularmoviesstagetwo.model.MoviesListResponse;
import com.example.android.popularmoviesstagetwo.network.MovieClient;
import com.example.android.popularmoviesstagetwo.network.MovieRetrofitInterface;
import com.example.android.popularmoviesstagetwo.tasks.FetchFavouritesAsyncTask;
import com.example.android.popularmoviesstagetwo.utils.Constants;

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
    private String sortCriteria= Constants.APIConstants.SORT_BY_POPULARITY;
    private MenuItem menuItemSortByMostPopular;
    private MenuItem menuItemSortByRating;
    private MenuItem menuItemSortByFavourites;
    private static final String MAIN_ACTIVITY_FRAGMENT_KEY="main_activity_fragment_key";
    private MainActivityFragment mainActivityFragmentInstance;

    public MainActivityFragment(){
        setHasOptionsMenu(true);
    }

    public interface BundleCallback{
        void onItemSelected(Movie movie);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main_fragment,menu);
        menuItemSortByMostPopular=menu.findItem(R.id.action_sort_by_most_popular);
        menuItemSortByRating=menu.findItem(R.id.action_sort_by_rating);
        menuItemSortByFavourites=menu.findItem(R.id.action_sort_by_favourites);
        if (sortCriteria.contentEquals(Constants.APIConstants.SORT_BY_POPULARITY)){
            if (!menuItemSortByMostPopular.isChecked()){
                menuItemSortByMostPopular.setChecked(true);
            }
        }else if (sortCriteria.contentEquals(Constants.APIConstants.SORT_BY_RATING)){
            if (!menuItemSortByRating.isChecked()){
                menuItemSortByRating.setChecked(true);
            }
        }else if (sortCriteria.contentEquals(Constants.APIConstants.SORT_BY_FAVOURITES)){
            if (!menuItemSortByFavourites.isChecked()){
                menuItemSortByFavourites.setChecked(true);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.action_sort_by_most_popular:
                if (isOnline()){
                    sortCriteria=Constants.APIConstants.SORT_BY_POPULARITY;
                    getMovies(sortCriteria);
                    if (!menuItemSortByMostPopular.isChecked()){
                        menuItemSortByMostPopular.setChecked(true);
                    }
                }
                return true;
            case R.id.action_sort_by_rating:
                if (isOnline()){
                    sortCriteria=Constants.APIConstants.SORT_BY_RATING;
                    getMovies(sortCriteria);
                    if (!menuItemSortByRating.isChecked()){
                        menuItemSortByRating.setChecked(true);
                    }
                }
                return true;
            case R.id.action_sort_by_favourites:
                sortCriteria=Constants.APIConstants.SORT_BY_FAVOURITES;
                if (!menuItemSortByFavourites.isChecked()){
                    menuItemSortByFavourites.setChecked(true);
                }
                new FetchFavouritesAsyncTask(getContext(),mMovieAdapter,movies).execute();
                return true;
            default:
                return true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        View rootView=inflater.inflate(R.layout.fragment_main,container,false);
        GridView movieGrid=(GridView)rootView.findViewById(R.id.movie_grid);
        noMoviesTextView=(TextView)rootView.findViewById(R.id.noMovieMessageTextView);
        movies=new ArrayList<>();
        movieRetrofitInterface= MovieClient.createService(MovieRetrofitInterface.class);
            mMovieAdapter = new MovieAdapter(getActivity(), movies);
            movieGrid.setAdapter(mMovieAdapter);
        movieGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie selectedMovie=movies.get(position);
                showDetailScreenForMovie(selectedMovie);
            }
        });
        if (isOnline()){
            getMovies(Constants.APIConstants.SORT_BY_POPULARITY);
            noMoviesTextView.setVisibility(View.GONE);
        }else {
            noMoviesTextView.setVisibility(View.VISIBLE);
        }
        return rootView;
    }

    private void showDetailScreenForMovie(Movie movie){
        Intent intent=new Intent(getActivity(),DetailActivity.class);
        intent.putExtra(Constants.MOVIE_TAG,movie);
        startActivity(intent);
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
            public void onResponse(Response<MoviesListResponse> response) {
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
            public void onFailure(Throwable t) {
                Toast.makeText(getContext(),getString(R.string.internet_connection_message),Toast.LENGTH_SHORT).show();
            }
        });
    }

}
