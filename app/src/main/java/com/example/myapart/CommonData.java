package com.example.myapart;

public class CommonData{
    private static CommonData instance;

    // Global variable
    private int data;
    private String email;
    private int flag;

    // Restrict the constructor from being instantiated
    private CommonData(){}
    public void setFlag(int d){
        this.flag=d;
    }
    public int getFlag(){return this.flag;}
    public void setData(int d){
        this.data=d;
    }
    public void setEmail(String d){this.email=d;}
    public int getData(){
        return this.data;
    }
    public String getEmail(){return this.email;}
    public static synchronized CommonData getInstance(){
        if(instance==null){
            instance=new CommonData();
        }
        return instance;
    }
}