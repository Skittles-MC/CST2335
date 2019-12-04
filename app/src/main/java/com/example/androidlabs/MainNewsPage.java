package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


/**
 *
 * TODO:Organize/reformat imports
 * Class info: Main page with to search and save a news article
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
 * NewsArticleSetterGetter getItem
 *
 * Update progress bar, list, and adapter
 * protected void onPostExecute
 *
 * update progress
 * protected void onProgressUpdate
 ****/
public class MainNewsPage extends AppCompatActivity {

    MyListAdapter newsAdapter;
    ArrayList<NewsArticleSetterGetter> newsLog = new ArrayList();
    ProgressBar bar;
    String queryURL = "https://newsapi.org/v2/everything?apiKey=acd83f1c540645bf94509063a0219b64&q=";
    String newsURL;

    protected SharedPreferences prefs;
    protected EditText searchEditText;
    protected SharedPreferences.Editor edit;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_main_page);

        //TODO: VARS
        ListView newsList = findViewById(R.id.newsList);
        newsList.setAdapter(newsAdapter = new MyListAdapter());
        bar = findViewById(R.id.newsBar);
        bar.setVisibility(View.INVISIBLE);
        bar.setProgress(25);
        searchEditText = findViewById(R.id.newsEditText);
        Toolbar tBar = (Toolbar) findViewById(R.id.navigation_toolbar);
        setSupportActionBar(tBar);

        newsList.setOnItemClickListener((lv, vw, pos, id) -> {
            Toast.makeText(MainNewsPage.this, "You clicked on: " + newsLog.get(pos).getTitle(), Toast.LENGTH_SHORT).show();


            //TODO:ADD MORE CONTENT
            Intent goToPage2 = new Intent(MainNewsPage.this, NewsArticleDetails.class);
            goToPage2.putExtra("newsTitle", newsLog.get(pos).getTitle());
            goToPage2.putExtra("newsDescription", newsLog.get(pos).getDescription());
            goToPage2.putExtra("newsURL", newsLog.get(pos).getUrl());
            startActivity(goToPage2);
        });

        Button searchButton = findViewById(R.id.newsButton1);
        searchButton.setOnClickListener(clik ->{
            newsLog.clear();
            String searchText = searchEditText.getText().toString();
            newsURL = queryURL + searchText;
            NewsQuery  newsQuery = new NewsQuery();
            bar.setVisibility(View.VISIBLE);
            newsQuery.execute(newsURL);

        });

        prefs = getSharedPreferences("NewsReserveName", MODE_PRIVATE);
        edit = prefs.edit();
        String previous = prefs.getString("NewsReserveName", "");
        searchEditText.setText(previous);

    }


    @Override
    protected void onPause(){
        super.onPause();
        edit.putString("NewsReserveName", searchEditText.getText().toString());
        edit.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.currency_exchange_main_page:
               // Intent goToCurrencyExchange = new Intent(MainNewsPage.this, CurrencyExchangeMain.class);
               // MainNewsPage.this.startActivityForResult(goToCurrencyExchange, 10);
                break;
            case R.id.car_charger_main_page:
                // Intent goToCarChargerFinder = new Intent(MainNewsPage.this, CarChargerFinder.class);
                // MainNewsPage.this.startActivityForResult(goToCarChargerFinder, 10);
                break;

            case R.id.recipe_main_page:
                //Intent goToNewsPage = new Intent(MainNewsPage.this, RecipePage.class);
                // MainNewsPage.this.startActivityForResult(goToNewsPage, 10);
                break;
            case R.id.news_main_page:
                Intent goToMainNewsPage = new Intent(MainNewsPage .this, MainNewsPage .class);
                MainNewsPage.this.startActivityForResult(goToMainNewsPage, 10);
                break;
            case R.id.go_to_app_help:
                helpAlert();
                break;
            case R.id.go_to_app_favourites:
                Intent goToNewsFavourites = new Intent(MainNewsPage .this, NewsArticleHelper .class);
                MainNewsPage .this.startActivityForResult(goToNewsFavourites, 10);
                break;
        }
        return true;
    }


    //TODO: VAR content
    public void helpAlert() {
        View middle = getLayoutInflater().inflate(R.layout.news_about_help, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setPositiveButton("Sounds Good!", (dialog, id) -> {
                    // What to do on Accept
                }).setView(middle);
        builder.create().show();
    }


    private class MyListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return newsLog.size();
        }

        @Override
        public NewsArticleSetterGetter getItem(int position) {
            return newsLog.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View thisRow = convertView;
            NewsArticleSetterGetter rowNews = getItem(position);

            thisRow = getLayoutInflater().inflate(R.layout.news_view_row, null);
            TextView itemTitle = thisRow.findViewById(R.id.newsTitle);
//            TextView itemDescript = thisRow.findViewById(R.id.newsAuthor);
            //TextView itemURL = thisRow.findViewById(R.id.newsURL);

            itemTitle.setText(rowNews.getTitle() + " ");
//            itemDescript.setText("Author: " + rowNews.getAuthor() + " ");
// itemURL.setText("URL: " + rowNews.getUrl() + " ");
            return thisRow;
        }
    }

