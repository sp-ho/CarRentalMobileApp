package com.example.carrentalproject;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carrentalproject.database.CarBaseHelper;

import java.util.ArrayList;

public class AvailableCarsActivity extends AppCompatActivity {
    private CarBaseHelper dbHelper;
    private long daysDifference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_cars);

        // initialize the dbHelper
        dbHelper = new CarBaseHelper(this); // pass the context

        // retrieve the selected dates from the intent
        long startDateMillis = getIntent().getLongExtra("start_date", 0);
        long endDateMillis = getIntent().getLongExtra("end_date", 0);

        // calculate the number of days between the two dates
        long diffInMillis = endDateMillis - startDateMillis;
        daysDifference = diffInMillis / (24 * 60 * 60 * 1000); // convert milliseconds to days

        // read the list of cars in SQLite database
        ArrayList<Car> carList = dbHelper.readCars();

        // set up the RecyclerView and Adapter
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        AvailableCarAdapter adapter = new AvailableCarAdapter(carList, daysDifference, startDateMillis, endDateMillis);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}