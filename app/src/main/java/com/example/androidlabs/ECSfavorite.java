package com.example.androidlabs;


import androidx.appcompat.app.AppCompatActivity;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;


public class ECSfavorite  extends AppCompatActivity {
    private ArrayList<ECStations> favStations = new ArrayList<>();

     private static SQLiteDatabase db;
        private static BaseAdapter myAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ecs_favorite);


        ListView favList = findViewById(R.id.favlist);
        favList.setAdapter(myAdapter = new MyListAdapter());


        DatabaseHelper dbOpener = new DatabaseHelper(this);
//check this after
        db = dbOpener.getWritableDatabase();

        String[] columns = {DatabaseHelper.COL_ID, DatabaseHelper.COL_TITLE, DatabaseHelper.COL_LATITUDE, DatabaseHelper.COL_LONGITUDE, DatabaseHelper.COL_PHONENO};
        Cursor results = db.query(false, DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null, null);

        // Find the column indices
        int idColIndex = results.getColumnIndex(DatabaseHelper.COL_ID);
        int titleColIndex = results.getColumnIndex(DatabaseHelper.COL_TITLE);
        int latitudeColIndex = results.getColumnIndex(DatabaseHelper.COL_LATITUDE);
        int longitudeColIndex = results.getColumnIndex(DatabaseHelper.COL_LONGITUDE);
        int phoneColIndex = results.getColumnIndex(DatabaseHelper.COL_PHONENO);

        // Iterate over the results, return true if there is a next item
        while (results.moveToNext()) {
            long id = results.getLong(idColIndex);
            String title = results.getString(titleColIndex);
            String lat = results.getString(latitudeColIndex);
            String lon = results.getString(longitudeColIndex);
            String phone = results.getString(phoneColIndex);









            favStations.add(new ECStations(id, title, lat, lon, phone));
        }









    }



    private class MyListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return favStations.size();
        }

        @Override
        public ECStations getItem(int i) {
            return favStations.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View oldView, ViewGroup parent) {

            View thisRow = oldView;

            if(thisRow== null)

            thisRow = getLayoutInflater().inflate(R.layout.list_view_fav ,null);

            TextView title = thisRow.findViewById(R.id.row_title_fav);
            TextView latitude =thisRow.findViewById(R.id.row_lat_fav);
            TextView longitude =thisRow.findViewById(R.id.row_long_fav);
            TextView phoneNo = thisRow.findViewById(R.id.row_phone_fav);



            title.setText(favStations.get(position).getTitle());
            latitude.setText(favStations.get(position).getLatitude());
            longitude.setText(favStations.get(position).getLongitude());
            phoneNo.setText(favStations.get(position).getPhoneNo());

            ECStations stations = new ECStations();

            title.setText(stations.getTitle1());
            latitude.setText("Latitude " + stations.getLatitude());
            longitude.setText("Longitude " + stations.getLongitude());
            phoneNo.setText("Phone # " + stations.getPhoneNo());
            return thisRow;
        }
    }
}


