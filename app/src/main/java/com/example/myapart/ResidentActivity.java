package com.example.myapart;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ResidentActivity extends AppCompatActivity {
    Button btnTransaction,btnExit,btnNotify,btnComplaint;
    TextView bal,Apt;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resident);
        btnTransaction = findViewById(R.id.transaction);
        btnNotify=findViewById(R.id.notify);
        btnComplaint=findViewById(R.id.complaint);
        btnExit=findViewById(R.id.exit);
        bal=findViewById(R.id.bal);
        Apt = findViewById(R.id.Apt);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        CommonData cd=CommonData.getInstance();
        email=cd.getEmail();
        DocumentReference documentReference = db.collection(email).document("secretary");
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(ResidentActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (documentSnapshot.exists()) {
                    Apt.setText(documentSnapshot.getString("ApartmentName"));

                }
            }
        });
        btnTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intToMain = new Intent(ResidentActivity.this, ResidentTransactionActivity.class);
                startActivity(intToMain);
            }
        });
        btnNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intToMain = new Intent(ResidentActivity.this, ResidentNoticeActivity.class);
                startActivity(intToMain);
            }
        });
        btnComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intToMain = new Intent(ResidentActivity.this, ResidentComplaintActivity.class);
                startActivity(intToMain);
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!CheckNetwork.isInternetAvailable(ResidentActivity.this))
                {
                    AlertDialog.Builder builder
                            = new AlertDialog
                            .Builder(ResidentActivity.this);
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
                else {
                    AlertDialog.Builder builder
                            = new AlertDialog
                            .Builder(ResidentActivity.this);
                    builder.setTitle("Do you want to Exit apartment !!!!");
                    builder
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    FileOutputStream fos = null;
                                    try {
                                        fos = openFileOutput("resident.txt", MODE_PRIVATE);
                                        String data = "";
                                        fos.write(data.getBytes());

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
                                    Intent intToMain = new Intent(ResidentActivity.this, ResidentLoginActivity.class);
                                    startActivity(intToMain);
                                    finish();
                                }
                            });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        });

    }
    @Override
    public void onStart() {
        super.onStart();
        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference notebookRef = db.collection(email).document("transaction").collection("transaction");
        notebookRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                int total=0;
                if (e != null) {
                    return;
                }
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Note note1 = documentSnapshot.toObject(Note.class);
                    String amount = note1.getAmount();
                    String debitcredit = note1.getDebit_Credit();
                    if (debitcredit.equals("Debit")) {
                        total = total - Integer.parseInt(amount);

                    } else {
                        total = total + Integer.parseInt(amount);

                    }

                }
                CommonData cd=CommonData.getInstance();
                cd.setData(total);
                bal.setText("₹ "+String.valueOf(total));
            }
        });

    }
}
