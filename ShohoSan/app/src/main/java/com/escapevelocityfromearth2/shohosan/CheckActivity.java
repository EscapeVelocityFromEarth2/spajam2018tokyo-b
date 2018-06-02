package com.escapevelocityfromearth2.shohosan;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class CheckActivity extends Activity {
    AlarmManager mManager;
    PendingIntent mPendingIntent;
    int START_BREAKFAST_HOUR = 4;
    int START_LUNCH_HOUR = 11;
    int START_DINNER_HOUR = 16;

    private static final int ALARM_TIMER_MSEC = 10 * 1000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        Button cancelButton = findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelAlarmManager();
                finish();
            }
        });

        if (needTakeMedicine(getDrugDataListNow())) {
            startAlarmManager();
        }
    }

    private Cursor getDrugDataListNow() {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        ArrayList<DrugData.MEDICAL_TIME> timings = new ArrayList<DrugData.MEDICAL_TIME>();
        if (START_BREAKFAST_HOUR <= hour && hour < START_LUNCH_HOUR) {
            //timings.add(DrugData.MEDICAL_TIME.AFTER_BREAKFAST);
            //timings.add(DrugData.MEDICAL_TIME.AFTER_BREAKFAST_DINNER);
            timings.add(DrugData.MEDICAL_TIME.AFTER_MEAL);
        } else if (START_LUNCH_HOUR <= hour && hour < START_DINNER_HOUR) {
            timings.add(DrugData.MEDICAL_TIME.AFTER_MEAL);
        } else {
            //timings.add(DrugData.MEDICAL_TIME.AFTER_DINNER);
            //timings.add(DrugData.MEDICAL_TIME.AFTER_BREAKFAST_DINNER);
            timings.add(DrugData.MEDICAL_TIME.AFTER_MEAL);
        }
        Cursor cursor = null; // Get drug data from DB
        return cursor;
    }

    private boolean needTakeMedicine(Cursor cursor) {
        //return cursor != null && cursor.getCount() > 0;
        return true;
    }

    private void startAlarmManager() {
        Intent intent = new Intent(this, AlarmActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        mManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        mManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, ALARM_TIMER_MSEC, mPendingIntent);
    }

    private void cancelAlarmManager() {
        if (mPendingIntent != null) {
            mManager.cancel(mPendingIntent);
        }
    }
}
