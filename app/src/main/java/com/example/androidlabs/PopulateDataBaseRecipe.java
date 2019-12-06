package com.example.androidlabs;




import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

//import com.google.android.material.snackbar.Snackbar;

/**
 * This class fetches the result from the database into an arraylist
 * and pass it to CustomAdapter class for display
 * It also facilitates deletion of a record
 */
public class PopulateDataBaseRecipe extends Activity {

    /**
     * lv stores the ListView
     */
    private ListView lv;
    /**
     * stores the database result
     */
    private CustomAdapterRecipe customAdapterRecipe;
    /**
     * stores the results fetched from the database
     */
    Cursor results;

    ArrayList<RecipeModelRecipe> objects = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listviewrecipe);
        lv=(ListView) findViewById(R.id.listViewForRecipe);

        MyDatabaseOpenHelperRecipe dbOpener = new MyDatabaseOpenHelperRecipe(this);
        final SQLiteDatabase db = dbOpener.getWritableDatabase();

        String [] columns = {MyDatabaseOpenHelperRecipe.COL_ID, MyDatabaseOpenHelperRecipe.COL_TEXT};

        results = db.query(false, MyDatabaseOpenHelperRecipe.TABLE_NAME, columns,
                null, null, null, null, null, null);
        int textColumnIndex = results.getColumnIndex(MyDatabaseOpenHelperRecipe.COL_TEXT);
        // int receiveOrSendColIndex = results.getColumnIndex(MyDatabaseOpenHelperRecipe.COL_RECEIVEORSEND);
        int idColIndex = results.getColumnIndex(MyDatabaseOpenHelperRecipe.COL_ID);

        results.moveToFirst();
/**
 * Iterating the whole database and loading the result in the arraylist
 */
        while(results.moveToNext())
        {
            String text = results.getString(textColumnIndex);
            //if 0 then send, if 1 then receive
            // int decisionValue = results.getInt(receiveOrSendColIndex);
            long id = results.getLong(idColIndex);

            //add the new Contact to the array list:
            // contactsList.add(new Contact(name, email, id));
            objects.add(new RecipeModelRecipe(text,id));//waiting for new ChatModel

        }


        customAdapterRecipe = new CustomAdapterRecipe(this, objects);
        lv.setAdapter(customAdapterRecipe);
        customAdapterRecipe.notifyDataSetChanged();

/**
 * Listening for record deletion
 */
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                long deleteRecord =objects.get(position).getId();
                results.moveToFirst();

                db.delete(MyDatabaseOpenHelperRecipe.TABLE_NAME, MyDatabaseOpenHelperRecipe.COL_ID+"="+deleteRecord,null );
                customAdapterRecipe.notifyDataSetChanged();//NEW LINE ADDED

                //Initiating Snackbar
                Snackbar sb = Snackbar.make(view, "Deletion Performed", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Go Back?", new View.OnClickListener() {
                            @Override
                            public void onClick(View e) {
                                //Log.e("Menu Example", "Clicked Undo");
                                finish();
                            }
                        });
                sb.show();
              /*  Intent back = new Intent(PopulateDataBaseRecipe.this, MainActivity.class);
                startActivity(back);
*/

            }
        });


    }







}
