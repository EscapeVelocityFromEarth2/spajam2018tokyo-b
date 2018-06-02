package com.escapevelocityfromearth2.shohosan;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import io.flic.lib.FlicBroadcastReceiver;
import io.flic.lib.FlicButton;

/**
 * Created by tsukamotohiroshinozomi on 2018/06/03.
 */

public class ExampleBroadcastReceiver extends FlicBroadcastReceiver {
    @Override
    protected void onRequestAppCredentials(Context context) {
        Config.setFlicCredentials();
    }

    @Override
    public void onButtonUpOrDown(Context context, FlicButton button, boolean wasQueued, int timeDiff, boolean isUp, boolean isDown) {
        if (isDown) {
//            Notification notification = new Notification.Builder(context)
//                    .setSmallIcon(R.mipmap.ic_launcher)
//                    .setContentTitle("Button was pressed")
//                    .setContentText("Pressed last time at ")
//                    .build();
//            ((NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE)).notify(1, notification);
            Toast.makeText(context, "You are good!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onButtonRemoved(Context context, FlicButton button) {
        Log.d("yo", "removed");
        Toast.makeText(context, "Button was removed", Toast.LENGTH_SHORT).show();
    }


}
