package com.example.museum.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.museum.ParseApplication;
import com.example.museum.R;
import com.example.museum.adapters.JournalAdapter;
import com.example.museum.fragments.CalendarFragment;
import com.example.museum.fragments.HomeFragment;
import com.example.museum.models.Journal;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.FindCallback;
import com.parse.ParseException;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    public static final String TAG = "HomeActivity";
    public static int REQUEST_CODE = 42;

    private Context context;
    public JournalAdapter adapter;
    public List<Journal> allJournals;

    private FragmentManager fragmentManager;
    private HomeFragment home;
    private CalendarFragment calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        context = this;
        allJournals = new ArrayList<>();
        adapter = new JournalAdapter(this, allJournals);

        fragmentManager = getSupportFragmentManager();
        home = new HomeFragment();
        calendar = new CalendarFragment();

        queryJournals();

        BottomNavigationView bottomNavigationView = findViewById(R.id.btmNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                Fragment fragment = home;
                if (item.getItemId() == R.id.miCalendar) {
                    fragment = calendar;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
    }

    public void createJournal() {
        Intent i = new Intent(this, WriteActivity.class);
        startActivityForResult(i, REQUEST_CODE);
    }

    public void readJournal(Journal journal) {
        Intent i = new Intent(this, ReadActivity.class);
        i.putExtra(Journal.class.getSimpleName(), Parcels.wrap(journal));
        startActivityForResult(i, REQUEST_CODE);
    }

    public void queryJournals() {
        allJournals.clear();
        allJournals.add(new Journal()); // dummy object to fill the spot for a "new journal" button
        ParseApplication.queryJournals(new FindCallback<Journal>() {
            @Override
            public void done(List<Journal> journals, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "issue fetching journals");
                    return;
                }
                allJournals.addAll(journals);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.flContainer);
        fragment.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}