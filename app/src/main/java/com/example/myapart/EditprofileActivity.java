package com.example.myapart;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Map;

public class EditprofileActivity extends AppCompatActivity{

    private Button update;
    private MaterialEditText update_name,update_apartmentcode,update_apartmentname;

    private FirebaseUser user;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private DocumentReference documentReference;
    private ProgressDialog progressDialog;
    private String name, apartmentname, apartmentcode;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    final Fragment fragment3 = new AccountActivity();
    final Fragment fragment1 = new HomeFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);
        getSupportActionBar().setTitle("Edit Profile");
        if(!CheckNetwork.isInternetAvailable(EditprofileActivity.this))  //if connection available
        {
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
        }

        update_name = findViewById(R.id.update_name);
        update_apartmentname = findViewById(R.id.update_apartmentname);
        update_apartmentcode = findViewById(R.id.update_apartmentcode);
        update = findViewById(R.id.update_profile);
        user = FirebaseAuth.getInstance().getCurrentUser();
        progressDialog = new ProgressDialog(this);


        final String EMAIL= firebaseAuth.getCurrentUser().getEmail();
        documentReference = firebaseFirestore.collection(EMAIL).document("secretary");


        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot.exists()) {
                    name = documentSnapshot.getString("Name");
                    apartmentname = documentSnapshot.getString("ApartmentName");
                    apartmentcode = documentSnapshot.getString("ApartmentCode");
                    update_name.setText(name);
                    update_apartmentname.setText(apartmentname);
                    update_apartmentcode.setText(apartmentcode);
                }
            }
        });


    update.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            updateProfile();
        }
        });
    }

    private void updateProfile() {
        final String fname = update_name.getText().toString();
        final String aptname = update_apartmentname.getText().toString();
        final String aptcode = update_apartmentcode.getText().toString();
        if(!CheckNetwork.isInternetAvailable(EditprofileActivity.this))
        {
            AlertDialog.Builder builder
                    = new AlertDialog
                    .Builder(EditprofileActivity.this);
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
        if (fname.isEmpty()) {
            update_name.setError("Enter Name ");
            update_name.requestFocus();
        }
        else
        if(!fname.matches("^[a-zA-Z ]+")){
            update_name.setError("Only Alphabates allowed");
            update_name.requestFocus();
        }
        else
        if (aptname.isEmpty()) {
            update_apartmentname.setError("Enter Email ");
            update_apartmentname.requestFocus();
        }
        else
        if (aptcode.isEmpty()) {
            update_apartmentcode.setError("Enter Contact Number ");
            update_apartmentcode.requestFocus();
        }
        else
        if (aptcode.length() < 6) {
            update_apartmentcode.setError("Minimum 6 character ");
            update_apartmentcode.requestFocus();
        }
        else {


            progressDialog.setMessage("Updating.......");
            progressDialog.show();

            final String EMAIL = firebaseAuth.getCurrentUser().getEmail();
            DocumentReference updateref = firebaseFirestore.collection(EMAIL)
                    .document("secretary");
            Map<String, Object> note = new HashMap<>();
            note.put("Name", fname);
            note.put("ApartmentName", aptname);
            note.put("ApartmentCode", aptcode);
            updateref.set(note);
            progressDialog.dismiss();
            AlertDialog.Builder builder
                    = new AlertDialog
                    .Builder(EditprofileActivity.this);
            builder.setTitle("Done !!!!");
            builder
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

    }
}