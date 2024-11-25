package com.example.carrentalproject;

import static com.example.carrentalproject.LoginActivity.username;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

public class ProfileActivity extends AppCompatActivity {
    private TextView greetCustomerTextView;
    private TextView reserveHistoryTextView;
    private DatabaseReference reservationRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // find the toolbar in the layout
        Toolbar toolbar = findViewById(R.id.customer_toolbar);
        setSupportActionBar(toolbar);

        // find TextView fields
        greetCustomerTextView = (TextView) findViewById(R.id.greetCustomer_textView);
        greetCustomerTextView.setText("Hello, " + username.getText().toString() + "!");
        reserveHistoryTextView = (TextView) findViewById(R.id.reservationHistory_textView);

        // get a reference to Firebase real-time database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reservationRef = database.getReference("reservations");

        // read reservations from Firebase
        readReservationsFromFirebase();
    }

    private void readReservationsFromFirebase() {
        reservationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Reservation> reservations = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Reservation reservation = snapshot.getValue(Reservation.class);
                    if (reservation != null &&
                            reservation.getCustomerId().equals(username.getText().toString())) {
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

    // display reservation history of customer
    private void displayReservations(List<Reservation> reservations) {
        StringBuilder historyBuilder = new StringBuilder();
        for (Reservation reservation : reservations) {
            historyBuilder.append("Reservation ID: ").append(reservation.getReserveId()).append("\n");
            historyBuilder.append("Car ID: ").append(reservation.getCarId()).append("\n");
            historyBuilder.append("Start Date: ").append(reservation.getStartDate()).append("\n");
            historyBuilder.append("End Date: ").append(reservation.getEndDate()).append("\n");
            historyBuilder.append("Pick-up Location: ").append(reservation.getPickupLocation()).append("\n");
            historyBuilder.append("Total Fee: $").append(reservation.getTotalFee()).append("\n\n");
        }
        reserveHistoryTextView.setText(historyBuilder.toString());
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_customer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_search) {
            // go to CustomerFragment (CustomerActivity)
            Intent intent = new Intent(ProfileActivity.this, CustomerActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.action_map) {
            // go to google map
            Intent intent = new Intent(ProfileActivity.this, CarMapsActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.action_profile) {
            // go to ProfileActivity
            Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.action_logout) {
            // go back to LoginActivity
            FirebaseAuth.getInstance().signOut(); // sign out the user
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}