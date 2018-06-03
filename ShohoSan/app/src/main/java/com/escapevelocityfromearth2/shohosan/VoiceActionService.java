package com.escapevelocityfromearth2.shohosan;

import android.accessibilityservice.AccessibilityService;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class VoiceActionService extends AccessibilityService {
    AlarmManager mManager;
    PendingIntent mPendingIntent;
    int START_BREAKFAST_HOUR = 4;
    int START_LUNCH_HOUR = 11;
    int START_DINNER_HOUR = 16;

    private static final int ALARM_TIMER_MSEC = 10 * 1000;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.d("VoiceActionService", "onAccessibilityEvent");
        AccessibilityNodeInfo accessibilityNodeInfo = event.getSource();
        if (null == accessibilityNodeInfo)
            return;

        String className = accessibilityNodeInfo.getClassName().toString(); //!< 音声認識の判断クラス
        final CharSequence text = accessibilityNodeInfo.getText();  //!< 音声認識に登録されたテキスト
        if(!className.contains("com.google.android.apps.gsa.searchplate")
                || null == text)
            return;

        String str = text.toString();
        if ((str.contains("ごはん") || str.contains("ご飯"))
                && ((str.contains("食べる") || str.contains("たべる"))
                || ((str.contains("これから") || str.contains("今から") || str.contains("いまから"))))) {
            if (needTakeMedicine(getDrugDataListNow())) {
                Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
                startAlarmManager();
                MyAlarmManager.showNotification(getApplicationContext());
            }
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
        Intent intent = new Intent(this, AlarmResultActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        mManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        mManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, ALARM_TIMER_MSEC, mPendingIntent);
    }

    @Override
    public void onInterrupt() {
    }

}