package com.example.android.inventoryapp;


import android.provider.BaseColumns;

public class ProductContract {

    /*
    Inner class that defines the contents of the location table
    */
    public static final class ProductEntry implements BaseColumns {

        public static final String TABLE_NAME = "product";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_RECEIVED = "received";
        public static final String COLUMN_SOLD = "sold";
        public static final String COLUMN_STOCK = "stock";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_VENDOR = "vendor";
        public static final String COLUMN_IMAGE = "image";

    }

}
