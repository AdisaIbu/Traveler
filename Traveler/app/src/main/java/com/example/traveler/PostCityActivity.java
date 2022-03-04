package com.example.traveler;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.reflect.TypeToken;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;


import java.lang.reflect.Type;
import java.util.ArrayList;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import adapters.ViewPagerPostImageAdapter;
import fragmets.CalendarFragment;
import model.City;
import utils.Utils;

public class PostCityActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String MY_PREFS_NAME = "ime";
    private Button saveCity;
    private EditText cityName;
    private EditText cityMemories;
    private ProgressBar progressBar;
    private TextView selectImages;


    private String currentUserId;
    private String currentUsername;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser firebaseUser;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Cities");

    private ViewPager2 viewPager2;
    private ArrayList<Uri> mArrayUri;
    private ViewPagerPostImageAdapter viewPagerPostImageAdapter;
    private final int PICK_IMAGE_MULTIPLE = 1;
    ActivityResultLauncher<Intent> someActivityResultLauncher;
    private StorageReference imageFolder;
    List<String> imagesUrls;

    private Timestamp timestamp;
//    private RadioButton selectedRadioButton;
//    private int selectedRadioId;
    private Satisfaction satisfaction;
    private Intent intent;
    private Date dateForFb;

    ArrayList<String> sliki;
    ArrayList<Uri> converted;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_city);

        Objects.requireNonNull(getSupportActionBar()).setElevation(0);



        firebaseAuth = FirebaseAuth.getInstance();
        //storageReference = FirebaseStorage.getInstance().getReference(); //eve go

        selectImages = findViewById(R.id.selectImagesTv);
        progressBar = findViewById(R.id.progressBarCityPost);
        saveCity = findViewById(R.id.saveCityButton);
        saveCity.setOnClickListener(this);
        cityName = findViewById(R.id.cityName);
        cityMemories = findViewById(R.id.memoriesEt);

        viewPager2 = findViewById(R.id.viewPagerCity);
        mArrayUri = new ArrayList<>();
        imagesUrls = new ArrayList<>();


        intent = getIntent();

        if (intent != null) {

            Long date = intent.getLongExtra("date", 0);
            dateForFb = new Date(date * 1000);

            satisfaction = (Satisfaction) getIntent().getSerializableExtra("satis");

        }



        if (CityApi.getInstance() != null) {

            currentUsername = CityApi.getInstance().getUsername();
            currentUserId = CityApi.getInstance().getUserId();
        }

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {

                } else {

                }
            }
        };

        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            Log.d("intentData", "onActivityResult: " + data);

                            if (data.getClipData() != null) {
                                int cout = data.getClipData().getItemCount();
                                for (int i = 0; i < cout; i++) {

                                    Uri imageuri = data.getClipData().getItemAt(i).getUri();
                                    mArrayUri.add(imageuri);
                                    Log.d("mArraySize", "onActivityResult: " + mArrayUri.size());
                                }


                            } else {
                                Uri imageuri = data.getData();
                                mArrayUri.add(imageuri);
                            }

                            viewPagerPostImageAdapter = new ViewPagerPostImageAdapter(PostCityActivity.this, mArrayUri);
                            viewPager2.setAdapter(viewPagerPostImageAdapter);
                            viewPagerPostImageAdapter.notifyDataSetChanged();

                        } else {

                        }

                        if(!mArrayUri.isEmpty()){
                            selectImages.setVisibility(View.GONE);
                            viewPager2.setVisibility(View.VISIBLE);
                        }

                    }
                });
    }


