package com.example.traveler;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import adapters.CityRecyclerAdapter;
import listeners.OnCityClickedListener;
import model.City;
import model.CityViewModel;

//public class CityListActivity extends AppCompatActivity implements OnCityClickedListener {
//
//
//    private FirebaseUser firebaseUser;
//    private FirebaseAuth firebaseAuth;
//    private FirebaseAuth.AuthStateListener authStateListener;
//
//    private FirebaseFirestore db = FirebaseFirestore.getInstance();
//    private StorageReference storageReference;
//    private List<City> cityList;
//    private RecyclerView recyclerView;
//    private CityRecyclerAdapter adapter;
//
//    private CollectionReference collectionReference = db.collection("Cities");
//    private TextView noEntryTv;
//
//    private CityViewModel cityViewModel;
//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_city_list);
//
//        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_menu);
//        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                return true;
//            }
//        });
//
//        firebaseAuth = FirebaseAuth.getInstance();
//        firebaseUser = firebaseAuth.getCurrentUser();
//
//        noEntryTv = findViewById(R.id.noThoughtsTv);
//        cityList = new ArrayList<>();
//
//        recyclerView = findViewById(R.id.recyclerView);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        Objects.requireNonNull(getSupportActionBar()).setElevation(0);
//
//
//
//        cityViewModel = new ViewModelProvider(CityListActivity.this)
//                .get(CityViewModel.class);
//
//
//
//
//    }
//
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        getMenuInflater().inflate(R.menu.menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//
//        switch (item.getItemId()) {
//
//            case R.id.actionAdd:
//
//
//                if (firebaseUser != null && firebaseAuth != null) {
//                    startActivity(new Intent(CityListActivity.this, PostCityActivity.class));
//                }
//
//                break;
//
//            case R.id.actionSignout:
//
//
//                if (firebaseUser != null && firebaseAuth != null) {
//                    firebaseAuth.signOut();
//
//                    startActivity(new Intent(CityListActivity.this, MainActivity.class));
//
//                    finish();
//                }
//
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        collectionReference.whereEqualTo("userId", CityApi.getInstance().getUserId())
//                .get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        if (!queryDocumentSnapshots.isEmpty()) {
//
//                            for (QueryDocumentSnapshot items : queryDocumentSnapshots) {
//                                City item = items.toObject(City.class);
//                                cityList.add(item);
//
//
//                            }
//
//
//                            adapter = new CityRecyclerAdapter(CityListActivity.this, cityList, CityListActivity.this::onCityClicked);
//                            recyclerView.setAdapter(adapter);
//                            adapter.notifyDataSetChanged();
//
//
//                        } else {
//                            noEntryTv.setVisibility(View.VISIBLE);
//                        }
//
//
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//
//                    }
//                });
//
//    }
//
//    @Override
//    public void onCityClicked(City city) {
//
//        startActivity(new Intent(CityListActivity.this, CityActivity.class));
//        Log.d("park", "onCityClicked: " + city.getName());
//
//        cityViewModel.setSelectedCity(city);
//
//
//    }
//}