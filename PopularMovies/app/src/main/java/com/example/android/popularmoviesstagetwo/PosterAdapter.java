package com.example.android.popularmoviesstagetwo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Peretz on 2016-07-05.
 */
public class PosterAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> arrayList;
    private int posterWidth;

    public PosterAdapter(Context c,ArrayList<String> filePaths,int a){
        context=c;
        arrayList=filePaths;
        posterWidth=a;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView image;
        if (convertView==null){
            image=new ImageView(context);
        }else {
            image=(ImageView)convertView;
        }
        String url = "http://image.tmdb.org/t/p/w185" + arrayList.get(position);
        Picasso.with(context).load(url).into(image);
        return image;
    }
}
