package fragmets;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.traveler.CityApi;
import com.example.traveler.R;
import com.example.traveler.Satisfaction;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import listeners.OnCityClickedListener;
import model.City;
import model.CityViewModel;


public class CityListFragment extends Fragment implements OnCityClickedListener, View.OnClickListener {
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference;
    private List<City> cityList;
    private RecyclerView recyclerView;
    private CityRecyclerAdapter adapter;

    private CollectionReference collectionReference = db.collection("Cities");
    private TextView noEntryTv;

    private CityViewModel cityViewModel;
    private SearchView searchView;
    private ImageButton high;
    private ImageButton medium;
    private ImageButton low;
    private Button all;


    public CityListFragment() {
        // Required empty public constructor
    }


    public static CityListFragment newInstance() {
        CityListFragment fragment = new CityListFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_city_list, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();

        noEntryTv = view.findViewById(R.id.noThoughtsTv);
        cityList = new ArrayList<>();

        recyclerView = view.findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        searchView = view.findViewById(R.id.search_bt);
        high = view.findViewById(R.id.highBt);
        high.setOnClickListener(this::onClick);
        medium= view.findViewById(R.id.mediumBt);
        medium.setOnClickListener(this);
        low= view.findViewById(R.id.lowBt);
        low.setOnClickListener(this);
        all = view.findViewById(R.id.allBt);
        all.setOnClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        searchView.setQueryHint("Search title");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                List<City> filteredList = new ArrayList<>();
                for (City city : cityList) {
                    if (city.getName().toLowerCase().contains(s.toLowerCase())) {
                        filteredList.add(city);
                    }
                }

                CityRecyclerAdapter adapter = new CityRecyclerAdapter(getContext(), filteredList, CityListFragment.this::onCityClicked);
                recyclerView.setAdapter(adapter);
                return false;
            }
        }); //finito


        cityViewModel = new ViewModelProvider(requireActivity())
                .get(CityViewModel.class);

        collectionReference.whereEqualTo("userId", CityApi.getInstance().getUserId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {

                            for (QueryDocumentSnapshot items : queryDocumentSnapshots) {
                                City item = items.toObject(City.class);
                                cityList.add(item);


                            }


                            adapter = new CityRecyclerAdapter(requireContext(), cityList, CityListFragment.this::onCityClicked);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();


                        } else {
                            noEntryTv.setVisibility(View.VISIBLE);
                        }


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


    }

    @Override
    public void onCityClicked(City city) {
        Log.d("park", "onCityClicked: " + city.getName());

        cityViewModel.setSelectedCity(city);


        String cityPostId = city.getDocumentId();

        CityApi cityApi = CityApi.getInstance();
        cityApi.setDocumentReference(cityPostId);

        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.city_fragment, CityDetailsFragment.newInstance());
        ft.commit();
    }


    private void filterList(String satisfaction) {
        List<City> filteredList2 = new ArrayList<>();
        for (City city : cityList) {
            if (city.getSatisfaction().toString().toLowerCase().contains(satisfaction.toLowerCase())) {
                filteredList2.add(city);
                Log.d("citiesk", "filterList: " + cityList);
            }else if (satisfaction.equals("ALL")){
                filteredList2 = cityList;
                adapter = new CityRecyclerAdapter(getContext(), filteredList2, CityListFragment.this::onCityClicked);
            }
        }
        adapter = new CityRecyclerAdapter(getContext(), filteredList2, CityListFragment.this::onCityClicked);
        recyclerView.setAdapter(adapter);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {

        int id = view.getId();
        String satis;

        switch (id) {

            case R.id.allBt:
                satis = "ALL";
                break;

            case R.id.mediumBt:

                satis = "MEDIUM";
                break;

            case R.id.lowBt:
                satis = "LOW";
                break;

            default:
                satis = "HIGH";
        }
        filterList(satis);
    }
}