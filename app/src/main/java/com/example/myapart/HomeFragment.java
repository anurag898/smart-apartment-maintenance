package com.example.myapart;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class HomeFragment extends Fragment{
    Button btnLogout,btnDebit,btnCredit,btnTransaction,balButton,btnNotice,btnComplaint;
    TextView bal,Apt;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home_fragment1, container, false);
        btnLogout = view.findViewById(R.id.logout);
        btnDebit = view.findViewById(R.id.debit);
        btnCredit = view.findViewById(R.id.credit);
        btnTransaction = view.findViewById(R.id.transaction);
        balButton=view.findViewById(R.id.balButton);
        btnNotice=view.findViewById(R.id.notify);
        bal=view.findViewById(R.id.bal);
        btnComplaint=view.findViewById(R.id.complaint);
        Apt = view.findViewById(R.id.Apt);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        DocumentReference documentReference = db.collection(mFirebaseAuth.getCurrentUser().getEmail()).document("secretary");
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(getContext(), "Error in Loading", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (documentSnapshot.exists()) {
                    Apt.setText(documentSnapshot.getString("ApartmentName"));

                }
            }
        });
        btnDebit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intToMain = new Intent(HomeFragment.this.getActivity(), DebitActivity.class);
                startActivity(intToMain);

            }
        });
        btnTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intToMain = new Intent(HomeFragment.this.getActivity(), TransactionActivity.class);
                startActivity(intToMain);

            }
        });
        btnComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intToMain = new Intent(HomeFragment.this.getActivity(), SecretaryComplaintActivity.class);
                startActivity(intToMain);

            }
        });
        btnCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intToMain = new Intent(HomeFragment.this.getActivity(), CreditActivity.class);
                startActivity(intToMain);
            }
        });

        btnNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intToMain = new Intent(HomeFragment.this.getActivity(), SecretaryNoticeActivity.class);
                startActivity(intToMain);
            }
        });

       return view;

    }
    @Override
    public void onStart() {
        super.onStart();
        mFirebaseAuth = FirebaseAuth.getInstance();
        final String EMAIL= mFirebaseAuth.getCurrentUser().getEmail();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference notebookRef = db.collection(EMAIL).document("transaction").collection("transaction");
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