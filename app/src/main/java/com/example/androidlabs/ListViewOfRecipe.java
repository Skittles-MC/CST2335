package com.example.androidlabs;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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
import java.util.HashMap;

/**
 * Matches the string with the webpage and saves the items
 * in an ArrayList
 *
 */

public class ListViewOfRecipe extends AppCompatActivity {

    /**
     * stores the recipe sent from the previous page
     */
    private String text;
    /**
     * A ListView object
     */
    private ListView lv;

    /**
     * Stores the matches items from the webpage
     */

    public ArrayList<HashMap<String, String>> recipeDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.listviewrecipe);
        /**
         * Initializing the ArrayList
         */

        Toolbar tBar = (Toolbar) findViewById(R.id.navigation_toolbar);
        setSupportActionBar(tBar);
        recipeDetails=new ArrayList<>();
        Intent intent = getIntent();
        text = intent.getStringExtra("searchRecipe");
        lv=(ListView) findViewById(R.id.listViewForRecipe);
        SearchRecipe searchRecipe = new SearchRecipe();
        /**
         * Calling AsyncTask
         */
        searchRecipe.execute();




    }//End of ON-CREATE


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /**
         * Inflate the menu items for use in the action bar
         */

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {


        switch(item.getItemId())
        {
            /**
             *what to do when the menu item is selected:
             */

            case R.id.go_to_app_favourites:
                Intent intentFromMenu = new Intent(ListViewOfRecipe.this, PopulateDataBaseRecipe.class);
                startActivity(intentFromMenu);
                break;
            case R.id.go_to_app_help:

                Intent helperMenuIntent = new Intent(ListViewOfRecipe.this, HelperMenuRecipe.class);
                startActivity(helperMenuIntent);
                break;
        }
        return true;
    }

    /**
     * Using AsyncTask to fetch strings from the webpage
     */
    private class SearchRecipe extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {

            /**
             * Setting the URL
             */
            String jsonURL = "http://torunski.ca/FinalProjectChickenBreast.json";

            try {

                URL urlForJson = new URL(jsonURL);
                HttpURLConnection urlConnectionForJson = (HttpURLConnection) urlForJson.openConnection();
                InputStream inStreamForJson = urlConnectionForJson.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStreamForJson, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                String result = sb.toString();//result has the full json thing.
                JSONObject jsonObject = new JSONObject(result);//line 1
                JSONArray recipes =jsonObject.getJSONArray("recipes");

                /**
                 * Iterating the JSON ARRAY
                 */
                for(int i=0; i<recipes.length();i++){

                    JSONObject c = recipes.getJSONObject(i);
                    /**
                     * stores the JSON title
                     */
                    String title = c.getString("title");
                    title=title.toLowerCase();
                    text=text.toLowerCase();
                    /**
                     * checking if the title matches with the search string
                     */
                    if(title.contains(text)){
                        /**
                         * store the imagerURL
                         */
                        String image=c.getString("image_url");
                        /**
                         * store the sourceURL
                         */
                        String sourceURL=c.getString("source_url");
                        /**
                         * The hashmap will store the variables in key, value format
                         */
                        HashMap<String, String>recipe = new HashMap<>();
                        /**
                         * stores the variables
                         */
                        recipe.put("title",title);
                        recipe.put("imageURL",image);
                        recipe.put("sourceURL",sourceURL);
                        recipeDetails.add(recipe);

                    }
                    /**
                     * If no strings matched
                     * store null in the HashMap
                     */
                    else{
                        HashMap<String, String>recipe = new HashMap<>();
                        recipe.put("title",null);
                    }
                }
            }//emd of try
            catch (MalformedURLException mfe) {
                mfe.printStackTrace();//ret = "Malformed URL exception";
            } catch (IOException ioe) {
                ioe.printStackTrace();// ret = "IO Exception. Is the Wifi connected?";
            }  catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }//end of doinbackground

        @Override                   //Type 3
        protected void onPostExecute(String sentFromDoInBackground) {
            super.onPostExecute(sentFromDoInBackground);
            /**
             * If no matching item found....
             */
            if(recipeDetails.isEmpty()){

                Toast.makeText(getApplicationContext(),"No Item Found",Toast.LENGTH_LONG).show();
            }
            /**
             * If matched items found then populate the list
             */
            else {
                ListAdapter adapter = new SimpleAdapter(ListViewOfRecipe.this, recipeDetails, R.layout.listviewpopulaterecipe,
                        new String[]{"title"}, new int[]{R.id.title});
                lv.setAdapter(adapter);
                /**
                 * If one of the item selected then display that
                 */
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Intent clickRecipeIntent = new Intent(ListViewOfRecipe.this, SingleRecipe.class);
                        clickRecipeIntent.putExtra("position", position);
                        clickRecipeIntent.putExtra("map", recipeDetails.get(position));
                        startActivity(clickRecipeIntent);
                    }
                });
            }


        }//end of onPostExecute

    }//End of SearchRecipe


}
