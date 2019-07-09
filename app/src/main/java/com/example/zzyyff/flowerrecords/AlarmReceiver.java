package com.example.zzyyff.flowerrecords;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.Keep;

import static android.content.Context.NOTIFICATION_SERVICE;

public class AlarmReceiver extends BroadcastReceiver {
    NotificationManager notificationManager;
    public PendingIntent pendingIntent;
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent intent1 = new Intent(context, Activity_center.class);

        pendingIntent = PendingIntent.getActivity(context,0,intent1,0);

        notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            //PendingIntent backPendingIntent = PendingIntent.getActivity(context,0,intent,0);
            if (Build.VERSION.SDK_INT >= 26){
                NotificationChannel mChannel = new NotificationChannel("123", "aaaaaaa",NotificationManager.IMPORTANCE_LOW);
                notificationManager.createNotificationChannel(mChannel);

                Notification notification = new Notification.Builder(context,"123")
                        .setSmallIcon(R.drawable.logo)
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.logo))
                        .setContentText("主人！记得写一天的小记哟~")
                        .setContentTitle("花记")
                        .setVisibility(Notification.VISIBILITY_PUBLIC)
                        .setWhen(System.currentTimeMillis())
                        .setTicker("记账提醒")
                        //.setFullScreenIntent(pendingIntent,true)
                        .setContentIntent(pendingIntent)
                        .setLights(0xff00eeff, 500, 200)
                        .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                        .setAutoCancel(true)
                        .build();
                notification.flags |= Notification.FLAG_AUTO_CANCEL;
                notificationManager.notify(6,notification);
            }
            else {
                Notification notification = null;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    notification = new Notification.Builder(context)
                            .setSmallIcon(R.drawable.logo)
                            .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.logo))
                            .setContentText("主人！记得写一天的小记哟~")
                            .setContentTitle("花记")
                            .setWhen(System.currentTimeMillis())
                            .setVisibility(Notification.VISIBILITY_PUBLIC)
                            .setTicker("记账提醒")
                            .setContentIntent(pendingIntent)
                            .setLights(0xff00eeff, 500, 200)
                            .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                            .setAutoCancel(true)
                            .build();
                }

                notificationManager.notify(6, notification);
            }



        }
}