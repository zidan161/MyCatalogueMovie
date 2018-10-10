package com.zapp.zidan.mycataloguemovie;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.zapp.zidan.mycataloguemovie.database.MoviePreference;
import com.zapp.zidan.mycataloguemovie.notification.AlarmReceiver;

public class SettingActivity extends AppCompatActivity {

    public static final String KEY_RELEASE = "release";
    public static final String KEY_DAILY = "daily";
    CheckBox cbDaily, cbRelease;
    MoviePreference preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        preference = new MoviePreference(this);

        cbDaily = findViewById(R.id.cb_daily);
        cbRelease = findViewById(R.id.cb_release);

        if (preference.isOn(KEY_DAILY)){
            cbDaily.setChecked(true);
        } else {
            cbDaily.setChecked(false);
        }

        if (preference.isOn(KEY_RELEASE)){
            cbRelease.setChecked(true);
        } else {
            cbRelease.setChecked(false);
        }

        cbDaily.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && !preference.isOn(KEY_DAILY)){
                    AlarmReceiver.alarmDailyReminder(SettingActivity.this);
                    preference.setOn(KEY_DAILY, true);
                } else {
                    AlarmReceiver.cancelAlarm(SettingActivity.this, AlarmReceiver.ID_ALARM_DAILY);
                    preference.setOn(KEY_DAILY, false);
                }
            }
        });

        cbRelease.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && !preference.isOn(KEY_RELEASE)){
                    AlarmReceiver.alarmReleaseReminder(SettingActivity.this);
                    preference.setOn(KEY_RELEASE, true);
                } else {
                    AlarmReceiver.cancelAlarm(SettingActivity.this, AlarmReceiver.ID_ALARM_RELEASE);
                    preference.setOn(KEY_RELEASE, false);
                }
            }
        });
    }
}