//        someActivityResultLauncher = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
//                new ActivityResultCallback<ActivityResult>() {
//                    @Override
//                    public void onActivityResult(ActivityResult result) {
//                        if (result.getResultCode() == Activity.RESULT_OK) {
//                            Intent data = result.getData();
//
//                            if (data != null) {
//
//                                imageUri = data.getData();
//                                addCityImage.setImageURI(imageUri);
//
//
//                            }
//                        }
//                    }
//                });

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            //   case R.id.calenderIcon:


            case R.id.saveCityButton:
                imageFolder = FirebaseStorage.getInstance().getReference().child("ImageFolder");


                saveCity();

                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.add_image_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.addImage:

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                someActivityResultLauncher.launch(intent);

                break;

            case R.id.addDateMenu:

                Utils.hideSoftKeyboard(getWindow().getDecorView());
                showBottomSheetDialog();

        }
        return super.onOptionsItemSelected(item);
    }

    private void showBottomSheetDialog() {

        FragmentManager fm = getSupportFragmentManager();
        CalendarFragment calendarFragment = CalendarFragment.newInstance();
        calendarFragment.show(fm, "fragment_calendar");


    }


    private void saveCity() {

        String nameCity = cityName.getText().toString().trim();
        String memories = cityMemories.getText().toString().trim();

        if (mArrayUri.isEmpty()) {
            mArrayUri = converted;

        }

        progressBar.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(nameCity) && !TextUtils.isEmpty(memories)
                && !mArrayUri.isEmpty() && satisfaction != null && dateForFb != null){


            City cityObj = new City();

            for (Uri uriItems : mArrayUri) {
                Uri individualImage = uriItems;

                StorageReference imageName = imageFolder.child("image" + individualImage.getLastPathSegment() + Timestamp.now().getSeconds());

                imageName.putFile(individualImage)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                imageName.getDownloadUrl()
                                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                Log.d("uriTag", "onSuccess: " + uri.toString());
                                                String imageUrl = uri.toString();
                                                imagesUrls.add(imageUrl); // ovde si ja polnime nie
                                                cityObj.setUsername(currentUsername);
                                                cityObj.setUserId(currentUserId);
                                                cityObj.setMemories(memories);
                                                cityObj.setName(nameCity);
                                                cityObj.setImageUrls(imagesUrls);
                                                cityObj.setTimestamp(dateForFb);
                                                cityObj.setSatisfaction(satisfaction);


                                                if (imagesUrls.size() == mArrayUri.size()) { // jas sum kralica
                                                    collectionReference.add(cityObj)
                                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                @Override
                                                                public void onSuccess(DocumentReference documentReference) {

                                                                    progressBar.setVisibility(View.INVISIBLE);

                                                                    String documentRefId =documentReference.getId();
                                                                    documentReference.update("documentId", documentRefId);

                                                                    startActivity(new Intent(PostCityActivity.this,
                                                                            BaseFragmentActivity.class));
                                                                    finish();
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {

                                                                }
                                                            });
                                                }


                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                e.printStackTrace();
                                                Log.d("onFailure", "onFailure: ");
                                            }
                                        });


                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                e.printStackTrace();

                            }
                        });
            }


        } else {
            Toast.makeText(this, "empty fields not allowed!", Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        selectImages.setVisibility(View.VISIBLE);
        viewPager2.setVisibility(View.GONE);

        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);


    }


    @Override
    protected void onStop() {
        super.onStop();

        if (firebaseAuth != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }

        SharedPreferences preferences = getSharedPreferences("ime", 0);
        preferences.edit().remove("name").apply();
        preferences.edit().remove("memories").apply();
        preferences.edit().remove("uris").apply();

    }


//    @Override
//    public void onSaveInstanceState(Bundle savedInstanceState) {
//        super.onSaveInstanceState(savedInstanceState);
//
//        savedInstanceState.putString(STATE_CITY_NAME, nameCity);
//        Log.d("IME", "onSaveInstanceState: " + nameCity);
//
//
//    }

//    public void onRestoreInstanceState(Bundle savedInstanceState) {
//
//        super.onRestoreInstanceState(savedInstanceState);
//
//
//        nameCity = savedInstanceState.getString(STATE_CITY_NAME);
//
//    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//
//        String cityName1= cityName.getText().toString().trim();
//
//                if(!TextUtils.isEmpty(cityName1)){
//                    postCityModel.setCity(cityName1);
//                    Log.d("city3", "onPause: " + cityName1);
//                }
//
//
//    }


    @Override
    protected void onPause() {
        super.onPause();


        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        Gson gson = new Gson();


        String jsonString;

        sliki = new ArrayList<>();
        for (Uri uriItems : mArrayUri) {
            String individualImage = uriItems.toString();
            sliki.add(individualImage);

            Log.d("domo", "onPause: " + sliki.size());
        }


        jsonString = gson.toJson(sliki);
        editor.putString("uris", jsonString);
        editor.putString("name", cityName.getText().toString().trim());
        editor.putString("memories", cityMemories.getText().toString().trim());
        editor.apply();


        // viewPager2.getAdapter(ViewPagerPostImageAdapter);


    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String name = prefs.getString("name", "");
        String memories = prefs.getString("memories", "");
        Log.d("ime", "onCreate: " + name);


        Gson gson = new Gson();
        String jsonSting = prefs.getString("uris", null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();

        sliki = gson.fromJson(jsonSting, type);
        Log.d("posly", "onResume: " + sliki);


        if (sliki == null) {
            sliki = new ArrayList<>();

        } else {
            converted = Utils.convertToUri(sliki);

            Log.d("converted", "onResume: " + converted);


            if(!converted.isEmpty()){
                selectImages.setVisibility(View.GONE);
                viewPager2.setVisibility(View.VISIBLE);
            }

              viewPagerPostImageAdapter = new ViewPagerPostImageAdapter(PostCityActivity.this, converted);
              viewPager2.setAdapter(viewPagerPostImageAdapter);
             viewPagerPostImageAdapter.notifyDataSetChanged();


        }


            cityName.setText(name);
            cityMemories.setText(memories);
        }


    }
