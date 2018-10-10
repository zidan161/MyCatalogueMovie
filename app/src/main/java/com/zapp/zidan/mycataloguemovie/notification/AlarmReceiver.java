package com.zapp.zidan.mycataloguemovie.notification;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.zapp.zidan.mycataloguemovie.BuildConfig;
import com.zapp.zidan.mycataloguemovie.MainActivity;
import com.zapp.zidan.mycataloguemovie.R;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver{

    public static final String EXTRA_ID = "extra_id";
    public static final int ID_ALARM_DAILY = 100;
    public static final int ID_ALARM_RELEASE = 200;
    private static final String API_KEY = BuildConfig.MOVIE_API_KEY;

    @Override
    public void onReceive(Context context, Intent intent) {
        int id = intent.getIntExtra(EXTRA_ID, 0);

        if (id == ID_ALARM_DAILY) {
            showNotification(context, id);
        } else if(id == ID_ALARM_RELEASE) {
            MyAsyncTask myAsyncTask = new MyAsyncTask(context);
            myAsyncTask.execute("https://api.themoviedb.org/3/movie/upcoming?api_key=" + API_KEY + "&language=en-US");
        }
    }

    public void showNotification(Context context, int id) {

        NotificationManager managerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Uri ringtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        String title = context.getResources().getString(R.string.app_name);
        String message = "Catalogue Movie Miss You";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setSound(ringtone)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setAutoCancel(true);

        managerCompat.notify(id, builder.build());
    }

    public static void alarmDailyReminder(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(EXTRA_ID, ID_ALARM_DAILY);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_ALARM_DAILY, intent, 0);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        Toast.makeText(context, "Daily Alarm set", Toast.LENGTH_SHORT).show();
    }

    public static void alarmReleaseReminder(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(EXTRA_ID, ID_ALARM_RELEASE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_ALARM_RELEASE, intent, 0);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        Toast.makeText(context, "Release Alarm set", Toast.LENGTH_SHORT).show();
    }

    public static void cancelAlarm(Context context, int id){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        int requestCode = id;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        alarmManager.cancel(pendingIntent);
    }
}
