package com.example.museum.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.museum.R;
import com.example.museum.contracts.WriteContract;
import com.example.museum.presenters.WritePresenter;
import com.google.android.material.snackbar.Snackbar;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WriteActivity extends AppCompatActivity implements WriteContract.View {

    private static final int MIN_CONTENT_WORDS = 6;

    private WriteContract.Presenter presenter;
    private EditText etTitle;
    private EditText etContent;
    private View layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        etTitle = findViewById(R.id.tvTitle);
        etContent = findViewById(R.id.tvContent);
        layout = findViewById(R.id.linearLayout);

        new WritePresenter(this);

        TextView tvDate = findViewById(R.id.tvDate);
        Format f = new SimpleDateFormat("MM/dd/yy");
        String date = f.format(new Date());
        tvDate.setText(date);

        Toolbar toolbar = findViewById(R.id.toolbar);
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

        if (errorCheck(layout, title, content)) {
            presenter.saveJournal(title, content);
        }
    }

    @Override
    public void setPresenter(WriteContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showProgress() { }

    @Override
    public void hideProgress() { }

    @Override
    public void error() {
        Snackbar snackbar = Snackbar
                .make(layout, getResources().getString(R.string.error_read), Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void success() {
        Intent i = new Intent();
        setResult(Activity.RESULT_OK, i);
        finish();
    }

    // error handling
    // returns true if it can proceed creating
    public static boolean errorCheck(View layout, String title, String content) {
        String[] words = content.split(" ");
        if (title.isEmpty()) {
            Snackbar snackbar = Snackbar
                    .make(layout, layout.getResources().getString(R.string.error_empty_title), Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;
        } else if (words.length < MIN_CONTENT_WORDS) {
            Snackbar snackbar = Snackbar
                    .make(layout, layout.getResources().getString(R.string.error_short_content), Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;
        }
        return true;
    }
}