package com.example.carrentalproject.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.carrentalproject.Car;
import com.example.carrentalproject.Reservation;
import com.example.carrentalproject.database.CarDbSchema.CarTable;
import com.example.carrentalproject.database.CarDbSchema.ReservationTable;

import java.util.ArrayList;

public class CarBaseHelper extends SQLiteOpenHelper {
    public static final int VERSION = 1;
    public static final String DATABASE_NAME = "carBase.db";

    // constructor
    public CarBaseHelper(Context context)
    {
        super (context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + CarTable.NAME +
                "(" + CarTable.Cols.CAR_ID + ", " +
                CarTable.Cols.CAR_MAKE + ", " +
                CarTable.Cols.CAR_MODEL + ", " +
                CarTable.Cols.CAR_FEE + ", " +
                CarTable.Cols.CAR_ADDRESS + ")");

        db.execSQL("CREATE TABLE " + ReservationTable.NAME +
                "(" + ReservationTable.Cols.RESERVE_ID + ", " +
                ReservationTable.Cols.CAR_ID + ", " +
                ReservationTable.Cols.CUSTOMER_ID + ", " +
                ReservationTable.Cols.START_DATE + ", " +
                ReservationTable.Cols.END_DATE + ", " +
                ReservationTable.Cols.PICKUP_LOCATION + ", " +
                ReservationTable.Cols.TOTAL_FEE + ")");
    }

    // upgrade the version if needed
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    // content values of car
    private static ContentValues getContentValues(Car car)
    {
        ContentValues values = new ContentValues();
        values.put(CarTable.Cols.CAR_ID, car.getCarId());
        values.put(CarTable.Cols.CAR_MAKE, car.getCarMake());
        values.put(CarTable.Cols.CAR_MODEL, car.getCarModel());
        values.put(CarTable.Cols.CAR_FEE, car.getCarRentalFee());
        values.put(CarTable.Cols.CAR_ADDRESS, car.getCarAddress());
        return values;
    }

    // content values of reservation
    private static ContentValues getContentValuesReservation(Reservation reservation)
    {
        ContentValues values = new ContentValues();
        values.put(ReservationTable.Cols.RESERVE_ID, reservation.getReserveId());
        values.put(ReservationTable.Cols.CAR_ID, reservation.getCarId());
        values.put(ReservationTable.Cols.CUSTOMER_ID, reservation.getCustomerId());
        values.put(ReservationTable.Cols.START_DATE, reservation.getStartDate());
        values.put(ReservationTable.Cols.END_DATE, reservation.getEndDate());
        values.put(ReservationTable.Cols.PICKUP_LOCATION, reservation.getPickupLocation());
        values.put(ReservationTable.Cols.TOTAL_FEE, reservation.getTotalFee());
        return values;
    }

    // insert a new car
    public void addNewCar(Car car)
    {
        // writing data into SQLite database
        SQLiteDatabase db = this.getWritableDatabase();

        // creating values of contentValues
        ContentValues contentValues = getContentValues(car);

        // insert values into table row
        db.insert(CarTable.NAME, null, contentValues);

        // close the database
        db.close();
    }

    // update info of car
    public void updateCar (Car car)
    {
        String car_id = car.getCarId();

        // creating values from ContentValues
        ContentValues contentValues = getContentValues(car);
        SQLiteDatabase db = this.getWritableDatabase();

        db.update(CarTable.NAME, contentValues, CarTable.Cols.CAR_ID + "=?",
                new String[] {car_id});
    }

    // read the cars
    public ArrayList<Car> readCars()
    {
        // reading data from database
        SQLiteDatabase db = this.getReadableDatabase();

        // create cursor variable to fetch all records
        Cursor carCursor = db.rawQuery("SELECT * FROM " + CarTable.NAME, null);

        // create ArrayList carModalArrayList
        ArrayList<Car> carModalArrayList = new ArrayList<>();

        // moving the cursor to 1st position
        // if true, read the whole thing until the cursor is moving to the next
        if (carCursor.moveToFirst())
        {
            do{
                // add data found in database table to ArrayList
                carModalArrayList.add(new Car(
                        carCursor.getString(0),
                        carCursor.getString(1),
                        carCursor.getString(2),
                        carCursor.getDouble(3),
                        carCursor.getString(4)));
            }
            // when the cursor move to the last row of table in database, it can't move to next row,
            // condition turns false and stops the loop
            while(carCursor.moveToNext());
        }
        carCursor.close();
        return carModalArrayList;
    }

