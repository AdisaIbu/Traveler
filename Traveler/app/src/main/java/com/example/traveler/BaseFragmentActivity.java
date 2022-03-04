package com.example.traveler;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import fragmets.CityDetailsFragment;
import fragmets.CityListFragment;
import model.City;
import model.CityViewModel;

public class BaseFragmentActivity extends AppCompatActivity {

    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_fragment);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();




        Objects.requireNonNull(getSupportActionBar()).setElevation(0);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container_view, CityListFragment.class, null)
                    .commit();
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_menu);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment selectedFragment = null;

                int id = item.getItemId();

                if (id == R.id.list_nav_button) {

                    selectedFragment = CityListFragment.newInstance();
                } else if (id == R.id.details_nav_button) {

                    selectedFragment = CityDetailsFragment.newInstance();

                } else if(id == R.id.my_profile){
                    Intent intent = new Intent(BaseFragmentActivity.this, MyProfile.class);
                    startActivity(intent);
                }

                if (selectedFragment != null) {

                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragment_container_view, selectedFragment, null)
                            .commit();
                }

                return true;
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.actionAdd:

                if (firebaseUser != null && firebaseAuth != null) {
                    startActivity(new Intent(BaseFragmentActivity.this, PostCityActivity.class));
                }

                break;


//            case R.id.actionSignout:
//
//
//
//                break;
        }
        return super.onOptionsItemSelected(item);
    }
}