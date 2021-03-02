package com.example.myapart;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DebitActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    Button debitsubmit;
    AppCompatEditText mamount,mdesc;
    TextView mdate;
    FirebaseFirestore db;
    private static final String TAG ="DebitActivity" ;
    FirebaseAuth mFirebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Debit");
        setContentView(R.layout.activity_debit);
        init();
        db = FirebaseFirestore.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        final String EMAIL= mFirebaseAuth.getCurrentUser().getEmail();
        findViewById(R.id.date).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        debitsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//            String amount = mamount.getText().toString();
//            String date = mdate.getText().toString();
//            String desc = mdesc.getText().toString();
                CommonData cd=CommonData.getInstance();
                int total=cd.getData();
                if(!CheckNetwork.isInternetAvailable(DebitActivity.this))
                {
                    AlertDialog.Builder builder
                            = new AlertDialog
                            .Builder(DebitActivity.this);
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
                if(mamount.getText().toString().isEmpty()){
                    mamount.setError("Please enter Amount");
                    mamount.requestFocus();
                }
                else  if(mdesc.getText().toString().isEmpty()){
                    mdesc.setError("Please enter Description");
                    mdesc.requestFocus();
                }
                else if(mdate.getText().toString().isEmpty()){
                    mdate.setError("Please enter Date");
                    mdate.requestFocus();
                }
                else if(Integer.parseInt(mamount.getText().toString())>total)
                {
                    AlertDialog.Builder builder
                            = new AlertDialog
                            .Builder(DebitActivity.this);
                    builder.setTitle("Insufficient Balance !!!!");
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
                {
                    Timestamp timeStampDate = null;
                    try {
                        DateFormat formatter;
                        formatter = new SimpleDateFormat("dd/MM/yyyy");
                        Date date = (Date) formatter.parse(mdate.getText().toString());
                        timeStampDate = new Timestamp(date);
                    } catch (ParseException e) {
                        System.out.println("Exception :" + e);

                    }
            Map<String,Object> account = new HashMap<>();
            account.put("Flat_NO","NA");
            account.put("Date", timeStampDate);
            account.put("Description",mdesc.getText().toString());
            account.put("Debit_Credit","Debit");
            account.put("Amount",mamount.getText().toString());
                db.collection(EMAIL)
                        .document("transaction")
                        .collection("transaction")
                        .document()
                        .set(account)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                AlertDialog.Builder builder
                                        = new AlertDialog
                                        .Builder(DebitActivity.this);
                                builder.setTitle("Done !!!!");
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
                                mamount.setText("");
                                mdate.setText("");
                                mdesc.setText("");
                                //Toast.makeText(DebitActivity.this, "Note saved", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(DebitActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, e.toString());
                            }
                        });
            }
            }
        });
    }

    private void init() {
        mamount = findViewById(R.id.amount);
        mdate= findViewById(R.id.date);
        mdesc = findViewById(R.id.desc);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        debitsubmit = findViewById(R.id.debit_submit);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        month=++month;
        String date =dayOfMonth+ "/" +  month  + "/" + year;
        mdate.setText(date);
    }


}

