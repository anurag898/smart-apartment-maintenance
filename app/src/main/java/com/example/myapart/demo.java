package com.example.myapart;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class demo extends AppCompatActivity {
    TextView bal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_fragment1);
        FirebaseAuth mFirebaseAuth;
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        final String EMAIL= mFirebaseAuth.getCurrentUser().getEmail();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        bal=findViewById(R.id.bal);
        CollectionReference notebookRef = db.collection(EMAIL).document("transaction").collection("transaction");
        notebookRef.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }
                String data = "";
                int total=0;
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Note note = documentSnapshot.toObject(Note.class);
                    String amount = note.getAmount();
                    String debitcredit=note.getDebit_Credit();
                    if(debitcredit.equals("Debit"))
                    {
                        total=total-Integer.parseInt(amount);

                    }
                    else
                    {
                        total=total+Integer.parseInt(amount);

                    }
                }
                bal.setText(data.toString());
            }
        });
    }
}
