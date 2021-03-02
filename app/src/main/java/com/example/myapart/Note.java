package com.example.myapart;

import com.google.firebase.Timestamp;

public class Note {
    private String Amount;
    private Timestamp Date;
    private String Flat_NO;
    private String Description;
    private String Debit_Credit;
    public Note() {
        //public no-arg constructor needed
    }
    public String getFlat_NO() {
        return Flat_NO;
    }
    public Timestamp getDate() {
        return Date;
    }
    public String getDescription() {
        return Description;
    }
    public String getAmount() {
        return Amount;
    }
    public String getDebit_Credit(){ return Debit_Credit;}
}
