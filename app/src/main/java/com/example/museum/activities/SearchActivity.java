package com.example.museum.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.museum.R;
import com.example.museum.TRApplication;
import com.example.museum.models.Piece;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    public static final String TAG = "SearchActivity";

    List<Piece> pieces;
    TextView etText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        pieces = new ArrayList<>();
        etText = findViewById(R.id.etText);
        TRApplication.initialize(this);
    }

}