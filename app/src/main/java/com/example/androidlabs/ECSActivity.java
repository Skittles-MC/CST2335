package com.example.androidlabs;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import android.os.AsyncTask;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
/**
 *
 *
 * Class info: Main page search electric car charging stations
 *
 *
 *
 * * onclick method to launch the program and go to different pages
 *      * Toast was used to show user actions
 * public void onCreate(Bundle savedInstanceState) {
 *
 *
 *      onPause method to save shared preferences values
 * protected void onPause(){
 *
 *  method to use inflator for tools bar
 *  public boolean onCreateOptionsMenu(Menu menu) {
 *
 *  used to display tool bars to go to different projects
 *   public boolean onOptionsItemSelected(MenuItem item) {
 *
 *   Displays an Alert Dialog the user with instructions for app use
 *    public void helpAlert() {
 *
 *
 * Customized adapter to manipulate listView
 * private class MyListAdapter extends BaseAdapter {
 *
 *
 *          obtain the number of items
 *          @return number of items on the list
 *
 * public int getCount()
 *
 * obtain the position of item in the array list
 * ECSStations getItem
 *
 * Update progress bar, list, and adapter
 * protected void onPostExecute
 *
 * update progress
 * protected void onProgressUpdate
 ****/
public class ECSActivity extends AppCompatActivity {


    ArrayList<ECStations> searchedStations = new ArrayList();
    MyListAdapter myAdapter;
    ProgressBar pgsBar;
    String searchLat ;
    String searchLong ;
    EditText latitudeText, longitudeText;
    String queryURL = "https://api.openchargemap.io/v3/poi/?output=json&countrycode=CA&camelcase=true&maxresults=10&latitude=";
    String carChargerURL;
    protected SharedPreferences.Editor edit;

    /**
     * OnPause with Shared Prefs commited
     */
    @Override
    protected void onPause() {
        super.onPause();
        edit.putString("lastSearch", latitudeText.getText().toString());
        edit.putString("lastSearch", longitudeText.getText().toString());
        edit.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecs_main);

        Toolbar toolBar = findViewById(R.id.ecs_toolbar);
        setSupportActionBar(toolBar);


        //get items from the layout
        Button gotoFavBtn = findViewById(R.id.fav_button);
        longitudeText = findViewById(R.id.longitude);
        latitudeText = findViewById(R.id.latitude);
        pgsBar = findViewById(R.id.progress);

        //show the result from last search
        SharedPreferences prefs = getSharedPreferences("lastSearch", MODE_PRIVATE);
        edit = prefs.edit();
        String lastLatitude = prefs.getString("latitude", "44");

        String lastLongitude = prefs.getString("longitude", "-71");


        latitudeText.setText(lastLatitude);
        longitudeText.setText(lastLongitude);



        //progress bar
        pgsBar.setVisibility(View.VISIBLE);
        pgsBar.setProgress(10);

       ListView list = findViewById(R.id.listviewecs);
//        list.setAdapter(myAdapter = new MyListAdapter());
        list.setOnItemClickListener((parent, view, position, id) -> {

            Intent detail = new Intent(ECSActivity.this, ECSdetail.class);


            detail.putExtra("title", searchedStations.get(position).getTitle1());
            detail.putExtra("latitude", searchedStations.get(position).getLatitude());
            detail.putExtra("longitude", searchedStations.get(position).getLongitude());
            detail.putExtra("phoneNo", searchedStations.get(position).getPhoneNo());


            startActivity(detail);


        });



        //search new stations
        Button searchBtn = findViewById(R.id.search_button1);
        searchBtn.setOnClickListener(xyz -> {
                searchLat = latitudeText.getText().toString();
                searchLong = longitudeText.getText().toString();
                StationFinder stations = new StationFinder();
                pgsBar.setVisibility(View.VISIBLE);
                carChargerURL = queryURL +searchLat + "&longitude=" +searchLong;
                stations.execute(queryURL);

            });



