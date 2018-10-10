package com.zapp.zidan.mycataloguemovie.moviedata;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;
import com.zapp.zidan.mycataloguemovie.BuildConfig;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import cz.msebera.android.httpclient.Header;

public class MovieAsyncTaskLoader extends AsyncTaskLoader<ArrayList<MovieItems>>{

    private ArrayList<MovieItems> mData;
    private boolean hasResult = false;
    private String searchMovie;
    private int idMovie;
    private static final String API_KEY = BuildConfig.MOVIE_API_KEY;

    public MovieAsyncTaskLoader(Context context, String searchMovie, int idMovie){
        super(context);
        onContentChanged();
        this.searchMovie = searchMovie;
        this.idMovie = idMovie;
    }

    @Override
    protected void onStartLoading(){
        if (takeContentChanged()){
            forceLoad();
        } else if (hasResult){
            deliverResult(mData);
        }
    }

    @Override
    public void deliverResult(final ArrayList<MovieItems> data){
        mData = data;
        hasResult = true;
        super.deliverResult(mData);
    }

    @Override
    protected void onReset(){
        super.onReset();
        onStopLoading();
        if (hasResult){
            mData = null;
            hasResult = false;
        }
    }

    @Override
    public ArrayList<MovieItems> loadInBackground() {
        SyncHttpClient client = new SyncHttpClient();

        final ArrayList<MovieItems> listMovieItems = new ArrayList<>();
        String url = "";

        if (idMovie == 10){
            url = "https://api.themoviedb.org/3/movie/popular?api_key=" + API_KEY + "&language=en-US&page=1";
        } else if (idMovie == 20){
            url = "https://api.themoviedb.org/3/search/movie?api_key=" + API_KEY + "&language=en-US&query=" + searchMovie;
        } else if (idMovie == 30){
            url = "https://api.themoviedb.org/3/movie/now_playing?api_key=" + API_KEY + "&language=en-US";
        } else if (idMovie == 40){
            url = "https://api.themoviedb.org/3/movie/upcoming?api_key=" + API_KEY + "&language=en-US";
        }

        client.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onStart(){
                super.onStart();
                setUseSynchronousMode(true);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject dataObject = new JSONObject(result);
                    JSONArray movieList = dataObject.getJSONArray("results");

                    for (int i = 0; i < movieList.length(); i++){
                        JSONObject movie = movieList.getJSONObject(i);
                        MovieItems movieItems = new MovieItems(movie);
                        listMovieItems.add(movieItems);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            }
        });
        return listMovieItems;
    }
}
