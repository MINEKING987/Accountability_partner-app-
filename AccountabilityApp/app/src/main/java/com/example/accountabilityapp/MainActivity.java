package com.example.accountabilityapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
RecyclerView rv;
private  DBhelper mydb;
private tasklistadap adaps;
private Sqlcmds cmds;
private Alarmcaller acal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Your Tasks");
        setContentView(R.layout.activity_main);
        cmds = new Sqlcmds();
        ArrayList<String> Titles= cmds.readall(this);
        datespojo p = new datespojo();
        p = cmds.readtoshow(this);
        ArrayList<String> froms= p.getDatesfrom();
        ArrayList<String> tos= p.getDatesto();
        if(Titles.size()>0){
        rv = findViewById(R.id.recyclerview);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adaps = new tasklistadap(this,Titles,froms,tos);
        rv.setAdapter(adaps);
            }
        else {
            Toast.makeText(this, "nothing to show here", Toast.LENGTH_SHORT).show();
        }
    }

    public void creator(View view) {
        Intent i = new Intent(this,TaskCreationActivity.class);
        startActivity(i);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 121:
                Toast.makeText(this, "update", Toast.LENGTH_SHORT).show();
                adaps.updateitem(this);
                return true;
            case 122:
                adaps.removeitem(this,item);
            case 123:
                adaps.details(this,item);
        }
        return super.onContextItemSelected(item);
    }
//    @Override
//    protected void onDestroy() {
//        mydb.close();
//        super.onDestroy();
//    }
}