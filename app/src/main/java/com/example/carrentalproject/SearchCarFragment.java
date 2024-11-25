package com.example.carrentalproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.carrentalproject.database.CarBaseHelper;

import java.util.ArrayList;

public class SearchCarFragment extends Fragment {
    private EditText carIdEditText;
    private EditText carModelEditText;
    private EditText carMakeEditText;
    private Button searchCarByIdButton;
    private Button searchCarByMakeButton;
    private Button searchCarByModelButton;
    private TextView searchDataTextView;
    private CarBaseHelper carBaseHelper;
    private CarSearchViewModel carSearchViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        carBaseHelper = new CarBaseHelper(requireContext());
        carSearchViewModel = new ViewModelProvider(this).get(CarSearchViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search_car, container, false);

        // initialize views
        carIdEditText = v.findViewById(R.id.searchCarId_editText);
        carModelEditText = v.findViewById(R.id.searchCarModel_editText);
        carMakeEditText = v.findViewById(R.id.searchCarMake_editText);
        searchDataTextView = v.findViewById(R.id.searchData_textView);

        // get the view of searchCarByIdButton
        searchCarByIdButton = v.findViewById(R.id.searchCarById_button);
        searchCarByIdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String carId = carIdEditText.getText().toString();
                Car car = carBaseHelper.searchCarById(carId);
                if (car != null) {
                    carSearchViewModel.setSearchData(car.toString());
                } else {
                    carSearchViewModel.setSearchData("Car not found.");
                }
            }
        });

        // get the view of searchCarByMakeButton
        searchCarByMakeButton = v.findViewById(R.id.searchCarByMake_button);
        searchCarByMakeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String carMake = carMakeEditText.getText().toString();
                ArrayList<Car> cars = carBaseHelper.searchCarsByMake(carMake);
                if (!cars.isEmpty()) {
                    StringBuilder carDataText = new StringBuilder();
                    for (Car car : cars) {
                        carDataText.append(car.toString()).append("\n");
                    }

                    String searchData = carDataText.toString();

                    // set the search result data in the ViewModel
                    carSearchViewModel.setSearchData(searchData);

                    searchDataTextView.setText(searchData);
                } else {
                    searchDataTextView.setText("No cars found for the specified make.");
                }
            }
        });

        // get the view of searchCarByModelButton
        searchCarByModelButton = v.findViewById(R.id.searchCarByModel_button);
        searchCarByModelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String carModel = carModelEditText.getText().toString();
                ArrayList<Car> cars = carBaseHelper.searchCarsByModel(carModel);
                if (!cars.isEmpty()) {
                    StringBuilder carDataText = new StringBuilder();
                    for (Car car : cars) {
                        carDataText.append(car.toString()).append("\n");
                    }
                    String searchData = carDataText.toString();

                    // set the search result data in the ViewModel
                    carSearchViewModel.setSearchData(searchData);

                    searchDataTextView.setText(searchData);
                } else {
                    searchDataTextView.setText("No cars found for the specified model.");
                }
            }
        });

        // set up ViewModel to observe search data changes
        carSearchViewModel.getSearchData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String searchData) {
                searchDataTextView.setText(searchData);
            }
        });
        return v;
    }
}
