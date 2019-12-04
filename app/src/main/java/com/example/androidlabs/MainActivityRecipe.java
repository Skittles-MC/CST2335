package com.example.androidlabs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

/**
 * Represents the landing page of Recipe
 * Takes a string for Recipe and sends it to the next page for Search
 */
public class MainActivityRecipe extends AppCompatActivity {

    /**
     *  Represents the EditText of the corresponding layout file
     */
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainrecipe);

        Toolbar tBar = (Toolbar) findViewById(R.id.navigation_toolbar);
        setSupportActionBar(tBar);



        /**
         * The String fileName specifies the name of the file,
         * mode is the security permissions – use Context.MODE_PRIVATE
         */
        SharedPreferences prefs =getSharedPreferences("fileName", Context.MODE_PRIVATE);

        editText=findViewById(R.id.searchOptionMainPage);

        if(prefs!=null){

            String lastSearchItem=prefs.getString("searchItem","No Last Search Item");
            editText.setText(lastSearchItem);
        }




    }//end of oncreate

    /**
     * Gets the search item
     * @return void.
     */
    public void searchItem(View view){

        String search=editText.getText().toString();
        /**
         * Starts a new Intent and sends the search string to the new Activity
         */
        Intent it = new Intent(MainActivityRecipe.this, ListViewOfRecipe.class);
        it.putExtra("searchRecipe",search);
        startActivity(it);


    }

    /**
     *
     * @param menu
     * @return true or false
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar, menu);

        return true;
    }

    /**
     *
     * @param item
     * @return true or false
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
                Intent goToRecipe = new Intent(MainActivityRecipe.this, MainActivityRecipe.class);
                 startActivity(goToRecipe);
                break;
            case R.id.news_main_page:
                /*Intent goToMainNewsPage = new Intent(MainNewsPage .this, MainNewsPage .class);
                MainNewsPage.this.startActivityForResult(goToMainNewsPage, 10);*/
                break;
            case R.id.go_to_app_help:
                Intent helperMenuIntent = new Intent(MainActivityRecipe.this, HelperMenuRecipe.class);
                startActivity(helperMenuIntent);

                break;
            case R.id.go_to_app_favourites:
                Intent intentFromMenu = new Intent(MainActivityRecipe.this, PopulateDataBaseRecipe.class);
                startActivity(intentFromMenu);
                break;

        }
        return true;
    }


    /**
     * Checks for the last searched recipe and saves it in shared preference
     */
    public void onStop() {


        super.onStop();
        SharedPreferences prefs =getSharedPreferences("fileName", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=prefs.edit();
        EditText editText=findViewById(R.id.searchOptionMainPage);
        if(editText!=null){
            editor.putString("searchItem", editText.getText().toString());

        }
        editor.commit();


    }



}

