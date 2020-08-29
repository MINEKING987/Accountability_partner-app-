package com.example.accountabilityapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Sqlcmds {


    public boolean addingitem(Context ct, String task,String latest,String until,String time){
        try {
            latest = latest + "-" +time;
            DBhelper dbHelper = new DBhelper(ct);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DBhelper.FeedEntry.COLUMN_NAME_TASK, task);
            values.put(DBhelper.FeedEntry.COLUMN_NAME_LatestDate, latest);
            values.put(DBhelper.FeedEntry.COLUMN_NAME_AlarmUntil, until);
            values.put(DBhelper.FeedEntry.COLUMN_NAME_Timedaily, time);
            long newRowId = db.insert(DBhelper.FeedEntry.TABLE_NAME, null, values);
            if(newRowId == -1){
                Toast.makeText(ct, "Error found...Please Retry(Don't enter the exact same task name)", Toast.LENGTH_SHORT).show();
            }
            else
            {Toast.makeText(ct, "Task successfully added!", Toast.LENGTH_SHORT).show();
            }
        }
        catch (SQLException e){
            Toast.makeText(ct, ""+e, Toast.LENGTH_SHORT).show();
        }
        return true;}
    public boolean deletingitem(Context ct, String title){
        DBhelper dbHelper = new DBhelper(ct);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = DBhelper.FeedEntry.COLUMN_NAME_TASK + " LIKE ?";
        String[] selectionArgs = {title};
        int deletedRows = db.delete(DBhelper.FeedEntry.TABLE_NAME, selection, selectionArgs);
        Toast.makeText(ct, "deleted", Toast.LENGTH_SHORT).show();
        return true;}
    public void updateitem(Context context,String title,String latdate,String until){
        Calendar calendar = Calendar.getInstance();
        Calendar untilcal = Calendar.getInstance();
        String full[],date[],time[];
        full =latdate.split("-");
        date = full[0].split("/");
        time = full[1].split(":");
        calendar.set(Integer.parseInt(date[0]),Integer.parseInt(date[1])-1,
                Integer.parseInt(date[2]),Integer.parseInt(time[0]),Integer.parseInt(time[1]));
        Log.i("chk", String.valueOf(until));
        date = until.split("/");
        untilcal.set(Integer.parseInt(date[0]),Integer.parseInt(date[1])-1,
                Integer.parseInt(date[2]),Integer.parseInt(time[0]),Integer.parseInt(time[1]));
        if(calendar.compareTo(untilcal) == -1)
        {
        calendar.add(Calendar.DATE,1);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd-hh:mm");
        String val = simpleDateFormat.format(calendar.getTime());
        DBhelper dbHelper = new DBhelper(context.getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBhelper.FeedEntry.COLUMN_NAME_LatestDate, val);
        String selection = DBhelper.FeedEntry.COLUMN_NAME_TASK + " LIKE ?";
        String[] selectionArgs = { title };
    int count = db.update(
                DBhelper.FeedEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        return;}
    else{
        deletingitem(context,title);
        }
    }
    public ArrayList<String> readall(Context ct){
        DBhelper dbHelper = new DBhelper(ct);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {DBhelper.FeedEntry.COLUMN_NAME_TASK};
        String sortOrder =
                DBhelper.FeedEntry.COLUMN_NAME_TASK;
        Cursor cursor = db.query(
                DBhelper.FeedEntry.TABLE_NAME,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );
        ArrayList<String> Titles = new ArrayList<>();
        while(cursor.moveToNext()) {
            String Title = cursor.getString(
                    cursor.getColumnIndexOrThrow(DBhelper.FeedEntry.COLUMN_NAME_TASK));
            Titles.add(Title);
        }
        cursor.close();
        return Titles;
    }
    public Pair<String, String> readlatestdate(Context ct){
        Pair<String, String> p;
        DBhelper dbHelper = new DBhelper(ct);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sortOrder =
                DBhelper.FeedEntry.COLUMN_NAME_LatestDate;
        Cursor cursor = db.query(
                DBhelper.FeedEntry.TABLE_NAME,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );
       if (cursor.getCount()>0){
           cursor.moveToFirst();
            String Latest = cursor.getString(
                    cursor.getColumnIndexOrThrow(DBhelper.FeedEntry.COLUMN_NAME_LatestDate));
            String title = cursor.getString(
                    cursor.getColumnIndexOrThrow(DBhelper.FeedEntry.COLUMN_NAME_TASK));
        p = new Pair(title, Latest);}
        else{
        p = new Pair("", "");
        }
        cursor.close();

        return p;
    }
    public ArrayList<String> readtimes(Context ct){
        DBhelper dbHelper = new DBhelper(ct);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {DBhelper.FeedEntry.COLUMN_NAME_TASK};
        String sortOrder =
                DBhelper.FeedEntry.COLUMN_NAME_TASK;
        Cursor cursor = db.query(
                DBhelper.FeedEntry.TABLE_NAME,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );
        ArrayList<String> Times = new ArrayList<>();
        while(cursor.moveToNext()) {
            String Time = cursor.getString(
                    cursor.getColumnIndexOrThrow(DBhelper.FeedEntry.COLUMN_NAME_Timedaily));
            Times.add(Time);
        }
        cursor.close();
        return Times;
    }
    public String readuntil(Context ct,String task){
        DBhelper dbHelper = new DBhelper(ct);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {DBhelper.FeedEntry.COLUMN_NAME_TASK};
        String whr = DBhelper.FeedEntry.COLUMN_NAME_TASK + " = ?";
        String[] whrs = {task};
        String sortOrder =
                DBhelper.FeedEntry.COLUMN_NAME_TASK;
        Cursor cursor = db.query(
                DBhelper.FeedEntry.TABLE_NAME,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                whr,              // The columns for the WHERE clause
                whrs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );
            if(cursor.getCount()>0){
                cursor.moveToFirst();
                String until = cursor.getString(
                    cursor.getColumnIndexOrThrow(DBhelper.FeedEntry.COLUMN_NAME_AlarmUntil));
        cursor.close();
        return until;}
            else{cursor.close();
                return null;}
    }
    public datespojo readtoshow(Context ct){
        datespojo p = new datespojo();
        DBhelper dbHelper = new DBhelper(ct);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sortOrder =
                DBhelper.FeedEntry.COLUMN_NAME_TASK;
        Cursor cursor = db.query(
                DBhelper.FeedEntry.TABLE_NAME,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );
        ArrayList<String> froms=new ArrayList<String>(),tos = new ArrayList<String>();
        if (cursor.getCount()>0) {
            while (cursor.moveToNext()) {
                String from = cursor.getString(
                        cursor.getColumnIndexOrThrow(DBhelper.FeedEntry.COLUMN_NAME_LatestDate));
                String to = cursor.getString(
                        cursor.getColumnIndexOrThrow(DBhelper.FeedEntry.COLUMN_NAME_AlarmUntil));
                tos.add(to);
                froms.add(from);
            }
            p.setDatesfrom(froms);
            p.setDatesto(tos);
        }
        else{
            froms.clear();
            froms.add("");
            tos.clear();
            tos.add("");
            p.setDatesfrom(froms);
            p.setDatesto(tos);
        }
            cursor.close();
            return p;
    }
}
