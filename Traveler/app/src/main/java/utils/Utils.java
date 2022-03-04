package utils;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Utils {

    public static String formatDate(Date date) {
        SimpleDateFormat simpleDateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
        simpleDateFormat.applyPattern("EEE, MMM d");

        return  simpleDateFormat.format(date);

    }
    public static void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }

    public static ArrayList<Uri> convertToUri(ArrayList<String> list){

        ArrayList<Uri> uriImages = new ArrayList<>();

        for (String uriItems : list) {
            Uri individualImage = Uri.parse(uriItems);
            uriImages.add(individualImage);
        }

        return uriImages;
    }
}
