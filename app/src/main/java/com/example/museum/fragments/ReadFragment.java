package com.example.museum.fragments;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.museum.ParseApplication;
import com.example.museum.R;
import com.example.museum.activities.ReadActivity;
import com.example.museum.models.Journal;
import com.example.museum.models.Piece;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.parse.ParseException;
import com.parse.SaveCallback;

import java.util.Objects;

public class ReadFragment extends Fragment {

    private static final String TAG = "ReadFragment";
    private Journal journal;

    private ImageView ivCover;
    private EditText etTitle;
    private EditText etContent;
    private ReadActivity activity;
    private Toolbar toolbar;
    private CollapsingToolbarLayout cToolbar;

    public boolean editable;
    public boolean edited; // prevents needless home updates

    public ReadFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_read, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        activity = (ReadActivity) getActivity();
        journal = activity.journal;

        etTitle = view.findViewById(R.id.etTitle);
        etContent = view.findViewById(R.id.etContent);
        ivCover = view.findViewById(R.id.ivCover);
        cToolbar = view.findViewById(R.id.cToolbar);

        etTitle.setText(journal.getTitle());
        etContent.setText(journal.getContent());

        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("");
        activity.setSupportActionBar(toolbar);

        updateImage(journal.getPiece());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            activity.goHome(edited);
        } else if (id == R.id.icEdit) {
            toggleEdit(item);
        } else if (id == R.id.icSettings) {
            activity.fragmentManager.beginTransaction().replace(R.id.flContainer, activity.settings).commit();
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.journal_menu, menu);
        activity.getSupportActionBar().setHomeAsUpIndicator(activity.getDrawable(R.drawable.ic_arrow_back));
        activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        menu.findItem(R.id.icSettings).setIcon(activity.getDrawable(R.drawable.ic_baseline_settings_24));
        initializeEdit(menu.findItem(R.id.icEdit));
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void toggleEdit(MenuItem icEdit) {
        if (editable) { // confirm edit
            // hides home icon so activity doesn't get destroyed before update; prevents crash
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            editable = false;
            edited = true;
            String title = String.valueOf(etTitle.getText());
            String content = String.valueOf(etContent.getText());
            String[] words = content.split(" ");

            // error handling
            if (title.length() == 0) {
                Toast.makeText(activity, "Title cannot be empty", Toast.LENGTH_SHORT).show();
                activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                return;
            } else if (words.length < 6) {
                Toast.makeText(activity, "Content must have at least 6 words", Toast.LENGTH_SHORT).show();
                activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                return;
            }

            // if only title was changed
            if (titleChanged(title) && !contentChanged(content)) updateTitle(title);
            else if (contentChanged(content)) updateJournal(title, content);
            else activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        else {
            editable = true;
            Glide.with(activity).load(0).into(ivCover);
        }
        initializeEdit(icEdit);
    }

    private void updateTitle(String title) {
        ParseApplication.updateTitle(journal, title, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "error while updating journal" + e);
                    Toast.makeText(activity, "Error while updating", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "journal title updated successfully");
                Glide.with(activity).load(journal.getPiece().getImageUrl()).into(ivCover);
                activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        });
    }

    private void updateJournal(String title, String content) {
        ParseApplication.updateJournal(journal, title, content, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "error while updating journal" + e);
                    Toast.makeText(activity, "Error while updating", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "journal saved successfully");
                updateImage(journal.getPiece());
            }
        });
    }

    public void updateImage(Piece piece) {
        Glide.with(activity).load(piece.getImageUrl()).into(ivCover);

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
                activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        Drawable home = editable ? activity.getDrawable(R.drawable.ic_outline_close) : activity.getDrawable(R.drawable.ic_arrow_back);
        Drawable edit = editable ? activity.getDrawable(R.drawable.ic_baseline_check) : activity.getDrawable(R.drawable.ic_baseline_edit);
        icEdit.setIcon(edit);
        Objects.requireNonNull(activity.getSupportActionBar()).setHomeAsUpIndicator(home);
        etTitle.setEnabled(editable);
        etTitle.setTextIsSelectable(editable);
        etContent.setEnabled(editable);
        etContent.setTextIsSelectable(editable);
    }
}