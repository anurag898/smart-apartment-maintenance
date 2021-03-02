package com.example.myapart;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ResidentLoginActivity extends AppCompatActivity {
    EditText emailId, password;
    Button btnSignIn;
    TextView tvSignUp,forgot;
    FirebaseAuth mFirebaseAuth;
    public String AptCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Join");
        setContentView(R.layout.activity_resident_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //final int net=checkConnection();
        if(!CheckNetwork.isInternetAvailable(ResidentLoginActivity.this))  //if connection available
        {
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
        }
        emailId = findViewById(R.id.editText);
        password = findViewById(R.id.editText2);
        btnSignIn = findViewById(R.id.button2);
       emailId.setOnFocusChangeListener(new View.OnFocusChangeListener(){
           public void onFocusChange(View v, boolean hasFocus) {
               if(!hasFocus) {
                   FirebaseFirestore db = FirebaseFirestore.getInstance();
                   DocumentReference documentReference = db.collection(emailId.getText().toString()).document("secretary");
                   documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                       @Override
                       public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                           if (e != null) {
                               //Toast.makeText(getContext(), "Error in Loading", Toast.LENGTH_SHORT).show();
                               return;
                           }
                           if (documentSnapshot.exists()) {
                               AptCode=documentSnapshot.getString("ApartmentCode");

                           }
                       }
                   });

               }
           }
       });
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailId.getText().toString();
                String pwd = password.getText().toString();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if(!CheckNetwork.isInternetAvailable(ResidentLoginActivity.this))
                {
                    AlertDialog.Builder builder
                            = new AlertDialog
                            .Builder(ResidentLoginActivity.this);
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
                if(email.isEmpty()){
                    emailId.setError("Please enter email id");
                    emailId.requestFocus();
                }
                else  if(pwd.isEmpty()){
                    password.setError("Please Apartment Code");
                    password.requestFocus();
                }
                else if (!email.trim().matches(emailPattern)) {
                    emailId.setError("Invalid Email");
                }
                else if(pwd.length()<6)
                {
                    password.setError("Minimum Six Character");
                }
                else  if(email.isEmpty() && pwd.isEmpty()){
                    Toast.makeText(ResidentLoginActivity.this,"Fields Are Empty!",Toast.LENGTH_SHORT).show();
                }
                else  if(pwd.equals(AptCode)){
                    FileOutputStream fos = null;
                    try {
                        fos = openFileOutput("resident.txt", MODE_PRIVATE);
                        fos.write(email.getBytes());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (fos != null) {
                            try {
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    CommonData cd=CommonData.getInstance();
                    cd.setEmail(email);
                    Intent intToHome = new Intent(ResidentLoginActivity.this,ResidentActivity.class);
                    startActivity(intToHome);
                }

                else{

                    AlertDialog.Builder builder
                            = new AlertDialog
                            .Builder(ResidentLoginActivity.this);
                    builder.setTitle("Incorrect Email or Apartment Code !!!!");
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
                    //Toast.makeText(LoginActivity.this,"Login Error, Please Login Again",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}

