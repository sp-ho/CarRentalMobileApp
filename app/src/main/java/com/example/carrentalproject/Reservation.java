package com.example.carrentalproject;

public class Reservation {
    private String reserveId;
    private String carId;
    private String customerId;
    private String startDate;
    private String endDate;
    private String pickupLocation;
    private double totalFee;

    public Reservation(){}

    public Reservation(String reserveId, String carId, String customerId,
                       String startDate, String endDate, String pickupLocation, double totalFee) {
        this.reserveId = reserveId;
        this.carId = carId;
        this.customerId = customerId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.pickupLocation = pickupLocation;
        this.totalFee = totalFee;
    }

    public String getReserveId() {
        return reserveId;
    }
    public void setReserveId(String reserveId) {
        this.reserveId = reserveId;
    }

    public String getCarId() {
        return carId;
    }
    public void setCarId(String carId) {
        this.carId = carId;
    }
    public String getCustomerId() {
        return customerId;
    }
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    public String getStartDate() {
        return startDate;
    }
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    public String getEndDate() {
        return endDate;
    }
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    public double getTotalFee() {
        return totalFee;
    }
    public void setTotalFee(double totalFee) {
        this.totalFee = totalFee;
    }
    public String getPickupLocation() {
        return pickupLocation;
    }
    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    @Override
    public String toString() {
        return "CarReservation{" +
                "reservationId=" + reserveId +
                ", carId='" + carId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", totalFee=" + totalFee +
                '}';
    }
}
