package com.example.androidlabs;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton cars = findViewById(R.id.charger);
        ImageButton newsArticleAPI = findViewById(R.id.news);
        ImageButton foodRecipeAPI = findViewById(R.id.recipe);
        ImageButton conversionAPI = findViewById(R.id.conversionAPI);

        cars.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ECSActivity.class));
        });


        newsArticleAPI.setOnClickListener(v -> {
           // @Override
           // public void onClick(View view) {
            //    Intent goToMainNewsPage = new Intent(MainActivity.this, MainNewsPage.class);
           //     startActivity(goToMainNewsPage);
           // }
                   startActivity(new Intent(MainActivity.this, MainNewsPage.class));
        });


        foodRecipeAPI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent goToFoodPage = new Intent(MainActivity.this, ChatRoomActivity.class);
//                startActivity(goToFoodPage);
            }
        });


        conversionAPI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent goToConversionPage = new Intent(MainActivity.this, ChatRoomActivity.class);
//                startActivity(goToConversionPage);
            }
        });

    }
}