package com.example.carrentalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

public class ReserveActivity extends AppCompatActivity {

    public static TextView addressTextView;
    private DatePicker startDateDatePicker;
    private DatePicker endDateDatePicker;
    private Button searchCarButton;
    public static long daysDifference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve);

        // find toolbar in layout
        Toolbar toolbar = findViewById(R.id.customer_toolbar);
        setSupportActionBar(toolbar);

        // retrieve the selected address from the intent
        String selectedAddress = getIntent().getStringExtra("selected_address");

        addressTextView = (TextView) findViewById(R.id.selectedAddress_textView);
        addressTextView.setText(selectedAddress);

        startDateDatePicker = findViewById(R.id.startDate_datePicker);
        endDateDatePicker = findViewById(R.id.endDate_datePicker);

        // get the current date using Calendar
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // set the current date in both date pickers
        startDateDatePicker.init(year, month, dayOfMonth, null);
        endDateDatePicker.init(year, month, dayOfMonth, null);

        // get the selected start date
        int startYear = startDateDatePicker.getYear();
        int startMonth = startDateDatePicker.getMonth();
        int startDayOfMonth = startDateDatePicker.getDayOfMonth();

        // get the selected end date
        int endYear = endDateDatePicker.getYear();
        int endMonth = endDateDatePicker.getMonth();
        int endDayOfMonth = endDateDatePicker.getDayOfMonth();

        // create Calendar instances for the selected start and end dates
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(startYear, startMonth, startDayOfMonth);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(endYear, endMonth, endDayOfMonth);

        // calculate the number of days between the two dates
        long diffInMillis = endCalendar.getTimeInMillis() - startCalendar.getTimeInMillis();
        daysDifference = diffInMillis / (24 * 60 * 60 * 1000); // Convert milliseconds to days

        // get the view of searchCarButton
        searchCarButton = (Button) findViewById(R.id.reserveSearch_button);
        searchCarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get the selected start and end dates from the DatePickers
                int startYear = startDateDatePicker.getYear();
                int startMonth = startDateDatePicker.getMonth();
                int startDayOfMonth = startDateDatePicker.getDayOfMonth();

                int endYear = endDateDatePicker.getYear();
                int endMonth = endDateDatePicker.getMonth();
                int endDayOfMonth = endDateDatePicker.getDayOfMonth();

                // create Calendar instances for the selected start and end dates
                Calendar startCalendar = Calendar.getInstance();
                startCalendar.set(startYear, startMonth, startDayOfMonth);

                Calendar endCalendar = Calendar.getInstance();
                endCalendar.set(endYear, endMonth, endDayOfMonth);

                // convert Calendar dates to milliseconds
                long startDateMillis = startCalendar.getTimeInMillis();
                long endDateMillis = endCalendar.getTimeInMillis();

                // get the selected address
                String selectedAddress = addressTextView.getText().toString();

                // create an Intent to launch AvailableCarsActivity
                Intent intent = new Intent(ReserveActivity.this, AvailableCarsActivity.class);

                // put the date and address data into the Intent as extras
                intent.putExtra("start_date", startDateMillis);
                intent.putExtra("end_date", endDateMillis);
                intent.putExtra("selected_address", selectedAddress);

                // start AvailableCarsActivity with the Intent
                startActivity(intent);
            }
        });
    } // end of onCreate

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_customer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_search) {
            // go to CustomerFragment (CustomerActivity)
            Intent intent = new Intent(ReserveActivity.this, CustomerActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.action_map) {
            // go to google map
            Intent intent = new Intent(ReserveActivity.this, CarMapsActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.action_profile) {
            // go to ProfileActivity
            Intent intent = new Intent(ReserveActivity.this, ProfileActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.action_logout) {
            // go back to LoginActivity
            FirebaseAuth.getInstance().signOut(); // sign out the user
            Intent intent = new Intent(ReserveActivity.this, LoginActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}