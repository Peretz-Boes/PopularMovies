package com.example.android.popularmoviesstagetwo;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.util.Log;
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
    private static final String LOG_TAG=PosterAdapter.class.getSimpleName();
    private Context context;
    private ArrayList<String> arrayList;
    private int posterWidth;
    private Cursor cursor;
    private boolean dataIsValid;
    private int idRowColumn;
    private DataSetObserver dataSetObserver;

    public PosterAdapter(Context c,ArrayList<String> filePaths,int a){
        context=c;
        arrayList=filePaths;
        posterWidth=a;
        if (dataIsValid){
            cursor.registerDataSetObserver(dataSetObserver);
        }
        Log.d(LOG_TAG,"in super");
    }

    public Cursor getCursor(){
        return cursor;
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
        if (dataIsValid&&cursor!=null&&cursor.moveToPosition(position)){
            return cursor.getLong(idRowColumn);
        }
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
