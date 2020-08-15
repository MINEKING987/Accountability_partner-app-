package com.example.accountabilityapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class tasklistadap extends RecyclerView.Adapter<tasklistadap.vh> {
    Context ct;
    String[] titles,froms,tos;
    private Sqlcmds cmds;
    int val;
    private Alarmcaller acal;
    Boolean[] arrs;
    public tasklistadap(Context mainActivity, ArrayList<String> titles, ArrayList<String> froms, ArrayList<String> tos) {
        ct = mainActivity;
        List title = titles;
        this.titles = (String[]) title.toArray(new String[] {});
        this.froms = (String[]) froms.toArray(new String[] {});
        this.tos = (String[]) tos.toArray(new String[] {});
        val = titles.size();
        arrs = new  Boolean[val];
        Arrays.fill(arrs,true);
    }

    @NonNull
    @Override
    public tasklistadap.vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ct).inflate(R.layout.row_item,parent,false);
        vh Vh = new vh(v);
        return Vh;
    }

    @Override
    public void onBindViewHolder(@NonNull tasklistadap.vh holder, int position) {
        holder.tv.setText(titles[position]);
        holder.datefrom.setText("Next Alarm's Date & time -->"+froms[position]);
        holder.dateto.setText("Alarms until -->"+tos[position]);
      //holder.ll.setVisibility(View.GONE);
        holder.ll.setVisibility(!arrs[position] ? View.VISIBLE:View.GONE);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.ll.setVisibility(!arrs[position] ? View.GONE:View.VISIBLE);
                arrs[position] = !arrs[position];
                chng();
            }
        });
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }

    public void updateitem(MainActivity mainActivity) {
        acal = new Alarmcaller();
        acal.callAlarm(mainActivity.getApplicationContext());
        chng();
    }

    public class vh extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView tv,datefrom,dateto,time;
        LinearLayout ll,ll2;
        public vh(@NonNull View itemView) {
            super(itemView);
            datefrom = itemView.findViewById(R.id.datefrom);
            dateto = itemView.findViewById(R.id.dateto);
            ll = itemView.findViewById(R.id.showthings);
            ll2 = itemView.findViewById(R.id.row_layout);
            tv = itemView.findViewById(R.id.textView);
            ll2.setOnCreateContextMenuListener(this);

        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle("Select an Action");
            contextMenu.add(this.getAdapterPosition(),121,0,"Update");
            contextMenu.add(this.getAdapterPosition(),122,1,"Delete");
            contextMenu.add(this.getAdapterPosition(),123,1,"Details");
        }
    }
    public String itemname(int position)
    {
        return titles[position];
    }
    public void chng(){
        notifyDataSetChanged();
    }
    public void removeitem(Context ct, MenuItem item){
        cmds = new Sqlcmds();
        String title = itemname(item.getGroupId());
        cmds.deletingitem(ct,title);
        int titlelength = titles.length;
        String[] abc = new String[titlelength-1];
        for(int i = 0; i<=titlelength-1;i++ ){
            if(i < item.getGroupId()){abc[i] = titles[i];}
            if(i > item.getGroupId()){abc[i-1] = titles[i];}
        }
        titles = abc;
        chng();
        if(title.length() > 0){
            acal = new Alarmcaller();
            acal.callAlarm(ct);
        };
    }
    public void details(MainActivity mainActivity, MenuItem item) {
        arrs[item.getGroupId()] = !arrs[item.getGroupId()];
        notifyItemChanged(item.getGroupId());
    }
}
