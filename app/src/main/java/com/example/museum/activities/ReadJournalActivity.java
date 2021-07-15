package com.example.museum.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.museum.R;
import com.example.museum.models.Journal;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.parceler.Parcels;

public class ReadJournalActivity extends AppCompatActivity {

    public static final String TAG = "ReadJournalActivity";
    private Journal journal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_journal);

        TextView tvTitle = findViewById(R.id.tvTitle);
        TextView tvDate = findViewById(R.id.tvDate);
        TextView tvContent = findViewById(R.id.tvContent);

        // unwrap journal
        journal = Parcels.unwrap(getIntent().getParcelableExtra(Journal.class.getSimpleName()));

        tvTitle.setText(journal.getTitle());
        tvDate.setText(journal.getSimpleDate());
        tvContent.setText(journal.getContent());

        FloatingActionButton fabExit = findViewById(R.id.fabExit);
        fabExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}