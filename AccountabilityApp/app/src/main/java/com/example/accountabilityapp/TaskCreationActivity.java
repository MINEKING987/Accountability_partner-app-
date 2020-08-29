package com.example.accountabilityapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import androidx.core.util.Pair;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.timepicker.MaterialTimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class TaskCreationActivity extends AppCompatActivity {
    String dateString1,dateString2,hr,min,ohr,omin;
    Button save,Datepick,Timepick;
    TextView datetext,timertext;
    EditText Taskname;
    Boolean datesel = false,timesel = false;
    private DBhelper mydb;
    private Sqlcmds cmds;
    private Alarmcaller acal;
    Calendar calendar = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Create new Task");
        setContentView(R.layout.activity_task_creation);
        save = findViewById(R.id.button_save);
        Datepick = findViewById(R.id.button);
        Timepick = findViewById(R.id.button2);
        Taskname = findViewById(R.id.Taskname);
        datetext = findViewById(R.id.daterangetext);
        timertext = findViewById(R.id.timetext);
        cmds = new Sqlcmds();




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


        Timepick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(TaskCreationActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour,int selectedMinute) {
                        String am_pm = (selectedHour < 12) ? "AM" : "PM";
                        int temphr = selectedHour;
                        if(selectedHour > 12){temphr -= 12;}
                        hr = String.valueOf(temphr);
                        min = String.valueOf(selectedMinute);
                        timesel = true;

                        if(selectedHour < 10){
                            hr = "0"+hr;
                            ohr = "0"+String.valueOf(selectedHour);}
                        else{ohr = String.valueOf(selectedHour);}
                        if(selectedMinute < 10){
                            min = "0"+min; }
                            timertext.setText(hr + ":" + min+" "+am_pm);

                        omin = String.valueOf(selectedMinute);

                    }
                }, hour, minute,false);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String task=null,latest=null,until=null,time=null;
                String xtask = Taskname.getText().toString(),xlatest,xuntil,xtime;
                if(xtask.equals(null)){
                    Toast.makeText(TaskCreationActivity.this,
                            "Please Enter the Task title", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(datesel == false){Toast.makeText(TaskCreationActivity.this,
                        "Please Select Date Range", Toast.LENGTH_SHORT).show();
                    return;}
                if(timesel == false){Toast.makeText(TaskCreationActivity.this,
                        "Please Select Time Range", Toast.LENGTH_SHORT).show();
                    return;}
                task = xtask;
                latest = dateString1;
                until = dateString2;
                time = ohr+":"+omin;
                cmds.addingitem(TaskCreationActivity.this,task,latest,until,time);
                Intent i = new Intent(TaskCreationActivity.this, MainActivity.class);
                acal = new Alarmcaller();
                acal.callAlarm(getApplicationContext());
                startActivity(i);
            }
        });

    }

}