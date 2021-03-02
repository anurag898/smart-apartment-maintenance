package com.example.myapart;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {
    private static long back_pressed;
    final Fragment fragment1 = new HomeFragment();
    final Fragment fragment3 = new AccountActivity();
    final FragmentManager fm = getSupportFragmentManager();
    private BottomNavigationView bottomNavigationView;
    FirebaseAuth mFirebaseAuth;
    Fragment active = fragment1;
    public static int total=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().setTitle("Smart Apartment Maintenance");
        if(!CheckNetwork.isInternetAvailable(HomeActivity.this))  //if connection available
        {
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
        }

        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation_view);

        fm.beginTransaction().add(R.id.nav_host_fragmemt, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.nav_host_fragmemt, fragment1, "1").commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        fm.beginTransaction().hide(active).show(fragment1).commit();
                        active = fragment1;
                        return true;

                    case R.id.navigation_account:
                        fm.beginTransaction().hide(active).show(fragment3).commit();
                        active = fragment3;
                        return true;
                }
                return false;

            }
        });


    }

}
