package com.zapp.zidan.mycataloguemovie.notification;

import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;
import com.zapp.zidan.mycataloguemovie.R;
import com.zapp.zidan.mycataloguemovie.moviedata.MovieItems;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class MyAsyncTask extends AsyncTask<String, Void, ArrayList<MovieItems>>{

    private Context context;

    public MyAsyncTask(Context context){
        this.context = context;
    }

    @Override
    protected ArrayList<MovieItems> doInBackground(String... strings) {
        Log.d("AsyncTask", "status : doInBackground");
        SyncHttpClient syncHttpClient = new SyncHttpClient();

        final ArrayList<MovieItems> listMovieItems = new ArrayList<>();

        String url = strings[0];

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMM dd, yyyy");
        final String todayDate = dateFormat.format(date);

        syncHttpClient.get(url, new AsyncHttpResponseHandler() {

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

                    for (int i = 0; i < movieList.length(); i++) {
                        JSONObject movie = movieList.getJSONObject(i);
                        MovieItems movieItems = new MovieItems(movie);
                        String release = movieItems.getDate();
                        if (release.equalsIgnoreCase(todayDate)) {
                            listMovieItems.add(movieItems);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
        return listMovieItems;
    }

    @Override
    protected void onPostExecute(ArrayList<MovieItems> items){
        super.onPostExecute(items);
        if(items.size() > 0) {
            for (int i = 0; i < items.size(); i++) {
                String name = items.get(i).getMovie();
                showNotification(context, AlarmReceiver.ID_ALARM_RELEASE, name);
            }
        } else {
            Log.d("onPostExecute", "Error items");
        }
    }


    private void showNotification(Context context, int id, String movie) {

        NotificationManager managerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Uri ringtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        String title = movie;
        String message = "Today "+movie+" is Release";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setSound(ringtone)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setAutoCancel(true);

        managerCompat.notify(id, builder.build());
    }
}