//    /**
//     * customized Alert Boxed
//     * WHEN YOU HIT THE HELP BUTTON
//     */
//    private void alertExample() {
//        View middle = getLayoutInflater().inflate(R.layout.news_about_help, null);
//
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage("The Message")
//                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        // What to do on Accept
//                    }
//                })
////                .setNegativeButton("Negative", new DialogInterface.OnClickListener() {
////                    public void onClick(DialogInterface dialog, int id) {
////                        // What to do on Cancel
////                    }
////                })
//                .setView(middle);
//
//        builder.create().show();
//    }

    /**
     * class used for Async Task that gets JSON Object from web pages
     */

    //TODO: IMAGES. NEED IMAGES HERE
        //TODO: IDS/VAR HERE

        //https://stackoverflow.com/questions/6407324/how-to-display-image-from-url-on-android
        //https://stackoverflow.com/questions/28023049/display-images-from-url-using-json-android
        //https://stackoverflow.com/questions/36197011/download-and-display-image-from-url-in-json-in-android
        //refer to lab 6?

    private class NewsQuery extends AsyncTask<String, Integer, String> {


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
            JSONObject jObject;

            try {       // Connect to the server:
                URL url = new URL(strings[0]);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inStream = urlConnection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"), 5);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }

                String result = sb.toString();
                jObject = new JSONObject(result);

                JSONArray newsArray = jObject.getJSONArray("articles");
                for (int i = 0; i < newsArray.length(); i++) {
                    JSONObject object = newsArray.getJSONObject(i);
                    JSONObject source = object.getJSONObject("source");
                    String id = source.getString("id");
                    String name = source.getString("name");

                    String author = object.getString("author");
                    String title = object.getString("title");
                    String description = object.getString("description");
                    String jsonURL = object.getString("url");
                    String urlToImage = object.getString("urlToImage");
                    String publishedAt = object.getString("publishedAt");
                    String content = object.getString("content");
                    newsLog.add(new NewsArticleSetterGetter ( name, author, title, description, jsonURL, urlToImage, publishedAt, content));
                    int progress = ((i + 1) * 100 /newsArray.length());
                    publishProgress(progress);

                }

            }

            //TODO: WALLAH
            catch (MalformedURLException e) { ret = "Malformed URL exception";
            } catch (IOException e)

            {
                ret = "IO Exception: WIFI not connected";
            } catch (JSONException e) {
                ret = "JSON exception";
            }
            return ret;
        }

        /**
         * Update progress bar, list, and adapter
         * @param s
         */
        @Override
        protected void onPostExecute(String s) {
            ProgressBar bar = findViewById(R.id.newsBar);
            bar.setVisibility(View.INVISIBLE);

            ListView newsList = findViewById(R.id.newsList);
            newsList.setAdapter(newsAdapter = new MyListAdapter());

        }
        /**
         * update progress
         * @param values
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }
}
