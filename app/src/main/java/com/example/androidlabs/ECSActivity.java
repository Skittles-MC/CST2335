package com.example.androidlabs;





import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.content.Intent;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.content.SharedPreferences;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;






public class ECSActivity extends AppCompatActivity {


    private static ArrayList<ECStations> searchedStations = new ArrayList<>();

    private static SQLiteDatabase db;

    private static BaseAdapter myAdapter;

    private static ProgressBar pgsBar;

    private static StationFinder station;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecs_main);






        //get items from the layout
        ListView list = findViewById(R.id.ecs_listview);
        Button searchBtn = findViewById(R.id.search_button);
        Button gotoFavBtn = findViewById(R.id.fav_button);
        EditText longitudeText = findViewById(R.id.longitude);
        EditText latitudeText = findViewById(R.id.latitude);
        pgsBar = findViewById(R.id.progress);

        //show the result from last search
        SharedPreferences prefs = getSharedPreferences("lastSearch", MODE_PRIVATE);
        String lastLatitude = prefs.getString("latitude", "");
        String lastLongitude = prefs.getString("longitude", "");

        latitudeText.setText(lastLatitude);
        longitudeText.setText(lastLongitude);
        pgsBar.setVisibility(View.VISIBLE);
        pgsBar.setProgress(0);

        StationFinder station= new StationFinder();
        station.execute(latitudeText.getText().toString(),longitudeText.getText().toString());

        DatabaseHelper dbOpener = new DatabaseHelper(this);
        db = dbOpener.getWritableDatabase();


        //toolbar setup
        Toolbar toolbar = findViewById(R.id.ecs_toolbar);
        setSupportActionBar(toolbar);


        //go to fav page
        gotoFavBtn.setOnClickListener(v -> {
            Intent favPage = new Intent(ECSActivity.this, ECSfavorite.class);
            startActivityForResult(favPage, 1);
        });


        //search new stations
        searchBtn.setOnClickListener(v -> {
            //set up progress bar
            pgsBar.setVisibility(View.VISIBLE);
            pgsBar.setProgress(0);
            StationFinder sfc = new StationFinder();
            sfc.execute(latitudeText.getText().toString(),longitudeText.getText().toString());

        });


        //populate rows for the listview
        list.setAdapter(myAdapter = new MyListAdapter());



        //set click listener for each item to open the detail page
        list.setOnItemClickListener((parent, view, position, id) -> {

            Bundle data = new Bundle();
            data.putString("title", searchedStations.get(position).getTitle1());
            data.putString("latitude", searchedStations.get(position).getLatitude());
            data.putString("longitude", searchedStations.get(position).getLongitude());
            data.putString("phoneNo", searchedStations.get(position).getPhoneNo());
            data.putString("address", searchedStations.get(position).getAddress());
            data.putBoolean("fav", searchedStations.get(position).isFav());


            Intent detail = new Intent(ECSActivity.this, ECSdetail.class);
            detail.putExtras(data);
            startActivityForResult(detail, 1);


        });

    }


    @Override
    protected void onStop() {
        super.onStop();
        EditText latitude = findViewById(R.id.latitude);
        EditText longitude = findViewById(R.id.longitude);

        SharedPreferences prefs = getSharedPreferences("lastSearch", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("latitude", latitude.getText().toString());
        editor.putString("longitude", longitude.getText().toString());

        editor.commit();
    }

    public void deleteStation(String latitude, String longitude){
        Cursor cursor = db.query(true, DatabaseHelper.TABLE_NAME,
                new String[]{DatabaseHelper.COL_ID},
                DatabaseHelper.COL_LATITUDE + " = ? AND " +
                        DatabaseHelper.COL_LONGITUDE + " = ? "
                , new String[]{latitude, longitude}, null, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_ID));
            Log.i("id to be deleted is" + id, "ecsfmain");
            db.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.COL_ID + "=?",
                    new String[]{Long.toString(id)});
        }
        cursor.close();
        Toast.makeText(this, R.string.ecs_Deleted,
                Toast.LENGTH_SHORT).show();
        for (ECStations station : searchedStations) {
            if (station.getLongitude().equals(longitude)) {
                station.setFav(false);
                break;
            }
        }
        myAdapter.notifyDataSetChanged();
    }


    public void addStation(String title, String latitude, String longitude, String address, String phoneNo){
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COL_TITLE, title);
        cv.put(DatabaseHelper.COL_LATITUDE, latitude);
        cv.put(DatabaseHelper.COL_LONGITUDE, longitude);
        cv.put(DatabaseHelper.COL_PHONENO, phoneNo);
        cv.put(DatabaseHelper.COL_ADDRESS, address);

        db.insert(DatabaseHelper.TABLE_NAME, null, cv);

        Toast.makeText(this, R.string.ecs_station_added,
                Toast.LENGTH_SHORT).show();

        //update current list to reflect the saved stations
        for (ECStations station : searchedStations) {
            if (station.getLongitude().equals(longitude)) {
                station.setFav(true);
                break;
            }
        }
        myAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {

        }
