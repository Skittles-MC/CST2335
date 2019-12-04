package com.example.androidlabs;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
/**
 *
 *  public boolean onCreateOptionsMenu(Menu menu) {
 *  inflator for displaying tool bars
 */

/**********************************************************************
 Filename: NewsArticleHelper.java
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
 * Class used to view saved articles from the database
 */
public class NewsArticleHelper extends AppCompatActivity {
    protected SQLiteDatabase db = null;
    MyListAdapter newsAdapter;
    ArrayList <NewsArticleSetterGetter> newsLog = new ArrayList();

    /**
     * Method used to retrieve and display toolbar
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_db_save);
        Toolbar tBar = (Toolbar) findViewById(R.id.navigation_toolbar);
        setSupportActionBar(tBar);

        NewsArticleDB opener = new NewsArticleDB(this);
        db =  opener.getWritableDatabase();
        ListView newsList = findViewById(R.id.newsSavedList);
        newsList.setAdapter(newsAdapter = new MyListAdapter());

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
        int authorColIndex = results.getColumnIndex(NewsArticleDB.COL_AUTHOR);
        int titleColIndex = results.getColumnIndex(NewsArticleDB.COL_TITLE);

        while(results.moveToNext())
        {
            long _id = results.getLong(idColIndex);
            String _title = results.getString(titleColIndex);
            String _author = results.getString(authorColIndex);
            newsLog.add(new NewsArticleSetterGetter(_id, _title, _author));

        }
        newsList.setOnItemClickListener((news1, view2, position, newsID) -> {
            Snackbar newsSnackBar = Snackbar.make(view2," ", Snackbar.LENGTH_LONG).setAction("DELETE?", a -> delete(position, newsLog.get(position).getId()));
            newsSnackBar.show();
        });
    }

    /**
     * Method used to remove elements from the database depending on the user condition/choice
     * @param position
     * @param id
     */
    private void delete (int position, long id){
        db.delete(NewsArticleDB.TABLE_NAME, NewsArticleDB.COL_ID + "=?", new String[] {Long.toString(id)});
        newsLog.remove(position);
        newsAdapter.notifyDataSetChanged();
    }

        private void view (int position, long id){

        Intent dataFromPreviousPage = getIntent();


        String openURL = dataFromPreviousPage.getStringExtra("newsURL");

        Toast.makeText(NewsArticleHelper.this, "Launching:"+ openURL , Toast.LENGTH_LONG).show();
        Intent webOpen = new Intent(android.content.Intent.ACTION_VIEW);
        webOpen.setData(Uri.parse(openURL));
        startActivity(webOpen);
    }

    /**
     *  Method used for displaying and handling toolbar choices on top of page
     *  Used to navigate to everyone's work
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
                //         Intent goToCurrencyExchange = new Intent(NewsSaved.this, CurrencyExchangeMain.class);
                //         NewsSaved.this.startActivityForResult(goToCurrencyExchange, 10);
                break;
            case R.id.car_charger_main_page:
         //       Intent goToCarChargerFinder = new Intent(NewsSaved.this, CarChargerFinder.class);
          //      NewsSaved.this.startActivityForResult(goToCarChargerFinder, 10);
                break;
            case R.id.recipe_main_page:
                //      Intent goToNewsPage = new Intent(NewsSaved.this, RecipePage.class);
                //      NewsSaved.this.startActivityForResult(goToNewsPage, 10);
                break;
            case R.id.news_main_page:
                Intent goToMainNewsPage = new Intent(NewsArticleHelper .this, MainNewsPage .class);
                NewsArticleHelper.this.startActivityForResult(goToMainNewsPage, 10);
                break;
            case R.id.go_to_app_favourites:
                Intent goToNewsFavourites = new Intent(NewsArticleHelper .this, NewsArticleHelper .class);
                NewsArticleHelper .this.startActivityForResult(goToNewsFavourites, 10);
                break;
            case R.id.go_to_app_help:
                newsHelpDialog();
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
     * Class used to display and save articles to the database
     */
    private class MyListAdapter extends BaseAdapter {

        //GETTER
        @Override
        public int getCount() {
            return newsLog.size();
        }

        //GETTER
        @Override
        public NewsArticleSetterGetter getItem(int position) {
            return newsLog.get(position);
        }


        //GETTER
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
            View thisRow = convertView;
            NewsArticleSetterGetter rowNews = getItem(position);

            thisRow = getLayoutInflater().inflate(R.layout.news_view_row, null);
            TextView itemTitle = thisRow.findViewById(R.id.newsArticleTitleMain);

            itemTitle.setText(rowNews.getTitle() + " ");
            return thisRow;
        }
    }
}
