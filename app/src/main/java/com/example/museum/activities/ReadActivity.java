package com.example.museum.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.museum.R;
import com.example.museum.contracts.ReadContract;
import com.example.museum.models.Cover;
import com.example.museum.models.Journal;
import com.example.museum.models.Piece;
import com.example.museum.presenters.ReadPresenter;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.Snackbar;

import org.parceler.Parcels;

import java.util.Objects;

public class ReadActivity extends AppCompatActivity implements ReadContract.View {

    public static int REQUEST_CODE = 22;

    private ReadContract.Presenter presenter;
    private RelativeLayout layout;
    private Journal journal;
    private Cover cover;
    private Context context;
    private ImageView ivCover;
    private EditText etTitle;
    private EditText etContent;
    private Menu menu;
    private CollapsingToolbarLayout cToolbar;
    private boolean editable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        // unwrap journal
        journal = Parcels.unwrap(getIntent().getParcelableExtra(Journal.class.getSimpleName()));
        cover = journal.getCover();
        context = this;

        etTitle = findViewById(R.id.etTitle);
        etContent = findViewById(R.id.etContent);
        TextView tvDate = findViewById(R.id.tvDate);
        ivCover = findViewById(R.id.ivCover);
        cToolbar = findViewById(R.id.cToolbar);
        layout = findViewById(R.id.relativeLayout);

        new ReadPresenter(this);

        etTitle.setText(journal.getTitle());
        etContent.setText(journal.getContent());
        tvDate.setText(journal.getSimpleDate());

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        updateImage(cover.getPiece());
    }

    // returns to home screen & updates journal
    public void goHome() {
        Intent i = new Intent();
        setResult(RESULT_OK, i);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            goHome();
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
        menu.findItem(R.id.icOptions).setIcon(getDrawable(R.drawable.ic_baseline_settings));
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
            String title = String.valueOf(etTitle.getText());
            String content = String.valueOf(etContent.getText());

            if (WriteActivity.errorCheck(layout, title, content)) {
                if (titleChanged(title)) {
                    presenter.updateTitle(title, journal);
                } else if (contentChanged(content)) {
                    presenter.updateJournal(title, content, journal);
                }
            } else {
                editable = true;
            }
        }
        else {
            editable = true;
            Glide.with(this).load(0).into(ivCover);
        }
        initializeEdit(icEdit);
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

    @Override
    public void setPresenter(ReadContract.Presenter presenter) { this.presenter = presenter; }

    @Override
    public void showProgress() { }

    @Override
    public void hideProgress() { }

    @Override
    public void error() {
        Snackbar snackbar = Snackbar
                .make(layout, getResources().getText(R.string.error_read), Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void successUpdateTitle() {
        // returns same image to header
        Glide.with(context).load(cover.getPiece().getImageUrl()).into(ivCover);
        toggleMenu(true);
    }

    @Override
    public void successUpdateJournal() {
        // updates image in header
        cover = journal.getCover();
        updateImage(cover.getPiece());
    }
}