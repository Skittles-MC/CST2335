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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**********************************************************************
 Filename: MainNewsPage.java
 Version: 1.5
 Authors:	Martin Choy
 Student No:	040835431
 Course Name/Number:	CST2335 Mobile Graphical Interface Programming
 Lab Sect:	013
 Assignment #: Final Project - 1
 Assignment name:  Final_GroupProject F19
 Due Date: Dec 4th 2019 , 11:59PM midnight
 Submission Date: Dec 4th 2019
 Professor: Shahzeb Khowaja
 *********************************************************************/

/**
 * The main News page which handles the landing page, searching and saving
 */
public class MainNewsPage extends AppCompatActivity {

    MyListAdapter newsAdapter;
    ArrayList<NewsArticleSetterGetter> newsLog = new ArrayList();
    ProgressBar mainToolBar;
    String queryURL = "https://newsapi.org/v2/everything?apiKey=acd83f1c540645bf94509063a0219b64&q=";
    String newsURL;

    protected SharedPreferences newsPrefs;
    protected EditText editTextSearch;
    protected SharedPreferences.Editor edit;


    /**
     * onCreate - Main purpose of this method is a staging area to deploy
     *  key features of the application. Also leads to other layouts/pages
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_main_page);

        ListView mainNewsListViewer = findViewById(R.id.newsListView);
        mainNewsListViewer.setAdapter(newsAdapter = new MyListAdapter());
        mainToolBar = findViewById(R.id.newsProgessBar);
        mainToolBar.setVisibility(View.INVISIBLE);
        mainToolBar.setProgress(30);
        editTextSearch = findViewById(R.id.newsSearchText);
        Toolbar mainToolBar = (Toolbar) findViewById(R.id.navigation_toolbar);
        setSupportActionBar(mainToolBar);

        mainNewsListViewer.setOnItemClickListener((lv, vw, pos, id) -> {

            Intent goToPageNews2 = new Intent(MainNewsPage.this, NewsArticleDetails.class);
            goToPageNews2.putExtra("newsArticleTitleMain", newsLog.get(pos).getTitle());
            goToPageNews2.putExtra("newsArticleDescription1", newsLog.get(pos).getDescription());
            goToPageNews2.putExtra("newsURL", newsLog.get(pos).getUrl());
            startActivity(goToPageNews2);
        });

        Button searchButton = findViewById(R.id.newsButtonSearch);
        searchButton.setOnClickListener(clik ->{
            newsLog.clear();
            String searchText = editTextSearch.getText().toString();
            newsURL = queryURL + searchText;
            NewsQuery  newsQuery = new NewsQuery();
            this.mainToolBar.setVisibility(View.VISIBLE);
            newsQuery.execute(newsURL);
        });
        newsPrefs = getSharedPreferences("NewsReserveName", MODE_PRIVATE);
        edit = newsPrefs.edit();
        String previous = newsPrefs.getString("NewsReserveName", "");
        editTextSearch.setText(previous);
    }


    /**
     * Method used to save the shared_preferences values
     */
    @Override
    protected void onPause(){
        super.onPause();
        edit.putString("NewsReserveName", editTextSearch.getText().toString());
        edit.commit();
    }

    /**
     * Method uses inflater to create toolbar
     * @param menu
     * @return true BOOLEAN statement
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar, menu);
        return true;
    }

    /**
     *  Method used for displaying and handling toolbar choices on top of page
     *  Used to navigate to everyone's work
     * @param item
     * @return true BOOLEAN statement
     */
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
                newsHelpDialog();
                break;
            case R.id.go_to_app_favourites:
                Intent goToNewsFavourites = new Intent(MainNewsPage .this, NewsArticleHelper .class);
                MainNewsPage .this.startActivityForResult(goToNewsFavourites, 10);
                break;
        }
        return true;
    }


    /**
     * Custom help dialog and information method. Pops up a information page with relevant instructions
     */
    public void newsHelpDialog() {
        View centerOfPage = getLayoutInflater().inflate(R.layout.news_about_help, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("Sounds Good!", (dialog, id) -> {
                }).setView(centerOfPage);
        builder.create().show();
    }


    /**
     * Method used for getters and setters. Customized to modify how listView looks
     */
    private class MyListAdapter extends BaseAdapter {

        //Getter
        @Override
        public int getCount() {
            return newsLog.size();
        }

        //Getter
        @Override
        public NewsArticleSetterGetter getItem(int position) {
            return newsLog.get(position);
        }
        //Getter
        @Override
        public long getItemId(int position) {
            return position;
        }

        /**
         * Method used to get Title information
         * @param position
         * @param convertView
         * @param parent
         * @return thisRow
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View thisRow;
            NewsArticleSetterGetter rowNews = getItem(position);

            thisRow = getLayoutInflater().inflate(R.layout.news_view_row, null);
            TextView itemTitle = thisRow.findViewById(R.id.newsArticleTitleMain);
            itemTitle.setText(rowNews.getTitle() + " ");
            return thisRow;
        }
    }

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

        /**
         * Method used to handle and connect online webpages and JSON objects/setting and getting
         * @param strings
         * @return
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
            catch (IOException e)
            {
                ret = "IO Exception: no INTERNET CONNECTION";
            }
            catch (JSONException e)
            {
                ret = "JSON exception/ERROR";
            }
            return ret;
        }

        /**
         * Method used to update the progress bar after searching for an article
         * @param s
         */
        @Override
        protected void onPostExecute(String s) {
            ProgressBar newsLoadingBar = findViewById(R.id.newsProgessBar);
            newsLoadingBar.setVisibility(View.INVISIBLE);
            ListView newsMainListView = findViewById(R.id.newsListView);
            newsMainListView.setAdapter(newsAdapter = new MyListAdapter());
        }
        /**
         * Updates current progress
         * @param values
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }
}
