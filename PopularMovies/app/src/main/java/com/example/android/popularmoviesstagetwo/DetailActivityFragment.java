package com.example.android.popularmoviesstagetwo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmoviesstagetwo.adapter.ReviewAdapter;
import com.example.android.popularmoviesstagetwo.adapter.TrailerAdapter;
import com.example.android.popularmoviesstagetwo.model.Movie;
import com.example.android.popularmoviesstagetwo.model.Review;
import com.example.android.popularmoviesstagetwo.model.ReviewListResponse;
import com.example.android.popularmoviesstagetwo.model.Trailer;
import com.example.android.popularmoviesstagetwo.model.TrailersList;
import com.example.android.popularmoviesstagetwo.network.MovieClient;
import com.example.android.popularmoviesstagetwo.network.MovieRetrofitInterface;
import com.example.android.popularmoviesstagetwo.tasks.ManageFavouritesAsyncTask;
import com.example.android.popularmoviesstagetwo.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    TextView movieTitle;
    TextView movieOverview;
    TextView movieRating;
    TextView movieReleaseDate;
    ImageView detailMoviePosterImage;
    Button favouriteButton;
    public Movie movieDetails;
    public ReviewAdapter reviewAdapter;
    public TrailerAdapter trailerAdapter;
    public MovieRetrofitInterface movieRetrofitInterface;

    public DetailActivityFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_detail,container,false);
        Bundle bundle=getArguments();
        if (bundle==null){
            rootView.setVisibility(View.INVISIBLE);
            return rootView;
        }
        rootView.setVisibility(View.VISIBLE);
        movieDetails=bundle.getParcelable(Constants.MOVIE_TAG);
        initializeViews(rootView);
        new ManageFavouritesAsyncTask(getActivity(),favouriteButton,movieDetails,false).execute();
        favouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ManageFavouritesAsyncTask(getActivity(),favouriteButton,movieDetails,true).execute();
            }
        });
        initializeReviewsAndTrailerLists(rootView);
        getReviews();
        getTrailers();
        return rootView;
    }

    private void initializeViews(View rootView){
        movieTitle=(TextView)rootView.findViewById(R.id.movie_title);
        movieOverview=(TextView)rootView.findViewById(R.id.movie_overview);
        movieRating=(TextView)rootView.findViewById(R.id.movie_rating);
        movieReleaseDate=(TextView)rootView.findViewById(R.id.movie_release_date);
        detailMoviePosterImage=(ImageView)rootView.findViewById(R.id.detail_movie_poster_image);
        favouriteButton=(Button)rootView.findViewById(R.id.favourite_button);
        if (movieDetails!=null){
            movieTitle.setText(movieDetails.getOriginalTitle());
            movieOverview.setText(movieDetails.getOverview());
            movieRating.setText(movieDetails.getRating());
            movieReleaseDate.setText(movieDetails.getMovieReleaseDate());
            Picasso.with(getContext()).load(movieDetails.getImageFullURL()).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(detailMoviePosterImage);
        }
    }

    private void initializeReviewsAndTrailerLists(View rootView){
        List<Review> reviews=new ArrayList<>();
        final List<Trailer> trailers=new ArrayList<>();
        reviewAdapter=new ReviewAdapter(getActivity(),reviews);
        trailerAdapter=new TrailerAdapter(getActivity(),trailers);
        ListView reviewList=(ListView)rootView.findViewById(R.id.review_list);
        reviewList.setAdapter(reviewAdapter);
        ListView trailerList=(ListView)rootView.findViewById(R.id.trailer_list);
        trailerList.setAdapter(trailerAdapter);
        movieRetrofitInterface= MovieClient.createService(MovieRetrofitInterface.class);
        trailerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String youtubeVideoId = trailers.get(position).getKey();
                String youtubeVideoUri = Constants.APIConstants.YOUTUBE_VIDEOS_PREFIX + youtubeVideoId;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeVideoUri));
                startActivity(intent);
            }
        });
    }

    private void getReviews(){
        Call<ReviewListResponse> reviewListResponseCall=movieRetrofitInterface.getMovieReviews(movieDetails.getId());
        reviewListResponseCall.enqueue(new Callback<ReviewListResponse>() {
            @Override
            public void onResponse(Response<ReviewListResponse> response) {
                List<Review> reviews=response.body().getResults();
                reviewAdapter.clear();
                if(reviews!=null) {
                    for (Review review : reviews) {
                        reviewAdapter.add(review);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getActivity(),"Throw up",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getTrailers(){
        Call<TrailersList> trailersListCall=movieRetrofitInterface.getMovieTrailers(movieDetails.getId());
        trailersListCall.enqueue(new Callback<TrailersList>() {
            @Override
            public void onResponse(Response<TrailersList> response) {
                List<Trailer> trailers=response.body().getResults();
                trailerAdapter.clear();
                if (trailers!=null) {
                    for (Trailer trailer : trailers) {
                        trailerAdapter.add(trailer);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getActivity(),"Throw up",Toast.LENGTH_LONG).show();
            }
        });
    }

}
