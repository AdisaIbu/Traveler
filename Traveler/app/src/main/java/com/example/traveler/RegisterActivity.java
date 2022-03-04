package com.example.traveler;

import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class RegisterActivity extends AppCompatActivity {

    private Button registerButton;
    private Button loginButton;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText emailEditText;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser firebaseUser;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        Objects.requireNonNull(getSupportActionBar()).setElevation(0);

        registerButton = findViewById(R.id.registerButton);
        usernameEditText = findViewById(R.id.usernameEt);
        emailEditText = findViewById(R.id.registerEmailEt);
        loginButton = findViewById(R.id.loginButton);
        passwordEditText = findViewById(R.id.registerPasswordEt);


        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                firebaseUser = firebaseAuth.getCurrentUser();

                if (firebaseUser != null) {
                    // korisnikot e vekje logiran

                } else {

                }
            }
        };

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    String email = emailEditText.getText().toString().trim();
                    String password = passwordEditText.getText().toString().trim();
                    String username = usernameEditText.getText().toString().trim();

                    createUserEmailAccount(email, password, username);

            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
           }
       });

    }

    private void createUserEmailAccount(String email, String password, String username) {
        if (!TextUtils.isEmpty(email)
                && !TextUtils.isEmpty(password)
                && !TextUtils.isEmpty(username)) {


            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                               @Override
                                               public void onComplete(@NonNull Task<AuthResult> task) {


                                                   if (task.isSuccessful()) {


                                                       firebaseUser = firebaseAuth.getCurrentUser();
                                                       assert firebaseUser != null;
                                                       String currentUserId = firebaseUser.getUid();


                                                       Map<String, String> userObj = new HashMap<>();
                                                       userObj.put("userId", currentUserId);
                                                       userObj.put("username", username);

                                                       collectionReference.add(userObj).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                           @Override
                                                           public void onSuccess(DocumentReference documentReference) {

                                                               documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                   @Override
                                                                   public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                       // za sigurnost proverka
                                                                       if (task.getResult().exists()) {

                                                                           String name = task.getResult().getString("username");

                                                                           CityApi cityApi = CityApi.getInstance();
                                                                           cityApi.setUserId(currentUserId);
                                                                           cityApi.setUsername(name);


                                                                           Intent intent = new Intent(RegisterActivity.this, BaseFragmentActivity.class);

                                                                           startActivity(intent);

                                                                       }

                                                                   }
                                                               });

                                                           }
                                                       }).addOnFailureListener(
                                                               new OnFailureListener() {
                                                                   @Override
                                                                   public void onFailure(@NonNull Exception e) {

                                                                   }
                                                               }
                                                       );



                                                   } else {

                                                   }

                                               }
                                           }
                    ).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

        } else {

            {
                Toast.makeText(RegisterActivity.this, "Empty fields not allowed",
                        Toast.LENGTH_LONG).show();
            }

        }


    }


    @Override
    protected void onStart() {
        super.onStart();

        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}