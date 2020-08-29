package com.example.accountabilityapp;

import java.io.Serializable;

public class accpojo implements Serializable {
    String mobno;
    String from;
    String to;
    String name;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getMobno() {
        return mobno;
    }

    public void setMobno(String mobno) {
        this.mobno = mobno;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }


}
