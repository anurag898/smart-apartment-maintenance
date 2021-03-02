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
import android.widget.TableLayout;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SecretaryNoticeActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private static final String TAG = "TransactionActivity";
    FirebaseAuth mFirebaseAuth;
    TableLayout tableLayout;
    private TextView textViewData;
    Button btnNotice;
    AppCompatEditText mdesc;
    TextView mdate,Textviewdata;
    String email;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Notify");
        setContentView(R.layout.activity_secretary_notice);
        textViewData = findViewById(R.id.textViewData);
        mdesc=findViewById(R.id.desc);
        mdate=findViewById(R.id.date);
        textViewData=findViewById(R.id.textViewData);
        mFirebaseAuth = FirebaseAuth.getInstance();
        btnNotice=findViewById(R.id.submit);
        db = FirebaseFirestore.getInstance();
        final String EMAIL= mFirebaseAuth.getCurrentUser().getEmail();
        findViewById(R.id.date).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        if(!CheckNetwork.isInternetAvailable(SecretaryNoticeActivity.this))  //if connection available
        {
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
        }
        btnNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//            String amount = mamount.getText().toString();
//            String date = mdate.getText().toString();
//            String desc = mdesc.getText().toString();
                CommonData cd=CommonData.getInstance();
                int total=cd.getData();
                if(!CheckNetwork.isInternetAvailable(SecretaryNoticeActivity.this))
                {
                    AlertDialog.Builder builder
                            = new AlertDialog
                            .Builder(SecretaryNoticeActivity.this);
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
                else  if(mdesc.getText().toString().isEmpty()){
                    mdesc.setError("Please enter Description");
                    mdesc.requestFocus();
                }
                else if(mdate.getText().toString().isEmpty()){
                    mdate.setError("Please enter Date");
                    mdate.requestFocus();
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
                    account.put("Date", timeStampDate);
                    account.put("Description",mdesc.getText().toString());
                    db.collection(EMAIL)
                            .document("notice")
                            .collection("notice")
                            .document()
                            .set(account)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    AlertDialog.Builder builder
                                            = new AlertDialog
                                            .Builder(SecretaryNoticeActivity.this);
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
                                    mdate.setText("");
                                    mdesc.setText("");
                                    //Toast.makeText(DebitActivity.this, "Note saved", Toast.LENGTH_SHORT).show();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SecretaryNoticeActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, e.toString());
                                }
                            });
                }
            }
        });



    }
    @Override
    protected void onStart() {
        super.onStart();


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        try {


            final String EMAIL = mFirebaseAuth.getCurrentUser().getEmail();
            final CollectionReference notebookRef = db.collection(EMAIL).document("notice").collection("notice");
            notebookRef.orderBy("Date", Query.Direction.DESCENDING).addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                    if (e != null) {
                        return;
                    }
                    String data = "";
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Note1 note = documentSnapshot.toObject(Note1.class);
                        // note.setDocumentId(documentSnapshot.getId());
                        // id[i]=notebookRef.document().getId();
                        Timestamp date = note.getDate();
                        Date date1 = date.toDate();
                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                        String strDate = formatter.format(date1);
                        String desc = note.getDescription();
                        data += "Date: " + strDate +  "\n" + desc + "\n\n";



                    }
                    if (data == null) {
                        AlertDialog.Builder builder
                                = new AlertDialog
                                .Builder(SecretaryNoticeActivity.this);
                        builder.setTitle("No Internet !!!!");
                        builder
                                .setNegativeButton(
                                        "OK",
                                        new DialogInterface
                                                .OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog,
                                                                int which) {

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
                        textViewData.setText(data);
                    }
                }
            });
        }
        catch(Exception e)
        {

        }
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
