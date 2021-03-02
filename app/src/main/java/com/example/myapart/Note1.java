package com.example.myapart;

import com.google.firebase.Timestamp;

public class Note1 {
    private Timestamp Date;
    private String Description;
    public Note1() {
        //public no-arg constructor needed
    }

    public Timestamp getDate() {
        return Date;
    }
    public String getDescription() {
        return Description;
    }

}
