package com.example.android.popularmoviesstagetwo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.LinearLayout;
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
    static boolean sortByPopular;
    static ArrayList<String> overview;
    static ArrayList<String> title;
    static ArrayList<String> date;
    static ArrayList<String> rating;
    public String url="http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=281ad0257e71bca17a21b42c9fee7304";
    private final String LOG_TAG=MainActivityFragment.class.getSimpleName();
    static PreferenceChangeListener preferenceChangeListener;
    static SharedPreferences sharedPreferences;
    static boolean sortByFavouriteMovies;
    static ArrayList<String>favouritePosters=new ArrayList<String>();
    static ArrayList<String>youtubeLinks1;
    static ArrayList<String>youtubeLinks2;
    static ArrayList<String>id;
    static ArrayList<Boolean> favouritedMovies;
    static ArrayList<ArrayList<String>> userComments;
    public MainActivityFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_main, container, false);
        WindowManager windowManager=(WindowManager)getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display display=windowManager.getDefaultDisplay();
        Point point=new Point();
        display.getSize(point);
        if (MainActivity.IS_A_TABLET){
            deviceWidth=point.x/6;
        }else {
            deviceWidth=point.x/3;
        }
        if (getActivity()!=null){
            ArrayList<String> arrayList=new ArrayList<String>();
            PosterAdapter imageAdapter=new PosterAdapter(getActivity(),arrayList,deviceWidth);
            imageGridView=(GridView)rootView.findViewById(R.id.image_grid_view);
            imageGridView.setColumnWidth(deviceWidth);
            imageGridView.setAdapter(imageAdapter);
        }
        imageGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                favouritedMovies=new ArrayList<Boolean>();
                Intent intent = new Intent(getActivity(), DetailActivity.class).putExtra("overview", overview.get(position))
                        .putExtra("poster", images.get(position))
                        .putExtra("title", title.get(position))
                        .putExtra("date", date.get(position))
                        .putExtra("rating", rating.get(position))
                        .putExtra("youtube",youtubeLinks1.get(position))
                        .putExtra("youtube2",youtubeLinks2.get(position))
                        .putExtra("comments",userComments.get(position))
                        .putExtra("favourited",favouritedMovies.get(position));
                startActivity(intent);

            }
        });
        return rootView;
    }
    private class PreferenceChangeListener implements SharedPreferences.OnSharedPreferenceChangeListener{

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            imageGridView.setAdapter(null);
            onStart();
        }
    }
    @Override
    public void onStart(){
        super.onStart();
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getActivity());
        preferenceChangeListener=new PreferenceChangeListener();
        sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceChangeListener);
        String orderType;
        if (sharedPreferences.getString("Order of movies shown","").equals(R.string.pref_order_most_popular)){
            getActivity().setTitle("Most popular movies now");
            sortByPopular=true;
            sortByFavouriteMovies=false;
        }else if (sharedPreferences.getString("Order of movies shown","").equals(R.string.pref_order_top_rated)){
            getActivity().setTitle("Highest rated movies now");
            sortByPopular=false;
            sortByFavouriteMovies=false;
        }
        else if (sharedPreferences.getString("Order of movies shown","").equals(R.string.pref_show_favourited_movies)){
            sortByPopular=false;
            sortByFavouriteMovies=true;
        }
        TextView textView=new TextView(getActivity());
        RelativeLayout relativeLayout=(RelativeLayout)getActivity().findViewById(R.id.fragment_layout);
        if (sortByFavouriteMovies){
            if (favouritePosters.size()==0){
                textView.setText("You have no favourites");
                if (relativeLayout.getChildCount()==1){
                    imageGridView.setVisibility(GridView.GONE);
                }
            }else {
                imageGridView.setVisibility(GridView.VISIBLE);
                relativeLayout.removeView(textView);
            }
            if (favouritePosters!=null&&getActivity()!=null){
                PosterAdapter posterAdapter=new PosterAdapter(getActivity(),favouritePosters,deviceWidth);
                imageGridView.setAdapter(posterAdapter);
            }
        }else {
            imageGridView.setVisibility(GridView.VISIBLE);

            ImageLoader imageLoader = new ImageLoader();
            if (isInternetAvailable()) {
                imageLoader.execute();
                Log.v(LOG_TAG, "async task completed");
            } else {
                TextView textView2 = new TextView(getActivity());
                LinearLayout linearLayout2 = (LinearLayout) getActivity().findViewById(R.id.container);
                textView2.setText("There is no Internet service");
                if (linearLayout2.getChildCount() == 1) {
                    linearLayout2.addView(textView);
                }
                imageGridView.setVisibility(GridView.GONE);
            }
        }
    }
    public boolean isInternetAvailable(){
        ConnectivityManager connectivityManager=(ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        return networkInfo !=null&&networkInfo.isConnected();
    }
    public class ImageLoader extends AsyncTask<Void,Void,ArrayList<String>> {


        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            while (true){
                try {
                    images=new ArrayList<>(Arrays.asList(getPathsFromURL(sortByPopular)));
                    return images;
                }catch (Exception e){
                    continue;
                }
            }
        }
        @Override
        protected void onPostExecute(ArrayList<String>results){
            if (results!=null&&getActivity()!=null){
                PosterAdapter posterAdapter=new PosterAdapter(getActivity(),results,deviceWidth);
                imageGridView.setAdapter(posterAdapter);
            }
        }
        public String[] getPathsFromURL(boolean sorting){

            while (true){
                HttpURLConnection httpURLConnection=null;
                BufferedReader bufferedReader=null;
                String JSONResult;
                try {
                    String urlString;
                    if (sortByPopular){
                        urlString = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=your_api_key";
                    }else {
                        urlString="http://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc&vote_count.gte=500&api_key=your_api_key";
                    }
                    URL url1=new URL(urlString);
                    httpURLConnection=(HttpURLConnection)url1.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.connect();
                    InputStream inputStream=httpURLConnection.getInputStream();
                    StringBuffer stringBuffer=new StringBuffer();
                    if (inputStream==null){
                        return null;
                    }
                    bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                    String data;
                    while ((data=bufferedReader.readLine())!=null){
                        stringBuffer.append(data+"\n");
                    }
                    if (stringBuffer.length()==0){
                        return null;
                    }
                    JSONResult=stringBuffer.toString();
                    Log.v(LOG_TAG,JSONResult);
                    try {
                        overview=new ArrayList<String>(Arrays.asList(getStringsFromAPI(JSONResult, "overview")));
                        title=new ArrayList<String>(Arrays.asList(getStringsFromAPI(JSONResult, "original_title")));
                        rating=new ArrayList<String>(Arrays.asList(getStringsFromAPI(JSONResult, "vote_average")));
                        date=new ArrayList<String>(Arrays.asList(getStringsFromAPI(JSONResult, "release_date")));
                        id=new ArrayList<String>(Arrays.asList(getStringsFromAPI(JSONResult,"id")));
                        while (true){
                            youtubeLinks1=new ArrayList<String>(Arrays.asList(getYoutubeVideosFromIds(id,0)));
                            youtubeLinks2=new ArrayList<String>(Arrays.asList(getYoutubeVideosFromIds(id,1)));
                            int nullNumber=0;
                            for (int i=0;i<youtubeLinks1.size();i++){
                                if (youtubeLinks1.get(i)==null){
                                    nullNumber++;
                                    youtubeLinks1.set(i,"no video has been found");
                                }
                            }
                            for (int i=0;i<youtubeLinks2.size();i++){
                                if (youtubeLinks2.get(i)==null){
                                    nullNumber++;
                                    youtubeLinks2.set(i,"no video has been found");
                                }
                            }
                            if (nullNumber>2){
                                continue;
                            }else {
                                break;
                            }
                        }
                        userComments=getUserReviewsFromIds(id);
                        return getDataFromJSON(JSONResult);
                    }catch (JSONException e){
                        return null;
                    }
                }catch (Exception e){
                    continue;
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
    }
    public String[] getYoutubeVideosFromIds(ArrayList<String> id,int position) {
        String[] results=new String[id.size()];
        for (int i=0;i<id.size();i++){


        while (true) {
            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;
            String JSONResult;
            try {
                String urlString = null;
                urlString = "http://api.themoviedb.org/3/movie/" + id.get(i) + "/videos?api_key=your_api_key";
                URL url1 = new URL(urlString);
                httpURLConnection = (HttpURLConnection) url1.openConnection();
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
                    String urlString=null;
                    urlString="http://api.themoviedb.org/3/movie/" + id.get(i) + "/reviews?api_key=your_api_key";
                    URL url1 = new URL(urlString);
                    httpURLConnection = (HttpURLConnection) url1.openConnection();
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
    public String[] getStringsFromAPI(String JSONStringParameter,String parameter) throws JSONException{
        JSONObject jsonObject=new JSONObject(JSONStringParameter);
        JSONArray jsonArray=jsonObject.getJSONArray("results");
        String[] results=new String[jsonArray.length()];
        for (int i=0;i<jsonArray.length();i++){
            JSONObject poster=jsonArray.getJSONObject(i);
            if (parameter.equals("vote_average")){
                Double decimalNumber=poster.getDouble("vote_average");
                String userRating=Double.toString(decimalNumber)+"/10";
                results[i]=userRating;
            }else {
                String posterPath = poster.getString(parameter);
                results[i] = posterPath;
            }
        }
        return results;
    }
    public String[] getDataFromJSON(String JSONStringParameter)throws JSONException{
        JSONObject jsonObject=new JSONObject(JSONStringParameter);
        JSONArray jsonArray=jsonObject.getJSONArray("results");
        String[] results=new String[jsonArray.length()];
        for (int i=0;i<jsonArray.length();i++){
            JSONObject poster=jsonArray.getJSONObject(i);
            String posterPath=poster.getString("poster_path");
            results[i]=posterPath;
        }
        return results;
    }
}
