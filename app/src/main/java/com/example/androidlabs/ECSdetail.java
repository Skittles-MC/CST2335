package com.example.androidlabs;





import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.content.Intent;

import android.os.Bundle;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class ECSdetail extends AppCompatActivity {

        SQLiteDatabase litedb;

        @Override
        protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_ecs_page);


            //get data from the list
            Intent info = getIntent();


            String title = info.getStringExtra("title");
            String latitude = info.getStringExtra("latitude");
            String longitude = info.getStringExtra("longitude");
            String address = info.getStringExtra("address");
            String phoneNo = info.getStringExtra("phoneNo");


            //get reference of views
            TextView titleView = findViewById(R.id.text_locale);
            TextView latitudeView = findViewById(R.id.text_latitude);
            TextView longitudeView = findViewById(R.id.text_longitude);
            TextView phoneNoView = findViewById(R.id.text_phoneNo);


            //set text into views
            titleView.setText("Title: " + title);
            latitudeView.setText("Latitude: " + latitude);
            longitudeView.setText("Longitude: " + longitude);
            phoneNoView.setText("Address: " + address);

            if (phoneNo.equals("null") || phoneNo.equals("")) {
                phoneNoView.setText("Phone Number: Not Available");
            } else phoneNoView.setText("Phone Number: " + phoneNo);

            DatabaseHelper dbOpener = new DatabaseHelper(this);
            litedb = dbOpener.getWritableDatabase();

            // buttons
            Button addfav = findViewById(R.id.fav_button);

            Button back = findViewById(R.id.backButton);


            back.setOnClickListener(v -> finish());

            addfav.setOnClickListener(v -> {

                Toast.makeText(this, "Successfully added to list!", Toast.LENGTH_LONG).show();

                ContentValues newRow = new ContentValues();

                newRow.put(DatabaseHelper.COL_TITLE, title);
                newRow.put(DatabaseHelper.COL_LATITUDE, latitude);
                newRow.put(DatabaseHelper.COL_LONGITUDE, longitude);
                newRow.put(DatabaseHelper.COL_PHONENO, phoneNo);
                newRow.put(DatabaseHelper.COL_ADDRESS, address);

                litedb.insert(DatabaseHelper.TABLE_NAME, null, newRow);

                Intent favs = new Intent(ECSdetail.this, ECSfavorite.class);

                startActivity(favs);

            });


        }

    }