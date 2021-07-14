package com.example.museum.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.museum.R;
import com.parse.ParseUser;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TextView tvTest = findViewById(R.id.tvTest);
        tvTest.setText("welcome " + ParseUser.getCurrentUser().getUsername());
    }
}