//from detail page to add station to fav/db
        else if (requestCode == 1 && resultCode == 2) {

            //if the data need to be added to fav
            if (data.getBooleanExtra("addToFav", false)) {
                String title = data.getStringExtra("title");
                String latitude = data.getStringExtra("latitude");
                String longitude = data.getStringExtra("longitude");
                String phoneNo = data.getStringExtra("phoneNo");
                String address = data.getStringExtra("address");

                addStation(title,latitude, longitude, address, phoneNo);

            }
        }
// from detail page to delete one station from fav
        else if (requestCode == 1 && resultCode == 3) {
            // if the data is needed to be deleted from favorite stations
            if (data.getBooleanExtra("deleteFromFav", false)) {
                String latitude = data.getStringExtra("latitude");
                String longitude = data.getStringExtra("longitude");
                deleteStation(latitude,longitude);
            }
        }
        //from favpage
        else if (requestCode == 1 && resultCode == 6) {

            int numOfDel = data.getIntExtra("numOfDel", 0);
            String latitude;
            for (int i = 0; i <= numOfDel; i++) {
                latitude = data.getStringExtra(i + "");
                for (ECStations station : searchedStations) {
                    if (station.getLatitude().equals(latitude)) {
                        station.setFav(false);
                        break;
                    }
                }
            }
            myAdapter.notifyDataSetChanged();

        }
    }


    public static class StationFinder extends AsyncTask<String, Integer, ArrayList<ECStations>> {
        ArrayList<ECStations> newStations = new ArrayList<>();

        @Override
        protected ArrayList<ECStations> doInBackground(String... strings) {
            String urlLatitude = "45.347571";
            String urlLongitude = "-75.756140";
            if(!strings[0].equals("")&& !strings[1].equals("")){
                urlLatitude = strings[0];
                urlLongitude = strings[1];
            }

            String queryURL = "https://api.openchargemap.io/v3/poi/?output=json&latitude="
                    +urlLatitude+"&longitude="+urlLongitude+"&maxresults=10";

            try {
                URL url = new URL(queryURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                publishProgress(2);
                InputStream inStream = urlConnection.getInputStream();

                //Set up the JSON object parser:
                // json is UTF-8 by default
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line;
                int progressCount = 10;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                    publishProgress(progressCount++);
                }
                String result = sb.toString();
                JSONArray jResults = new JSONArray(result);
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject addressInfo = jResults.getJSONObject(i).getJSONObject("AddressInfo");
                    String title = addressInfo.getString("Title");
                    String latitude = addressInfo.getDouble("Latitude") + "";
                    String longitude = addressInfo.getDouble("Longitude") + "";
                    String phoneNo = addressInfo.getString("ContactTelephone1");
                    String address = addressInfo.getString("AddressLine1");
                    newStations.add(new ECStations(title, latitude, longitude, phoneNo, address));
                }
                urlConnection.disconnect();
                inStream.close();
            } catch (MalformedURLException mfe) {
                mfe.printStackTrace();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } catch (JSONException json) {
                json.printStackTrace();
            }
            return newStations;
        }


        @Override                   //Type 3
        protected void onPostExecute(ArrayList<ECStations> newStations) {
            super.onPostExecute(newStations);

            searchedStations = newStations;

            myAdapter.notifyDataSetChanged();
            pgsBar.setVisibility(View.GONE);
            Cursor cursor = null;
            //check if already saved
            if (searchedStations.size() > 0) {
                for (ECStations station : searchedStations) {
                    cursor = db.query(false, DatabaseHelper.TABLE_NAME, new String[]{DatabaseHelper.COL_ADDRESS},
                            DatabaseHelper.COL_LATITUDE + " =? AND " + DatabaseHelper.COL_LONGITUDE + "=?",
                            new String[]{station.getLatitude(), station.getLongitude()}, null, null, null, null);
                    if (cursor.getCount() > 0) {
                        station.setFav(true);
                    }
                }
                cursor.close();
            }
        }

        @Override                       //Type 2
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            pgsBar.setProgress(values[0]);
        }
    }

    class MyListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return searchedStations.size();
        }

        @Override
        public Object getItem(int i) {
            return searchedStations.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View oldView, ViewGroup parent) {
            View thisView = oldView;
            thisView = getLayoutInflater().inflate(R.layout.list_view_ecs, parent, false);

            TextView title = thisView.findViewById(R.id.row_title);
            TextView address = thisView.findViewById(R.id.row_address);
            ImageView saved = thisView.findViewById(R.id.row_saved);

            if (!searchedStations.get(position).isFav()) {
                saved.setVisibility(View.GONE);
            }
            title.setText(searchedStations.get(position).getTitle1());
            address.setText(searchedStations.get(position).getAddress());
            return thisView;
        }
    }



}


