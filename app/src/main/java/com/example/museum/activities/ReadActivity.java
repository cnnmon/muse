package com.example.museum.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.museum.ParseApplication;
import com.example.museum.R;
import com.example.museum.models.Cover;
import com.example.museum.models.Journal;
import com.example.museum.models.Piece;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.parse.ParseException;
import com.parse.SaveCallback;
import com.r0adkll.slidr.Slidr;

import org.parceler.Parcels;

import java.util.Objects;

public class ReadActivity extends AppCompatActivity {

    public static final String TAG = "ReadActivity";
    public static int REQUEST_CODE = 22;

    private Journal journal;
    private Cover cover;
    private Context context;
    private ImageView ivCover;
    private EditText etTitle;
    private EditText etContent;
    private TextView tvDate;
    private Toolbar toolbar;
    private Menu menu;
    private CollapsingToolbarLayout cToolbar;

    private boolean editable;
    private boolean edited; // prevents needless home updates

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        // slide right to return to home
        Slidr.attach(this);

        context = this;

        // unwrap journal
        journal = Parcels.unwrap(getIntent().getParcelableExtra(Journal.class.getSimpleName()));
        cover = journal.getCover();

        etTitle = findViewById(R.id.etTitle);
        etContent = findViewById(R.id.etContent);
        tvDate = findViewById(R.id.tvDate);
        ivCover = findViewById(R.id.ivCover);
        cToolbar = findViewById(R.id.cToolbar);

        etTitle.setText(journal.getTitle());
        etContent.setText(journal.getContent());
        tvDate.setText(journal.getSimpleDate());

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        updateImage(cover.getPiece());
    }

    // returns to home screen & updates journal (if needed)
    public void goHome(boolean edited) {
        Intent i = new Intent();
        setResult(RESULT_OK, i);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            goHome(edited);
        } else if (id == R.id.icSave) {
            toggleEdit(item);
        } else if (id == R.id.icOptions) {
            Intent i = new Intent(this, OptionsActivity.class);
            i.putExtra(Journal.class.getSimpleName(), Parcels.wrap(journal));
            startActivityForResult(i, REQUEST_CODE);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.read_menu, menu);
        getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.drawable.ic_arrow_back));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        menu.findItem(R.id.icOptions).setIcon(getDrawable(R.drawable.ic_baseline_settings_24));
        initializeEdit(menu.findItem(R.id.icSave));
        this.menu = menu;

        // tint black
        for(int i = 0; i < menu.size(); i++){
            Drawable drawable = menu.getItem(i).getIcon();
            if(drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    private void toggleMenu(boolean isVisible) {
        Glide.with(this).load(cover.getPiece().getImageUrl()).into(ivCover);
        getSupportActionBar().setDisplayHomeAsUpEnabled(isVisible);
        if (menu != null) { // else crashes if clicked too fast
            menu.findItem(R.id.icOptions).setVisible(isVisible);
            menu.findItem(R.id.icSave).setVisible(isVisible);
        }
    }

    public void toggleEdit(MenuItem icEdit) {
        if (editable) { // confirm edit
            // hides home icon so activity doesn't get destroyed before update; prevents crash
            toggleMenu(false);
            editable = false;
            edited = true;
            String title = String.valueOf(etTitle.getText());
            String content = String.valueOf(etContent.getText());
            String[] words = content.split(" ");

            // error handling
            if (title.length() == 0) {
                Toast.makeText(this, "Title cannot be empty", Toast.LENGTH_SHORT).show();
                toggleMenu(true);
                return;
            } else if (words.length < 6) {
                Toast.makeText(this, "Content must have at least 6 words", Toast.LENGTH_SHORT).show();
                toggleMenu(true);
                return;
            }

            // if only title was changed
            if (titleChanged(title) && !contentChanged(content)) updateTitle(title);
            else if (contentChanged(content)) updateJournal(title, content);
            else toggleMenu(true);
        }
        else {
            editable = true;
            Glide.with(this).load(0).into(ivCover);
        }
        initializeEdit(icEdit);
    }

    private void updateTitle(String title) {
        ParseApplication.updateTitle(journal, title, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "error while updating journal" + e);
                    Toast.makeText(context, "Error while updating", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "journal title updated successfully");
                Glide.with(context).load(cover.getPiece().getImageUrl()).into(ivCover);
                toggleMenu(true);
            }
        });
    }

    private void updateJournal(String title, String content) {
        ParseApplication.updateJournal(journal, title, content, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "error while updating journal" + e);
                    Toast.makeText(context, "Error while updating", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "journal saved successfully");
                cover = journal.getCover();
                updateImage(cover.getPiece());
            }
        });
    }

    public void updateImage(Piece piece) {
        // set vibrant color to toolbar
        CustomTarget<Bitmap> target = new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @org.jetbrains.annotations.Nullable Transition<? super Bitmap> transition) {
                Palette palette = Palette.from(resource).maximumColorCount(10).generate();
                Palette.Swatch swatch = palette.getVibrantSwatch();
                if (swatch == null) swatch = palette.getLightMutedSwatch();
                if (swatch != null) {
                    cToolbar.setContentScrimColor(swatch.getRgb());
                    ivCover.setBackgroundColor(swatch.getRgb());
                }
                toggleMenu(true);
            }

            @Override
            public void onLoadCleared(@org.jetbrains.annotations.Nullable Drawable placeholder) { }
        };

        Glide.with(this).asBitmap().load(piece.getImageUrl()).centerCrop().into(target);
    }

    // used to prevent needless updates to database
    private boolean contentChanged(String content) {
        return !content.equals(journal.getContent());
    }

    private boolean titleChanged(String title) {
        return !title.equals(journal.getTitle());
    }

    // visual parts of edit
    @SuppressLint("UseCompatLoadingForDrawables")
    public void initializeEdit(MenuItem icEdit) {
        Drawable home = editable ? getDrawable(R.drawable.ic_outline_close) : getDrawable(R.drawable.ic_arrow_back);
        Drawable edit = editable ? getDrawable(R.drawable.ic_baseline_check) : getDrawable(R.drawable.ic_baseline_edit);
        icEdit.setIcon(edit);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(home);
        etTitle.setEnabled(editable);
        etTitle.setTextIsSelectable(editable);
        etContent.setEnabled(editable);
        etContent.setTextIsSelectable(editable);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            journal = Parcels.unwrap(data.getParcelableExtra(Journal.class.getSimpleName()));
            cover = journal.getCover();
            Piece piece = cover.getPiece();
            updateImage(piece);
        }
    }
}