    // delete a specific car by car ID
    public void deleteCar(String carId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CarTable.NAME, CarTable.Cols.CAR_ID + "=?", new String[]{carId});
        db.close();
    }

    // methods for searching car(s) by ID, make and model
    public Car searchCarById(String carId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Car car = null;

        Cursor cursor = db.query(
                CarTable.NAME,
                null,
                CarTable.Cols.CAR_ID + "=?",
                new String[]{carId},
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            int carIdIndex = cursor.getColumnIndex(CarTable.Cols.CAR_ID);
            int carMakeIndex = cursor.getColumnIndex(CarTable.Cols.CAR_MAKE);
            int carModelIndex = cursor.getColumnIndex(CarTable.Cols.CAR_MODEL);
            int carFeeIndex = cursor.getColumnIndex(CarTable.Cols.CAR_FEE);
            int carAddressIndex = cursor.getColumnIndex(CarTable.Cols.CAR_ADDRESS);

            if (carIdIndex != -1 && carMakeIndex != -1 && carModelIndex != -1 &&
                    carFeeIndex != -1 && carAddressIndex != -1) {
                car = new Car(
                        cursor.getString(carIdIndex),
                        cursor.getString(carMakeIndex),
                        cursor.getString(carModelIndex),
                        cursor.getDouble(carFeeIndex),
                        cursor.getString(carAddressIndex)
                );
            } else {
                Log.d("search error", "data not found in database");
            }
        }

        cursor.close();
        db.close();

        return car;
    }

    public ArrayList<Car> searchCarsByMake(String carMake) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Car> cars = new ArrayList<>();

        Cursor cursor = db.query(
                CarTable.NAME,
                null,
                CarTable.Cols.CAR_MAKE + "=?",
                new String[]{carMake},
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            int carIdIndex = cursor.getColumnIndex(CarTable.Cols.CAR_ID);
            int carMakeIndex = cursor.getColumnIndex(CarTable.Cols.CAR_MAKE);
            int carModelIndex = cursor.getColumnIndex(CarTable.Cols.CAR_MODEL);
            int carFeeIndex = cursor.getColumnIndex(CarTable.Cols.CAR_FEE);
            int carAddressIndex = cursor.getColumnIndex(CarTable.Cols.CAR_ADDRESS);

            if (carIdIndex != -1 && carMakeIndex != -1 && carModelIndex != -1 &&
                    carFeeIndex != -1 && carAddressIndex != -1) {
                Car car = new Car(
                        cursor.getString(carIdIndex),
                        cursor.getString(carMakeIndex),
                        cursor.getString(carModelIndex),
                        cursor.getDouble(carFeeIndex),
                        cursor.getString(carAddressIndex)
                );
                cars.add(car);
            }
        }

        cursor.close();
        db.close();

        return cars;
    }

    public ArrayList<Car> searchCarsByModel(String carModel) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Car> cars = new ArrayList<>();

        Cursor cursor = db.query(
                CarTable.NAME,
                null,
                CarTable.Cols.CAR_MODEL + "=?",
                new String[]{carModel},
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            int carIdIndex = cursor.getColumnIndex(CarTable.Cols.CAR_ID);
            int carMakeIndex = cursor.getColumnIndex(CarTable.Cols.CAR_MAKE);
            int carModelIndex = cursor.getColumnIndex(CarTable.Cols.CAR_MODEL);
            int carFeeIndex = cursor.getColumnIndex(CarTable.Cols.CAR_FEE);
            int carAddressIndex = cursor.getColumnIndex(CarTable.Cols.CAR_ADDRESS);

            if (carIdIndex != -1 && carMakeIndex != -1 && carModelIndex != -1 &&
                    carFeeIndex != -1 && carAddressIndex != -1) {
                Car car = new Car(
                        cursor.getString(carIdIndex),
                        cursor.getString(carMakeIndex),
                        cursor.getString(carModelIndex),
                        cursor.getDouble(carFeeIndex),
                        cursor.getString(carAddressIndex)
                );
                cars.add(car);
            }
        }

        cursor.close();
        db.close();

        return cars;
    }

    // add new reservation to SQLite database
    public void addNewReservation(Reservation reservation)
    {
        // writing data into database
        SQLiteDatabase db = this.getWritableDatabase();

        // creating values of contentValues
        ContentValues contentValues = getContentValuesReservation(reservation);

        // insert values into table row
        db.insert(ReservationTable.NAME, null, contentValues);

        // close the database
        db.close();
    }

    // read reservation data from database
    public ArrayList<Reservation> readReservations()
    {
        // reading data from database
        SQLiteDatabase db = this.getReadableDatabase();

        // create cursor variable to fetch all records
        Cursor reservationCursor = db.rawQuery("SELECT * FROM " + ReservationTable.NAME, null);

        // create ArrayList reservationModalArrayList
        ArrayList<Reservation> reservationModalArrayList = new ArrayList<>();

        // moving the cursor to 1st position
        // if true, read the whole thing until the cursor is moving to the next
        if (reservationCursor.moveToFirst())
        {
            do{
                // add data found in database table to ArrayList
                reservationModalArrayList.add(new Reservation(
                        reservationCursor.getString(0),
                        reservationCursor.getString(1),
                        reservationCursor.getString(2),
                        reservationCursor.getString(3),
                        reservationCursor.getString(4),
                        reservationCursor.getString(5),
                        reservationCursor.getDouble(6)));
            }
            // when the cursor move to the last row of table in database, it can't move to next row,
            // condition turns false and stops the loop
            while(reservationCursor.moveToNext());
        }
        reservationCursor.close();
        return reservationModalArrayList;
    }
}
