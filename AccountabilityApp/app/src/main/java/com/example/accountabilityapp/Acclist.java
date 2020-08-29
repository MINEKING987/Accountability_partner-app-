package com.example.accountabilityapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Acclist extends AppCompatActivity {

    RecyclerView ls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Accountability Listing");
        setContentView(R.layout.activity_acclist);
        accpojo[] vals = (accpojo[]) getIntent().getSerializableExtra("values");
        ls = findViewById(R.id.acclistview);
        ls.setLayoutManager(new LinearLayoutManager(this));
        accadap adaps = new accadap(this,vals);
        ls.setAdapter(adaps);
    }
    }
