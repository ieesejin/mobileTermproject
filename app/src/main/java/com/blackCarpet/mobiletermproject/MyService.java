package com.blackCarpet.mobiletermproject;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

public class MyService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
        createNotification();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    public void createNotification(){
        Intent actionIntent = new Intent(this, BlackService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel("channel_id", "channel_name", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("BlackCarpet");
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id");
        builder.setSmallIcon(R.drawable.layor_icon2)
                .setContentTitle("Black Carpet")
                .setContentText("Click for start Screen saver")
                .setContentIntent(pendingIntent);

        Notification notification = builder.build();

        this.startForeground(1001, notification);
    }
}
