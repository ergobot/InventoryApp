package com.example.android.inventoryapp;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventoryapp.ProductContract.ProductEntry;
import com.example.android.inventoryapp.contentprovider.ProductContentProvider;

import java.io.FileDescriptor;
import java.io.IOException;

public class DetailActivity extends Activity {

    EditText detailTitle;
    ImageView detailImage;
    TextView detailReceived;
    TextView detailSold;
    TextView detailStock;
    EditText detailPrice;
    EditText detailVendor;
    String image;

    private Uri productUri;

    private static final int PICK_PHOTO_FOR_PRODUCT = 9002;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.product_edit);

        detailTitle = (EditText) findViewById(R.id.product_edit_name);
        detailImage = (ImageView) findViewById(R.id.detail_image);
        detailReceived = (TextView) findViewById(R.id.detail_received);
        detailSold = (TextView) findViewById(R.id.detail_sold);
        detailStock = (TextView) findViewById(R.id.detail_stock);
        detailPrice = (EditText) findViewById(R.id.detail_price);
        PayTextWatcher ptw = new PayTextWatcher(detailPrice, "%.2f $");
        detailPrice.addTextChangedListener(ptw);
        detailVendor = (EditText) findViewById(R.id.detail_vendor);
        detailVendor.addTextChangedListener(new TextWatcher() {
            boolean ignoreChange = false;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String output;
                String input = charSequence.toString().replaceAll("\\D+", "");
                if (!ignoreChange) {
                    switch (input.length()) {
                        case 7:
                            output = String.format("%s-%s", input.substring(0, 3), input.substring(3, 7));
                            break;
                        case 10:
                            output = String.format("(%s) %s-%s", input.substring(0, 3), input.substring(3, 6), input.substring(6, 10));
                            break;
                        case 11:
                            output = String.format("%s (%s) %s-%s", input.substring(0, 1), input.substring(1, 4), input.substring(4, 7), input.substring(7, 11));
                            break;
                        case 12:
                            output = String.format("+%s (%s) %s-%s", input.substring(0, 2), input.substring(2, 5), input.substring(5, 8), input.substring(8, 12));
                            break;
                        default:
                            output = input;
                    }
                    ignoreChange = true;
                    detailVendor.setText(output);
                    detailVendor.setSelection(detailVendor.getText().length());
                    ignoreChange = false;
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        Button saveButton = (Button) findViewById(R.id.save_button);
        Button deleteButton = (Button) findViewById(R.id.delete_button);
        Button orderButton = (Button) findViewById(R.id.order_button);

        Bundle extras = getIntent().getExtras();

        // check from the saved Instance
        productUri = (bundle == null) ? null : (Uri) bundle
                .getParcelable(ProductContentProvider.CONTENT_ITEM_TYPE);

        // Or passed from the other activity
        if (extras != null) {
            productUri = extras
                    .getParcelable(ProductContentProvider.CONTENT_ITEM_TYPE);

            fillData(productUri);
        } else if (productUri != null) {
            fillData(productUri);
        } else {
            fillNewDetail();
        }


        detailImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageChooser();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (validInput()) {
                    setResult(RESULT_OK);
                    finish();
                } else {

                }
            }

        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteProduct();
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Patterns.PHONE.matcher(detailVendor.getText().toString()).matches()) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + detailVendor.getText().toString()));
                    startActivity(intent);
                } else {
                    makeToast(getResources().getString(R.string.toast_bad_phone));
                }
            }
        });


    }


    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    /* Choose an image from Gallery */
    void openImageChooser() {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.intent_select)), PICK_PHOTO_FOR_PRODUCT);
        } else {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.intent_select)), PICK_PHOTO_FOR_PRODUCT);
        }


    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_PHOTO_FOR_PRODUCT) {
                // Get the url from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // Get the path from the Uri
                    image = selectedImageUri.toString();
                    Log.i("image", "Image Path : " + image);
                    // Set the image in ImageView
                    detailImage.setImageURI(selectedImageUri);
                }
            }
        }
    }

    private boolean validInput() {
        if (TextUtils.isEmpty(detailTitle.getText().toString())) {
            makeToast(getResources().getString(R.string.toast_no_name));
            return false;
        }
        if (TextUtils.isEmpty(detailPrice.getText().toString())) {
            makeToast(getResources().getString(R.string.toast_no_price));
            return false;
        }
        return true;
    }

    public void changeAmount(View view) {


        int received = Integer.parseInt(detailReceived.getText().toString());
        int sold = Integer.parseInt(detailSold.getText().toString());
        switch (view.getId()) {
            case R.id.detail_received_decrement:
                detailReceived.setText(String.valueOf(--received));
                break;
            case R.id.detail_received_increment:
                detailReceived.setText(String.valueOf(++received));
                break;
            case R.id.detail_sold_decrement:
                detailSold.setText(String.valueOf(--sold));
                break;
            case R.id.detail_sold_increment:
                detailSold.setText(String.valueOf(++sold));
                break;
        }
        int stock = received - sold;
        detailStock.setText(String.valueOf(stock));
    }

    private void fillNewDetail() {
        String received = "0";
        String sold = "0";
        String stock = "0";
        image = "";

        detailReceived.setText("0");
        detailReceived.setText(received);
        detailSold.setText(sold);
        detailStock.setText(stock);
    }

    private void fillData(Uri uri) {
        String[] projection = {
                ProductEntry.COLUMN_NAME,
                ProductEntry.COLUMN_VENDOR,
                ProductEntry.COLUMN_RECEIVED,
                ProductEntry.COLUMN_SOLD,
                ProductEntry.COLUMN_STOCK,
                ProductEntry.COLUMN_PRICE,
                ProductEntry.COLUMN_VENDOR,
                ProductEntry.COLUMN_IMAGE};
        Cursor cursor = getContentResolver().query(uri, projection, null, null,
                null);
        if (cursor != null) {
            cursor.moveToFirst();

            String name = cursor.getString(cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_NAME));
            String vendor = cursor.getString(cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_VENDOR));
            String received = cursor.getString(cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_RECEIVED));
            String sold = cursor.getString(cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_SOLD));
            String stock = cursor.getString(cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_STOCK));
            String price = cursor.getString(cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_PRICE));
            image = cursor.getString(cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_IMAGE));

            detailTitle.setText(name);
            if (received.isEmpty()) {
                detailReceived.setText("0");
            } else {
                detailReceived.setText(received);
            }
            detailSold.setText(sold.isEmpty() ? "0" : sold);
            detailStock.setText(stock.isEmpty() ? "0" : stock);
            detailPrice.setText(price);
            detailVendor.setText(vendor.isEmpty() ? "" : vendor);
            if (!image.isEmpty()) {
                detailImage.setImageURI(Uri.parse(image));
            }
            // always close the cursor
            cursor.close();
        }
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putParcelable(ProductContentProvider.CONTENT_ITEM_TYPE, productUri);
    }

    @Override
    public void onBackPressed() {
        if (validInput()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    private void saveState() {

        String name = detailTitle.getText().toString();
        String received = detailReceived.getText().toString();
        String sold = detailSold.getText().toString();
        String stock = detailStock.getText().toString();
        String price = detailPrice.getText().toString();
        String vendor = detailVendor.getText().toString();

        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_NAME, name);
        values.put(ProductEntry.COLUMN_VENDOR, vendor);
        values.put(ProductEntry.COLUMN_RECEIVED, received);
        values.put(ProductEntry.COLUMN_SOLD, sold);
        values.put(ProductEntry.COLUMN_STOCK, stock);
        values.put(ProductEntry.COLUMN_PRICE, price);
        values.put(ProductEntry.COLUMN_IMAGE, image == null ? "" : image);

        if (productUri == null) {
            // New product
            productUri = getContentResolver().insert(
                    ProductContentProvider.CONTENT_URI, values);
            productUri = Uri.parse(ProductContentProvider.CONTENT_URI.toString() + "/" + productUri.getLastPathSegment());
        } else {
            // Update product
            getContentResolver().update(productUri, values, null, null);
        }
    }

    private void makeToast(String message) {
        Toast.makeText(DetailActivity.this, message,
                Toast.LENGTH_LONG).show();
    }

    private void deleteProduct() {
        if (productUri != null) {
            getContentResolver().delete(productUri, "", null);
        }

    }
}