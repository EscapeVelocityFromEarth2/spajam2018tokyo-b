package com.escapevelocityfromearth2.shohosan;


import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

public class MyAlarmManager {

    private static final int ALARM_ID = 0;

    public static void setAlarm(Context context, long delay) {
        // アラームの登録

        cancelAlarm(context);

        Intent push = new Intent();
        push.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        push.setClass(context, AlarmResultActivity.class);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent =  PendingIntent.getActivity(
                context,
                ALARM_ID,
                push,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
        alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(System.currentTimeMillis() + delay, null), pendingIntent);
//        showNotification(context, push);
    }

    public static void cancelAlarm(Context context) {
        // アラームキャンセル

        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        manager.cancel(ALARM_ID);
    }

    public static void showNotification(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(
                    new NotificationChannel("alarm_id","test", NotificationManager.IMPORTANCE_HIGH
                    ));            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "alarm_id")
                    .setSmallIcon(R.drawable.icon)
                    .setCategory(Notification.CATEGORY_MESSAGE)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentTitle(context.getResources().getString(R.string.notification_title))
                    .setContentText(context.getResources().getString(R.string.notification_content));

            NotificationManagerCompat manager = NotificationManagerCompat.from(context);
            manager.notify(Util.NOTIFICATION_ID, builder.build());
        } else {
            Notification.Builder notificationBuilder = new Notification.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setPriority(Notification.PRIORITY_DEFAULT)
                    .setCategory(Notification.CATEGORY_MESSAGE)
                    .setContentTitle(context.getResources().getString(R.string.notification_title))
                    .setContentText(context.getResources().getString(R.string.notification_content));

            //PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(context, 0, push, PendingIntent.FLAG_CANCEL_CURRENT);
            //notificationBuilder.setContentText("Heads-Up Notification on Android L or above.")
            //        .setFullScreenIntent(fullScreenPendingIntent, true);
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(Util.NOTIFICATION_ID, notificationBuilder.build());

        }
    }

    public static void cancelNotification(Context context) {
        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        manager.cancel(Util.NOTIFICATION_ID);
    }

}
