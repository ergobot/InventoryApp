package com.example.android.inventoryapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.inventoryapp.ProductContract.ProductEntry;

public class ProductDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 3;

    static final String DATABASE_NAME = "product.db";

    public ProductDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_PRODUCT_TABLE = "CREATE TABLE " + ProductEntry.TABLE_NAME + " (" +
                ProductEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ProductEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                ProductEntry.COLUMN_VENDOR + " TEXT NOT NULL, " +
                ProductEntry.COLUMN_RECEIVED + " INTEGER NOT NULL, " +
                ProductEntry.COLUMN_SOLD + " INTEGER NOT NULL, " +
                ProductEntry.COLUMN_STOCK + " INTEGER NOT NULL, " +
                ProductEntry.COLUMN_PRICE + " INTEGER NOT NULL, " +
                ProductEntry.COLUMN_IMAGE + " TEXT NOT NULL);";
        sqLiteDatabase.execSQL(SQL_CREATE_PRODUCT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ProductEntry.TABLE_NAME);

    }

}
