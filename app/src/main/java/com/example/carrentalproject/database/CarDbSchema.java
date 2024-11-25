package com.example.carrentalproject.database;

public class CarDbSchema {
    public static final class CarTable {
        public static final String NAME = "car"; // CarTable.NAME

        public static final class Cols {
            public static final String CAR_ID = "car_id";
            public static final String CAR_MAKE = "car_make";
            public static final String CAR_MODEL = "car_model";
            public static final String CAR_FEE = "car_fee";
            public static final String CAR_ADDRESS = "car_address";
        }
    }

    public static final class ReservationTable {
        public static final String NAME = "reservation"; // ReservationTable.NAME

        public static final class Cols {
            public static final String RESERVE_ID = "reserve_id";
            public static final String CAR_ID = "car_id";
            public static final String CUSTOMER_ID = "customer_id";
            public static final String START_DATE = "start_date";
            public static final String END_DATE = "end_date";
            public static final String PICKUP_LOCATION = "pickup_location";
            public static final String TOTAL_FEE = "total_fee";
        }
    }
}
