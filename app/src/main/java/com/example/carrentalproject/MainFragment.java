package com.example.carrentalproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.carrentalproject.database.CarBaseHelper;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainFragment extends Fragment implements CarFragment.OnCarUpdateListener{
    public static final String KEY_INDEX = "index";
    public static int mCurrentIndex = 0;
    public static TextView carIdTextView;
    public static TextView carModelTextView;
    public static TextView carMakeTextView;
    public static TextView carFeePerDayTextView;
    public static TextView carAddressTextView;
    public static TextView carAvailableTextView;
    public Button prevCarButton;
    public Button nextCarButton;
    private Button carDetailsButton;
    private Button allCarsButton;
    public static ArrayList<Car> carList;
    public static ArrayList<Reservation> reservationList;
    private CarBaseHelper carBaseHelper;
    private Button searchCarButton;

    public void initializeAllData(){
        // initialize the carBaseHelper
        carBaseHelper = new CarBaseHelper(getActivity());

        // initialize ArrayLists for the car and reservation objects
        carList = new ArrayList<>();
        reservationList = new ArrayList<>();

        // initialize Hash Map collections
        Map<String, Object> carHm = new HashMap<>();
        Map<String, Object> reservationHm = new HashMap<>();

        // create car and reservation objects
        Car car1 = new Car("HD1", "Honda", "CR-V", 100.00, "821 Sainte Croix Ave, Saint-Laurent, Quebec H4L 3X9");
        Car car2 = new Car("HD2", "Honda", "Civic", 90.00, "821 Sainte Croix Ave, Saint-Laurent, Quebec H4L 3X9");
        Car car3 = new Car("HD3", "Honda", "Accord", 95.00, "550 Beaumont Ave 2nd floor, Montreal, Quebec H3N 1T7");
        Car car4 = new Car("VW1", "Volkswagen", "Tiguan", 98.00, "550 Beaumont Ave 2nd floor, Montreal, Quebec H3N 1T7");
        Car car5 = new Car("VW2", "Volkswagen", "Jetta", 98.00, "821 Sainte Croix Ave, Saint-Laurent, Quebec H4L 3X9");
        Car car6 = new Car("VW3", "Volkswagen", "GTI", 100.00, "550 Beaumont Ave 2nd floor, Montreal, Quebec H3N 1T7");
        Car car7 = new Car("FD1", "Ford", "Mustang", 100.00, "821 Sainte Croix Ave, Saint-Laurent, Quebec H4L 3X9");
        Car car8 = new Car("FD2", "Ford", "Maverick", 117.00, "550 Beaumont Ave 2nd floor, Montreal, Quebec H3N 1T7");
        Car car9 = new Car("FD3", "Ford", "Edge", 98.00, "821 Sainte Croix Ave, Saint-Laurent, Quebec H4L 3X9");
        Car car10 = new Car("FD4", "Ford", "Explorer", 90.00, "550 Beaumont Ave 2nd floor, Montreal, Quebec H3N 1T7");

        Reservation reservation1 = new Reservation("RES-testing", "CarIDTest",
                "test@email.com", "10/11/2023", "10/13/2023",
                "821 Sainte Croix Ave, Saint-Laurent, Quebec H4L 3X9", 190);

        // populate the car and reservation objects into the ArrayLists
        carList.add(car1);
        carList.add(car2);
        carList.add(car3);
        carList.add(car4);
        carList.add(car5);
        carList.add(car6);
        carList.add(car7);
        carList.add(car8);
        carList.add(car9);
        carList.add(car10);
        reservationList.add(reservation1);

        // add car and reservation objects to SQLite database
        carBaseHelper.addNewCar(car1);
        carBaseHelper.addNewCar(car2);
        carBaseHelper.addNewCar(car3);
        carBaseHelper.addNewCar(car4);
        carBaseHelper.addNewCar(car5);
        carBaseHelper.addNewCar(car6);
        carBaseHelper.addNewCar(car7);
        carBaseHelper.addNewCar(car8);
        carBaseHelper.addNewCar(car9);
        carBaseHelper.addNewCar(car10);
        carBaseHelper.addNewReservation(reservation1);

        // add car and reservation objects to Hash Map
        carHm.put(car1.getCarId(), car1);
        carHm.put(car2.getCarId(), car2);
        carHm.put(car3.getCarId(), car3);
        carHm.put(car4.getCarId(), car4);
        carHm.put(car5.getCarId(), car5);
        carHm.put(car6.getCarId(), car6);
        carHm.put(car7.getCarId(), car7);
        carHm.put(car8.getCarId(), car8);
        carHm.put(car9.getCarId(), car9);
        carHm.put(car10.getCarId(), car10);
        reservationHm.put(reservation1.getReserveId(), reservation1);

        // add hash map data to Firebase real-time database
        FirebaseDatabase.getInstance().getReference().child("cars").updateChildren(carHm);
        FirebaseDatabase.getInstance().getReference().child("reservations").updateChildren(reservationHm);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialize the all_cars array if savedInstanceState is null
        if (savedInstanceState == null) {
            initializeAllData();
        } else {
            // restore the mCurrentIndex
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX);

            // restore the carList
            ArrayList<Car> savedCarList = savedInstanceState.getParcelableArrayList("carList");
            if (savedCarList != null) {
                carList = savedCarList;
            }
        }

    } // end on onCreate

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        // display values of car info
        carIdTextView = v.findViewById(R.id.carID_textView);
        carIdTextView.setText("Car ID: "
                + carList.get(mCurrentIndex).getCarId());

        carModelTextView = v.findViewById(R.id.carModel_textView);
        carModelTextView.setText("Car Model: "
                + carList.get(mCurrentIndex).getCarModel());

        carMakeTextView = v.findViewById(R.id.carMake_textView);
        carMakeTextView.setText("Car Make: "
                + carList.get(mCurrentIndex).getCarMake());

        carFeePerDayTextView = v.findViewById(R.id.carFeePerDay_textView);
        carFeePerDayTextView.setText("Car Fee/Day: C$"
                + carList.get(mCurrentIndex).getCarRentalFee());

        carAddressTextView = v.findViewById(R.id.carAddress_textView);
        carAddressTextView.setText("Car Address: "
                + carList.get(mCurrentIndex).getCarAddress());

        // get the view of prevCarButton
        prevCarButton = v.findViewById(R.id.prevCar_button);
        prevCarButton.setOnClickListener(new View.OnClickListener(){

            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex - 1 + carList.size()) % carList.size();
                carIdTextView.setText("Car ID: "
                        + carList.get(mCurrentIndex).getCarId());
                carModelTextView.setText("Car Model: "
                        + carList.get(mCurrentIndex).getCarModel());
                carMakeTextView.setText("Car Make: "
                        + carList.get(mCurrentIndex).getCarMake());
                carFeePerDayTextView.setText("Car Fee/Day: C$"
                        + carList.get(mCurrentIndex).getCarRentalFee());
                carAddressTextView.setText("Car Address: "
                        + carList.get(mCurrentIndex).getCarAddress());
            }
        });

        // get the view of nextCarButton
        nextCarButton = v.findViewById(R.id.nextCar_button);
        nextCarButton.setOnClickListener(new View.OnClickListener(){

            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex + 1) % carList.size();
                carIdTextView.setText("Car ID: "
                        + carList.get(mCurrentIndex).getCarId());
                carModelTextView.setText("Car Model: "
                        + carList.get(mCurrentIndex).getCarModel());
                carMakeTextView.setText("Car Make: "
                        + carList.get(mCurrentIndex).getCarMake());
                carFeePerDayTextView.setText("Car Fee/Day: C$"
                        + carList.get(mCurrentIndex).getCarRentalFee());
                carAddressTextView.setText("Car Address: "
                        + carList.get(mCurrentIndex).getCarAddress());
            }
        });

        // get the view of carDetailsButton
        carDetailsButton = v.findViewById(R.id.carDetails_button);
        carDetailsButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                String carId = carList.get(mCurrentIndex).getCarId();
                String carModel = carList.get(mCurrentIndex).getCarModel();
                String carMake = carList.get(mCurrentIndex).getCarMake();
                double carFeePerDay = carList.get(mCurrentIndex).getCarRentalFee();
                String carAddress = carList.get(mCurrentIndex).getCarAddress();

                //cCreate a new CarFragment instance with the data and replace the current fragment
                CarFragment fragment = CarFragment.newInstance(carId, carModel, carMake, carFeePerDay, carAddress);
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        // get the view of allCarsButton
        allCarsButton = v.findViewById(R.id.allCars_button);
        allCarsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AllCarsActivity.class);
                intent.putParcelableArrayListExtra("carList", carList);
                startActivity(intent);
            }
        });

        // get the view of searchCarButton
        searchCarButton = (Button) v.findViewById(R.id.searchCar_button);
        searchCarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchCarFragment searchCarFragment = new SearchCarFragment();
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, searchCarFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return v;
    } // end of onCreateView

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        Log.d("TAG", "onSaveInstanceState() called");

        // save the carList
        if (carList != null) {
            outState.putParcelableArrayList("carList", carList);
        }

        // save the mCurrentIndex
        outState.putInt(KEY_INDEX, mCurrentIndex);
    }

    // method for unit test
    public ArrayList<Car> getCarList(){
        return carList;
    }

    @Override
    public void onCarUpdated(String carId, String carModel, String carMake,
                             double carFee, String carAddress, boolean carAvailable) {
        // update the TextView fields with the received data
        carIdTextView.setText("Car ID: " + carId);
        carModelTextView.setText("Car Model: " + carModel);
        carMakeTextView.setText("Car Make: " + carMake);
        carFeePerDayTextView.setText("Car Fee/Day: C$" + carFee);
        carAddressTextView.setText("Car Address: " + carAddress);
    }

    @Override
    public void onResume() {
        super.onResume();

        // update the UI with the latest data from the carList
        carIdTextView.setText("Car ID: " + carList.get(mCurrentIndex).getCarId());
        carModelTextView.setText("Car Model: " + carList.get(mCurrentIndex).getCarModel());
        carMakeTextView.setText("Car Make: " + carList.get(mCurrentIndex).getCarMake());
        carFeePerDayTextView.setText("Car Fee/Day: C$" + carList.get(mCurrentIndex).getCarRentalFee());
        carAddressTextView.setText("Car Address: " + carList.get(mCurrentIndex).getCarAddress());
    }
}
