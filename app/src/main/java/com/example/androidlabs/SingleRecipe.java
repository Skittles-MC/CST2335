package com.example.androidlabs;


import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * The class displays the image, weblink for the selected recipe
 */

public class SingleRecipe extends AppCompatActivity {

    /**
     * stores the URL of the image
     */
    String imageURL;
    /**
     * stores the title of the Webpage
     */
    String title;
    /**
     * stores the sourceURL
     */
    String sourceURL;
    /**
     * ProgressBar object
     */
    ProgressBar progressBar = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.singlerecipe);
        /**
         * initializing the class variables
         */
        Toolbar tBar = (Toolbar) findViewById(R.id.navigation_toolbar);
        setSupportActionBar(tBar);
        progressBar = new ProgressBar(this);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);


        Intent intent = getIntent();
        int position = intent.getIntExtra("position",0);
        //TextView test=(TextView)findViewById(R.id.position);

        //test.setText(String.valueOf(position));
        HashMap<String, String> hashMap = (HashMap<String, String>) intent.getSerializableExtra("map");

        //  Object object = hashMap.get(position);

        title = hashMap.get("title");

        TextView titleWidget=(TextView)findViewById(R.id.titleWebPage);
        title=title.toUpperCase();
        titleWidget.setText("RECIPE: \n"+title);


        sourceURL = hashMap.get("sourceURL");
        TextView sourceWidget=(TextView)findViewById(R.id.sourceURLSingleRecipe);
        sourceWidget.setText("LINK: " +sourceURL);



        imageURL = hashMap.get("imageURL");
        //TextView imageURLWidget=(TextView)findViewById(R.id.imageSourceSingleRecipe);
        //  imageURLWidget.setText(imageURL);
        RetrieveImage retrieveImage = new RetrieveImage();
        retrieveImage.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }//end of oncreate


    public void visitPage(View view) {

        AlertDialog alertDialog = new AlertDialog.Builder(SingleRecipe.this).create();
        alertDialog.setTitle("Alert");


        alertDialog.setMessage("You are leaving the App");

        alertDialog.show() ;
        finish();


        /*alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        */

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(sourceURL));
        startActivity(browserIntent);

    }



    class RetrieveImage extends AsyncTask<String,Integer,String> {

        private Bitmap image;
        @Override
        protected String doInBackground(String... strings) {

            try {
                // String urlString = "http://openweathermap.org/img/w/" + icon;


                image=null;
                URL urlForImage = new URL("https://upload.wikimedia.org/wikipedia/commons/9/9d/Rosemary_Chicken_%28263792483%29.jpeg");
                HttpURLConnection connectionForImage = (HttpURLConnection) urlForImage.openConnection();
                publishProgress(25);
                try {
                    Thread.sleep(1500);
                } catch (Exception e) {
                    Log.i("ASYNC FROZE", "Frozen");
                }
                publishProgress(75);
                connectionForImage.connect();
                int responseCode = connectionForImage.getResponseCode();
                if (responseCode==200) {
                    image = BitmapFactory.decodeStream(connectionForImage.getInputStream());

                }
          /* FileOutputStream outputStream = openFileOutput("ImageOne", Context.MODE_PRIVATE);
           image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
            outputStream.flush();
            outputStream.close();*/
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

            return null;




        }//end of doinBackground

        @Override                   //Type 3
        protected void onPostExecute(String sentFromDoInBackground) {
            super.onPostExecute(sentFromDoInBackground);

            ImageView imageWidget=(ImageView) findViewById(R.id.imageOfRecipe);
            imageWidget.setImageBitmap(image);
            progressBar.setVisibility(View.INVISIBLE);


        }//end of OnpostExecute

        protected void onProgressUpdate(Integer... values) {
            //super.onProgressUpdate(values);
            //  try{Thread.sleep(2000);} catch(Exception e){Log.i("ASYNC FROZE", "Frozen"); }

            progressBar.setProgress(values[0]);
            //try{Thread.sleep(3000);} catch(Exception e){Log.i("ASYNC FROZE", "Frozen"); }

        }
    }//end of async task

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menurecipe, menu);

        // it.putExtra("searchRecipe",search);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {


        switch(item.getItemId())
        {
            //what to do when the menu item is selected:
            case R.id.favorite:
                //Toast.makeText(this, "This is the initial message", Toast.LENGTH_LONG).show();
                /*Intent intentFromMenu = new Intent(SingleRecipe.this, PopulateDataBaseRecipe.class);
                startActivity(intentFromMenu);
                break;*/
                MyDatabaseOpenHelperRecipe dbOpener = new MyDatabaseOpenHelperRecipe(this);
                SQLiteDatabase db = dbOpener.getWritableDatabase();

                ContentValues newRowValues = new ContentValues();
                newRowValues.put(MyDatabaseOpenHelperRecipe.COL_TEXT, title);
                long newId = db.insert(MyDatabaseOpenHelperRecipe.TABLE_NAME, null, newRowValues);

                Toast.makeText(this, "Saved in Favorite List", Toast.LENGTH_LONG).show();
                break;
            case R.id.help:

                Intent helperMenuIntent = new Intent(SingleRecipe.this, HelperMenuRecipe.class);
                startActivity(helperMenuIntent);
                break;


        }
        return true;
    }


}
