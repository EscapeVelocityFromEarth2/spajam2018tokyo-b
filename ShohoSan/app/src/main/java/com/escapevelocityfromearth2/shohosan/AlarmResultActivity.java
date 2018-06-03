package com.escapevelocityfromearth2.shohosan;

import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.escapevelocityfromearth2.shohosan.view.AlarmDialog;

public class AlarmResultActivity extends AppCompatActivity {

    AlarmDialog dialog;

    int TYPE_ALARM = 0;
    int TYPE_DISCONNECT = 1;
    int type = TYPE_ALARM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_result);

        type = getIntent().getIntExtra("type", TYPE_ALARM);

        ActionBar bar = getSupportActionBar();
        if (bar != null) bar.setTitle("");
        setTitle("");
        showDistDialog();
        MyAlarmManager.cancelNotification(getApplicationContext());
    }

    public void showDistDialog() {
        dialog = new AlarmDialog();
//        dialog.setTag(tag);
        dialog.setDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                finishActivity();
            }
        });
        dialog.setActivity(this);
        dialog.setType(type);
        dialog.show(getSupportFragmentManager(), AlarmDialog.class.getName());
//        dialog.setType(type);
//        dialog.requestBeacon(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("test", "onDestroy()");
    }

    public void finishActivity() {
        this.finish();
    }
}
