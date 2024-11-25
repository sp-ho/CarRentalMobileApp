package com.example.carrentalproject;

import static com.example.carrentalproject.MainFragment.carList;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.carrentalproject.database.CarBaseHelper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Iterator;

public class CarFragment extends Fragment {
    private EditText carIdEditText;
    private EditText carModelEditText;
    private EditText carMakeEditText;
    private EditText carFeePerDayEditText;
    private Spinner carAddressSpinner;
    private Button updateCarButton;
    private Button deleteCarButton;
    private Button addCarButton;
    private String carIdRetrieve;
    private String carModelRetrieve;
    private String carMakeRetrieve;
    private double carFeePerDayRetrieve;
    private String carAddressRetrieve;
    private static final String EXTRA_CAR_ID = "com.example.carrentalproject.car_id";
    private static final String EXTRA_CAR_MODEL = "com.example.carrentalproject.car_model";
    private static final String EXTRA_CAR_MAKE = "com.example.carrentalproject.car_make";
    private static final String EXTRA_CAR_FEE= "com.example.carrentalproject.car_fee";
    private static final String EXTRA_CAR_ADDRESS = "com.example.carrentalproject.car_address";
    private CarBaseHelper carBaseHelper;
    private View v;
    DatabaseReference carDbRef;

    public CarFragment() {
    }

    public static CarFragment newInstance(String carId, String carModel, String carMake,
                                          double carFee, String carAddress) {
        CarFragment fragment = new CarFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_CAR_ID, carId);
        args.putString(EXTRA_CAR_MODEL, carModel);
        args.putString(EXTRA_CAR_MAKE, carMake);
        args.putDouble(EXTRA_CAR_FEE, carFee);
        args.putString(EXTRA_CAR_ADDRESS, carAddress);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // cars table in Firebase
        carDbRef = FirebaseDatabase.getInstance().getReference().child("cars");

