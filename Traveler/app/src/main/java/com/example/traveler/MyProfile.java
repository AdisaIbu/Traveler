package com.example.traveler;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import adapters.CityRecyclerAdapter;
import fragmets.CityDetailsFragment;
import fragmets.CityListFragment;
import fragmets.EditMyProfile;
import model.City;

public class MyProfile extends AppCompatActivity {
    private ShapeableImageView profileImage;
    private TextView nameProfile;
    private TextView emailProfile;
    private TextView membershipDate;
    private TextView numberOfPosts;
    private Button signout;

    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Cities");
    private StorageReference storageRef;

    private String currentUsername;
    private String currentUserId;
    private List<City> cityList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        Objects.requireNonNull(getSupportActionBar()).setElevation(0);

        storageRef = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        profileImage=findViewById(R.id.image_person);
        nameProfile=findViewById(R.id.username_ep);
        emailProfile=findViewById(R.id.email_ep);
        membershipDate=findViewById(R.id.membership_date);
        numberOfPosts=findViewById(R.id.numberOfPosts);
        ImageButton back = findViewById(R.id.arrow_back);
        ImageButton edit = findViewById(R.id.edit_my_profile);
        signout = findViewById(R.id.actionSignout);

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firebaseUser != null && firebaseAuth != null) {
                    firebaseAuth.signOut();

                    startActivity(new Intent(MyProfile.this, MainActivity.class));

                    finish();
                }

            }
        });


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyProfile.this, EditActivity.class));


            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(MyProfile.this, BaseFragmentActivity.class));
            }
        });

        CityApi cityApi=CityApi.getInstance();
        if(cityApi!=null){
            currentUsername = cityApi.getUsername();
            currentUserId=cityApi.getUserId();
        }

        getImageFromFirebase();

        collectionReference.whereEqualTo("userId", CityApi.getInstance().getUserId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {

                            for (QueryDocumentSnapshot items : queryDocumentSnapshots) {
                                City item = items.toObject(City.class);
                                cityList.add(item);

                                int sizeOf= cityList.size();
                                String s=String.valueOf(sizeOf);
                                numberOfPosts.setText(s);

                            }
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private void getImageFromFirebase(){
        storageRef.child("profileImages/my_image"+currentUserId)
                .getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String imageUrl=uri.toString();
                        Log.d("MyProfileImage", "onSuccess: " + imageUrl);

                        Glide.with(MyProfile.this).load(imageUrl).into(profileImage);
                        nameProfile.setText(currentUsername);

                    }
                })
                .addOnFailureListener(e -> Log.d("MyProfile", "onFailure: " +e.getMessage()));
    }
}