package com.example.myapart;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class AccountActivity extends Fragment {

    private NavigationView account_navigation;
    private ImageView account_userimage;
    private TextView name, apartmentname, apartmentcode;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firebaseFirestore;
    private DocumentReference documentReference;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_account, container, false);
        account_navigation = view.findViewById(R.id.account_navigation);
        account_userimage = view.findViewById(R.id.account_userimage);
        name = view.findViewById(R.id.name);
        apartmentname = view.findViewById(R.id.apartmentname);
        apartmentcode = view.findViewById(R.id.apartmentcode);
        Menu menuNav = account_navigation.getMenu();
        MenuItem editprofile=menuNav.findItem(R.id.account_edit_profile);
        editprofile.setVisible(true);
        account_navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.account_edit_profile:
                        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                        if(user==null){
                            Toast.makeText(getContext(), "Please LogIn First", Toast.LENGTH_SHORT).show();
                        }
                        else
                            startActivity(new Intent(getContext(), EditprofileActivity.class));

                        return true;
                    case R.id.logout:
                        if(!CheckNetwork.isInternetAvailable(AccountActivity.this.getActivity()))
                        {
                            AlertDialog.Builder builder
                                    = new AlertDialog
                                    .Builder(AccountActivity.this.getActivity());
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
                                    .Builder(AccountActivity.this.getActivity());
                            builder.setTitle("Do you want to Logout !!!!");
                            builder
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            FirebaseAuth.getInstance().signOut();
                                            Intent intToMain = new Intent(AccountActivity.this.getActivity(), LoginActivity.class);
                                            startActivity(intToMain);
                                            getActivity().finish();
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
                        return true;
                    default:
                        break;
                }
                return false;
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        try {
                final String EMAIL= firebaseAuth.getCurrentUser().getEmail();
                documentReference = firebaseFirestore.collection(EMAIL).document("secretary");
                documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Toast.makeText(getContext(), "Error in Loading", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (documentSnapshot.exists()) {
                            name.setText("Name:  "+documentSnapshot.getString("Name"));
                            apartmentname.setText("Apt:      "+documentSnapshot.getString("ApartmentName"));
                            apartmentcode.setText("Code:   "+documentSnapshot.getString("ApartmentCode"));


                        }
                    }
                });


        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }//on start


}