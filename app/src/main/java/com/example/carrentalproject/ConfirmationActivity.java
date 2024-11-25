package com.example.carrentalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ConfirmationActivity extends AppCompatActivity {

    private TextView reserveCarDetailsTextView;
    private TextView reserveIdTextView;
    private TextView dateRangeTextView;
    private TextView pickupLocationTextView;
    private TextView totalFeeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        // find toolbar in layout
        Toolbar toolbar = findViewById(R.id.customer_toolbar);
        setSupportActionBar(toolbar);

        // retrieve data from Intent extras from AvailableCarAdapter
        Intent intent = getIntent();
        String reservationId = intent.getStringExtra("reservation_id");
        String carId = intent.getStringExtra("car_id");
        String carMakeModel = intent.getStringExtra("car_make_model");
        long startDateMillis = intent.getLongExtra("start_date", 0);
        long endDateMillis = intent.getLongExtra("end_date", 0);
        double totalFee = intent.getDoubleExtra("total_fee", 0.0);
        String pickupLocation = intent.getStringExtra("pickup_location");

        // format the date strings using SimpleDateFormat
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        String startDateStr = dateFormat.format(new Date(startDateMillis));
        String endDateStr = dateFormat.format(new Date(endDateMillis));

        // find the widgets and set texts
        reserveIdTextView = (TextView) findViewById(R.id.reservationId_textView);
        reserveIdTextView.setText("Reservation ID: " + reservationId);
        reserveCarDetailsTextView = (TextView) findViewById(R.id.reserveCarDetails_textView);
        reserveCarDetailsTextView.setText(carId + " " + carMakeModel);
        dateRangeTextView = (TextView) findViewById(R.id.dateRange_textView);
        dateRangeTextView.setText("Date: " + startDateStr + " - " + endDateStr);
        pickupLocationTextView = (TextView) findViewById(R.id.pickupLocation_textView);
        pickupLocationTextView.setText("Pick-up Location: " + pickupLocation + "\n");
        totalFeeTextView = (TextView) findViewById(R.id.totalFee_textView);
        totalFeeTextView.setText("Total: $" + totalFee);
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
            Intent intent = new Intent(ConfirmationActivity.this, CustomerActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.action_map) {
            // go to google maps
            Intent intent = new Intent(ConfirmationActivity.this, CarMapsActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.action_profile) {
            // go to ProfileActivity
            Intent intent = new Intent(ConfirmationActivity.this, ProfileActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.action_logout) {
            // go back to LoginActivity
            FirebaseAuth.getInstance().signOut(); // sign out the user
            Intent intent = new Intent(ConfirmationActivity.this, LoginActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}