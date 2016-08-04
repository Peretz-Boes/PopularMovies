package com.example.android.popularmovies;

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
    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView= inflater.inflate(R.layout.fragment_detail, container, false);
        getData();
        return rootView;
    }
    public void getData(){
        Intent intent=getActivity().getIntent();
        String title=intent.getStringExtra("title");
        String date=intent.getStringExtra("date");
        String rating=intent.getStringExtra("rating");
        String poster=intent.getStringExtra("poster");
        String overview=intent.getStringExtra("overview");
        String url = "http://image.tmdb.org/t/p/w185" + poster;
        ImageView imageView=(ImageView)rootView.findViewById(R.id.image);
        Picasso.with(getActivity()).load(url).into(imageView);
        TextView textView=(TextView)rootView.findViewById(R.id.title);
        textView.setText(title);
        TextView textView1=(TextView)rootView.findViewById(R.id.date);
        textView1.setText(date);
        TextView textView2=(TextView)rootView.findViewById(R.id.rating);
        textView2.setText(rating);
        TextView textView3=(TextView)rootView.findViewById(R.id.overview);
        textView3.setText(overview);
    }
}
