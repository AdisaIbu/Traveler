package fragmets;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.traveler.R;


public class EditMyProfile extends Fragment {



    public EditMyProfile() {
        // Required empty public constructor
    }


    public static EditMyProfile newInstance() {
        EditMyProfile fragment = new EditMyProfile();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_edit, container, false);
    }
}