package com.example.androidlabs;







import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.content.Intent;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;



import android.os.AsyncTask;
import android.widget.Button;
import android.widget.EditText;


import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import android.os.Bundle;


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




    BaseAdapter myAdapter;

    private static ProgressBar pgsBar;


    EditText latitudeText, longitudeText;
    String queryURL = "https://api.openchargemap.io/v3/poi/?output=json&countrycode=CA&camelcase=true&maxresults=10&latitude=";
    String carChargerURL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecs_main);


        //get items from the layout


        Button gotoFavBtn = findViewById(R.id.fav_button);
        longitudeText = findViewById(R.id.longitude);
        latitudeText = findViewById(R.id.latitude);
        pgsBar = findViewById(R.id.progress);

        //show the result from last search
        SharedPreferences prefs = getSharedPreferences("lastSearch", MODE_PRIVATE);
        String lastLatitude = prefs.getString("latitude", "44");
        String lastLongitude = prefs.getString("longitude", "-71");


        //progress bar
        pgsBar.setVisibility(View.VISIBLE);
        pgsBar.setProgress(0);




        ListView list = findViewById(R.id.ecslistview);

        list.setAdapter(myAdapter = new MyListAdapter());

        list.setOnItemClickListener((parent, view, position, id) -> {

            Intent detail = new Intent(ECSActivity.this, ECSdetail.class);


            detail.putExtra("title", searchedStations.get(position).getTitle1());
            detail.putExtra("latitude", searchedStations.get(position).getLatitude());
            detail.putExtra("longitude", searchedStations.get(position).getLongitude());
            detail.putExtra("phoneNo", searchedStations.get(position).getPhoneNo());


            startActivity(detail);


        });

        latitudeText.setText(lastLatitude);
        longitudeText.setText(lastLongitude);
        //search new stations
        Button searchBtn = findViewById(R.id.search_button1);
        if (searchBtn != null) {

            searchBtn.setOnClickListener(v -> {


                String searchLat = latitudeText.getText().toString();
                String searchLong = longitudeText.getText().toString();

                carChargerURL = queryURL + searchLat + "&longitude=" + searchLong;
                StationFinder station = new StationFinder();
                station.execute(carChargerURL);

            });
        }


        //go to fav page
        gotoFavBtn.setOnClickListener(v -> {
            Intent favPage = new Intent(ECSActivity.this, ECSfavorite.class);
            startActivityForResult(favPage, 1);
        });





    }




    private class StationFinder extends AsyncTask<String, Integer, String> {
        String title, latitude, longitude, phoneNo;


        @Override
        protected String doInBackground(String... strings) {

            String ret = null;

            try {       // Connects to the server using the internet
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inStream = urlConnection.getInputStream();
                publishProgress(25);

                // Sets up the JSON object parser
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                // Reads the contents of the JSON input and saves results as a string
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                String result = sb.toString();

                // Saves the string of results in a JSON array
                JSONArray jsonArray = new JSONArray(result);
                publishProgress(50);

                // Iterates over the array by JSON object
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    // Accesses the "addressInfo" object within the 1st JSON object
                    JSONObject jsonObject2 = jsonObject1.getJSONObject("addressInfo");
                    // Below strings are accesses from within the inner JSON object
                    String title = jsonObject2.getString("title");
                    this.title = title;
                    String lat = jsonObject2.getString("latitude");
                    latitude = lat;
                    String lon = jsonObject2.getString("longitude");
                    longitude = lon;
                    String phone = jsonObject2.getString("contactTelephone1");

                    if (phone != "null") {
                        phoneNo = phone;
                    } else {
                        phoneNo = "N/A";
                    }
                    // Adds the new message to the ArrayList
                    searchedStations.add(new ECStations(this.title, latitude, longitude, phoneNo));

                    publishProgress(75);
                }

                // Catches various exceptions from above Try block
            } catch (MalformedURLException mfe) {
                ret = "Malformed URL Exception";
            } catch (IOException ioe) {
                ret = "IO Exception.";
            } catch (JSONException jse) {
                ret = "JSON Exception";
            }
            // What is returned here will be passed as a parameter to onPostExecute
            return ret;
        }




        @Override                   //Type 3
        protected void onPostExecute(String sentFromDoInBackground) {
            super.onPostExecute(sentFromDoInBackground);

            searchedStations.clear();
            ListView searchList = findViewById(R.id.ecslistview);
            searchList.setAdapter(myAdapter = new MyListAdapter());





}

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            // Updates GUI stuff only.setVisibility(View.VISIBLE);

        }



        }




   private class MyListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return searchedStations.size();
        }

        @Override
        public ECStations getItem(int position) {
            return searchedStations.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View oldView, ViewGroup parent) {
            View thisView = oldView;

            if (oldView == null)
                thisView = getLayoutInflater().inflate(R.layout.list_view_ecs, null);

                TextView itemText = thisView.findViewById(R.id.row_title);
                ECStations ecs = getItem(position);
                itemText.setText(ecs.getTitle1());


                return thisView;
            }


        }
    }



