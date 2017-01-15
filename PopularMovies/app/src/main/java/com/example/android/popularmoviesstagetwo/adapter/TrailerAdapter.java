package com.example.android.popularmoviesstagetwo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.popularmoviesstagetwo.R;
import com.example.android.popularmoviesstagetwo.model.Trailer;

import java.util.List;

/**
 * Created by Peretz on 2017-01-15.
 */
public class TrailerAdapter extends ArrayAdapter<Trailer>{

    public TrailerAdapter(Context context,List<Trailer> trailers){
        super(context,0,trailers);
    }

    @Override
    public View getView(int position,View rootView,ViewGroup parent){
        if (rootView==null){
            rootView= LayoutInflater.from(getContext()).inflate(R.layout.trailer_list_item,parent,false);
        }

        TextView trailerTextView=(TextView)rootView.findViewById(R.id.trailer_name);
        trailerTextView.setText(getContext().getString(R.string.name_of_trailer,(position+1)));
        return rootView;

    }

}
