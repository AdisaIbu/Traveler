package fragmets;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.traveler.BaseFragmentActivity;
import com.example.traveler.CityApi;
import com.example.traveler.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import adapters.ViewPagerPostImageAdapter;
import model.City;
import model.CityViewModel;
import utils.Utils;


public class CityDetailsFragment extends Fragment {
    private CityViewModel cityViewModel;
    private ImageButton delete;

    private String currentDocRef;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Cities");

    private TextView cityName;
    private TextView cityMemories;
    private TextView date;
    private ViewPager2 viewPager2;
    private List<Uri> images = new ArrayList<Uri>();
    private TextView noCity;
    private TextView deleteCityTv;


    public CityDetailsFragment() {
        // Required empty public constructor
    }


    public static CityDetailsFragment newInstance() {
        CityDetailsFragment fragment = new CityDetailsFragment();

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

        View view = inflater.inflate(R.layout.fragment_city_details, container, false);

        delete = view.findViewById(R.id.deleteBt);
        cityName = view.findViewById(R.id.detailsCityName);
        viewPager2 = view.findViewById(R.id.DetailsVp);
        cityMemories = view.findViewById(R.id.detailsMemories);
        date = view.findViewById(R.id.detailsDate);
        noCity = view.findViewById(R.id.noCitySelected);
        deleteCityTv = view.findViewById(R.id.deleteCityTv);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (CityApi.getInstance() != null) {
                    currentDocRef = CityApi.getInstance().getDocumentReference();
                }

                collectionReference.document(currentDocRef)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(requireContext(), "The post has been deleted", Toast.LENGTH_SHORT)
                                        .show();
                                Intent intent = new Intent(getActivity(), BaseFragmentActivity.class);
                                startActivity(intent);
                            }
                        });

            }
        });

        cityViewModel = new ViewModelProvider(requireActivity())
                .get(CityViewModel.class);



        cityViewModel.getSelectedCity().observe(getViewLifecycleOwner(), new Observer<City>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChanged(City city) {
                noCity.setVisibility(View.INVISIBLE);
           delete.setVisibility(View.VISIBLE);
           deleteCityTv.setVisibility(View.VISIBLE);

                ColorStateList colorStateList = new ColorStateList(
                                    new int[][]{
                                           new int[]{android.R.attr.state_enabled}//enabled
                                    },
                                    new int[] {getResources().getColor(R.color.buttonColor) }
                            );

                String text = "<font color=#ccff00> <b> City name: </b> </font> <font color=#FF000000> " + city.getName().trim()+"</font>";
                cityName.setText(Html.fromHtml(text));
                String text2 = "<font color=#ccff00> <b> Memories: </b> </font> <font color=#FF000000> " + city.getMemories().trim()+"</font>";
                cityMemories.setText(Html.fromHtml(text2));
                date.setText(Utils.formatDate(city.getTimestamp()));

                List<String> stringsUrls = city.getImageUrls();

                for (String urlItems : stringsUrls) {
                    String individualImage = urlItems;

                    Uri uri =  Uri.parse(String.valueOf(individualImage));
                    images.add(uri);
                    Log.d("uris", "onChanged: " + images);

                }


                ViewPagerPostImageAdapter viewPagerPostImageAdapter = new
                        ViewPagerPostImageAdapter(requireActivity(), images);
                viewPager2.setAdapter(viewPagerPostImageAdapter);
                viewPagerPostImageAdapter.notifyDataSetChanged();

                Log.d("details", "onChanged: " + city.getName());

            }
        });
    }
}