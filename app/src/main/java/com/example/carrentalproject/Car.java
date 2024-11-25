package com.example.carrentalproject;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Car implements Parcelable {
    // instance variables
    private String carId;
    private String carMake;
    private String carModel;
    private double carRentalFee;
    private String carAddress;

    // default constructor
    public Car (){}

    // constructor with parameters
    public Car(String carId, String carMake, String carModel, double carRentalFee, String carAddress) {
        this.carId = carId;
        this.carMake = carMake;
        this.carModel = carModel;
        this.carRentalFee = carRentalFee;
        this.carAddress = carAddress;
    }

    // getters and setters
    public String getCarId() {
        return carId;
    }
    public void setCarId(String carId) {
        this.carId = carId;
    }
    public String getCarMake() {
        return carMake;
    }
    public void setCarMake(String carMake) {
        this.carMake = carMake;
    }
    public String getCarModel() {
        return carModel;
    }
    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }
    public double getCarRentalFee() {
        return carRentalFee;
    }
    public void setCarRentalFee(double carRentalFee) {
        this.carRentalFee = carRentalFee;
    }
    public String getCarAddress() {
        return carAddress;
    }
    public void setCarAddress(String carAddress) {
        this.carAddress = carAddress;
    }

    // implementations of Parcelable
    protected Car(Parcel in) {
        carId = in.readString();
        carMake = in.readString();
        carModel = in.readString();
        carRentalFee = in.readDouble();
        carAddress = in.readString();
    }

    public static final Creator<Car> CREATOR = new Creator<Car>() {
        @Override
        public Car createFromParcel(Parcel in) {
            return new Car(in);
        }

        @Override
        public Car[] newArray(int size) {
            return new Car[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(carId);
        parcel.writeString(carMake);
        parcel.writeString(carModel);
        parcel.writeDouble(carRentalFee);
        parcel.writeString(carAddress);
    }

    @Override
    public String toString() {
        return "Car ID: " + carId +
                "\nCar Make: " + carMake +
                "\nCar Model: " + carModel +
                "\nCar Rental Fee: C$" + carRentalFee +
                "\nCar Address: " + carAddress + "\n";
    }

    public double getTotalFee(long daysDifference) {
        return carRentalFee * daysDifference;
    }
}
