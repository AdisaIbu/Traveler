package fragmets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;

import com.example.traveler.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;
import java.util.Date;


//public class BottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener {
//      Calendar calendar = Calendar.getInstance();
//
//    private ImageButton calendarButton;
//    private ImageButton priorityButton;
//    private RadioGroup priorityRadioGroup;
//    private RadioButton selectedRadioButton;
//    private int selectedButtonId;
//    private CalendarView calendarView;
//    private Date dateAdded;
//
////    private boolean isEdit;
////    private Priority priority;
//
//    public BottomSheetFragment() {
//    }
//
//    @Override
//    public View onCreateView(
//            LayoutInflater inflater, ViewGroup container,
//            Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_bottom_sheet, container, false);
//
//          calendarView = view.findViewById(R.id.calendarView);
//          calendarButton = view.findViewById(R.id.calendarButtonFagment);
//
//          priorityButton = view.findViewById(R.id.satisfactionButton);
//          priorityRadioGroup = view.findViewById(R.id.radioGroupSatisfaction);
//
//
//        return view;
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//
////        if (sharedViewModel.getSelectedItem().getValue() != null) {
////            isEdit = sharedViewModel.getIsEdit();
////            Task task = sharedViewModel.getSelectedItem().getValue();
////            enterTodo.setText(task.getTask());
////            Log.d("MY", "onViewCreated: " + isEdit + " " + task.getTask());
////        }
//    }
//
//    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
////        sharedViewModel = new ViewModelProvider(requireActivity())
////                .get(SharedViewModel.class);
////
//        calendarButton.setOnClickListener(view12 -> {
//           calendarView.setVisibility(calendarView.getVisibility() == View.GONE ?
//                 View.VISIBLE : View.GONE);
////            Utils.hideSoftKeyboard(view12);
////
// });
//      calendarView.setOnDateChangeListener((calendarView, year, month, dayOfMoth) -> {
//           calendar.clear();
//          calendar.set(year, month, dayOfMoth);
//           dateAdded = calendar.getTime();
//       });
//
//        priorityButton.setOnClickListener(view13 -> {
////            Utils.hideSoftKeyboard(view13);
//           priorityRadioGroup.setVisibility(
//                    priorityRadioGroup.getVisibility() == View.GONE ? View.VISIBLE : View.GONE
//            );
////            priorityRadioGroup.setOnCheckedChangeListener((radioGroup, checkedId) -> {
////                if (priorityRadioGroup.getVisibility() == View.VISIBLE) {
////                     selectedButtonId = checkedId;
////                   selectedRadioButton = view.findViewById(selectedButtonId);
////                   if (selectedRadioButton.getId() == R.id.radioButton_high) {
////                        priority = Priority.HIGH;
////                    } else if (selectedRadioButton.getId() == R.id.radioButton_med) {
////                        priority = Priority.MEDIUM;
////                    }else if (selectedRadioButton.getId() == R.id.radioButton_low) {
////                        priority = Priority.LOW;
////                    }else {
////                        priority = Priority.LOW;
////                    }
////                }else{
////                    priority = Priority.LOW;
////                }
////            });
//     });
////
////        saveButton.setOnClickListener(view1 -> {
////            String task = enterTodo.getText().toString().trim();
////
////            if (!TextUtils.isEmpty(task) && dueDate != null && priority != null) {
////                Task myTask = new Task(task, priority,
////                        dueDate, Calendar.getInstance().getTime(),
////                        false);
////                if (isEdit) {
////                    Task updateTask = sharedViewModel.getSelectedItem().getValue();
////
////                    updateTask.setTask(task);
////                    updateTask.setDateCreated(Calendar.getInstance().getTime());
////                    updateTask.setPriority(priority);
////                    updateTask.setDueDate(dueDate);
////                    TaskViewModel.update(updateTask);
////                    sharedViewModel.setIsEdit(false);
////
////
////                } else {
////                    TaskViewModel.insert(myTask);
////                }
////                enterTodo.setText("");
////                if (this.isVisible()) {
////                    this.dismiss();
////                }
////
////            }else {
////                Snackbar.make(saveButton, R.string.empty_field, Snackbar.LENGTH_LONG)
////                        .show();
////            }
////        });
//
//
//    }
//
//    @Override
//    public void onClick(View view) {
//
////        int id = view.getId();
////        if (id == R.id.today_chip) {
////            //set data for today
////            calendar.add(Calendar.DAY_OF_YEAR, 0);
////            dueDate = calendar.getTime();
////            Log.d("TIME", "onClick: " + dueDate.toString());
////
////
////        } else if (id == R.id.tomorrow_chip) {
////
////            calendar.add(Calendar.DAY_OF_YEAR, 1);
////            dueDate = calendar.getTime();
////            Log.d("TIME", "onClick: " + dueDate.toString());
////
////        } else if (id == R.id.next_week_chip) {
////
////            calendar.add(Calendar.DAY_OF_YEAR, 7);
////            dueDate = calendar.getTime();
////            Log.d("TIME", "onClick: " + dueDate.toString());
////
////        }
////
// }
//}