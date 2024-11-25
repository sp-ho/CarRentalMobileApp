package com.example.carrentalproject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CarSearchViewModel extends ViewModel {
    private MutableLiveData<String> searchData = new MutableLiveData<>();

    // getter method for search data LiveData
    public LiveData<String> getSearchData() {
        return searchData;
    }

    // setter method to update search data
    public void setSearchData(String data) {
        searchData.setValue(data);
    }
}