        //go to fav page
        gotoFavBtn.setOnClickListener(v -> {
            Intent favPage = new Intent(ECSActivity.this, ECSfavorite.class);
            startActivityForResult(favPage, 1);
        });





    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.car_charger_main_page:
                Toast.makeText(this, "This is where you are now!", Toast.LENGTH_LONG).show();
                break;
            case R.id.recipe_main_page:
                Intent goToRecipePage = new Intent(ECSActivity.this, MainNewsPage.class);
                ECSActivity.this.startActivityForResult(goToRecipePage, 10);
                break;
            case R.id.currency_exchange_main_page:
              Intent goToCurrencyExchange = new Intent(ECSActivity.this, CurrencyConvert.class);
               ECSActivity.this.startActivityForResult(goToCurrencyExchange, 10);
//                break;
//            case R.id.news_main_page:
//                Intent goToNewsPage = new Intent(ECSActivity.this, MainNewsPage.class);
//                ECSActivity.this.startActivityForResult(goToNewsPage, 10);
//                break;
            case R.id.go_to_app_favourites:
                Intent goToCarChargerFavourites = new Intent(ECSActivity.this, ECSfavorite.class);
                ECSActivity.this.startActivityForResult(goToCarChargerFavourites, 10);
                break;
            case R.id.go_to_app_help:
                helpAlert();
                break;


        }
        return true;
    }

    public void helpAlert() {
        View middle = getLayoutInflater().inflate(R.layout.actvity_ecs_help, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setPositiveButton("Sounds Good!", (dialog, id) -> {
                    // What to do on Accept
                }).setView(middle);
        builder.create().show();
    }


    private class StationFinder extends AsyncTask<String, Integer, String> {
        String title, latitude, longitude, phoneNo;

        /**
         * connect to web pages using URL and obtain JSON objects
         * @param strings
         * @return strings pulled from JSON objects
         */

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         *
         * This will normally run on a background thread. But to better
         * support testing frameworks, it is recommended that this also tolerates
         * direct execution on the foreground thread, as part of the {@link #execute} call.
         *
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param
         *
         * *** The parameters of the task.
         *
         * @return A result, defined by the subclass of this task.
         *
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */


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
                    JSONObject object = jsonArray.getJSONObject(i);
                    // Accesses the "addressInfo" object within the 1st JSON object
                    JSONObject source = object.getJSONObject("addressInfo");
                    // Below strings are accesses from within the inner JSON object
                    String title1 = source.getString("title");
                    title = title1;
                    String lat = source.getString("latitude");
                    latitude = lat;
                    String lon = source.getString("longitude");
                    longitude = lon;
                    String phone = source.getString("contactTelephone1");
                    if (phone != "null") {
                        phoneNo = phone;
                    } else {
                        phoneNo = "N/A";
                    }
                    // Adds the new message to the ArrayList
                    searchedStations.add(new ECStations(title, latitude, longitude, phoneNo));

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


        /**
         * Update progress bar, list, and adapter
         * @param sentFromDoInBackground
         */

        @Override                   //Type 3
        protected void onPostExecute(String sentFromDoInBackground) {
            super.onPostExecute(sentFromDoInBackground);

            searchedStations.clear();
            ListView searchList = findViewById(R.id.listviewecs);
            searchList.setAdapter(myAdapter = new MyListAdapter());
            myAdapter.notifyDataSetChanged();





}

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            // Updates GUI stuff only.setVisibility(View.VISIBLE);

        }



        }

    /**
     * List Adapter for displaying displaying stations in listview
     */


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


                thisView = getLayoutInflater().inflate(R.layout.ecs_view_row, null);

                TextView itemText = thisView.findViewById(R.id.row_title);
                ECStations ecs = getItem(position);
                itemText.setText(ecs.getTitle1());


                return thisView;
            }


        }
    }



