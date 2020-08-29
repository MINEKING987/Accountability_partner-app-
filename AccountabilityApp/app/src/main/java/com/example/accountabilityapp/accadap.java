package com.example.accountabilityapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class accadap extends RecyclerView.Adapter<accadap.avh> {
    Context ct;
    accpojo[] vals;
    SharedPreferences prefs;

    public accadap(Context context, accpojo[] vals) {
        ct = context;
        this.vals = vals;
        prefs = PreferenceManager.getDefaultSharedPreferences(ct.getApplicationContext());
    }

    @NonNull
    @Override
    public accadap.avh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ct).inflate(R.layout.acclist_row,parent,false);
        avh aVh = new avh(v);
        return aVh;
    }

    @Override
    public void onBindViewHolder(@NonNull accadap.avh holder, int position) {
        Log.i("Get Data", vals[position].getMobno());
        holder.tv1.setText(vals[position].getName());
        holder.tv2.setText(vals[position].getFrom());
        holder.tv3.setText(vals[position].getTo());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                        ct);
                alertDialog2.setTitle("Confirm See...");
                alertDialog2.setMessage("Are you sure you want see the number? you can only make new partner once a month!");
                alertDialog2.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                PackageManager packageManager = ct.getPackageManager();
                                Intent i = new Intent(Intent.ACTION_VIEW);

                                if(prefs.getString("used","false") == "false"){
                                    try {
                                    String url = "https://wa.me/"+ vals[position].getMobno() +"?text=" + URLEncoder.encode("Hi there!, I would like to be you accountability partner!", "UTF-8");
                                    i.setPackage("com.whatsapp");
                                    i.setData(Uri.parse(url));
                                    if (i.resolveActivity(packageManager) != null) {
                                        ct.startActivity(i);
                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                        Query theQuery = ref.child("entrys").orderByChild("mobno").equalTo(vals[position].getMobno());
                                        Log.i("chk", "this is running");
                                        theQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                                    appleSnapshot.getRef().removeValue();

                                                }
                                                Calendar cal = Calendar.getInstance();
                                                cal.add(Calendar.MONTH,1);
                                                SharedPreferences.Editor editor = prefs.edit();
                                                editor.putString("used", "true");
                                                editor.putString("nxtdate", String.valueOf(cal.getTime()));
                                                editor.commit();
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                Log.i("chk", "onCancelled", databaseError.toException());
                                            }
                                        });
                                    }
                                } catch (Exception e){
                                    e.printStackTrace();
                                    Toast.makeText(ct, "Please check if whatsapp is installed", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{

                                Toast.makeText(ct, "Please wait until "+prefs.getString("nxtdate",null)+" before Trying again", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                alertDialog2.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alertDialog2.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return vals.length;
    }

    public class avh extends RecyclerView.ViewHolder {
        TextView tv1,tv2,tv3;
        public avh(@NonNull View itemView) {
            super(itemView);
            tv1 = itemView.findViewById(R.id.acctv1);
            tv2 = itemView.findViewById(R.id.acctv2);
            tv3 = itemView.findViewById(R.id.acctv3);
        }
    }
}
