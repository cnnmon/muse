package com.example.museum.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.museum.ParseApplication;
import com.example.museum.R;
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
    private Toolbar toolbar;

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

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.write_menu, menu);
        getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.drawable.ic_arrow_back));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        for(int i = 0; i < menu.size(); i++){
            Drawable drawable = menu.getItem(i).getIcon();
            if(drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.icSave) {
            createJournal();
        }
        return true;
    }

    private void createJournal() {
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
}