package com.example.mohamed.inventoryappstage1;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mohamed.inventoryappstage1.Data.InventoryAppContract;
import com.example.mohamed.inventoryappstage1.Data.InventoryAppDbHelper;

public class MainActivity extends AppCompatActivity {
    private TextInputEditText productName;
    private TextInputEditText productPrice;
    private TextInputEditText productQuantity;
    private TextInputEditText supplierName;
    private TextInputEditText supplierPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        productName = (TextInputEditText) findViewById(R.id.produtName);
        productPrice = (TextInputEditText) findViewById(R.id.produtPrice);
        productQuantity = (TextInputEditText) findViewById(R.id.productQuantity);
        supplierName = (TextInputEditText) findViewById(R.id.supplierName);
        supplierPhone = (TextInputEditText) findViewById(R.id.supplierPhone);
        Button insertingBtn = (Button) findViewById(R.id.addtoDB);
        Button queryBtn = (Button) findViewById(R.id.getfromDB);
        insertingBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                insertData();


            }
        });

        queryBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                queryData();
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void insertData() {
        InventoryAppDbHelper dbHelper = new InventoryAppDbHelper(this);
        SQLiteDatabase sql = dbHelper.getWritableDatabase();
        ContentValues products = new ContentValues();
        String pname, suppliername;
        pname = productName.getText().toString();
        int pprice = productPrice.getInputType();
        int pquantity = productQuantity.getInputType();
        suppliername = supplierName.getText().toString();
        int supplierphone = supplierPhone.getInputType();
        if (pname.trim().equals("")) {
            productName.setError(getResources().getString(R.string.error));
        } else {
            products.put(InventoryAppContract.InventoryEntry.COLUMN_PRODUCT_NAME, pname);
        }
        if (productPrice.equals("")) {
            productPrice.setError(getResources().getString(R.string.error));
        } else {
            products.put(InventoryAppContract.InventoryEntry.COLUMN_PRODUCT_PRICE, pprice);
        }
        if (productQuantity.equals("")) {
            productQuantity.setError(getResources().getString(R.string.error));
        } else {
            products.put(InventoryAppContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY, pquantity);
        }
        if (suppliername.trim().equals("")) {
            supplierName.setError(getResources().getString(R.string.error));
        } else {
            products.put(InventoryAppContract.InventoryEntry.COLUMN_SUPPLIER_NAME, suppliername);
        }
        if (supplierPhone.equals("")) {
            supplierPhone.setError(getResources().getString(R.string.error));
        } else {
            products.put(InventoryAppContract.InventoryEntry.COLUMN_SUPPLIER_PHONE, supplierphone);
        }
        long newRowInsertedID = sql.insert(InventoryAppContract.InventoryEntry.TABLE_NAME, null, products);
        if (newRowInsertedID != -1) {
            Toast.makeText(this, getResources().getString(R.string.successmsg), Toast.LENGTH_SHORT).show();
            productName.setText("");
            productPrice.setText("");
            productQuantity.setText("");
            supplierName.setText("");
            supplierPhone.setText("");
        } else {
            Toast.makeText(this, getResources().getString(R.string.failedmsg), Toast.LENGTH_SHORT).show();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void queryData() {
        InventoryAppDbHelper dbHelper = new InventoryAppDbHelper(this);
        SQLiteDatabase sql = dbHelper.getReadableDatabase();
        String[] proj = {
                InventoryAppContract.InventoryEntry.COLUMN_PRODUCT_NAME,
                InventoryAppContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY
        };
        TextView data = (TextView) findViewById(R.id.showdatatv);
        try (Cursor cursor = sql.query(InventoryAppContract.InventoryEntry.TABLE_NAME,
                proj,
                null,
                null,
                null,
                null,
                InventoryAppContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY
        )) {

            int productNameColumnIndex = cursor.getColumnIndex(InventoryAppContract.InventoryEntry.COLUMN_PRODUCT_NAME);
            int quantityColumnIndex = cursor.getColumnIndex(InventoryAppContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY);
            while (cursor.moveToNext()) {
                String productnameString = cursor.getString(productNameColumnIndex);
                int quantity = cursor.getInt(quantityColumnIndex);
                data.append("\n" + "Product: " + productnameString + " | Quantity: " + quantity);
                data.setVisibility(View.VISIBLE);
            }
        }

    }

}