package com.example.accountabilityapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;

public class Alarmcaller {
    final static int RQS_1 = 1516601;
    private Sqlcmds cmds;

    public void callAlarm(Context ct) {
        cmds = new Sqlcmds();
        Pair<String, String> datestrs = cmds.readlatestdate(ct);
        String until = cmds.readuntil(ct,datestrs.first);
        if(datestrs.first == "" && datestrs.second == "" || until == null){return;}
        Calendar now = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        String full[],date[],time[];
        full =datestrs.second.split("-");
        date = full[0].split("/");
        time = full[1].split(":");
        calendar.set(Calendar.YEAR,Integer.parseInt(date[0]));
        calendar.set(Calendar.MONTH,Integer.parseInt(date[1])-1);
        calendar.set(Calendar.DAY_OF_MONTH,Integer.parseInt(date[2]));
        calendar.set(Calendar.HOUR_OF_DAY,Integer.parseInt(time[0]));
        calendar.set(Calendar.MINUTE,Integer.parseInt(time[1]));
        int chk = calendar.compareTo(now);
        Log.i("chk", String.valueOf(now.getTime()));
        Log.i("chk", String.valueOf(calendar.getTime()));
        Log.i("chk", String.valueOf(chk));
        if(chk <= 0){
            cmds.updateitem(ct,datestrs.first,datestrs.second,until);
            callAlarm(ct);
        }
        else{
            Intent intent = new Intent(ct, Alarmreciever.class);
            intent.putExtra("item",datestrs.first);
            intent.putExtra("date",datestrs.second);
            intent.putExtra("until",until);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(ct, RQS_1, intent, 0);
            AlarmManager alarmManager = (AlarmManager) ct.getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            Toast.makeText(ct, "alarm caller called", Toast.LENGTH_SHORT).show();
        }
    }
}