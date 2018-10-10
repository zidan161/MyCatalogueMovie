package com.zapp.zidan.favouriteapp;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;

import static DatabaseFavourite.DatabaseContract.MovieColumns.DATE;
import static DatabaseFavourite.DatabaseContract.MovieColumns.IMAGE;
import static DatabaseFavourite.DatabaseContract.MovieColumns.NAME;
import static DatabaseFavourite.DatabaseContract.MovieColumns.OVERVIEW;
import static DatabaseFavourite.DatabaseContract.getColumnInt;
import static DatabaseFavourite.DatabaseContract.getColumnString;
import static android.provider.BaseColumns._ID;

public class MovieItems implements Parcelable{

    private int id;
    private String movie;
    private String date;
    private String img;
    private String overview;

    public MovieItems(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMovie(String movie) {
        this.movie = movie;
    }

    public String getMovie() {
        return movie;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getImg() {
        return img;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getOverview() {
        return overview;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.movie);
        dest.writeString(this.date);
        dest.writeString(this.img);
        dest.writeString(this.overview);
    }

    public MovieItems(Cursor cursor){
        setId(getColumnInt(cursor, _ID));
        setMovie(getColumnString(cursor, NAME));
        setOverview(getColumnString(cursor, OVERVIEW));
        setDate(getColumnString(cursor, DATE));
        setImg(getColumnString(cursor, IMAGE));
    }

    protected MovieItems(Parcel in) {
        this.id = in.readInt();
        this.movie = in.readString();
        this.date = in.readString();
        this.img = in.readString();
        this.overview = in.readString();
    }

    public static final Creator<MovieItems> CREATOR = new Creator<MovieItems>() {
        @Override
        public MovieItems createFromParcel(Parcel source) {
            return new MovieItems(source);
        }

        @Override
        public MovieItems[] newArray(int size) {
            return new MovieItems[size];
        }
    };
}
