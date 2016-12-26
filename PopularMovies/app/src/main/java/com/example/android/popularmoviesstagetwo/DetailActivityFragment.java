package com.example.android.popularmoviesstagetwo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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
import java.util.prefs.PreferenceChangeListener;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    public static final String LOG_TAG = DetailActivityFragment.class.getSimpleName();
    private View rootView;
    public static String title;
    public static String date;
    public static String rating;
    public static String poster;
    public static String overview;
    public static String comments;
    public static boolean sortByPopular;
    static ArrayList<String> ids;
    static ArrayList<String> youtubeLinks1;
    static ArrayList<String> youtubeLinks2;
    static ArrayList<ArrayList<String>> userComments;
    static PreferenceChangeListener preferenceChangeListener;
    static SharedPreferences sharedPreferences;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        getData();
        return rootView;
    }

    public void getData() {
        Intent intent = getActivity().getIntent();
        title = intent.getStringExtra("title");
        date = intent.getStringExtra("date");
        rating = intent.getStringExtra("rating");
        poster = intent.getStringExtra("poster");
        overview = intent.getStringExtra("overview");
        comments = intent.getStringExtra("comments");
        String url = "http://image.tmdb.org/t/p/w185" + poster;
        ImageView imageView = (ImageView) rootView.findViewById(R.id.image);
        Picasso.with(getActivity()).load(url).into(imageView);
        TextView textView = (TextView) rootView.findViewById(R.id.title);
        textView.setText(title);
        TextView textView1 = (TextView) rootView.findViewById(R.id.date);
        textView1.setText(date);
        TextView textView2 = (TextView) rootView.findViewById(R.id.rating);
        textView2.setText(rating);
        TextView textView3 = (TextView) rootView.findViewById(R.id.overview);
        textView3.setText(overview);
        TextView textView4 = (TextView) rootView.findViewById(R.id.user_comments);
        textView4.setText(comments);
        VideoLoader videoLoader = new VideoLoader();
        videoLoader.execute();
    }

    public class VideoLoader extends AsyncTask<Void, Void, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            try {

                while (true) {
                    youtubeLinks1 = new ArrayList<>(Arrays.asList(getYoutubeVideosFromIds(ids, 0)));
                    youtubeLinks2 = new ArrayList<>(Arrays.asList(getYoutubeVideosFromIds(ids, 1)));
                    int nullCount = 0;
                    for (int i = 0; i < youtubeLinks1.size(); i++) {
                        if (youtubeLinks1.get(i) == null) {
                            nullCount++;
                            youtubeLinks1.set(i, "No youtube video has been found");
                        }
                    }
                    for (int i = 0; i < youtubeLinks2.size(); i++) {
                        if (youtubeLinks2.get(i) == null) {
                            nullCount++;
                            youtubeLinks2.set(i, "No youtube video has been found");
                        }
                    }
                    if (nullCount > 2) {
                        break;
                    }
                    userComments = getUserReviewsFromIds(ids);
                }
            } catch (Exception e) {
                System.out.println("Error");
            }

            return null;
        }

        public String[] getYoutubeVideosFromIds(ArrayList<String> id, int position) {
            String[] results = new String[id.size()];
            for (int i = 0; i < id.size(); i++) {
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
                            results[i] = getYoutubeVideosFromJSON(JSONResult, position);
                        } catch (JSONException E) {
                            results[i] = "no video found";
                        }
                    } catch (Exception e) {

                    } finally {
                        if (httpURLConnection != null) {
                            httpURLConnection.disconnect();
                        }
                        if (bufferedReader != null) {
                            try {
                                bufferedReader.close();
                            } catch (final IOException e) {

                            }
                        }
                    }
                }
            }
            return results;
        }

        public ArrayList<ArrayList<String>> getUserReviewsFromIds(ArrayList<String> id) {
            outerloop:
            while (true) {
                ArrayList<ArrayList<String>> results = new ArrayList<>();
                for (int i = 0; i < id.size(); i++) {
                    HttpURLConnection httpURLConnection = null;
                    BufferedReader bufferedReader = null;
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
                        } catch (JSONException E) {
                            return null;
                        }
                    } catch (Exception e) {
                        continue outerloop;
                    } finally {
                        if (httpURLConnection != null) {
                            httpURLConnection.disconnect();
                        }
                        if (bufferedReader != null) {
                            try {
                                bufferedReader.close();
                            } catch (final IOException e) {

                            }
                        }
                    }
                }
                return results;
            }
        }

        public ArrayList<String> getUserReviewsFromJSON(String JSONStringParameter) throws JSONException {
            JSONObject jsonObject = new JSONObject(JSONStringParameter);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            ArrayList<String> results = new ArrayList<>();
            if (jsonArray.length() == 0) {
                results.add("There are no reviews for this movie");
                return results;
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject resultObject = jsonArray.getJSONObject(i);
                results.add(resultObject.getString("content"));
            }
            return results;
        }

        public String getYoutubeVideosFromJSON(String JSONStringParameter, int position) throws JSONException {
            JSONObject jsonObject = new JSONObject(JSONStringParameter);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            JSONObject jsonObject1;
            String resultString = "no videos have been found";
            if (position == 0) {
                jsonObject1 = jsonArray.getJSONObject(0);
                resultString = jsonObject1.getString("key");
            } else if (position == 1) {
                if (jsonArray.length() > 1) {
                    jsonObject1 = jsonArray.getJSONObject(1);
                } else {
                    jsonObject1 = jsonArray.getJSONObject(0);
                }
                resultString = jsonObject1.getString("key");
            }
            return resultString;
        }

    }

}
