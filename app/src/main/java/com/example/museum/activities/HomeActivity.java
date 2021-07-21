package com.example.museum.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.museum.R;
import com.example.museum.TRApplication;
import com.example.museum.fragments.HomeFragment;
import com.example.museum.fragments.ProfileFragment;
import com.example.museum.models.Journal;

import org.parceler.Parcels;

public class HomeActivity extends AppCompatActivity {

    public static final String TAG = "HomeActivity";
    public static int REQUEST_CODE = 42;
    private FragmentManager fragmentManager;
    private HomeFragment home;
    private ProfileFragment profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        TRApplication.initialize(this);

        fragmentManager = getSupportFragmentManager();
        home = new HomeFragment();
        profile = new ProfileFragment();
    }

    public void goHome() {
        fragmentManager.beginTransaction().replace(R.id.flContainer, home).commit();
    }

    public void goProfile() {
        fragmentManager.beginTransaction().replace(R.id.flContainer, profile).commit();
    }

    public void createJournal() {
        Intent i = new Intent(this, WriteActivity.class);
        startActivityForResult(i, REQUEST_CODE);
    }

    public void readJournal(Journal journal) {
        Intent i = new Intent(this, JournalActivity.class);
        i.putExtra(Journal.class.getSimpleName(), Parcels.wrap(journal));
        startActivityForResult(i, REQUEST_CODE);
    }
}