package com.example.android.popularmoviesstagetwo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.popularmoviesstagetwo.R;
import com.example.android.popularmoviesstagetwo.model.Review;

import java.util.List;

/**
 * Created by Peretz on 2017-01-15.
 */
public class ReviewAdapter extends ArrayAdapter<Review> {

    public ReviewAdapter(Context context,List<Review> reviews){
        super(context,0,reviews);
    }

    public View getView(int position,View rootView,ViewGroup parent){
        Review review=getItem(position);
        if (rootView==null){
            rootView= LayoutInflater.from(getContext()).inflate(R.layout.review_list_item,parent,false);
        }

        TextView authorTextView=(TextView)rootView.findViewById(R.id.author_title);
        authorTextView.setText(review.getAuthor());

        TextView reviewTextView=(TextView)rootView.findViewById(R.id.review_content);
        reviewTextView.setText(review.getContent());
        return rootView;

    }

}
