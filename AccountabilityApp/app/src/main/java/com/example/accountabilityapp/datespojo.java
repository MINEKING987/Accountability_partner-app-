package com.example.accountabilityapp;

import android.content.Context;

import java.util.ArrayList;

public class datespojo {
    ArrayList<String> datesfrom,datesto;

    public datespojo() {
    }

    public ArrayList<String> getDatesfrom() {
        return datesfrom;
    }

    public ArrayList<String> getDatesto() {
        return datesto;
    }

    public void setDatesfrom(ArrayList<String> datesfrom) {
        this.datesfrom = datesfrom;
    }

    public void setDatesto(ArrayList<String> datesto) {
        this.datesto = datesto;
    }
}
