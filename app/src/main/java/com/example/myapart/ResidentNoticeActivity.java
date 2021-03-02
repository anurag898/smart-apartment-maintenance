package com.example.myapart;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ResidentNoticeActivity extends AppCompatActivity {
    FirebaseAuth mFirebaseAuth;
    TableLayout tableLayout;
    TextView textviewdata;
    String email;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resident_notice);
        getSupportActionBar().setTitle("Notice");
        textviewdata = findViewById(R.id.textViewData);
        mFirebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        CommonData cd=CommonData.getInstance();
        email=cd.getEmail();
        if (!CheckNetwork.isInternetAvailable(ResidentNoticeActivity.this))  //if connection available
        {
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        try {


            final CollectionReference notebookRef = db.collection(email).document("notice").collection("notice");
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
                                .Builder(ResidentNoticeActivity.this);
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
                        textviewdata.setText(data);
                    }
                }
            });
        }
        catch(Exception e)
        {

        }
    }
}
