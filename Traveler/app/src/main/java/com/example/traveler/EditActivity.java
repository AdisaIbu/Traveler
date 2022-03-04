package com.example.traveler;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import model.City;
import model.CityViewModel;

public class EditActivity extends AppCompatActivity {
    private ShapeableImageView profileImage;
    private ImageView editImage;
    private EditText editUsername;
    private Button updateProfile;
    private ProgressBar progressBar;

    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference collectionReference=db.collection("Users");
    private StorageReference storageReference;
    private CityViewModel cityViewModel;

    private Uri imageUri;
    private String currentUserId;
    private String currentUsername;
    private ImageButton backToProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        profileImage=findViewById(R.id.image_person_ep);
        editImage=findViewById(R.id.addImage_edit_bt);
        editUsername=findViewById(R.id.username_editProfile);
        updateProfile=findViewById(R.id.update_profile);
        progressBar=findViewById(R.id.progressBar_editProfile);
        backToProfile=findViewById(R.id.arrowEditBack);

        backToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditActivity.this, MyProfile.class));
            }
        });

        CityApi cityApi=CityApi.getInstance();
        if(cityApi!=null){
            currentUserId = cityApi.getUserId();
            currentUsername=cityApi.getUsername();
            editUsername.setText(currentUsername);
        }

        storageReference= FirebaseStorage.getInstance().getReference();
        cityViewModel=new ViewModelProvider(this).get(CityViewModel.class);

        cityViewModel.getSelectedCity().observe(this, new Observer<City>() {
            @Override
            public void onChanged(City city) {
                editUsername.setText(city.getName());

            }
        });


        ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();

                            if(data!=null){
                                imageUri=data.getData();
                                profileImage.setImageURI(imageUri);
                            }
                        }
                    }
                });

        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                someActivityResultLauncher.launch(intent);
            }
        });

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                updateProfileAccount();
            }
        });
    }

    private void updateProfileAccount(){
        String newUsername=editUsername.getText().toString().trim();
        if(!newUsername.isEmpty()&&
                imageUri!=null){

            final StorageReference filePath=storageReference
                    .child("profileImages")
                    .child("my_image"+currentUserId);

            filePath.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String newUsername=editUsername.getText().toString().trim();

                                    collectionReference.document(currentUserId)
                                            .update("username", newUsername)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    CityApi cityApi=CityApi.getInstance();
                                                    cityApi.setUsername(newUsername);

                                                    progressBar.setVisibility(View.INVISIBLE);
                                                    Intent intent = new Intent(EditActivity.this, MyProfile.class);
                                                    startActivity(intent);
                                                }
                                            })
                                            .addOnFailureListener(e -> Log.d("EditProfile", "onFailure: " +e.getMessage()));
                                }
                            });

                        }
                    })
                    .addOnFailureListener(e -> Log.d("EditProfile", "onFailure: " + e.getMessage()));
        }
    }
}