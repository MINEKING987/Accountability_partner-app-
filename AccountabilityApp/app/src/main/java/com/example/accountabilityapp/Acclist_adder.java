package com.example.accountabilityapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

public class Acclist_adder extends AppCompatActivity {
    Button Datepick;
    Boolean datesel;
    EditText name;
    String dateString1,dateString2,mobno;
    TextView datetext;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Add new Accountability Listing");
        setContentView(R.layout.activity_acclist_adder);
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        name = findViewById(R.id.accetext);
        Datepick = findViewById(R.id.accdrange);
        datetext = findViewById(R.id.accdtext);
        Datepick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar c =Calendar.getInstance();
                c.set(Calendar.DAY_OF_MONTH,Calendar.DAY_OF_MONTH-1);
                long nows = c.getTimeInMillis();
                CalendarConstraints.Builder calcon  = new CalendarConstraints.Builder();
                calcon.setValidator(DateValidatorPointForward.now());
                MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.dateRangePicker();
                builder.setTitleText("Select Date range");
                builder.setCalendarConstraints(calcon.build());
                final MaterialDatePicker materialDatePicker= builder.build();
                materialDatePicker.show(getSupportFragmentManager(),"Date_Range");

                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        datesel = true;
                        Pair<Long, Long> sel = (Pair<Long, Long>) selection;
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
                        dateString1 = simpleDateFormat.format(sel.first);
                        dateString2 = simpleDateFormat.format(sel.second);
                        String selecteddates = dateString1+"-->"+dateString2;
                        datetext.setText(""+selecteddates);
                    }
                });
            }
        });
    }

    public void submit(View view) {

        String Name = name.getText().toString();
        if(Name != null && datesel == true)
        {
            mobno = prefs.getString("mobnum",null);
            accpojo enter = new accpojo();
            enter.setName(Name);
            enter.setFrom(dateString1);
            enter.setTo(dateString2);
            enter.setMobno(mobno);
            myRef.child("entrys").child(UUID.randomUUID().toString()).setValue(enter);
            Log.i("chk","Value entered");
            Intent accint=new Intent(this,MainActivity.class);
            Toast.makeText(this, "Listing successfully added!", Toast.LENGTH_SHORT).show();
            startActivity(accint);
            finish();
        }
    }
}