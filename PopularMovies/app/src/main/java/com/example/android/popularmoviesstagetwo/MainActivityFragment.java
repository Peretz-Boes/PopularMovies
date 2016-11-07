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
import android.widget.CursorAdapter;
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
    static ArrayList<ArrayList<String>> userComments;
    private static final int CURSOR_LOADER_ID = 0;
    PosterAdapter posterAdapter;
    private CursorAdapter cursorAdapter;
    static ArrayList<String> youtubeLinks1;
    static ArrayList<String> youtubeLinks2;

    public MainActivityFragment() {
    }

    /*@Override
    public void onActivityCreated(Bundle savedInstanceState){
        Cursor cursor=getActivity().getContentResolver().query(MovieProvider.Movies.CONTENT_URI,null,null,null,null);
        Log.i(LOG_TAG, "cursor count: " + cursor.getCount());
        if (cursor==null||cursor.getCount()==0){
            insertMovie();
        }
        getLoaderManager().initLoader(CURSOR_LOADER_ID,null,this);
        super.onActivityCreated(savedInstanceState);
    }*/
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
                            .putExtra("comments", userComments.get(position))
                            .putExtra("favourited", favouritedMovies.get(position));
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), DetailActivity.class)
                            .putExtra("overview", overview.get(position))
                            .putExtra("poster", images.get(position))
                            .putExtra("title", title.get(position))
                            .putExtra("date", date.get(position))
                            .putExtra("rating", rating.get(position))
                            .putExtra("comments", userComments.get(position))
                            .putExtra("favourited", favouritedMovies.get(position));
                    startActivity(intent);
                }

            }
        });
        return rootView;
    }

    /*public void insertMovie(){
        Log.d(LOG_TAG, "inserting movie");
        ArrayList<ContentProviderOperation>batchOperations=new ArrayList<>(favouritePosters.size());
        for (Movie movie:favouritePosters){
            ContentProviderOperation.Builder builder=ContentProviderOperation.newInsert(MovieProvider.Movies.CONTENT_URI);
            builder.withValue(MovieColumns.TITLE,movie.getTitle());
            builder.withValue(MovieColumns.RATING,movie.getUserRating());
            builder.withValue(MovieColumns.THUMBNAIL,movie.getThumbnail());
            batchOperations.add(builder.build());
        }

        try {
            getActivity().getContentResolver().applyBatch(MovieProvider.AUTHORITY,batchOperations);
        }catch (RemoteException| OperationApplicationException e){
            Log.e(LOG_TAG,"Error applying batch insert",e);
        }
    }
    */

    private class PreferenceChangeListener implements SharedPreferences.OnSharedPreferenceChangeListener {

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            imageGridView.setAdapter(null);
            connectToInternet();
        }
    }

    public ArrayList<Boolean>bindFavouritesDataToMoviesData(){
        ArrayList<Boolean>results=new ArrayList<>();
        for (int i=0;i<title.size();i++){
            results.add(false);
        }
        for(String favouritedTitles:favouriteTitles){
            for(int i=0;i<favouriteTitles.size();i++){
                if(favouritedTitles.equals(title.get(i))){
                    results.set(i,true);
                }
            }
        }
        return results;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadFavouriteMoviesData();
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
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String movieJsonString;

            try {
                URL url;
                if (sortByPopular) {
                    url = new URL("http://api.themoviedb.org/3/movie/popular?api_key=281ad0257e71bca17a21b42c9fee7304");
                } else {
                    url = new URL("http://api.themoviedb.org/3/movie/top_rated?api_key=281ad0257e71bca17a21b42c9fee7304");
                }

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    movieJsonString = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    movieJsonString = null;
                }
                movieJsonString = buffer.toString();
                try {
                    overview = new ArrayList<>(Arrays.asList(getStringDataFromJSON(movieJsonString,"overview")));
                    title = new ArrayList<>(Arrays.asList(getStringDataFromJSON(movieJsonString, "original_title")));
                    rating = new ArrayList<>(Arrays.asList(getStringDataFromJSON(movieJsonString, "vote_average")));
                    date=new ArrayList<>(Arrays.asList(getStringDataFromJSON(movieJsonString,"release_date")));
                    id=new ArrayList<>(Arrays.asList(getStringDataFromJSON(movieJsonString,"id")));
                    while (true) {
                        youtubeLinks1 = new ArrayList<>(Arrays.asList(getYoutubeVideosFromIds(id, 0)));
                        youtubeLinks2=new ArrayList<>(Arrays.asList(getYoutubeVideosFromIds(id,1)));
                        int nullCount=0;
                        for(int i=0;i<youtubeLinks1.size();i++){
                            if (youtubeLinks1.get(i)==null){
                                nullCount++;
                                youtubeLinks1.set(i,"No youtube video has been found");
                            }
                        }
                        for (int i=0;i<youtubeLinks2.size();i++){
                            if (youtubeLinks2.get(i)==null){
                                nullCount++;
                                youtubeLinks2.set(i,"No youtube video has been found");
                            }
                        }
                        if (nullCount>2){
                            break;
                        }
                        userComments=getUserReviewsFromIds(id);
                        return getMovieDataFromJson(movieJsonString);
                    }
                } catch (Exception e) {
                    System.out.println("Error");
                }
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
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

    public String[]getYoutubeVideosFromIds(ArrayList<String>id,int position){
        String[] results=new String[id.size()];
        for (int i=0;i<id.size();i++){
            while (true) {
                HttpURLConnection httpURLConnection = null;
                BufferedReader bufferedReader = null;
                String JSONResult;
                try {
                    URL url = new URL("http://api.themoviedb.org/3/movie/" + id.get(i) + "/videos?api_key=281ad0257e71bca17a21b42c9fee7304");
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.connect();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    StringBuffer stringBuffer = new StringBuffer();
                    if (inputStream == null) {
                        return null;
                    }
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String data;
                    while ((data = bufferedReader.readLine()) != null) {
                        stringBuffer.append(data + "\n");
                    }
                    if (stringBuffer.length() == 0) {
                        return null;
                    }
                    JSONResult = stringBuffer.toString();
                    Log.v(LOG_TAG, JSONResult);
                    try {
                        results[i]=getYoutubeVideosFromJSON(JSONResult,position);
                    }catch (JSONException E){
                        results[i]="no video found";
                    }
                }catch (Exception e){

                }finally {
                    if (httpURLConnection!=null){
                        httpURLConnection.disconnect();
                    }
                    if (bufferedReader!=null){
                        try {
                            bufferedReader.close();
                        }catch (final IOException e){

                        }
                    }
                }
            }
        }
        return results;
    }

    public ArrayList<ArrayList<String>>getUserReviewsFromIds(ArrayList<String>id){
        outerloop:
        while (true){
            ArrayList<ArrayList<String>>results=new ArrayList<>();
            for (int i=0;i<id.size();i++){
                HttpURLConnection httpURLConnection=null;
                BufferedReader bufferedReader=null;
                String JSONResult;
                try {
                    URL url = new URL("http://api.themoviedb.org/3/movie/" + id.get(i) + "/reviews?api_key=281ad0257e71bca17a21b42c9fee7304");
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.connect();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    StringBuffer stringBuffer = new StringBuffer();
                    if (inputStream == null) {
                        return null;
                    }
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String data;
                    while ((data = bufferedReader.readLine()) != null) {
                        stringBuffer.append(data + "\n");
                    }
                    if (stringBuffer.length() == 0) {
                        return null;
                    }
                    JSONResult = stringBuffer.toString();
                    Log.v(LOG_TAG, JSONResult);
                    try {
                        results.add(getUserReviewsFromJSON(JSONResult));
                    }catch (JSONException E){
                        return null;
                    }
                }catch (Exception e){
                    continue outerloop;
                }finally {
                    if (httpURLConnection!=null){
                        httpURLConnection.disconnect();
                    }
                    if (bufferedReader!=null){
                        try {
                            bufferedReader.close();
                        }catch (final IOException e){

                        }
                    }
                }
            }
            return results;
        }
    }
    public ArrayList<String>getUserReviewsFromJSON(String JSONStringParameter)throws JSONException{
        JSONObject jsonObject=new JSONObject(JSONStringParameter);
        JSONArray jsonArray=jsonObject.getJSONArray("results");
        ArrayList<String>results=new ArrayList<>();
        if (jsonArray.length()==0){
            results.add("There are no reviews for this movie");
            return results;
        }
        for (int i=0;i<jsonArray.length();i++){
            JSONObject resultObject=jsonArray.getJSONObject(i);
            results.add(resultObject.getString("content"));
        }
        return results;
    }

    public String getYoutubeVideosFromJSON(String JSONStringParameter,int position)throws JSONException{
        JSONObject jsonObject=new JSONObject(JSONStringParameter);
        JSONArray jsonArray=jsonObject.getJSONArray("results");
        JSONObject jsonObject1;
        String resultString="no videos have been found";
        if (position==0){
            jsonObject1=jsonArray.getJSONObject(0);
            resultString=jsonObject1.getString("key");
        }else if (position==1){
            if (jsonArray.length()>1){
                jsonObject1=jsonArray.getJSONObject(1);
            }else {
                jsonObject1=jsonArray.getJSONObject(0);
            }
            resultString=jsonObject1.getString("key");
        }
        return resultString;
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
            if (favouritePosters.size()==0){
                noFavouritedMoviestextView.setText("You have not favourited any movies");
                if (relativeLayout.getChildCount()==1){
                    relativeLayout.addView(noFavouritedMoviestextView);
                    imageGridView.setVisibility(View.GONE);
                }else {
                    imageGridView.setVisibility(View.VISIBLE);
                    relativeLayout.removeView(noFavouritedMoviestextView);
                }
                if (favouritePosters!=null&&getActivity()!=null){
                    PosterAdapter posterAdapter=new PosterAdapter(getActivity(),favouritePosters,deviceWidth);
                    imageGridView.setAdapter(posterAdapter);
                }
            }else {
                imageGridView.setVisibility(View.VISIBLE);
                relativeLayout.removeView(noFavouritedMoviestextView);
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
        Cursor cursor=getActivity().getContentResolver().query(favouritedMoviesUri,null,null,null,"title");
        favouritePosters=new ArrayList<>();
        favouriteComments=new ArrayList<>();
        favouritePosters=new ArrayList<>();
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
            favouritePosters.add(cursor.getString(cursor.getColumnIndex(MovieProvider.CONTENT_PROVIDER_NAME)));
            favouritedMovies.add(true);
            favouriteComments.add(makeStringArrayList(cursor.getString(cursor.getColumnIndex(MovieProvider.CONTENT_PROVIDER_NAME))));
            favouriteTitles.add(cursor.getString(cursor.getColumnIndex(MovieProvider.CONTENT_PROVIDER_NAME)));
            favouriteOverviews.add(cursor.getString(cursor.getColumnIndex(MovieProvider.CONTENT_PROVIDER_NAME)));
            favouriteYoutubeLinks.add(cursor.getString(cursor.getColumnIndex(MovieProvider.CONTENT_PROVIDER_NAME)));
            favouriteYoutubeLinks2.add(cursor.getString(cursor.getColumnIndex(MovieProvider.CONTENT_PROVIDER_NAME)));
            favouriteDates.add(cursor.getString(cursor.getColumnIndex(MovieProvider.CONTENT_PROVIDER_NAME)));
            favouriteRatings.add(cursor.getString(cursor.getColumnIndex(MovieProvider.CONTENT_PROVIDER_NAME)));
        }
    }
    public ArrayList<String>makeStringArrayList(String string){
        ArrayList<String> results=new ArrayList<>(Arrays.asList(string.split("divider 123")));
        return results;
    }

    public String[]getStringDataFromJSON(String JSONStringParameter,String parameter) throws JSONException{
        JSONObject jsonObject=new JSONObject(JSONStringParameter);
        JSONArray jsonArray=new JSONArray("results");
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
