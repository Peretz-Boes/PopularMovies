package com.example.android.popularmoviesstagetwo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.android.popularmoviesstagetwo.R;
import com.example.android.popularmoviesstagetwo.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Peretz on 2016-12-23.
 */
public class MovieAdapter extends ArrayAdapter<Movie> {
    public MovieAdapter(Context context,List<Movie> movies){
        super(context,0,movies);
    }

    public View getView(int position,View convertView,ViewGroup parent){
        Movie movie=getItem(position);
        if (convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.movie_grid_item,parent,false);
        }

        ImageView moviePoster=(ImageView)convertView.findViewById(R.id.movie_poster);
        Picasso.with(getContext()).load(movie.getImageFullURL()).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(moviePoster);
        return convertView;
    }

}
