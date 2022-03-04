package model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class CityViewModel extends ViewModel {

    private final MutableLiveData<City> selectedCity=new MutableLiveData<>();
    private final MutableLiveData<List<City>> selectedCities=new MutableLiveData<>();


    public void setSelectedCity(City city) {

        selectedCity.setValue(city);

    }

    public LiveData<City> getSelectedCity() {
        return selectedCity;
    }

    public void setSelectedCities(List<City> cities) {

        selectedCities.setValue(cities);
    }

    public LiveData<List<City>> getSelectedCities(){
        return selectedCities;
    }



}
