package com.example.androidlabs;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class ECSfavorite  extends AppCompatActivity {
    private ArrayList<ECStations> favStations = new ArrayList<>();
    private static DatabaseHelper dbOpener;
    private static SQLiteDatabase db;
    private static BaseAdapter myAdapter;
    private Intent delData= new Intent();
    private int numOfDeleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecs_favorite);

        Button back = findViewById(R.id.back_button);
        TextView emptyView = findViewById(R.id.favorite_list);
        emptyView.setVisibility(View.GONE);
        dbOpener = new DatabaseHelper(this);

        db = dbOpener.getWritableDatabase();




        //set click listener for back to main button
        back.setOnClickListener(view->{
            setResult(6,delData);
            finish();
        });


        //get data from db to be saved locally
        Cursor cursor= db.query(false,DatabaseHelper.TABLE_NAME,
                new String[]{
                        DatabaseHelper.COL_TITLE,
                        DatabaseHelper.COL_LATITUDE,
                        DatabaseHelper.COL_LONGITUDE,
                        DatabaseHelper.COL_PHONENO,
                        DatabaseHelper.COL_ADDRESS},
                null, null, null, null, null, null);

        //save data from db to local variable
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            String title = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TITLE));
            String latitude = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_LATITUDE));
            String longitude = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_LONGITUDE));
            String phoneNo = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_PHONENO));
            String address = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_ADDRESS));
            ECStations stations = new ECStations(title, latitude, longitude, phoneNo, address);
            stations.setFav(true);
            cursor.moveToNext();
            favStations.add(stations);
        }
        cursor.close();

        // set up list view
        ListView favList = findViewById(R.id.listView_favorite);

        myAdapter = new MyListAdapter();
        favList.setAdapter(myAdapter);

        if(myAdapter.getCount() == 0){
            emptyView.setVisibility(View.VISIBLE);
        }


        favList.setOnItemClickListener((parent, view, position, id) -> {
            Intent detail = new Intent(ECSfavorite.this,ECSdetail.class);
            detail.putExtra("title", favStations.get(position).getTitle());
            detail.putExtra("latitude", favStations.get(position).getLatitude());
            detail.putExtra("longitude", favStations.get(position).getLongitude());
            detail.putExtra("phoneNo", favStations.get(position).getPhoneNo());
            detail.putExtra("address", favStations.get(position).getAddress());
            detail.putExtra("fav", favStations.get(position).isFav());
            startActivityForResult(detail, 5);
        });

    }

    /**
     * if an list item is deleted, do the following statements
     * @param requestCode - from this page
     * @param resultCode - from previous page
     * @param data - data need processing
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            // from detail page, to delete one station from fav
            if (requestCode == 5 && resultCode == 3) {
                // if the data is needed to be deleted from favorite stations
                if (data.getBooleanExtra("deleteFromFav", false)) {
                    String latitude = data.getStringExtra("latitude");
                    String longitude = data.getStringExtra("longitude");

                    Cursor cursor = db.query(true, DatabaseHelper.TABLE_NAME,
                            new String[]{DatabaseHelper.COL_ID},
                            DatabaseHelper.COL_LATITUDE + " = ? AND " +
                                    DatabaseHelper.COL_LONGITUDE + " = ? "
                            , new String[]{latitude, longitude}, null, null, null, null);
                    cursor.moveToFirst();
                    if (cursor.getCount() > 0) {
                        int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_ID));
                        Log.i("id to be deleted is" + id, "eccsfmain");
                        db.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.COL_ID + "=?",
                                new String[]{Long.toString(id)});
                    }
                    cursor.close();

                    for(ECStations station: favStations){
                        if(station.getLongitude().equals(longitude)){
                            favStations.remove(station);
                            Toast.makeText(this, R.string.ecs_Deleted,
                                    Toast.LENGTH_SHORT).show();
                            myAdapter.notifyDataSetChanged();

                            delData.putExtra("numOfDel", numOfDeleted);
                            delData.putExtra(numOfDeleted+"", latitude);
                            numOfDeleted++;
                            break;
                        }
                    }


                    View emptyView = findViewById(R.id.favorite_list);
                    if(myAdapter.getCount() == 0){

                        emptyView.setVisibility(View.VISIBLE);
                    }else{
                        emptyView.setVisibility(View.GONE);
                    }
                }
            }


        }
    }

    /**
     * Defines the list adapter for the stations list
     */
    class MyListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return favStations.size();
        }

        @Override
        public Object getItem(int i) {
            return favStations.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View oldView, ViewGroup parent) {

            oldView = getLayoutInflater().inflate(R.layout.list_view_ecs, parent, false);

            TextView title = oldView.findViewById(R.id.row_title);
            TextView address = oldView.findViewById(R.id.row_address);
            ImageView saved = oldView.findViewById(R.id.row_saved);
//            saved.setVisibility(View.GONE);

            title.setText(favStations.get(position).getTitle());
            address.setText(favStations.get(position).getAddress());
            return oldView;
        }
    }
}


