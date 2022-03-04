package fragmets;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CalendarView;
import android.widget.ImageButton;

import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.traveler.PostCityActivity;
import com.example.traveler.R;
import com.example.traveler.Satisfaction;
import com.google.firebase.Timestamp;


import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import utils.Utils;


public class CalendarFragment extends DialogFragment {

   private ImageButton satisfactionButton;
   private ImageButton calendarButton;
    private  RadioGroup radioSatisfactionGroup;
   private CalendarView calendarView;
   private ImageButton sendDataToActivity;
   private TextView dateTv;
   private Calendar calendar = Calendar.getInstance();
   private Date dateAdded;

    private RadioButton high;
    private RadioButton medium;
    private RadioButton low;

    private RadioButton selectedRadioButton;
    private int selectedRadioId;
    private Satisfaction satisfaction;
    private Long dateT;




    public CalendarFragment() {
        // Required empty public constructor
    }

    @NonNull
    public static CalendarFragment newInstance() {
        CalendarFragment fragment = new CalendarFragment();
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
        View view = inflater.inflate(R.layout.fragment_bottom_sheet, container, false);

        calendarView = view.findViewById(R.id.calendarView);
        calendarButton = view.findViewById(R.id.calendarButtonDialog);

        satisfactionButton = view.findViewById(R.id.satisfactionButton);
        radioSatisfactionGroup = view.findViewById(R.id.radioGroupSatisfaction);
        dateTv = view.findViewById(R.id.showDateTv);
        sendDataToActivity = view.findViewById(R.id.saveToPostBt);


        high = view.findViewById(R.id.radioButtonHigh);
        medium= view.findViewById(R.id.radioButtonMed);
        low = view.findViewById(R.id.radioButtonLow);




        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                calendarView.setVisibility(calendarView.getVisibility() == View.GONE ?
                        View.VISIBLE : View.GONE);

                calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMoth) {

                        calendar.clear();
                        calendar.set(year, month, dayOfMoth);
                        dateAdded = calendar.getTime();
                        dateTv.setVisibility(View.VISIBLE);
                        dateTv.setText(Utils.formatDate(dateAdded));

                       // dateT = dateAdded.getTime();
                        Log.d("datum", "onSelectedDayChange: " + dateAdded);

                        Timestamp ts = new Timestamp(dateAdded);
                        Log.d("tss", "onSelectedDayChange: " + ts);
                        dateT = ts.getSeconds();



                    }
                });


            }
        });

        sendDataToActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.putExtra("date", dateT);
                intent.putExtra("satis", satisfaction);

                intent.setClass(getActivity(), PostCityActivity.class);
                getActivity().startActivity(intent);


            }
        });


        satisfactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioSatisfactionGroup.setVisibility(radioSatisfactionGroup.getVisibility() == View.GONE ?
                        View.VISIBLE : View.GONE);

                radioSatisfactionGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                   @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                        if (radioSatisfactionGroup.getVisibility() == View.VISIBLE) {
                            selectedRadioId = checkedId;

                           //selectedRadioButton = view.findViewById(selectedRadioId);
//
//                           ColorStateList colorStateList = new ColorStateList(
//                                    new int[][]{
//                                           new int[]{android.R.attr.state_enabled}//enabled
//                                    },
//                                    new int[] {getResources().getColor(R.color.buttonColor) }
//                            );
//
//
//
                           //    selectedRadioButton.setButtonTintList(colorStateList);

                            Log.d("selekcija", "onCheckedChanged: " + selectedRadioId);
                            if(selectedRadioId == high.getId()){
                                satisfaction = Satisfaction.HIGH;
                            } else if (selectedRadioId == medium.getId()) {
                            satisfaction = Satisfaction.MEDIUM;
                        }else if (selectedRadioId == low.getId()) {
                            satisfaction = Satisfaction.LOW;
                       }else {
                            satisfaction = Satisfaction.LOW;
                        }
                    }else{
                       satisfaction = Satisfaction.LOW;
                   }

                       }

               });


          }
        });


        Objects.requireNonNull(getDialog()).getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getDialog().getWindow().setGravity(Gravity.BOTTOM);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
     getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }
}