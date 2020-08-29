package com.example.accountabilityapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
RecyclerView rv;
private tasklistadap adaps;
private Sqlcmds cmds;
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.opt_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.logout:
                SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("mobnum", null);
                editor.commit();
                Intent inta = new Intent(this,login.class);
                startActivity(inta);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void acclist(View view) {
        DatabaseReference entryssRef = FirebaseDatabase.getInstance().getReference("entrys/");
        ProgressDialog nDialog;
        nDialog = new ProgressDialog(this);
        nDialog.setMessage("Loading..");
        nDialog.setTitle("Getting Data");
        nDialog.setIndeterminate(false);
        nDialog.setCancelable(false);
        nDialog.show();
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                Log.i("Count " ,""+dataSnapshot.getChildrenCount());
                accpojo[] vals = new accpojo[(int) dataSnapshot.getChildrenCount()];
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    accpojo acc = postSnapshot.getValue(accpojo.class);
                    vals[i] = acc;
                    Log.i("Get Data", vals[i].getMobno());
                    i++;
                }
                Intent accint=new Intent(MainActivity.this,Acclist.class);
                accint.putExtra("values",vals);
                startActivity(accint);
                nDialog.dismiss();
                finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("chk", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        entryssRef.addValueEventListener(postListener);


    }

    public void acclistcreator(View view) {

        Intent accnewint=new Intent(this,Acclist_adder.class);
        startActivity(accnewint);
    }
}