package com.example.android.popularmoviesstagetwo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    static GridView imageGridView;
    static ArrayList<String> images;
    static int deviceWidth;
    static boolean sortByPopular = true;
    static ArrayList<String> overview;
    static ArrayList<String> title;
    static ArrayList<String> date;
    static ArrayList<String> rating;
    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    static PreferenceChangeListener preferenceChangeListener;
    static SharedPreferences sharedPreferences;
    static boolean sortByFavouriteMovies;
    static ArrayList<String> favouritePosters;
    static ArrayList<String> favouriteTitles;
    static ArrayList<String> favouriteDates;
    static ArrayList<String> favouriteOverviews;
    static ArrayList<ArrayList<String>> favouriteComments;
    static ArrayList<String> favouriteYoutubeLinks;
    static ArrayList<String> favouriteYoutubeLinks2;
    static ArrayList<String> favouriteRatings;
    static ArrayList<String> id;
    static ArrayList<Boolean> favouritedMovies;
    static Boolean sortByPopularValue;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        WindowManager windowManager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        if (MainActivity.IS_A_TABLET) {
            deviceWidth = point.x / 6;
        } else {
            deviceWidth = point.x / 3;
        }
        if (getActivity() != null) {
            ArrayList<String> arrayList = new ArrayList<String>();
            PosterAdapter imageAdapter = new PosterAdapter(getActivity(), arrayList, deviceWidth);
            imageGridView = (GridView) rootView.findViewById(R.id.image_grid_view);
            imageGridView.setColumnWidth(deviceWidth);
            imageGridView.setAdapter(imageAdapter);
        }
        imageGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!sortByFavouriteMovies) {
                    favouritedMovies = bindFavouritesDataToMoviesData();
                    Intent intent = new Intent(getActivity(), DetailActivity.class)
                            .putExtra("overview", overview.get(position))
                            .putExtra("poster", images.get(position))
                            .putExtra("title", title.get(position))
                            .putExtra("date", date.get(position))
                            .putExtra("rating", rating.get(position))
                            .putExtra("favourited", favouritedMovies.get(position));
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), DetailActivity.class)
                            .putExtra("overview", overview.get(position))
                            .putExtra("poster", images.get(position))
                            .putExtra("title", title.get(position))
                            .putExtra("date", date.get(position))
                            .putExtra("rating", rating.get(position))
                            //.putExtra("comments", userComments.get(position))
                            .putExtra("favourited", favouritedMovies.get(position));
                    startActivity(intent);
                }

            }
        });
        return rootView;
    }

    private class PreferenceChangeListener implements SharedPreferences.OnSharedPreferenceChangeListener {

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            imageGridView.setAdapter(null);
            connectToInternet();
        }
    }

    public ArrayList<Boolean>bindFavouritesDataToMoviesData(){
        ArrayList<Boolean>results=new ArrayList<>();
            for (int i = 0; i < title.size(); i++) {
                results.add(false);
            }
        if (favouriteTitles!=null) {
            for (String favouritedTitles : favouriteTitles) {
                for (int i = 0; i < favouriteTitles.size(); i++) {
                    if (favouritedTitles.equals(title.get(i))) {
                        results.set(i, true);
                    }
                }
            }
        }
        return results;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (favouritedMovies!=null){
            loadFavouriteMoviesData();
            Log.v(LOG_TAG,"Calling load favourited movies data");
        }
        if (images == null) {
            connectToInternet();
        }
    }

    public boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public class ImageLoader extends AsyncTask<Void, Void, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            try {
                images = new ArrayList<>(Arrays.asList(getMovieDataFromApi(sortByPopular)));
                return images;
            } catch (Exception e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<String> results) {
            if (results != null) {
                PosterAdapter posterAdapter = new PosterAdapter(getActivity(), results, deviceWidth);
                imageGridView.setAdapter(posterAdapter);
            }
        }

        public String[] getMovieDataFromApi(boolean sortByPopular) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieJsonString;

            try {
                URL url;
                if (sortByPopular) {
                    url = new URL("http://api.themoviedb.org/3/movie/popular?api_key=281ad0257e71bca17a21b42c9fee7304");
                    sortByPopularValue=true;
                } else {
                    url = new URL("http://api.themoviedb.org/3/movie/top_rated?api_key=281ad0257e71bca17a21b42c9fee7304");
                    sortByPopularValue=false;
                }


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
                try {
                    overview = new ArrayList<>(Arrays.asList(getStringDataFromJSON(movieJsonString,"overview")));
                    title = new ArrayList<>(Arrays.asList(getStringDataFromJSON(movieJsonString, "original_title")));
                    rating = new ArrayList<>(Arrays.asList(getStringDataFromJSON(movieJsonString, "vote_average")));
                    date=new ArrayList<>(Arrays.asList(getStringDataFromJSON(movieJsonString,"release_date")));
                    id=new ArrayList<>(Arrays.asList(getStringDataFromJSON(movieJsonString, "id")));
                    return getMovieDataFromJson(movieJsonString);
                } catch (Exception e) {
                    System.out.println("Error");
                }
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
            try {
                return getMovieDataFromJson(movieJsonString);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
            }
            return null;
        }

    }

    public String[] getMovieDataFromJson(String movieJsonStringParameter) throws JSONException {
        JSONObject jsonObject = new JSONObject(movieJsonStringParameter);
        JSONArray jsonArray = jsonObject.getJSONArray("results");
        String[] results = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject poster = jsonArray.getJSONObject(i);
            String posterPath = poster.getString("poster_path");
            results[i] = posterPath;
        }
        return results;
    }

    public void connectToInternet(){
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getActivity());
        preferenceChangeListener=new PreferenceChangeListener();
        sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceChangeListener);
        if (sharedPreferences.getString(getString(R.string.pref_order_key), "").equals(getResources().getString(R.string.pref_order_most_popular))){
            sortByPopular=true;
            sortByFavouriteMovies=false;
        }else if (sharedPreferences.getString(getString(R.string.pref_order_key),"").equals(getResources().getString(R.string.pref_order_top_rated))){
            sortByPopular=false;
            sortByFavouriteMovies=false;
        }else if (sharedPreferences.getString(getString(R.string.pref_order_key),"").equals(getResources().getString(R.string.pref_show_favourited_movies))){
            sortByPopular=false;
            sortByFavouriteMovies=true;
        }
        TextView noFavouritedMoviestextView=new TextView(getActivity());
        RelativeLayout relativeLayout=(RelativeLayout)getActivity().findViewById(R.id.fragment_layout);
        if (sortByFavouriteMovies){
            if (favouritePosters!=null) {
                if (favouritePosters.size() == 0) {
                    noFavouritedMoviestextView.setText("You have not favourited any movies");
                    if (favouritePosters != null && getActivity() != null) {
                        PosterAdapter posterAdapter = new PosterAdapter(getActivity(), favouritePosters, deviceWidth);
                        imageGridView.setAdapter(posterAdapter);
                    }
                } else {
                    imageGridView.setVisibility(View.VISIBLE);
                    relativeLayout.removeView(noFavouritedMoviestextView);
                }
            }else {
                noFavouritedMoviestextView.setText("Your list of favourited movies is null");
            }
        }

        ImageLoader imageLoader=new ImageLoader();
        if (isInternetAvailable()) {
            imageLoader.execute();
            Log.v(LOG_TAG,"async task completed");
        }else {
            TextView textView=new TextView(getActivity());
            textView.setText("There is no Internet service");
            if (relativeLayout.getChildCount()==1){
                relativeLayout.addView(textView);
            }
            imageGridView.setVisibility(GridView.GONE);
        }
    }
    public void loadFavouriteMoviesData(){
        String contentProviderURL="content://com.example.android.popularmoviesstagetwo.MovieProvider";
        Uri favouritedMoviesUri=Uri.parse(contentProviderURL);
        Cursor cursor=getActivity().getContentResolver().query(favouritedMoviesUri,null,null,null,"title");//error at this line
        favouritePosters=new ArrayList<>();
        favouriteComments=new ArrayList<>();
        favouriteDates=new ArrayList<>();
        favouriteOverviews=new ArrayList<>();
        favouritedMovies=new ArrayList<>();
        favouriteTitles=new ArrayList<>();
        favouriteYoutubeLinks=new ArrayList<>();
        favouriteYoutubeLinks2=new ArrayList<>();
        favouriteRatings=new ArrayList<>();
        if (cursor==null){
            return;
        }
        while (cursor.moveToNext()){
            favouritePosters.add(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.TITLES)));
            favouriteComments.add(makeStringArrayList(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.TITLES))));
            favouriteTitles.add(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.TITLES)));
            favouriteOverviews.add(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.TITLES)));
            favouriteYoutubeLinks.add(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.TITLES)));
            favouriteYoutubeLinks2.add(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.TITLES)));
            favouriteDates.add(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.TITLES)));
            favouriteRatings.add(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.TITLES)));
            favouritedMovies.add(true);
        }
    }
    public ArrayList<String>makeStringArrayList(String string){
        ArrayList<String> results=new ArrayList<>(Arrays.asList(string.split("divider 123")));
        return results;
    }

    public String[]getStringDataFromJSON(String JSONStringParameter,String parameter) throws JSONException{
        JSONObject jsonObject=new JSONObject(JSONStringParameter);
        JSONArray jsonArray=jsonObject.getJSONArray("results");
        String[]results=new String[jsonArray.length()];
        for(int i=0;i<jsonArray.length();i++){
            JSONObject object=jsonArray.getJSONObject(i);
            if (parameter.equals("vote_average")) {
                Double voteAverage = object.getDouble("vote_average");
                String movieRating = Double.toString(voteAverage) + "/10";
                results[i]=movieRating;
            }else {
                String movieData=object.getString(parameter);
                results[i]=movieData;
            }
        }
        return results;
    }

}
