package com.example.myapart;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotActivity extends AppCompatActivity {
    private Button forgot;
    private TextView emailid, goback;
    private FirebaseAuth firebaseAuth;
    private FrameLayout backgroundforgot;
    private LinearLayout emailback;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setTitle("Forgot Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        forgot = findViewById(R.id.forgot);
        emailid = findViewById(R.id.emailid);
        firebaseAuth = FirebaseAuth.getInstance();
        backgroundforgot = findViewById(R.id.backgroundforgot);
        goback = findViewById(R.id.goback);
        emailback = findViewById(R.id.emailgone);

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intSignUp = new Intent(ForgotActivity.this, LoginActivity.class);
                startActivity(intSignUp);
            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkEmailAndPassword();

            }
        });
    }
    private void checkEmailAndPassword() {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if(!CheckNetwork.isInternetAvailable(ForgotActivity.this))
        {
            AlertDialog.Builder builder
                    = new AlertDialog
                    .Builder(ForgotActivity.this);
            builder.setTitle("No Internet !!!!");
            builder
                    .setNegativeButton(
                            "OK",
                            new DialogInterface
                                    .OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which)
                                {

                                    // If user click no
                                    // then dialog box is canceled.
                                    dialog.cancel();
                                }
                            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        else
        if(emailid.getText().toString().isEmpty()){
            emailid.setError("Please enter email id");
            emailid.requestFocus();
        }
        if (emailid.getText().toString().matches(emailPattern)) {

            forgot.setEnabled(false);
            forgot.setTextColor(Color.argb(50, 255, 255, 255));
            sendLink();


        } else {
            // error.setAlpha(1);
            emailid.setError("Invalid Email");
        }
    }
    public void sendLink() {
        String email = emailid.getText().toString().trim();
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            emailback.setVisibility(View.VISIBLE);
                           /* getFragmentManager().beginTransaction()
                                    .setCustomAnimations(R.anim.slide_from_right, R.anim.slideout_from_left)
                                    .replace(R.id.signupin_framelayout, new SignInFragment()).commit();*/
                            emailid.setText("");
                            emailid.setFocusable(false);
                            AlertDialog.Builder builder
                                    = new AlertDialog
                                    .Builder(ForgotActivity.this);
                            builder.setTitle("Link send on registered email id successfully !!!!");
                            builder
                                    .setNegativeButton(
                                            "OK",
                                            new DialogInterface
                                                    .OnClickListener() {

                                                @Override
                                                public void onClick(DialogInterface dialog,
                                                                    int which)
                                                {

                                                    // If user click no
                                                    // then dialog box is canceled.
                                                    dialog.cancel();
                                                }
                                            });
                            AlertDialog alertDialog = builder.create();

                            // Show the Alert Dialog box
                            alertDialog.show();

                        } else {
                            forgot.setEnabled(true);
                            forgot.setTextColor(Color.rgb(255, 255, 255));
                            AlertDialog.Builder builder
                                    = new AlertDialog
                                    .Builder(ForgotActivity.this);
                            builder.setTitle("Email does not exists !!!!");
                            builder
                                    .setNegativeButton(
                                            "OK",
                                            new DialogInterface
                                                    .OnClickListener() {

                                                @Override
                                                public void onClick(DialogInterface dialog,
                                                                    int which)
                                                {

                                                    // If user click no
                                                    // then dialog box is canceled.
                                                    dialog.cancel();
                                                }
                                            });
                            AlertDialog alertDialog = builder.create();

                            // Show the Alert Dialog box
                            alertDialog.show();
                        }
                    }
                });
    }
}