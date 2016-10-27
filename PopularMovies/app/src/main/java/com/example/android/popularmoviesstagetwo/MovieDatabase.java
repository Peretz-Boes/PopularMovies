package com.example.android.popularmoviesstagetwo;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by Peretz on 2016-10-16.
 */
@Database(version = MovieDatabase.VERSION)
public final class MovieDatabase {
    private MovieDatabase() {

    }

    public static final int VERSION = 2;

    @Table(MovieColumns.class)
    public static final String MOVIES = "movies";
    @Table(ArchivedMovieColumns.class)
    public static final String ARCHIVED_MOVIES = "archived_movies";
}
