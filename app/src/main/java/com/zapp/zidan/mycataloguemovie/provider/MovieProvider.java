package com.zapp.zidan.mycataloguemovie.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.zapp.zidan.mycataloguemovie.database.MovieHelper;

import static com.zapp.zidan.mycataloguemovie.database.DatabaseContract.AUTH;
import static com.zapp.zidan.mycataloguemovie.database.DatabaseContract.CONTENT_URI;
import static com.zapp.zidan.mycataloguemovie.database.DatabaseContract.TABLE_MOVIE;

public class MovieProvider extends ContentProvider {

    private MovieHelper helper;

    private static final int ALL_MOVIE = 1;
    private static final int ID_MOVIE = 2;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTH, TABLE_MOVIE, ALL_MOVIE);
        uriMatcher.addURI(AUTH, TABLE_MOVIE+"/#", ID_MOVIE);
    }

    @Override
    public boolean onCreate() {
        helper = new MovieHelper(getContext());
        helper.open();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        switch (uriMatcher.match(uri)){
            case ALL_MOVIE:
                cursor = helper.queryProvider();
                break;
            default:
                cursor = null;
                break;
        }

        if (cursor != null){
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long insert;
        switch (uriMatcher.match(uri)){
            case ALL_MOVIE:
                insert = helper.insertProvider(values);
                break;
            default:
                insert = 0;
                break;
        }

        if (insert > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return Uri.parse(CONTENT_URI+"/"+insert);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int delete;
        switch (uriMatcher.match(uri)){
            case ID_MOVIE:
                delete = helper.deleteProvider(uri.getLastPathSegment());
                break;
            default:
                delete = 0;
                break;
        }
        if (delete > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return delete;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int update;
        switch (uriMatcher.match(uri)){
            case ID_MOVIE:
                update = helper.updateProvider(uri.getLastPathSegment(), values);
                break;
            default:
                update = 0;
                break;
        }

        if (update > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return update;
    }
}