        // retrieve arguments
        Bundle args = getArguments();
        if (args != null) {
            carIdRetrieve = args.getString(EXTRA_CAR_ID);
            carModelRetrieve = args.getString(EXTRA_CAR_MODEL);
            carMakeRetrieve = args.getString(EXTRA_CAR_MAKE);
            carFeePerDayRetrieve = args.getDouble(EXTRA_CAR_FEE, 0);
            carAddressRetrieve = args.getString(EXTRA_CAR_ADDRESS);
        }
    }

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_car, container, false);

        // initialize database helper
        carBaseHelper = new CarBaseHelper((getActivity()));

        // read data from SQLite database
        ArrayList<Car> carListDb = carBaseHelper.readCars();

        // create a StringBuilder to build the text content for the TextView
        StringBuilder carDataText = new StringBuilder();

        // append each car's data to the StringBuilder
        for (Car car : carListDb) {
            carDataText.append("Car ID: ").append(car.getCarId()).append("\n");
            carDataText.append("Car Make: ").append(car.getCarMake()).append("\n");
            carDataText.append("Car Model: ").append(car.getCarModel()).append("\n");
            carDataText.append("Car Fee/Day: C$").append(car.getCarRentalFee()).append("\n");
            carDataText.append("Car Address: C$").append(car.getCarAddress()).append("\n");
        }

        // find the TextView in layout
        TextView carDataTextView = (TextView) v.findViewById(R.id.carData_textView);

        // set the text content of the TextView
        carDataTextView.setText(carDataText.toString());

        // get the views of EditTexts
        carIdEditText = v.findViewById(R.id.carId_editText);
        carIdEditText.setText(carIdRetrieve + "");
        carModelEditText = v.findViewById(R.id.carModel_editText);
        carModelEditText.setText(carModelRetrieve + "");
        carMakeEditText = v.findViewById(R.id.carMake_editText);
        carMakeEditText.setText(carMakeRetrieve + "");
        carFeePerDayEditText = v.findViewById(R.id.carFeePerDay_editText);
        carFeePerDayEditText.setText(carFeePerDayRetrieve + "");

        // find the Spinner in layout
        carAddressSpinner = v.findViewById(R.id.carAddress_spinner);

        // define the array of options
        String[] addressOptions = {"Main Campus", "Park Campus"};

        // create an ArrayAdapter and set it to the Spinner
        ArrayAdapter<String> addAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, addressOptions);
        addAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        carAddressSpinner.setAdapter(addAdapter);

        // get the view of updateCarButton
        updateCarButton = v.findViewById(R.id.updateCar_button);
        updateCarButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                // update the car information based on the user's input
                String carId = carIdEditText.getText().toString();
                String carModel = carModelEditText.getText().toString();
                String carMake = carMakeEditText.getText().toString();
                String carFeePerDayStr = carFeePerDayEditText.getText().toString();

                // get the selected address option from the Spinner
                String carLocation = carAddressSpinner.getSelectedItem().toString();
                String carAddress = "";

                // set the car's address based on the selected value
                if (carLocation.equals("Main Campus")) {
                    carAddress = "821 Sainte Croix Ave, Saint-Laurent, Quebec H4L 3X9";
                } else if (carLocation.equals("Park Campus")) {
                    carAddress = "550 Beaumont Ave 2nd floor, Montreal, Quebec H3N 1T7";
                }

                // check for input validation
                if (carId.isEmpty() || carModel.isEmpty() || carMake.isEmpty() || carFeePerDayStr.isEmpty() || carLocation.isEmpty()) {
                    // display an error message if any field is empty
                    Toast.makeText(getActivity(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        double carFeePerDay = Double.parseDouble(carFeePerDayStr);

                        // find the car in carList by car ID and update its properties
                        boolean carFound = false;
                        for (Car car : carList) {
                            if (car.getCarId().equals(carId)) {
                                car.setCarModel(carModel);
                                car.setCarMake(carMake);
                                car.setCarRentalFee(carFeePerDay);
                                car.setCarAddress(carAddress);

                                carFound = true;
                                break; // exit the loop once the car is found and updated
                            }
                        }

                        if (!carFound) {
                            // display an error message if the car with the given ID was not found
                            Toast.makeText(getActivity(), "Car not found with ID: " + carId, Toast.LENGTH_SHORT).show();
                        } else {
                            // update the data in the database
                            carBaseHelper.updateCar(new Car(carId, carMake, carModel, carFeePerDay, carAddress));

                            // refresh the TextView with the updated data
                            refreshCarDataTextView();

                            String toastMessage = "Car " + carId + " is updated";
                            Toast.makeText(getActivity(), toastMessage, Toast.LENGTH_SHORT).show();
                        }
                    } catch (NumberFormatException e) {
                        // handle the case where the user entered an invalid number for carFeePerDay
                        Toast.makeText(getActivity(), "Invalid car fee value. Please enter a valid number.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // get the view of deleteCarButton
        deleteCarButton = (Button) v.findViewById(R.id.deleteCar_button);
        deleteCarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get the car ID to delete
                String carIdToDelete = carIdEditText.getText().toString();

                // check if the car ID is empty
                if (carIdToDelete.isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter a car ID to delete", Toast.LENGTH_SHORT).show();
                    return; // exit the method if car ID is empty
                }

                // find the car in carList by car ID and remove it
                boolean carFound = false;
                Iterator<Car> iterator = carList.iterator();
                while (iterator.hasNext()) {
                    Car car = iterator.next();
                    if (car.getCarId().equals(carIdToDelete)) {
                        iterator.remove();
                        carFound = true;
                        break; // exit the loop once the car is found and removed
                    }
                }

                if (carFound) {
                    // delete the car from the database using carBaseHelper
                    carBaseHelper.deleteCar(carIdToDelete);

                    // refresh the TextView with the updated data
                    refreshCarDataTextView();

                    String toastMessage = "Car " + carIdToDelete + " is deleted";
                    Toast.makeText(getActivity(), toastMessage, Toast.LENGTH_SHORT).show();
                } else {
                    // display an error message if the car with the specified ID was not found
                    Toast.makeText(getActivity(), "Car not found with ID: " + carIdToDelete, Toast.LENGTH_SHORT).show();
                }
            }
        });

        // get view of addCarButton
        addCarButton = (Button) v.findViewById(R.id.addCar_button);
        addCarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // obtain the car information based on the user's input
                String carId = carIdEditText.getText().toString();
                String carModel = carModelEditText.getText().toString();
                String carMake = carMakeEditText.getText().toString();
                String carFeePerDayStr = carFeePerDayEditText.getText().toString();
                String carLocation = carAddressSpinner.getSelectedItem().toString();

                String carAddress = "";

                // check for input validation
                if (carId.isEmpty() || carModel.isEmpty() || carMake.isEmpty() || carFeePerDayStr.isEmpty() || carLocation.isEmpty()) {
                    // display an error message if any field is empty
                    Toast.makeText(getActivity(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        double carFeePerDay = Double.parseDouble(carFeePerDayStr);

                        // set the car's address based on the selected value
                        if (carLocation.equals("Main Campus")) {
                            carAddress = "821 Sainte Croix Ave, Saint-Laurent, Quebec H4L 3X9";
                        } else if (carLocation.equals("Park Campus")) {
                            carAddress = "550 Beaumont Ave 2nd floor, Montreal, Quebec H3N 1T7";
                        }

                        // create a new Car object with the entered information
                        Car newCar = new Car(carId, carMake, carModel, carFeePerDay, carAddress);

                        // add the new car to the database
                        carBaseHelper.addNewCar(newCar);

                        // add the new car to carList arraylist
                        carList.add(newCar);

                        // insert the new car to Firebase
                        insertCarToFirebase();

                        // refresh the TextView with the updated data
                        refreshCarDataTextView();

                        // display a toast message to indicate that the car was added
                        String toastMessage = "Car " + carId + " is added to SQLite";
                        Toast.makeText(getActivity(), toastMessage, Toast.LENGTH_SHORT).show();

                    } catch (NumberFormatException e) {
                        // handle the case where the user entered an invalid number for carFeePerDay
                        Toast.makeText(getActivity(), "Invalid car fee value. Please enter a valid number.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        // call the method to initially populate the TextView with data
        refreshCarDataTextView();

        return v;
    }

    public interface OnCarUpdateListener {
        void onCarUpdated(String carId, String carModel, String carMake,
                          double carFee, String carAddress, boolean carAvailable);
    }

    private void refreshCarDataTextView() {

        // read data from the database
        ArrayList<Car> carListDb = carBaseHelper.readCars();

        // create a StringBuilder to build the text content for the TextView
        StringBuilder carDataText = new StringBuilder();

        // append each car's data to the StringBuilder
        for (Car car : carListDb) {
            carDataText.append("Car ID: ").append(car.getCarId()).append("\n");
            carDataText.append("Car Make: ").append(car.getCarMake()).append("\n");
            carDataText.append("Car Model: ").append(car.getCarModel()).append("\n");
            carDataText.append("Car Fee/Day: C$").append(car.getCarRentalFee()).append("\n");
            carDataText.append("Car Address: ").append(car.getCarAddress()).append("\n\n");
        }

        // find the TextView in layout
        TextView carDataTextView = v.findViewById(R.id.carData_textView);

        // set the text content of the TextView with the updated data
        carDataTextView.setText(carDataText.toString());
    }

    public void insertCarToFirebase(){
        String carId = carIdEditText.getText().toString();
        String carMake = carMakeEditText.getText().toString();
        String carModel = carModelEditText.getText().toString();
        double carFee = Double.parseDouble(carFeePerDayEditText.getText().toString());
        String carAddress = carAddressSpinner.getSelectedItem().toString();

        Car car = new Car(carId, carMake, carModel, carFee, carAddress);

        carDbRef.push().setValue(car);
        Toast.makeText(getActivity(),"Car inserted to Firebase", Toast.LENGTH_SHORT).show();
    }
}
