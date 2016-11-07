package com.example.android.popularmoviesstagetwo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    private View rootView;
    public static String title;
    public static String date;
    public static String rating;
    public static String poster;
    public static String overview;
    public static String comments;
    public static String youtubeLinks1;
    public static String youtubeLinks2;

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
    }
}


    /*public class VideoLoader extends AsyncTask<Void,Void,ArrayList<String>>{
        private ArrayList<String>youtubeLinks1;
        private ArrayList<String>youtubeLinks2;
        private ArrayList<String>id;
        private final String LOG_TAG=MainActivityFragment.class.getSimpleName();

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            youtubeLinks1=new ArrayList<>(Arrays.asList(getYoutubeVideosFromIds(id,0)));
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
        }

        public String[] getYoutubeVideosFromIds(ArrayList<String> id,int position) {
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


    }
}*/
