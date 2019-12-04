package com.example.androidlabs;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Page to display details of an article and save article
 */


/**
 * display detailed information about an article
 * allow user to save to article
 * send saved article information to database
 *
 *
 * * get article information from news main page
 *      * Connect and save to database
 * protected void onCreate(Bundle savedInstanceState)
 */

public class NewsArticleDetails extends AppCompatActivity {


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
                Intent goToMainNewsPage = new Intent(NewsArticleDetails .this, MainNewsPage .class);
                NewsArticleDetails.this.startActivityForResult(goToMainNewsPage, 10);
                break;

            case R.id.go_to_app_help:
                helpAlert();
                break;

            case R.id.go_to_app_favourites:
                Intent goToNewsFavourites = new Intent(NewsArticleDetails .this, NewsArticleHelper .class);
                NewsArticleDetails .this.startActivityForResult(goToNewsFavourites, 10);
                break;



        }
        return true;
    }


    //TODO: IMPLEMENT CHANGES
    public void helpAlert() {
        View middle = getLayoutInflater().inflate(R.layout.news_about_help, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setPositiveButton("Sounds Good!", (dialog, id) -> {
                }).setView(middle);
        builder.create().show();
    }



//    ImageView urlNewsImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_page_info);
        SQLiteDatabase db;

        Toolbar tBar = findViewById(R.id.navigation_toolbar);
        setSupportActionBar(tBar);


        Intent dataFromPreviousPage = getIntent();
        String title = dataFromPreviousPage.getStringExtra("newsTitle");
        String description = dataFromPreviousPage.getStringExtra( "newsDescription");
     //   String urlToImage = dataFromPreviousPage.getStringExtra("newsImage");
        //getUrlToImage
        String openURL = dataFromPreviousPage.getStringExtra("newsURL");

        TextView savedTitle = findViewById(R.id.newsTitle);
        savedTitle.setText(title);


        TextView savedDescription = findViewById(R.id.newsDescription);
        savedDescription.setText(description);


//        ImageView savedImage = findViewById(R.id.newsImage);
////        savedImage.setImageBitmap(urlToImage);

        Button gotoURL = findViewById(R.id.newsGoToPage);


        gotoURL.setOnClickListener(clik ->{
            Toast.makeText(NewsArticleDetails.this, "Launching:"+ openURL , Toast.LENGTH_LONG).show();
            Intent webOpen = new Intent(android.content.Intent.ACTION_VIEW);
            webOpen.setData(Uri.parse(openURL));
            startActivity(webOpen);

        });



        NewsArticleDB dbOpener = new NewsArticleDB(this);
        db = dbOpener.getWritableDatabase();
        String [] columns = {NewsArticleDB.COL_ID,
                NewsArticleDB.COL_NAME,
                NewsArticleDB.COL_AUTHOR, NewsArticleDB.COL_TITLE,
                NewsArticleDB.COL_DESCRIPTION,  NewsArticleDB.COL_URL,
                NewsArticleDB.COL_URL_TO_IMAGE,
                NewsArticleDB.COL_PUBLISHED_AT,
                NewsArticleDB.COL_CONTENT
        };
        Cursor results = db.query(false, NewsArticleDB.TABLE_NAME, columns, null, null, null, null, null, null);
        int idColIndex = results.getColumnIndex(NewsArticleDB.COL_ID);
        int nameColIndex = results.getColumnIndex(NewsArticleDB.COL_NAME);
        int authorColIndex = results.getColumnIndex(NewsArticleDB.COL_AUTHOR);
        int titleColIndex = results.getColumnIndex(NewsArticleDB.COL_TITLE);
        int descriptionColIndex = results.getColumnIndex(NewsArticleDB.COL_DESCRIPTION);
        int urlColIndex = results.getColumnIndex(NewsArticleDB.COL_URL);
        int urlToImageColIndex = results.getColumnIndex(NewsArticleDB.COL_URL_TO_IMAGE);
        int publishedAtColIndex = results.getColumnIndex(NewsArticleDB.COL_PUBLISHED_AT);
        int contentColIndex = results.getColumnIndex(NewsArticleDB.COL_CONTENT);


        Button saveButton = findViewById(R.id.newsSave);

        saveButton.setOnClickListener( click ->
        {
            Toast.makeText(NewsArticleDetails.this, "PAGE SAVED (STORED UNDER FAVOURITES)" , Toast.LENGTH_LONG).show();
            ContentValues newRowValues = new ContentValues();
            newRowValues.put(NewsArticleDB.COL_TITLE, title);
            newRowValues.put(NewsArticleDB.COL_DESCRIPTION, description);
            newRowValues.put(NewsArticleDB.COL_URL, openURL);
            long newId = db.insert(NewsArticleDB.TABLE_NAME, null, newRowValues);
        });

    }




}
