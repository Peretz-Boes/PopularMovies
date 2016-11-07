package com.example.android.popularmoviesstagetwo;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by Peretz on 2016-10-15.
 */
public interface MovieColumns {
    @DataType(DataType.Type.INTEGER)
    @PrimaryKey
    @AutoIncrement
    String _ID = "_id";
    @DataType(DataType.Type.TEXT)
    @NotNull
    String TITLE = "title";
    @DataType(DataType.Type.INTEGER)
    @NotNull
    String THUMBNAIL = "thumbnail";
    @DataType(DataType.Type.INTEGER)
    @NotNull
    String RATING = "rating";
    @DataType(DataType.Type.INTEGER)
    @NotNull
    String DATE = "date";
    @DataType(DataType.Type.TEXT)
    @NotNull
    String COMMENTS = "comments";
    @DataType(DataType.Type.TEXT)
    @NotNull
    String YOUTUBE_LINKS_1 = "youtube links 1";
    @DataType(DataType.Type.TEXT)
    @NotNull
    String YOUTUBE_LINKS_2="youtube links 2";
}
