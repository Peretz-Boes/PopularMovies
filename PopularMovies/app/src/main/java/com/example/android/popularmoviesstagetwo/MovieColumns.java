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
    public static final String _ID = "_id";
    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String TITLE = "title";
    @DataType(DataType.Type.INTEGER)
    @NotNull
    public static final String THUMBNAIL = "thumbnail";
    @DataType(DataType.Type.INTEGER)
    @NotNull
    public static final String RATING = "rating";
    @DataType(DataType.Type.INTEGER)
    @NotNull
    public static final String DATE = "date";
    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String COMMENTS = "comments";
    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String YOUTUBE_LINKS = "youtube links";
}
