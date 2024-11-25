package com.example.carrentalproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.List;

public class CustomerFragment extends Fragment {

    private AutoCompleteTextView addressAutoCompleteTextView;
    private ImageView addressSuggestionsIcon;
    private Button roadTripsButton;
    private Button startCountryButton;
    private Button stopCountryButton;
    private Button webServiceButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_customer, container, false);

        // find AutoCompleteTextView in layout
        addressAutoCompleteTextView = v.findViewById(R.id.address_auto_complete);

        // create the options for the AutoCompleteTextView
        List<String> possibleAddresses = new ArrayList<>();
        possibleAddresses.add("821 Sainte Croix Ave, Saint-Laurent, Quebec H4L 3X9");
        possibleAddresses.add("550 Beaumont Ave 2nd floor, Montreal, Quebec H3N 1T7");

        // create an ArrayAdapter for the AutoCompleteTextView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, possibleAddresses);
        addressAutoCompleteTextView.setAdapter(adapter);

        // find address suggestion icon in layout
        addressSuggestionsIcon = v.findViewById(R.id.ic_address_suggestions);
        addressSuggestionsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get the address entered by the user
                String selectedAddress = addressAutoCompleteTextView.getText().toString();

                // create an Intent to start the ReserveActivity
                Intent intent = new Intent(requireContext(), ReserveActivity.class);

                // put the selected address as an extra in the intent
                intent.putExtra("selected_address", selectedAddress);

                // start the ReserveActivity
                startActivity(intent);
            }
        });

        // get the link of road trips
        roadTripsButton = (Button) v.findViewById(R.id.roadTrips_button);
        roadTripsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://lazytrips.com/blog/best-road-trips-from-montreal"));
                startActivity(intent);
            }
        });

        // start service for playing song
        startCountryButton = (Button) v.findViewById(R.id.startCountry_button);
        startCountryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start media player android service
                getActivity().startService(new Intent(getActivity(), CarService.class));
            }
        });

        // stop service for playing song
        stopCountryButton = (Button) v.findViewById(R.id.stopCountry_button);
        stopCountryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start media player android service
                getActivity().stopService(new Intent(getActivity(), CarService.class));
            }
        });

        // start REST API of car manufacturers
        webServiceButton = (Button) v.findViewById(R.id.webService_button);
        webServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Start web service", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), CarContentActivity.class);
                startActivity(intent);
            }
        });
        return v;
    }
}
