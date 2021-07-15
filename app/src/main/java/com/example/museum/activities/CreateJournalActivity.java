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

import com.example.museum.Callback;
import com.example.museum.R;
import com.example.museum.TRApplication;
import com.example.museum.models.Cover;
import com.example.museum.models.Journal;
import com.example.museum.models.Piece;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.ParseUser;

import org.json.JSONException;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class CreateJournalActivity extends AppCompatActivity {

    public static final String TAG = "CreateJournalActivity";

    private Context context;
    private EditText etTitle;
    private EditText etContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_journal);

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

                saveJournal(ParseUser.getCurrentUser(), title, content);
            }
        });
    }

    private void saveJournal(ParseUser currentUser, String title, String content) {
        Journal journal = new Journal();
        journal.setTitle(title);
        journal.setContent(content);
        journal.setUser(currentUser);

        TRApplication.onAnalysis(content, new Callback() {
            @Override
            public void run() {
                // Needed to have for callback; isn't called
            }

            @Override
            public void run(Map<String, List<Piece>> options) {
                try {
                    Cover cover = new Cover(options);
                    journal.setCover(cover.getJson());
                    journal.saveInBackground(e -> {
                        if (e != null) {
                            Log.e(TAG, "error while saving post" + e);
                            Toast.makeText(context, "Error while saving", Toast.LENGTH_SHORT).show();
                        }
                        Log.i(TAG, "post saved successfully");
                        Intent i = new Intent();
                        setResult(RESULT_OK, i);
                        finish();
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}