package com.example.myapart;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

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

public class SecretaryComplaintActivity extends AppCompatActivity {
    private static final String TAG = "";
    private static String EMAIL = "";
    FirebaseAuth mFirebaseAuth;
    TableLayout tableLayout;
    private TextView textViewData;
    Button btncomplaint;
    AppCompatEditText mdesc, mfno;
    TextView mdate, Textviewdata;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getSupportActionBar().setTitle("Complaints");
            setContentView(R.layout.activity_secretary_complaint);
            mFirebaseAuth = FirebaseAuth.getInstance();
            EMAIL= mFirebaseAuth.getCurrentUser().getEmail();
            textViewData = findViewById(R.id.textViewData);
            mfno = findViewById(R.id.fno);
            mdesc = findViewById(R.id.desc);
            mdate = findViewById(R.id.date);
            textViewData = findViewById(R.id.textViewData);
            mFirebaseAuth = FirebaseAuth.getInstance();
            btncomplaint = findViewById(R.id.submit);
            db = FirebaseFirestore.getInstance();
            if (!CheckNetwork.isInternetAvailable(SecretaryComplaintActivity.this))  //if connection available
            {
                Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
            }

        }
        @Override
        protected void onStart () {
            super.onStart();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            try {

                final CollectionReference notebookRef = db.collection(EMAIL).document("complaint").collection("complaint");
                notebookRef.orderBy("Date", Query.Direction.DESCENDING).addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }
                        String data = "";
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Note2 note = documentSnapshot.toObject(Note2.class);
                            // note.setDocumentId(documentSnapshot.getId());
                            // id[i]=notebookRef.document().getId();

                            String fno = note.getFlat_NO();
                            Timestamp date = note.getDate();
                            Date date1 = date.toDate();
                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                            String strDate = formatter.format(date1);
                            String desc = note.getDescription();
                            data += "Flat No: " + fno + "\n" + "Date: " + strDate + "\n" + desc + "\n\n";


                        }
                        if (data == null) {
                            AlertDialog.Builder builder
                                    = new AlertDialog
                                    .Builder(SecretaryComplaintActivity.this);
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
                        } else {
                            textViewData.setText(data);
                        }
                    }
                });
            } catch (Exception e) {

            }
        }

    }