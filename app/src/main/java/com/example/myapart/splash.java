package com.example.myapart;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class splash extends AppCompatActivity {
    Animation topAnim,bottomAnim;
    ImageView image;
    TextView logotext,fullform;
    private static int splash_screen=3500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
//Set animation to elements
        image=findViewById(R.id.image);
        logotext=(TextView)findViewById(R.id.textView3);
        fullform=(TextView)findViewById(R.id.textView4);
        fullform.setAnimation(bottomAnim);
        image.setAnimation(topAnim);
        logotext.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(splash.this,FirstActivity.class);
                startActivity(intent);
                finish();
            }
        },splash_screen);
    }
}
