package com.example.android.inventoryapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.android.inventoryapp.ProductContract.ProductEntry;

public class ProductCursorAdapter extends SimpleCursorAdapter {

    private int mSelectedPosition;
    Cursor items;
    private Context context;
    private int layout;

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        Cursor c = getCursor();

        final LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(layout, parent, false);

        int idCol = c.getColumnIndex(ProductEntry.COLUMN_ID);
        int nameCol = c.getColumnIndex(ProductEntry.COLUMN_NAME);
        int priceCol = c.getColumnIndex(ProductEntry.COLUMN_PRICE);
        int soldCol = c.getColumnIndex(ProductEntry.COLUMN_SOLD);
        int stockCol = c.getColumnIndex(ProductEntry.COLUMN_STOCK);

        int id = c.getInt(idCol);
        String name = c.getString(nameCol);
        int price = c.getInt(priceCol);
        int sold = c.getInt(soldCol);
        int stock = c.getInt(stockCol);


        TextView name_text = (TextView) v.findViewById(R.id.name);
        TextView price_text = (TextView) v.findViewById(R.id.price);
        TextView sold_text = (TextView) v.findViewById(R.id.sold);
        TextView stock_text = (TextView) v.findViewById(R.id.stock);
        Button sale_button = (Button) v.findViewById(R.id.sale);
        if (name_text != null) {
            name_text.setText(name);
        }
        if (price_text != null) {
            price_text.setText(String.valueOf(price));
        }
        if (sold_text != null) {
            sold_text.setText(String.valueOf(sold));
        }
        if (stock_text != null) {
            stock_text.setText(String.valueOf(stock));
        }
        sale_button.setTag(id);

        return v;
    }


    public ProductCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
        this.context = context;
        this.layout = layout;
    }


    @Override
    public void bindView(View v, Context context, Cursor c) {

        int idCol = c.getColumnIndex(ProductEntry.COLUMN_ID);
        int nameCol = c.getColumnIndex(ProductEntry.COLUMN_NAME);
        int priceCol = c.getColumnIndex(ProductEntry.COLUMN_PRICE);
        int soldCol = c.getColumnIndex(ProductEntry.COLUMN_SOLD);
        int stockCol = c.getColumnIndex(ProductEntry.COLUMN_STOCK);

        int id = c.getInt(idCol);
        String name = c.getString(nameCol);
        String price = c.getString(priceCol);
        int sold = c.getInt(soldCol);
        int stock = c.getInt(stockCol);


        TextView name_text = (TextView) v.findViewById(R.id.name);
        TextView price_text = (TextView) v.findViewById(R.id.price);
        TextView sold_text = (TextView) v.findViewById(R.id.sold);
        TextView stock_text = (TextView) v.findViewById(R.id.stock);
        Button sale_button = (Button) v.findViewById(R.id.sale);
        if (name_text != null) {
            name_text.setText(name);
        }
        if (price_text != null) {
            price_text.setText(price);
        }
        if (sold_text != null) {
            sold_text.setText(String.valueOf(sold));
        }
        if (stock_text != null) {
            stock_text.setText(String.valueOf(stock));
        }
        sale_button.setTag(id);

        int position = c.getPosition();

    }


    public void setSelectedPosition(int position) {
        mSelectedPosition = position;
        notifyDataSetChanged();

    }
}