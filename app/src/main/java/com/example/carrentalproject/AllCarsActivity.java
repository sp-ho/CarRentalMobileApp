package com.example.carrentalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class AllCarsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Car> carList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_cars);

        // find the toolbar in the layout
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // get the ArrayList of car from the MainActivity
        carList = getIntent().getParcelableArrayListExtra("carList");

        recyclerView = findViewById(R.id.carRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // linear layout manager for vertical scrolling

        CarAdapter carAdapter = new CarAdapter(carList, new CarAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Car car) {
                String toastMessage = "Car Rental Fee/Day: C$" + car.getCarRentalFee();
                Toast.makeText(AllCarsActivity.this, toastMessage, Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(carAdapter);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_search) {
            // replace the current fragment with SearchCarFragment
            SearchCarFragment searchCarFragment = new SearchCarFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, searchCarFragment)
                    .addToBackStack(null) // adds the transaction to the back stack
                    .commit();
            return true;
        } else if (itemId == R.id.action_edit) {
            // replace the current fragment with CarFragment
            CarFragment carFragment = new CarFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, carFragment)
                    .addToBackStack(null) // adds the transaction to the back stack
                    .commit();
            return true;
        } else if (itemId == R.id.action_allCars) {
            // go to AllCarsActivity
            Intent intent = new Intent(AllCarsActivity.this, AllCarsActivity.class);
            intent.putParcelableArrayListExtra("carList", carList);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.action_reservation) {
            // go to ManageReserveActivity
            Intent intent = new Intent(AllCarsActivity.this, ManageReserveActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.action_logout) {
            // go back to LoginActivity
            FirebaseAuth.getInstance().signOut(); // sign out the user
            Intent intent = new Intent(AllCarsActivity.this, LoginActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}