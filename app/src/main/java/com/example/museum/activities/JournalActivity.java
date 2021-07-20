package com.example.museum.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.museum.Callback;
import com.example.museum.R;
import com.example.museum.TRApplication;
import com.example.museum.models.Cover;
import com.example.museum.models.Journal;
import com.example.museum.models.Piece;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.parceler.Parcels;

import java.util.List;
import java.util.Map;

public class JournalActivity extends AppCompatActivity {

    public static final String TAG = "JournalActivity";

    private Journal journal;
    private EditText etTitle;
    private EditText etContent;
    private ImageView ivCover;
    private Context context;
    private CollapsingToolbarLayout cToolbar;
    private boolean editable;
    private int textColor;
    private boolean edited; // prevents needless home updates

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);

        Toolbar toolbar = findViewById(R.id.toolbar);
        cToolbar = findViewById(R.id.cToolbar);

        etTitle = findViewById(R.id.etTitle);
        etContent = findViewById(R.id.etContent);
        ivCover = findViewById(R.id.ivCover);
        context = this;
        textColor = Color.BLACK;

        // unwrap journal
        journal = Parcels.unwrap(getIntent().getParcelableExtra(Journal.class.getSimpleName()));

        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        etTitle.setText(journal.getTitle());
        etContent.setText(journal.getContent());

        updateImage(journal.getPiece());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.journal_menu, menu);
        Drawable home = this.getDrawable(R.drawable.ic_arrow_back);
        home.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(home);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // initialize visuals & colors
        Drawable delete = this.getDrawable(R.drawable.ic_baseline_delete);
        menu.findItem(R.id.icDelete).setIcon(tint(delete));
        initializeEdit(menu.findItem(R.id.icEdit));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (edited) { // if any edits were made, notify home to update journals
                Intent i = new Intent();
                setResult(RESULT_OK, i);
            }
            finish();
        } else if (id == R.id.icEdit) {
            toggleEdit(item);
        } else if (id == R.id.icDelete) {
            deleteJournal();
        }
        return true;
    }

    private void deleteJournal() {
        journal.deleteInBackground(e -> {
            if (e != null) {
                Log.e(TAG, "error while deleting journal" + e);
                Toast.makeText(context, "Error while deleting", Toast.LENGTH_SHORT).show();
            }
            Log.i(TAG, "journal deleted successfully");
            Intent i = new Intent();
            setResult(RESULT_OK, i);
            finish();
        });
    }

    private void updateImage(Piece piece) {
        Glide.with(this).load(piece.getImageUrl()).into(ivCover);

        // set vibrant color to toolbar
        CustomTarget<Bitmap> target = new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                Palette palette = Palette.from(resource).maximumColorCount(10).generate();
                Palette.Swatch swatch = palette.getVibrantSwatch();
                if (swatch == null) swatch = palette.getLightMutedSwatch();
                if (swatch != null) {
                    cToolbar.setContentScrimColor(swatch.getRgb());
                    ivCover.setBackgroundColor(swatch.getRgb());
                }
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {
            }
        };

        Glide.with(this).asBitmap().load(piece.getImageUrl()).centerCrop().into(target);
    }

    private void toggleEdit(MenuItem icEdit) {
        if (editable) {
            editable = false;
            edited = true;
            String title = String.valueOf(etTitle.getText());
            String content = String.valueOf(etContent.getText());
            String[] words = content.split(" ");

            // error handling
            if (title.length() == 0) {
                Toast.makeText(this, "Title cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            } else if (words.length < 6) {
                Toast.makeText(this, "Content must have at least 6 words", Toast.LENGTH_SHORT).show();
                return;
            }

            // if only title was changed
            if (titleChanged(title) && !contentChanged(content)) updateTitle(title);
            else if (contentChanged(content)) updateJournal(title, content);
        }
        else editable = true;
        initializeEdit(icEdit);
    }

    // used to prevent needless updates to database
    private boolean contentChanged(String content) {
        return !content.equals(journal.getContent());
    }

    private boolean titleChanged(String title) {
        return !title.equals(journal.getTitle());
    }

    // visual parts of edit
    private void initializeEdit(MenuItem icEdit) {
        Drawable home = editable ? this.getDrawable(R.drawable.ic_outline_close) :
                this.getDrawable(R.drawable.ic_arrow_back);
        Drawable edit = editable ? this.getDrawable(R.drawable.ic_baseline_check) :
                this.getDrawable(R.drawable.ic_baseline_edit);
        icEdit.setIcon(tint(edit));
        getSupportActionBar().setHomeAsUpIndicator(tint(home));
        toggleEdit(etTitle, editable);
        toggleEdit(etContent, editable);
    }

    private Drawable tint(Drawable drawable) {
        drawable.setColorFilter(textColor, PorterDuff.Mode.SRC_ATOP);
        return drawable;
    }

    // prevent needless cover generation
    private void updateTitle(String title) {
        journal.setTitle(title);
        journal.saveInBackground(e -> {
            if (e != null) {
                Log.e(TAG, "error while updating journal" + e);
                Toast.makeText(context, "Error while updating", Toast.LENGTH_SHORT).show();
            }
            Log.i(TAG, "journal title updated successfully");
        });
    }

    private void updateJournal(String title, String content) {
        journal.setTitle(title);
        journal.setContent(content);
        // so activity doesn't get destroyed before update; causes app to crash
        getSupportActionBar().setHomeButtonEnabled(false);

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
                            Log.e(TAG, "error while updating journal" + e);
                            Toast.makeText(context, "Error while updating", Toast.LENGTH_SHORT).show();
                        }
                        Log.i(TAG, "journal saved successfully");
                        getSupportActionBar().setHomeButtonEnabled(true);
                        updateImage(cover.getActivePiece());
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void toggleEdit(EditText editText, boolean isEnabled) {
        editText.setEnabled(isEnabled);
        editText.setTextIsSelectable(isEnabled);
    }
}