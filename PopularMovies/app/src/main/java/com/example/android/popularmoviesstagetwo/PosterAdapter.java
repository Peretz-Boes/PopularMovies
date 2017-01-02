package com.example.android.popularmoviesstagetwo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Peretz on 2016-12-23.
 */
public class PosterAdapter extends BaseAdapter {
    private static final String LOG_TAG=PosterAdapter.class.getSimpleName();
    private Context context;
    private ArrayList<String> moviesList;


    public PosterAdapter(Context c,ArrayList<String> movieList,int a) {
        this.context = c;
        this.moviesList = movieList;
    }

    @Override
    public int getCount() {
        return moviesList.size();
    }

    @Override
    public Object getItem(int position) {
        return moviesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView image;
        if (convertView==null){
            image=new ImageView(context);
        }else {
            image=(ImageView)convertView;
        }
        String url = "http://image.tmdb.org/t/p/w185" + moviesList.get(position);
        Picasso.with(context).load(url).into(image);
        return image;
    }
}
