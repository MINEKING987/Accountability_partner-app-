package com.example.accountabilityapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class Alarmreciever extends BroadcastReceiver {
    Context ct;
    private Sqlcmds cmds;
    @Override
    public void onReceive(Context context, Intent intent) {
        ct = context;
        cmds = new Sqlcmds();
        Log.i("almclr","reciever was run");
        String title = intent.getExtras().getString("item");
        String date =  intent.getExtras().getString("date");
        String until =  intent.getExtras().getString("until");
        cmds.updateitem(context,title,date,until);
        Intent intent2 = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "CHANNEL_ID")
                .setSmallIcon(R.drawable.alarm_add)
                .setContentTitle("Alarm Reached!")
                .setContentText("textContent")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
               NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(11651, builder.build());
    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "name";
            String description = "desc";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = ct.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
