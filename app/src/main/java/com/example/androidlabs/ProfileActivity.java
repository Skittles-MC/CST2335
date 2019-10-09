package com.example.androidlabs;



import android.content.Intent;

import android.graphics.Bitmap;

import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {
    private ImageView imageButton;
    private Button chatRoomButton;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private TextView userEmail;
    private static final String ACTIVITY_NAME="PROFILE ACTIVITY";


    @Override
    protected void onResume() {
        super.onResume();
        Log.e(ACTIVITY_NAME, "In function : onResume" );
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.e(ACTIVITY_NAME, "In function : onPause " );
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.e(ACTIVITY_NAME, "In function : onStop" );
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(ACTIVITY_NAME, "In function : onDestroy");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        imageButton = findViewById(R.id.imageButton);
        chatRoomButton = findViewById(R.id.goToChat);
        userEmail = findViewById(R.id.userEmail);

        Intent intent = getIntent();
        String email = intent.getStringExtra("EMAIL");
        userEmail.setText(email);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v ){
                dispatchTakePictureIntent();
            }
        });

        chatRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToChatPage = new Intent(ProfileActivity.this, ChatRoomActivity.class);
                startActivity(goToChatPage);
            }
        });
        Log.e(ACTIVITY_NAME, "In function : onCreate ");
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ImageButton mimagebutton= findViewById(R.id.imageButton);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mimagebutton.setImageBitmap(imageBitmap);
        }

        Log.e(ACTIVITY_NAME, "In function : onActivityResult " );

    }

}

