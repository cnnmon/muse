package com.example.museum.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.museum.ParseApplication;
import com.example.museum.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.ParseException;
import com.parse.SaveCallback;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WriteActivity extends AppCompatActivity {

    public static final String TAG = "CreateJournalActivity";

    private Context context;
    private EditText etTitle;
    private EditText etContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        context = this;
        etTitle = findViewById(R.id.tvTitle);
        etContent = findViewById(R.id.tvContent);

        TextView tvDate = findViewById(R.id.tvDate);
        Format f = new SimpleDateFormat("MM/dd/yy");
        String date = f.format(new Date());
        tvDate.setText(date);

        FloatingActionButton fabExit = findViewById(R.id.fabExit);
        fabExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        FloatingActionButton fabSave = findViewById(R.id.fabSave);
        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = String.valueOf(etTitle.getText());
                String content = String.valueOf(etContent.getText());
                String[] words = content.split(" ");

                if (title.isEmpty()) {
                    Toast.makeText(context, "Title cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                } else if (words.length < 6) {
                    Toast.makeText(context, "Content must have at least 6 words", Toast.LENGTH_SHORT).show();
                    return;
                }

                ParseApplication.saveJournal(title, content, new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Log.e(TAG, "error while creating journal" + e);
                            Toast.makeText(context, "Error while creating", Toast.LENGTH_SHORT).show();
                        }
                        Log.i(TAG, "journal created successfully");
                        Intent i = new Intent();
                        setResult(RESULT_OK, i);
                        finish();
                    }
                });
            }
        });
    }
}