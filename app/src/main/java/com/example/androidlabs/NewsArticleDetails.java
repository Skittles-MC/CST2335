package com.example.androidlabs;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**********************************************************************
 Filename: NewsArticleDetails.java
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
 * display detailed information about an article
 * allow user to save to article
 * send saved article information to database
 *
 *
 * * get article information from news main page
 *      * Connect and save to database
 * protected void onCreate(Bundle savedInstanceState)
 */

/**
 * Class used to display the details of the articles searched. Also saves them into database
 */
public class NewsArticleDetails extends AppCompatActivity {


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
                Intent goToMainNewsPage = new Intent(NewsArticleDetails .this, MainNewsPage .class);
                NewsArticleDetails.this.startActivityForResult(goToMainNewsPage, 10);
                break;

            case R.id.go_to_app_help:
                newsHelpDialog();
                break;

            case R.id.go_to_app_favourites:
                Intent goToNewsFavourites = new Intent(NewsArticleDetails .this, NewsArticleHelper .class);
                NewsArticleDetails .this.startActivityForResult(goToNewsFavourites, 10);
                break;



        }
        return true;
    }

    /**
     * Custom help dialog and information method. Pops up a information page with relevant instructions
     */
    public void newsHelpDialog() {
        View middle = getLayoutInflater().inflate(R.layout.news_about_help, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("Sounds Good!", (dialog, id) -> { }).setView(middle);
        builder.create().show();
    }

    /**
     * Method used for getting article information, setting up toolbar, connecting to the database
     * Saving to the database. Launching sites to the web
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_page_info);
        SQLiteDatabase newsDatabase;

        Toolbar newsToolBar = findViewById(R.id.navigation_toolbar);
        setSupportActionBar(newsToolBar);

        Intent mainNewsPageDataHandle = getIntent();
        String newsPageTitle = mainNewsPageDataHandle.getStringExtra("newsArticleTitleMain");
        String newsPageDescription = mainNewsPageDataHandle.getStringExtra( "newsArticleDescription1");
        String newsPageLaunchURL = mainNewsPageDataHandle.getStringExtra("newsURL");

        TextView savedNewsPageTitle = findViewById(R.id.newsArticleTitleMain);
        savedNewsPageTitle.setText(newsPageTitle);
        TextView savedDescription = findViewById(R.id.newsArticleDescription1);
        savedDescription.setText(newsPageDescription);
        Button newsPageURLLauncher = findViewById(R.id.newsGoToPage);

        newsPageURLLauncher.setOnClickListener(clik ->{
            Toast.makeText(NewsArticleDetails.this, "Launching:"+ newsPageLaunchURL , Toast.LENGTH_LONG).show();
            Intent webOpen = new Intent(android.content.Intent.ACTION_VIEW);
            webOpen.setData(Uri.parse(newsPageLaunchURL));
            startActivity(webOpen);

        });


        NewsArticleDB newsDataBase = new NewsArticleDB(this);
        newsDatabase = newsDataBase.getWritableDatabase();
        Button saveButton = findViewById(R.id.newsArticleSave);

        saveButton.setOnClickListener( click ->
        {
            Toast.makeText(NewsArticleDetails.this, "PAGE SAVED (STORED UNDER FAVOURITES)" , Toast.LENGTH_LONG).show();
            ContentValues newRowValues = new ContentValues();
            newRowValues.put(NewsArticleDB.COL_TITLE, newsPageTitle);
            newRowValues.put(NewsArticleDB.COL_DESCRIPTION, newsPageDescription);
            newRowValues.put(NewsArticleDB.COL_URL, newsPageLaunchURL);
            long newId = newsDatabase.insert(NewsArticleDB.TABLE_NAME, null, newRowValues);
        });

    }




}
