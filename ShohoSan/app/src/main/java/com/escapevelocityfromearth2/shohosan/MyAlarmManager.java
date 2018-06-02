package com.escapevelocityfromearth2.shohosan;


import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

public class MyAlarmManager {

    public static void setAlarm(Context context, long delay) {
        // アラームの登録

        Intent push = new Intent();
        push.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        push.setClass(context, AlarmResultActivity.class);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent =  PendingIntent.getActivity(
                context,
                0,
                push,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
        alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(System.currentTimeMillis() + delay, null), pendingIntent);
//        showNotification(context, push);
    }

    public static void showNotification(Context context, Intent push) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "alarm_id")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setCategory(Notification.CATEGORY_MESSAGE)
                    .setContentTitle("Sample Notification")
                    .setContentText("This is a normal notification.");

            PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(context, 0, push, PendingIntent.FLAG_CANCEL_CURRENT);
            builder.setContentText("Heads-Up Notification on Android L or above.")
                    .setFullScreenIntent(fullScreenPendingIntent, true);

            NotificationManagerCompat manager = NotificationManagerCompat.from(context);
            manager.notify(Util.NOTIFICATION_ID, builder.build());
        } else {
            Notification.Builder notificationBuilder = new Notification.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setPriority(Notification.PRIORITY_DEFAULT)
                    .setCategory(Notification.CATEGORY_MESSAGE)
                    .setContentTitle("Sample Notification")
                    .setContentText("This is a normal notification.");

            PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(context, 0, push, PendingIntent.FLAG_CANCEL_CURRENT);
            notificationBuilder.setContentText("Heads-Up Notification on Android L or above.")
                    .setFullScreenIntent(fullScreenPendingIntent, true);
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(Util.NOTIFICATION_ID, notificationBuilder.build());

        }
    }

    public static void cancelAlarm() {
        // アラームキャンセル
    }


}
