package com.example.android.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
/*
Works Cited:
https://developer.android.com/about/versions/marshmallow/android 6.0-changes.html-information on HttpClient
https://youtu.be/-jxfxmHs1DY and https://github.com/delaroy/Android-Grid-View-and-Webservice/information on use of Picasso and a JSON Web Service
https://gist.github.com/udacityandroid/41aca2eb9ff6942e769b code for SettingsActivity.java
https://github.com/udacity/Sunshine-Version-2/blob/6a891b971f7ad3730c8dd4e3c0553c8331559adf/app/src/main/res/values/strings.xml code for strings in strings.xml
http://docs.themoviedb.apiary.io/# information on how to use the moviedb.org api
https://developer.android.com/reference/org/json/JSONObject.html information on the JSON Object
 */
public class MainActivity extends ActionBarActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private GridView mGridView;
    private ArrayList<GridItem> mGridData;
    private GridViewAdapter mGridAdapter;
    private ProgressBar mProgressBar;
    private String FEED_URL = "http://api.themoviedb.org/3/movie/popular?api_key=[]";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        mGridView = (GridView) findViewById(R.id.gridView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mGridData = new ArrayList<>();
        mGridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, mGridData);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GridItem item = (GridItem) parent.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                ImageView imageView = (ImageView) view.findViewById(R.id.grid_item_image);
                int[] screenLocation = new int[2];
                imageView.getLocationOnScreen(screenLocation);
                intent.putExtra("left", screenLocation[0]).putExtra("top", screenLocation[1]).putExtra("width", imageView.getWidth()).putExtra("height", imageView.getHeight());

                startActivity(intent);
            }
        });

        new AsyncHttpTask().execute(FEED_URL);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {
        private void formatData(String preferenceType) {
            if (preferenceType.equals(getString(R.string.pref_order_top_rated))) {
                FEED_URL = "http://api.themoviedb.org/3/movie/top_rated?api_key=[]";
            } else if (!preferenceType.equals(getString(R.string.pref_order_most_popular))) {
                Log.d(TAG, "Setting type not found " + preferenceType);
            }
        }

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            try {
                // Create Apache HttpClient
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse httpResponse = httpclient.execute(new HttpGet(params[0]));
                int statusCode = httpResponse.getStatusLine().getStatusCode();

                // 200 represents HTTP OK
                if (statusCode == 200) {
                    String response = streamToString(httpResponse.getEntity().getContent());
                    parseResult();
                    result = 1; // Successful
                } else {
                    result = 0; //"Failed
                }
            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }

            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            // Download complete. Lets update UI

            if (result == 1) {
                mGridAdapter.setGridData(mGridData);
            } else {
                Toast.makeText(MainActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }

            //Hide progressbar
            mProgressBar.setVisibility(View.GONE);
        }


        String streamToString(InputStream stream) throws IOException {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
            String line;
            String result = "";
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }

            // Close stream
            if (null != stream) {
                stream.close();
            }
            return result;
        }

        /**
         * Parsing the feed results and get the list
         */
        private void parseResult() {
            try {
                JSONObject response = new JSONObject();
               response.getJSONArray("results");
                response.getString("backdrop_path");
                }
             catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
