package com.zapp.zidan.mycataloguemovie.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.zapp.zidan.mycataloguemovie.database.DatabaseContract.MovieColumns.DATE;
import static com.zapp.zidan.mycataloguemovie.database.DatabaseContract.MovieColumns.IMAGE;
import static com.zapp.zidan.mycataloguemovie.database.DatabaseContract.MovieColumns.NAME;
import static com.zapp.zidan.mycataloguemovie.database.DatabaseContract.MovieColumns.OVERVIEW;
import static com.zapp.zidan.mycataloguemovie.database.DatabaseContract.TABLE_MOVIE;
import static android.provider.BaseColumns._ID;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "database_movie";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE_MOVIE = "create table "
            +TABLE_MOVIE
            +"("+_ID+" integer primary key autoincrement, "
            +NAME+" text, "
            +OVERVIEW+" text, "
            +DATE+" text,"
            +IMAGE+" text);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MOVIE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+TABLE_MOVIE);
        onCreate(db);
    }
}
