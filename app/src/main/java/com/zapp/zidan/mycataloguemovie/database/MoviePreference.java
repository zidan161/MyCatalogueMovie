package com.zapp.zidan.mycataloguemovie.database;

import android.content.Context;
import android.content.SharedPreferences;

public class MoviePreference {

    private SharedPreferences preferences;

    public MoviePreference(Context context){
        String PREF_NAME = "Favorite";
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void setFavorite(String key, boolean value){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean isFavorite(String key){
        return preferences.getBoolean(key, false);
    }

    public void setOn(String key, boolean value){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean isOn(String key){
        return preferences.getBoolean(key, false);
    }

    public void setFirstTime(boolean value){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("first", value);
        editor.apply();
    }

    public boolean isFirsTime(){
        return preferences.getBoolean("first", true);
    }
}
