package com.example.carrentalproject;

import static com.example.carrentalproject.MainFragment.carList;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // use FragmentManager to add MainFragment to the fragment_container of MainActivity
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null)
        {
            fragment = new MainFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
        // find the toolbar in the layout
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
            Intent intent = new Intent(MainActivity.this, AllCarsActivity.class);
            intent.putParcelableArrayListExtra("carList", carList);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.action_reservation) {
            // go to ManageReserveActivity
            Intent intent = new Intent(MainActivity.this, ManageReserveActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.action_logout) {
            // go back to LoginActivity
            FirebaseAuth.getInstance().signOut(); // sign out the user
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}