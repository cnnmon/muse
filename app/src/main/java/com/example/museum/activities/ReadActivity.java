package com.example.museum.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.museum.R;
import com.example.museum.fragments.ReadFragment;
import com.example.museum.fragments.SettingsFragment;
import com.example.museum.models.Journal;

import org.parceler.Parcels;

public class ReadActivity extends AppCompatActivity {

    public static final String TAG = "JournalActivity";

    public Journal journal;
    public FragmentManager fragmentManager;
    public ReadFragment read;
    public SettingsFragment settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        // unwrap journal
        journal = Parcels.unwrap(getIntent().getParcelableExtra(Journal.class.getSimpleName()));
        fragmentManager = getSupportFragmentManager();
        read = new ReadFragment();
        settings = new SettingsFragment();
    }

    // returns to home screen & updates journals if edited
    public void goHome(boolean edited) {
        if (edited) {
            Intent i = new Intent();
            setResult(RESULT_OK, i);
        }
        finish();
    }
}