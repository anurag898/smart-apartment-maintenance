package com.example.myapart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class FirstActivity extends AppCompatActivity {
    Button btnSecretary,btnResident;
    String flag=null;
    String comp="1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        btnSecretary=findViewById(R.id.secretary);
        btnResident=findViewById(R.id.resident);
        btnSecretary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intToMain = new Intent(FirstActivity.this, LoginActivity.class);
                startActivity(intToMain);

            }
        });
        btnResident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileInputStream fis = null;
                try {
                    fis = openFileInput("resident.txt");
                    InputStreamReader isr = new InputStreamReader(fis);
                    BufferedReader br = new BufferedReader(isr);
                    StringBuilder sb = new StringBuilder();
                   flag=br.readLine();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (flag!=null) {
                    CommonData cd=CommonData.getInstance();
                    cd.setEmail(flag);
                    Intent intToMain = new Intent(FirstActivity.this, ResidentActivity.class);
                    startActivity(intToMain);
                } else {
                    Intent intToMain = new Intent(FirstActivity.this, ResidentLoginActivity.class);
                    startActivity(intToMain);
                }

            }
        });
    }
}
