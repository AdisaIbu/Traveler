package com.example.traveler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private Button login;
    private EditText emailEt;
    private EditText passwordEt;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser firebaseUser;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Objects.requireNonNull(getSupportActionBar()).setElevation(0);

        login = findViewById(R.id.loginButtonLg);
        emailEt = findViewById(R.id.loginEmailEt);
        passwordEt = findViewById(R.id.loginPasswordEt);

        firebaseAuth = FirebaseAuth.getInstance();


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signinEmailAndPassword(emailEt.getText().toString().trim(),
                        passwordEt.getText().toString().trim());
            }
        });


    }

    private void signinEmailAndPassword(String email, String password) {

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {

            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                firebaseUser = firebaseAuth.getCurrentUser();
                                assert firebaseUser != null;
                                String currentUserId = firebaseUser.getUid();

                                collectionReference.whereEqualTo("userId", currentUserId)
                                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                            @Override
                                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                                                assert value != null;
                                                if(!value.isEmpty()){

                                                    for (QueryDocumentSnapshot snapshot : value){
                                                        CityApi cityApi = CityApi.getInstance();
                                                        cityApi.setUserId(currentUserId);
                                                        cityApi.setUsername(snapshot.getString("username"));

                                                        startActivity(new Intent(LoginActivity.this,
                                                                BaseFragmentActivity.class));
                                                    }

                                                }

                                            }
                                        });

                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        } else {
            Toast.makeText(LoginActivity.this, "Empty fields not allowed",
                    Toast.LENGTH_LONG).show();
        }

    }
}