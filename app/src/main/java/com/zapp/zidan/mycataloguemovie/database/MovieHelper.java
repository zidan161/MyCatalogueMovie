package com.zapp.zidan.mycataloguemovie.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import static com.zapp.zidan.mycataloguemovie.database.DatabaseContract.MovieColumns.NAME;
import static com.zapp.zidan.mycataloguemovie.database.DatabaseContract.TABLE_MOVIE;
import static android.provider.BaseColumns._ID;

public class MovieHelper {

    private static String TABLE_NAME = TABLE_MOVIE;
    private Context context;
    private DatabaseHelper dbhelper;
    private SQLiteDatabase database;

    public MovieHelper(Context context){
        this.context = context;
    }

    public MovieHelper open() throws SQLException {
        dbhelper = new DatabaseHelper(context);
        database = dbhelper.getWritableDatabase();
        return this;
    }

    public void close(){
        dbhelper.close();
    }

    public Cursor queryProvider(){
        return database.query(TABLE_NAME
                , null
                , null
                , null
                , null
                , null
                ,_ID+" DESC"
                , null);
    }

    public long insertProvider(ContentValues values){
        return database.insert(TABLE_NAME, null, values);
    }

    public int updateProvider(String id, ContentValues values){
        return database.update(TABLE_NAME, values, _ID+" = ?", new String[]{id});
    }

    public int deleteProvider(String id){
        return database.delete(TABLE_NAME, _ID+" = ?", new String[]{id});
    }
}
