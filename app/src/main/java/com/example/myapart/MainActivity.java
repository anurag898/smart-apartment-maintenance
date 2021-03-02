package com.example.myapart;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText emailId, password,name,confirm,apartmentname,apartmentcode;
    Button btnSignUp;
    TextView tvSignIn;
    FirebaseAuth mFirebaseAuth;
    FirebaseFirestore db;
    private static final String TAG ="MainActivity" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Sign Up");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_main);
        if(!CheckNetwork.isInternetAvailable(MainActivity.this))  //if connection available
        {
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
        }
        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.editText);
        password = findViewById(R.id.editText2);
        btnSignUp = findViewById(R.id.button2);
        tvSignIn = findViewById(R.id.textView);
        name=findViewById(R.id.name);
        apartmentname=findViewById(R.id.apartmentname);
        apartmentcode=findViewById(R.id.apartmentcode);
        confirm=findViewById(R.id.confirm);
        db = FirebaseFirestore.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailId.getText().toString();
                String pwd = password.getText().toString();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if(!CheckNetwork.isInternetAvailable(MainActivity.this))
                {
                    AlertDialog.Builder builder
                            = new AlertDialog
                            .Builder(MainActivity.this);
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
                if(name.getText().toString().isEmpty()){
                    name.setError("Please enter Full Name");
                    name.requestFocus();
                }
                else if(!name.getText().toString().matches("^[a-zA-Z ]+")){
                    name.setError("Only Alphabates allowed");
                    name.requestFocus();
                }
                else if(email.isEmpty()){
                    emailId.setError("Please enter email id");
                    emailId.requestFocus();
                }
                else if (!email.trim().matches(emailPattern)) {
                    emailId.setError("Invalid Email");
                }
                else  if(pwd.isEmpty()){
                    password.setError("Please enter your password");
                    password.requestFocus();
                }
                else if(pwd.length()<6)
                {
                    password.setError("Minimum Six Character");
                }
                else  if(confirm.getText().toString().isEmpty()){
                    confirm.setError("Please enter your confirm password");
                    confirm.requestFocus();
                }
                else  if(!confirm.getText().toString().matches(pwd)){
                    confirm.setError("confirm password incorrect");
                    confirm.requestFocus();
                }
                else if(apartmentname.getText().toString().isEmpty()){
                    apartmentname.setError("Please enter Apartment Name");
                    apartmentname.requestFocus();
                }
                else if(!apartmentname.getText().toString().matches("^[a-zA-Z0-9 ]+")){
                    apartmentname.setError("Only Alphabates allowed");
                    apartmentname.requestFocus();
                }
                else  if(apartmentcode.getText().toString().isEmpty()){
                    apartmentcode.setError("Please enter Apartment code");
                    apartmentcode.requestFocus();
                }
                else if(apartmentcode.length()<6)
                {
                    apartmentcode.setError("Minimum Six Character");
                }
                else  if(email.isEmpty() && pwd.isEmpty()){
                    Toast.makeText(MainActivity.this,"Fields Are Empty!",Toast.LENGTH_SHORT).show();
                }
                else  if(!(email.isEmpty() && pwd.isEmpty())){
                    mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                //Toast.makeText(MainActivity.this,"SignUp Unsuccessful, Please Try Again",Toast.LENGTH_SHORT).show();
                                AlertDialog.Builder builder
                                        = new AlertDialog
                                        .Builder(MainActivity.this);
                                builder.setTitle("Email Already in Use !!!!");
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
                                //Toast.makeText(CreditActivity.this, "Note saved", Toast.LENGTH_SHORT).show();
                                alertDialog.show();
                            }
                            else {
                                String email = emailId.getText().toString();
                                Map<String,Object> account = new HashMap<>();
                                account.put("Name", name.getText().toString());
                                account.put("ApartmentName",apartmentname.getText().toString());
                                account.put("ApartmentCode",apartmentcode.getText().toString());
                                db.collection(email)
                                        .document("secretary")
                                        .set(account)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(MainActivity.this, "Note saved", Toast.LENGTH_SHORT).show();

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                                                Log.d(TAG, e.toString());
                                            }
                                        });
                                startActivity(new Intent(MainActivity.this,HomeActivity.class));
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(MainActivity.this,"Error Occurred!",Toast.LENGTH_SHORT).show();

                }
            }
        });

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });
    }
}
