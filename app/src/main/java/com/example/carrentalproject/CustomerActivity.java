package com.example.carrentalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.auth.FirebaseAuth;

public class CustomerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        // use FragmentManager to add CustomerFragment to the fragment_container of CustomerActivity
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null)
        {
            fragment = new CustomerFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }

        // find the toolbar in the layout
        Toolbar toolbar = findViewById(R.id.customer_toolbar);
        setSupportActionBar(toolbar);
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
            Intent intent = new Intent(CustomerActivity.this, CustomerActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.action_map) {
            // go to google map
            Intent intent = new Intent(CustomerActivity.this, CarMapsActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.action_profile) {
            // go to ProfileActivity
            Intent intent = new Intent(CustomerActivity.this, ProfileActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.action_logout) {
            // go back to LoginActivity
            FirebaseAuth.getInstance().signOut(); // sign out the user
            Intent intent = new Intent(CustomerActivity.this, LoginActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}