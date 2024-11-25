package com.example.carrentalproject;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.example.carrentalproject.databinding.ActivityCarMapsBinding;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

public class CarMapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private ActivityCarMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCarMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // obtain the SupportMapFragment and get notified when the map is ready to be used
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // create LatLng objects for the two locations
        LatLng location1 = new LatLng(45.51460, -73.67559); // Vanier College - Main Campus
        LatLng location2 = new LatLng(45.52880, -73.61954); // Vanier College - Park-Extension Campus
        // create LatLngBounds that include both locations
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(location1);
        builder.include(location2);
        LatLngBounds bounds = builder.build();

        // set the camera to show the LatLngBounds with padding
        int padding = 100; // adjust the padding as needed
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);

        // move the camera to display both markers
        mMap.moveCamera(cameraUpdate);

        // add markers for both locations
        mMap.addMarker(new MarkerOptions().position(location1).title("Vanier College - Main Campus"));
        mMap.addMarker(new MarkerOptions().position(location2).title("Vanier College - Park-Extension Campus"));
    }
}