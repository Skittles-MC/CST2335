package com.example.androidlabs;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

/**********************************************************************
 Filename: MainActivity.java
 Version: 1.5
 Authors:	Martin Choy
            Mark Deley
            Rozil Maranan
            Muhammadaraf Mir
 Course Name/Number:	CST2335 Mobile Graphical Interface Programming
 Lab Sect:	013
 Assignment #: Final Project - 1
 Assignment name:  Final_GroupProject F19
 Due Date: Dec 4th 2019 , 11:59PM midnight
 Submission Date: Dec 4th 2019
 Professor: Shahzeb Khowaja
 *********************************************************************/
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
                   startActivity(new Intent(MainActivity.this, MainNewsPage.class));
        });


        foodRecipeAPI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });


        conversionAPI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }
}