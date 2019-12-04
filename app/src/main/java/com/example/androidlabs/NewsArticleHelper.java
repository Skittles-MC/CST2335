package com.example.androidlabs;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
/**
 *
 * Class info:
 * Page that show saved articles from database
 *
 * obtain data from database and display
 * protected void onCreate(Bundle savedInstanceState) {
 *
 * delete instances from database
 *  private void delete (int position, long id){
 *
 *
 *  public boolean onCreateOptionsMenu(Menu menu) {
 *  inflator for displaying tool bars
 */

public class NewsArticleHelper extends AppCompatActivity {
    protected SQLiteDatabase db = null;
    MyListAdapter newsAdapter;
    ArrayList <NewsArticleSetterGetter> newsLog = new ArrayList();

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

//        TextView titleText =findViewById(R.id.newsSaved1);
//        TextView descriptionText = findViewById(R.id.newsSaved2);

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




        while(results.moveToNext())
        {
            long _id = results.getLong(idColIndex);
            String _title = results.getString(titleColIndex);
            String _author = results.getString(authorColIndex);
//            Bitmap _urlImage = NewsArticleDB.
//
//            _urlImage = results.getString(urlToImageColIndex);



            newsLog.add(new NewsArticleSetterGetter(_id, _title, _author));

        }



        //TODO: ADD SNACKBAR TO MAINPAGE AND INSTEAD HAVE A PROPER DEALTE FUNCTION/REMOVE FUNCTION and as well as view function
        //https://stackoverflow.com/questions/41224019/can-we-perform-2-different-actions-in-snack-bar-at-a-time-in-android
        newsList.setOnItemClickListener((lv, vw, pos, id) -> {
            Snackbar sb = Snackbar.make(vw, "Delete " + newsLog.get(pos).getTitle(), Snackbar.LENGTH_LONG).setAction("Confirm", a -> delete(pos, newsLog.get(pos).getId()));
//                    Snackbar snackbar = Snackbar
//                            .make(coordinatorLayout, "Try again!", Snackbar.LENGTH_LONG)
//                            .setAction("RETRY", new View.OnClickListener() {

            sb.show();

        });



    }

    private void delete (int position, long id){
        db.delete(NewsArticleDB.TABLE_NAME, NewsArticleDB.COL_ID + "=?", new String[] {Long.toString(id)});
        //db.view
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

        // db.delete(NewsArticleDB.TABLE_NAME, NewsArticleDB.COL_ID + "=?", new String[] {Long.toString(id)});
        //db.view
      //  newsLog.remove(position);
      //  newsAdapter.notifyDataSetChanged();
    }


//    Button gotoURL = findViewById(R.id.newsGoToPage);
//
//
//        gotoURL.setOnClickListener(clik ->{
//        Toast.makeText(NewsArticleDetails.this, "Launching:"+ openURL , Toast.LENGTH_LONG).show();
//        Intent webOpen = new Intent(android.content.Intent.ACTION_VIEW);
//        webOpen.setData(Uri.parse(openURL));
//        startActivity(webOpen);
//
//    });

    //TODO: move/format

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
                helpAlert();
                break;
        }
        return true;
    }




    public void helpAlert() {
        View middle = getLayoutInflater().inflate(R.layout.news_about_help, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setPositiveButton("Sounds Good!", (dialog, id) -> {
                    // What to do on Accept
                }).setView(middle);
        builder.create().show();
    }

    /**
     * List Adapter for displaying saved articles
     */
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
            //itemURL.setText("URL: " + rowNews.getUrl() + " ");

            return thisRow;
        }
    }

}
