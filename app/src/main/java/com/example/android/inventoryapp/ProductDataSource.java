package com.example.android.inventoryapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ProductDataSource {

    // Database fields
    private SQLiteDatabase database;
    private ProductDbHelper dbHelper;
    private ProductContract.ProductEntry entry;
    private String[] allColumns = {
            entry.COLUMN_ID,
            entry.COLUMN_NAME,
            entry.COLUMN_VENDOR,
            entry.COLUMN_RECEIVED,
            entry.COLUMN_SOLD,
            entry.COLUMN_STOCK,
            entry.COLUMN_PRICE,
            entry.COLUMN_IMAGE
    };

    public ProductDataSource(Context context) {
        dbHelper = new ProductDbHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void addProduct(Product product) {
        open();
        ContentValues values = new ContentValues();
        values.put(entry.COLUMN_ID, product.id);
        values.put(entry.COLUMN_NAME, product.name);
        values.put(entry.COLUMN_VENDOR, product.vendor);
        values.put(entry.COLUMN_RECEIVED, product.received);
        values.put(entry.COLUMN_SOLD, product.sold);
        values.put(entry.COLUMN_STOCK, product.stock);
        values.put(entry.COLUMN_PRICE, product.price);
        values.put(entry.COLUMN_IMAGE, product.image);
        database.insert(entry.TABLE_NAME, null, values);
    }

    public Cursor readData() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                entry._ID,
                entry.COLUMN_ID,
                entry.COLUMN_NAME,
                entry.COLUMN_VENDOR,
                entry.COLUMN_RECEIVED,
                entry.COLUMN_SOLD,
                entry.COLUMN_STOCK,
                entry.COLUMN_PRICE,
                entry.COLUMN_IMAGE
        };

        String sortOrder =
                entry.COLUMN_NAME + " DESC";

        Cursor c = db.query(
                entry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        return c;
    }

    public void deleteAll(){
        open();
        database.delete(entry.TABLE_NAME,null,null);
    }

    public void updateNameById(String name, long id){

        ContentValues values = new ContentValues();
        values.put(entry.COLUMN_NAME, name);

        String selection = entry.COLUMN_ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(id)};

        dbHelper.getReadableDatabase().update(
                entry.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );
    }




}
