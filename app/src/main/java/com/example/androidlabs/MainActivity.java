package com.example.androidlabs;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Button button;
    private TextView email;
    private String userEmailString;
    private static final String ACTIVITY_NAME="PROFILE ACTIVITY";
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.log_in_butt);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPause();
            }
        });
    }
    @Override
    protected void onPause() {
        super.onPause();
        Intent intent = new Intent(this, ProfileActivity.class);
        email = findViewById(R.id.email);

        userEmailString = email.getText().toString();
        sharedPreferences = getSharedPreferences("email", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", userEmailString);
        editor.commit();
        intent.putExtra("EMAIL", userEmailString);
        startActivity(intent);
        Log.e(ACTIVITY_NAME, "In function : onPause " );
    }
}
