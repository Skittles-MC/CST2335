package com.example.myappconvert;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class CurrencyConvert extends AppCompatActivity {
    Toolbar toolbar;
    EditText inputNumber;
    TextView title, outputConvert;
    Button button;
    String convertFrom, convertTo;
    boolean fromSelected, toSelected, calculated;
    SharedPreferences prefs;
    double f1 = 1;
    double coe = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert);
        toolbar =  findViewById(R.id.toolbar);
        prefs = getApplicationContext().getSharedPreferences("data", Context.MODE_PRIVATE);
        title = findViewById(R.id.Title);
        inputNumber = findViewById(R.id.Input);
        outputConvert = findViewById(R.id.Output);


        String lastOutput = prefs.getString("lastOutput", null);
        if (lastOutput != null) {
            outputConvert.setText(lastOutput);

        }
// Fetching the JSON object
        button = findViewById(R.id.Button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!inputNumber.getText().toString().equals("")) {
                    f1 = Double.parseDouble(inputNumber.getText().toString());
                }


                if (f1 > 0 && convertFrom != null && convertTo != null) {

                    JSONFetch obj = new JSONFetch();
                    obj.execute(convertFrom);
                }
            }
        });



        final ListView listView = findViewById(R.id.list);

        String[] values = new String[]{"CAD", "HKD", "ISK", "PHP", "DKK", "HUF", "CZK", "AUD", "RON", "SEK",


        };


        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.listitem, R.id.itemText, values);


        listView.setAdapter(adapter);
//List of Currency
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                String itemValue = (String) listView.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), itemValue, Toast.LENGTH_LONG).show();
                if (!fromSelected) {
                    convertFrom = itemValue;

                    //update title text:
                    title.setText("Convert To:");
                    fromSelected = true;
                    calculated = false;

                } else {
                    if (!itemValue.equals(convertFrom)) {
                        convertTo = itemValue;

                        //complete title text:
                        title.setText("Converting...");
                        toSelected = true;
                    }
                }

            }
        });


    }

    @Override
    protected void onPause() {

        super.onPause();

        if (calculated) {
            String outputText = outputConvert.getText().toString();
            if (!outputText.equals("")) prefs.edit().putString("lastOutput", outputText).apply();


        }
    }

    class JSONFetch extends AsyncTask<String, Void, Void> {

        ProgressDialog dialog = new ProgressDialog(CurrencyConvert.this);
        String error = " ";
            String apiResponse = "";



//Parsing in json
        @Override
        protected Void doInBackground(String... params) {
            String curs = convertFrom + "," + convertTo;
            OkHttpClient client = new OkHttpClient();
            if (convertFrom.equals("EUR")) curs = convertTo;
            if (convertTo.equals("EUR")) curs = convertFrom;
//json URL
            Request request = new Request.Builder().url("https://api.exchangeratesapi.io/latest?symbols=" + curs).build();

            try {
                Response response = client.newCall(request).execute();
                apiResponse = response.body().string();
            } catch (Exception e) {
                error = e.toString();
            }
            return null;
        }
/*
*
*
* */
        @Override
        protected void onPreExecute() {
            dialog.setMessage("Converting...");
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void v) {
            dialog.dismiss();
            if (!error.isEmpty()) {

            }
            try {
                double fromRatio, toRatio;

                if (convertFrom.equals("EUR")) fromRatio = 1;
                else
                    fromRatio = Double.parseDouble(new JSONObject(apiResponse).getJSONObject("rates").getString(convertFrom));

                if (convertTo.equals("EUR")) toRatio = 1;
                else
                    toRatio = Double.parseDouble(new JSONObject(apiResponse).getJSONObject("rates").getString(convertTo));
//calculations
                coe = toRatio / fromRatio;

                double out = coe * f1;


                outputConvert.setText(f1 + " " + convertFrom + "(s) = " + Double.toString(out) + " " + convertTo + "(s)");


                title.setText("Convert From:");
                toSelected = false;
                fromSelected = false;
                calculated = true;

            } catch (Exception e) {

            }

            super.onPostExecute(v);
        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }
    /*
     * This method is for the toolbar
     *
     *
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {


            case R.id.item1:
                inputNumber.getText().toString();
                Toast.makeText(getApplicationContext(), "Favourite", Toast.LENGTH_SHORT).show();
                break;

            case R.id.help:
               Intent intent = new Intent(CurrencyConvert.this, MyProfile.class);
               startActivity(intent);
                break;


        } return true;
    }



}







