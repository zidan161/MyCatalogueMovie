package DatabaseFavourite;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {

    public static final String TABLE_MOVIE = "movie_table";

    public static class MovieColumns implements BaseColumns{

        public static String NAME = "movie_name";
        public static String OVERVIEW = "movie_overview";
        public static String DATE = "movie_date";
        public static String IMAGE = "movie_image";
    }

    public static final String AUTH = "com.zapp.zidan.mycataloguemovie";

    public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
            .authority(AUTH)
            .appendPath(TABLE_MOVIE)
            .build();

    public static String getColumnString(Cursor cursor, String columnName){
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static int getColumnInt(Cursor cursor, String columnName){
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    public static long getColumnLong(Cursor cursor, String columnName){
        return cursor.getLong(cursor.getColumnIndex(columnName));
    }
}
