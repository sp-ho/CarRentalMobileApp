package com.example.carrentalproject;

import static com.example.carrentalproject.LoginActivity.username;
import static com.example.carrentalproject.ReserveActivity.addressTextView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.carrentalproject.database.CarBaseHelper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class AvailableCarAdapter extends RecyclerView.Adapter<AvailableCarAdapter.ViewHolder> {
    private List<Car> carList;
    private long daysDifference;
    private long startDateMillis;
    private long endDateMillis;
    private String reservationId = generateReservationId();

    public AvailableCarAdapter(List<Car> carList, long daysDifference, long startDateMillis, long endDateMillis) {
        this.carList = carList;
        this.daysDifference = daysDifference;
        this.startDateMillis = startDateMillis;
        this.endDateMillis = endDateMillis;
    }

    // generate unique reservation ID using timestamp and Random()
    private String generateReservationId() {
        long timestamp = System.currentTimeMillis(); // current timestamp
        int random = new Random().nextInt(1000); // random number
        return "RES-" + timestamp + "-" + random;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Car car = carList.get(position);

        holder.carMakeModelTextView.setText(car.getCarMake() + " " + car.getCarModel());
        holder.carFeeTextView.setText("Daily Fee: $" + car.getCarRentalFee());

        /*Log.d("numberOfDays", String.valueOf(daysDifference));*/
        double totalFee = car.getTotalFee(daysDifference);

        // set the total fee in the TextView
        holder.totalFeeTextView.setText("Total: $" + totalFee);

        // set an OnClickListener on the select button to handle car selection for reservation
        holder.selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCarDetailsDialog(v.getContext(), car, totalFee);
            }
        });
    }

    // alert dialog for confirming the reservation
    private void showCarDetailsDialog(Context context, Car car, double totalFee) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.car_details_dialog, null);

        // define widgets
        TextView carModelTextView = dialogView.findViewById(R.id.carModelTextView);
        TextView startDateTextView = dialogView.findViewById(R.id.startDateTextView);
        TextView endDateTextView = dialogView.findViewById(R.id.endDateTextView);
        TextView totalFeeTextView = dialogView.findViewById(R.id.totalFeeTextView);
        Button confirmButton = dialogView.findViewById(R.id.confirmButton);

        // set view fo widgets
        carModelTextView.setText("Car Model: " + car.getCarModel());
        startDateTextView.setText("Start Date: " + formatDate(startDateMillis));
        endDateTextView.setText("End Date: " + formatDate(endDateMillis));
        totalFeeTextView.setText("Total Fee: $" + totalFee);

        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // create a Reservation object
                Reservation newReservation = new Reservation(
                        reservationId, car.getCarId(), username.getText().toString(),
                        formatDate(startDateMillis), formatDate(endDateMillis),
                        addressTextView.getText().toString(),totalFee
                );

                // write reservation data to SQLite
                CarBaseHelper dbHelper = new CarBaseHelper(context);
                dbHelper.addNewReservation(newReservation);

                // write reservation data to Firebase
                writeReservationToFirebase(newReservation);

                // create an Intent for ConfirmationActivity
                Intent intent = new Intent(context, ConfirmationActivity.class);

                // put the data as extras in the intent to pass to ConfirmationActivity
                intent.putExtra("reservation_id", reservationId);
                intent.putExtra("car_id", car.getCarId());
                intent.putExtra("car_make_model", car.getCarMake() + " " + car.getCarModel());
                intent.putExtra("start_date", startDateMillis);
                intent.putExtra("end_date", endDateMillis);
                intent.putExtra("total_fee", totalFee);
                intent.putExtra("pickup_location", addressTextView.getText().toString());

                context.startActivity(intent);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void writeReservationToFirebase(Reservation reservation) {
        // get a reference to your Firebase Realtime Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reservationsRef = database.getReference("reservations");

        // generate a new unique key for the reservation
        String reservationKey = reservationsRef.push().getKey();

        // set the reservation data at the generated key
        reservationsRef.child(reservationKey).setValue(reservation);
    }

    // set the format for date
    private String formatDate(long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        Date date = new Date(millis);
        return sdf.format(date);
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView totalFeeTextView;
        public TextView carIdTextView;
        private TextView carMakeModelTextView;
        private TextView carFeeTextView;
        private Button selectButton;

        public ViewHolder(View itemView) {
            super(itemView);
            carIdTextView = itemView.findViewById(R.id.carIdTextView);
            carMakeModelTextView = itemView.findViewById(R.id.carMakeModelTextView);
            carFeeTextView = itemView.findViewById(R.id.carFeeTextView);
            selectButton = itemView.findViewById(R.id.selectButton);
            totalFeeTextView = itemView.findViewById(R.id.totalFeeTextView);
        }
    }
}
