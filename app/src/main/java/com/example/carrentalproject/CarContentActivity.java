package com.example.carrentalproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class CarContentActivity extends AppCompatActivity {
    private TextView carContentTextView;
    private Button carContentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_content);

        // find toolbar in the layout
        Toolbar toolbar = findViewById(R.id.customer_toolbar);
        setSupportActionBar(toolbar);

        // get view of carContentTextView
        carContentTextView = (TextView) findViewById(R.id.carContent_textView);

        // implement button event listener
        carContentButton = (Button) findViewById(R.id.carContent_button);
        carContentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // send http request to server and pause http response
                String uri = "https://vpic.nhtsa.dot.gov/api/vehicles/getallmanufacturers?format=json"; // copy paste the rest link
                /*String uri = "https://timetable-lookup.p.rapidapi.com/codes/entertainment/";*/
                new AsyncHttpClient().get(uri, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        // parse http response
                        String strResponse = new String(responseBody);
                        carContentTextView.setText(strResponse);
                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        // display message error
                        carContentTextView.setText("Error. Check your service endpoint.");
                    }
                });
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
            Intent intent = new Intent(CarContentActivity.this, CustomerActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.action_map) {
            // go to google map
            Intent intent = new Intent(CarContentActivity.this, CarMapsActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.action_profile) {
            // go to ProfileActivity
            Intent intent = new Intent(CarContentActivity.this, ProfileActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.action_logout) {
            // go back to LoginActivity
            FirebaseAuth.getInstance().signOut(); // sign out the user
            Intent intent = new Intent(CarContentActivity.this, LoginActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}