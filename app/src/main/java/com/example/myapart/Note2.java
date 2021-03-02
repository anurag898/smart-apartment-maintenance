package com.example.myapart;

import com.google.firebase.Timestamp;

public class Note2 {
    private String Flat_no;
    private Timestamp Date;
    private String Description;
    public Note2() {
        //public no-arg constructor needed
    }
    public String getFlat_NO() {
        return Flat_no;
    }
    public Timestamp getDate() {
        return Date;
    }
    public String getDescription() {
        return Description;
    }

}
