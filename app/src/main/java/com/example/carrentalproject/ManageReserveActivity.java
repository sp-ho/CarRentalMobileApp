package com.example.carrentalproject;

import static com.example.carrentalproject.MainFragment.carList;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ManageReserveActivity extends AppCompatActivity {
    private DatabaseReference reservationRef;
    private AutoCompleteTextView customerIdAutoCompleteTextView;
    private AutoCompleteTextView reservationIdAutoCompleteTextView;
    private AutoCompleteTextView carIdAutoCompleteTextView;
    private Button searchReservationButton;
    private TextView reservationListTextView;
    private List<String> customerIds = new ArrayList<>();
    private List<String> reservationIds = new ArrayList<>();
    private List<String> carIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_reserve);

        // find the toolbar in the layout
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // find widgets
        customerIdAutoCompleteTextView = findViewById(R.id.customerId_autoCompleteTextView);
        reservationIdAutoCompleteTextView = findViewById(R.id.reservationId_autoCompleteTextView);
        carIdAutoCompleteTextView = findViewById(R.id.carId_autoCompleteTextView);
        reservationListTextView = findViewById(R.id.reservationList_textView);

        // initialize Firebase database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reservationRef = database.getReference("reservations");

        // fetch customer IDs from Firebase and set up auto-complete for customer ID AutoCompleteTextView
        fetchCustomerIdsFromFirebase();

        // fetch reservation IDs from Firebase and set up auto-complete for reservation ID AutoCompleteTextView
        fetchReservationIdsFromFirebase();

        // fetch car IDs from Firebase and set up auto-complete for car ID AutoCompleteTextView
        fetchCarIdsFromFirebase();

        // get view of searchReservationButton
        searchReservationButton = findViewById(R.id.searchReservation_button);
        searchReservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // read reservations from Firebase
                readReservationsFromFirebase();
            }
        });
    } // end of onCreate

    private void fetchCustomerIdsFromFirebase() {
        reservationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                customerIds.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String customerId = snapshot.child("customerId").getValue(String.class);
                    if (customerId != null) {
                        customerIds.add(customerId);
                    }
                }
                setAutoCompleteForCustomerIds();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void fetchReservationIdsFromFirebase() {
        reservationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                reservationIds.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String reservationId = snapshot.child("reserveId").getValue(String.class);
                    if (reservationId != null) {
                        reservationIds.add(reservationId);
                    }
                }
                setAutoCompleteForReservationIds();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void fetchCarIdsFromFirebase() {
        reservationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                carIds.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String carId = snapshot.child("carId").getValue(String.class);
                    if (carId != null) {
                        carIds.add(carId);
                    }
                }
                setAutoCompleteForCarIds();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void setAutoCompleteForCustomerIds() {
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, customerIds);
        customerIdAutoCompleteTextView.setAdapter(adapter);
    }

    private void setAutoCompleteForReservationIds() {
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, reservationIds);
        reservationIdAutoCompleteTextView.setAdapter(adapter);
    }

    private void setAutoCompleteForCarIds() {
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, carIds);
        carIdAutoCompleteTextView.setAdapter(adapter);
    }

    // read the reservation records in Firebase real-time database
    private void readReservationsFromFirebase() {
        reservationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Reservation> reservations = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Reservation reservation = snapshot.getValue(Reservation.class);
                    if (reservation != null &&
                            (reservation.getCustomerId()
                                    .equals(customerIdAutoCompleteTextView.getText().toString()) ||
                            reservation.getReserveId()
                                    .equals(reservationIdAutoCompleteTextView.getText().toString()) ||
                            reservation.getCarId()
                                    .equals(carIdAutoCompleteTextView.getText().toString()))) {
                        reservations.add(reservation);
                    }
                }
                displayReservations(reservations);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("ProfileActivity", "loadReservations:onCancelled", databaseError.toException());
            }
        });
    }

    // display reservation list
    private void displayReservations(List<Reservation> reservations) {
        StringBuilder listBuilder = new StringBuilder();
        for (Reservation reservation : reservations) {
            listBuilder.append("Reservation ID: ").append(reservation.getReserveId()).append("\n");
            listBuilder.append("Customer ID: ").append(reservation.getCustomerId()).append("\n");
            listBuilder.append("Car ID: ").append(reservation.getCarId()).append("\n");
            listBuilder.append("Start Date: ").append(reservation.getStartDate()).append("\n");
            listBuilder.append("End Date: ").append(reservation.getEndDate()).append("\n");
            listBuilder.append("Pick-up Location: ").append(reservation.getPickupLocation()).append("\n");
            listBuilder.append("Total Fee: $").append(reservation.getTotalFee()).append("\n\n");
        }
        reservationListTextView.setText(listBuilder.toString());
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
                    .addToBackStack(null)
                    .commit();
            return true;
        } else if (itemId == R.id.action_edit) {
            // replace the current fragment with CarFragment
            CarFragment carFragment = new CarFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, carFragment)
                    .addToBackStack(null)
                    .commit();
            return true;
        } else if (itemId == R.id.action_allCars) {
            // go to AllCarsActivity
            Intent intent = new Intent(ManageReserveActivity.this, AllCarsActivity.class);
            intent.putParcelableArrayListExtra("carList", carList);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.action_reservation) {
            // go to ManageReserveActivity
            Intent intent = new Intent(ManageReserveActivity.this, ManageReserveActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.action_logout) {
            // go back to LoginActivity
            FirebaseAuth.getInstance().signOut(); // sign out the user
            Intent intent = new Intent(ManageReserveActivity.this, LoginActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}