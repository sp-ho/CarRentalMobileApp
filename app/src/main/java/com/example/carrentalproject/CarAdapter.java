package com.example.carrentalproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder>{
    private ArrayList<Car> carList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Car car);
    }

    public CarAdapter(ArrayList<Car> carList, OnItemClickListener listener) {
        this.carList = carList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_all_cars_item, parent, false);
        return new CarViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        Car car = carList.get(position);
        holder.bindCarData(car);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(car);
            }
        });
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    static class CarViewHolder extends RecyclerView.ViewHolder {
        TextView carIdTextView;
        TextView carInfoTextView;

        CarViewHolder(View itemView) {
            super(itemView);
            carIdTextView = itemView.findViewById(R.id.carIdTextView);
            carInfoTextView = itemView.findViewById(R.id.carInfoTextView);
        }

        void bindCarData(Car car) {
            carIdTextView.setText("Car ID: " + car.getCarId());
            String carInfo = "Model: " + car.getCarModel() +
                    "\nMake " + car.getCarMake() +
                    "\nRental Fee/Day: C$" + car.getCarRentalFee() +
                    "\nAddress: " + car.getCarAddress();
            carInfoTextView.setText(carInfo);
        }
    }
}
