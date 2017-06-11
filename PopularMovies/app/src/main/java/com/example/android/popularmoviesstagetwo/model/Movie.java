package com.example.android.popularmoviesstagetwo.model;

import com.example.android.popularmoviesstagetwo.utils.Constants;
import com.example.android.popularmoviesstagetwo.utils.Constants.APIConstants;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Peretz on 2016-12-22.
 */
public class Movie implements Serializable {

    private String poster_path;
    private Boolean adult;
    private String overview;
    private String release_date;
    private List<Integer> genre_ids;
    private String id;
    private String original_title;
    private String original_language;
    private String title;
    private String backdrop_path;
    private Float popularity;
    private int vote_count;
    private Boolean video;
    private Float vote_average;

    public Movie(String id, String original_title, String overview, String poster_path, Float vote_average, String release_date) {
        this.poster_path = poster_path;
        this.overview = overview;
        this.id = id;
        this.original_title = original_title;
        this.vote_average = vote_average;
        this.release_date = release_date;
    }

    public String getImageFullURL() {
        return Constants.APIConstants.IMAGE_URL + APIConstants.SMALL_SIZED_IMAGE + getPosterPath();
    }

    public String getRating() {
        return getVoteAverage() + "/" + Constants.APIConstants.MAXIMUM_RATING;
    }

    public String getMovieReleaseDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-dd-mm");
        String date = "";
        try {
            Date newDate = format.parse(getRelease_date());
            format = new SimpleDateFormat("MMM dd, yyyy");
            date = format.format(newDate);
        } catch (ParseException e) {

        }
        return date;
    }

    public Float getVoteAverage() {
        return vote_average;
    }

    public void setVoteAverage(Float vote_average) {
        this.vote_average = vote_average;
    }

    public Boolean getVideo() {
        return video;
    }

    public void setVideo(Boolean video) {
        this.video = video;
    }

    public int getVoteCount() {
        return vote_count;
    }

    public void setVoteCount(int vote_count) {
        this.vote_count = vote_count;
    }

    public Float getPopularity() {
        return popularity;
    }

    public void setPopularity(Float popularity) {
        this.popularity = popularity;
    }

    public String getBackdropPath() {
        return backdrop_path;
    }

    public void setBackdropPath(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalLanguage() {
        return original_language;
    }

    public void setOriginalLanguage(String original_language) {
        this.original_language = original_language;
    }

    public String getOriginalTitle() {
        return original_title;
    }

    public void setOriginalTitle(String original_title) {
        this.original_title = original_title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Integer> getGenre_ids() {
        return genre_ids;
    }

    public void setGenre_ids(List<Integer> genre_ids) {
        this.genre_ids = genre_ids;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Boolean getAdult() {
        return adult;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    public String getPosterPath() {
        return poster_path;
    }

    public void setPosterPath(String poster_path) {
        this.poster_path = poster_path;
    }

}
