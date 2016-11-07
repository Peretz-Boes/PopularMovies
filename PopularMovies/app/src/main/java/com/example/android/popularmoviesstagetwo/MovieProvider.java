package com.example.android.popularmoviesstagetwo;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by Peretz on 2016-10-16.
 */
@ContentProvider(authority = MovieProvider.AUTHORITY,database = MovieDatabase.class)
public final class MovieProvider {
    static final String CONTENT_PROVIDER_NAME="com.example.provider.movies";
    public static final String AUTHORITY="com.example.android.popularmoviesstagetwo.MovieProvider";
    static final Uri BASE_CONTENT_URI=Uri.parse("content://"+AUTHORITY);
    interface Path{
        String MOVIES="movies";
        String ARCHIVED_MOVIES="archived_movies";
    }
    private static Uri buildUri(String... paths){
        Uri.Builder builder=BASE_CONTENT_URI.buildUpon();
        for (String path:paths){
            builder.appendPath(path);
        }
        return builder.build();
    }
    @TableEndpoint(table = MovieDatabase.MOVIES)public static class Movies{
        @ContentUri(
                path = Path.MOVIES,
                type = "vnd.android.cursor.dir/movies",
                defaultSort = MovieColumns.RATING +"ASC")
        public static final Uri CONTENT_URI=buildUri(Path.MOVIES);
        @InexactContentUri(
                name="MOVIE_ID",
                path = Path.MOVIES+"/#",
                type = "vnd.item.cursor.item/movie",
                whereColumn = MovieColumns._ID,
                pathSegment = 1)
        public static Uri withId(long id){
            return buildUri(Path.MOVIES,String.valueOf(id));
        }
    }
    @TableEndpoint(table = MovieDatabase.ARCHIVED_MOVIES)public static class ArchivedMovies{
        @ContentUri(
                path = Path.ARCHIVED_MOVIES,
                type = "vnd.android.cursor.dir/archived_movie",
                defaultSort = ArchivedMovieColumns.RATING+"ASC")
        public static final Uri CONTENT_URI=buildUri(Path.ARCHIVED_MOVIES);
        @InexactContentUri(
                name = "ARCHIVED_MOVIE_ID",
                path = Path.ARCHIVED_MOVIES+"/#",
                type = "vnd.android.cursor.item/archived_planet",
                whereColumn = ArchivedMovieColumns._ID,
                pathSegment = 1
        )
        public static Uri withId(long id){
            return buildUri(Path.ARCHIVED_MOVIES,String.valueOf(id));
        }
    }
}

