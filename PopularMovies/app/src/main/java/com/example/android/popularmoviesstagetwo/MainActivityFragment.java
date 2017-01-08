package com.example.android.popularmoviesstagetwo;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    GridView imageGridView;
    TextView noMoviesMessageTextView;
    ArrayList<Movie> moviesDataList;

    public MainActivityFragment() {

    }

    public interface BundleCallback{
        void onItemSelected(Movie movie);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        imageGridView = (GridView) rootView.findViewById(R.id.image_grid_view);
        noMoviesMessageTextView = (TextView) rootView.findViewById(R.id.noMovieMessageTextView);
        loadMoviesData();
        return rootView;
    }

    private void loadMoviesData() {
        if (isInternetAvailable()) {
            ImageLoader imageLoader = new ImageLoader();
            imageLoader.execute();
        }
    }

    public boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public class ImageLoader extends AsyncTask<Void, Void, ArrayList<Movie>> {

        @Override
        protected ArrayList<Movie> doInBackground(Void... params) {
            String movieJsonString = getJsonFromHttpConnection();
            ArrayList<Movie> parsedMovieList = parseJsonData(movieJsonString);
            return parsedMovieList;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> results) {
            displayMoviesResults(results);
        }

        private void displayMoviesResults(ArrayList<Movie> results){
            if (results!=null&&results.size()>0){
                PosterAdapter posterAdapter=new PosterAdapter(getActivity(),results);
                imageGridView.setAdapter(posterAdapter);
                imageGridView.setVisibility(View.VISIBLE);
                noMoviesMessageTextView.setVisibility(View.GONE);
            }else {
                noMoviesMessageTextView.setVisibility(View.VISIBLE);
                imageGridView.setVisibility(View.GONE);
            }
        }

        public String getJsonFromHttpConnection() {

            String urlPath = "http://api.themoviedb.org/3/movie/popular?api_key=281ad0257e71bca17a21b42c9fee7304";
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieJsonString;

            try {
                URL url = new URL(urlPath);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    movieJsonString = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    movieJsonString = null;
                }
                movieJsonString = buffer.toString();
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                movieJsonString = null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
            return movieJsonString;
        }

    }

    public ArrayList<Movie> parseJsonData(String movieJsonString) {
        if (movieJsonString == null) {
            return null;
        }
        ArrayList<Movie> movieList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(movieJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            for (int count = 0; count < jsonArray.length(); count++) {
                JSONObject jsonMovie = jsonArray.getJSONObject(count);
                String overview = jsonMovie.getString("overview");
                String title = jsonMovie.getString("original_title");
                String date = jsonMovie.getString("release_date");
                String id = jsonMovie.getString("id");
                String posterPath = jsonMovie.getString("poster_path");
                String rating = jsonMovie.getString("vote_average") + "/10";
                Movie movie = new Movie(date, overview, posterPath, rating, title);
                movieList.add(movie);
            }
        } catch (Exception e) {
            System.out.println("Error");
        }
        return movieList;
    }
}
