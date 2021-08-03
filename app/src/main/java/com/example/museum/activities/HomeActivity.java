package com.example.museum.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.museum.R;
import com.example.museum.adapters.JournalAdapter;
import com.example.museum.contracts.HomeContract;
import com.example.museum.fragments.CalendarFragment;
import com.example.museum.fragments.HomeFragment;
import com.example.museum.models.Journal;
import com.example.museum.presenters.HomePresenter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity implements HomeContract.View {

    public static int REQUEST_CODE = 42;

    public JournalAdapter adapter;
    public Map<String, List<Journal>> datedJournals;

    private HomeContract.Presenter presenter;
    private List<Journal> allJournals;
    private View layout;
    private FragmentManager fragmentManager;
    private HomeFragment home;
    private CalendarFragment calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        allJournals = new ArrayList<>();
        datedJournals = new ArrayMap<>();
        adapter = new JournalAdapter(this, allJournals);

        new HomePresenter(this);
        queryJournals();

        layout = findViewById(R.id.relativeLayout);
        fragmentManager = getSupportFragmentManager();
        home = new HomeFragment();
        calendar = new CalendarFragment();

        BottomNavigationView bottomNavigationView = findViewById(R.id.btmNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                Fragment fragment = home;
                if (item.getItemId() == R.id.miCalendar) {
                    fragment = calendar;
                }
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out);
                ft.replace(R.id.flContainer, fragment).commit();
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
        datedJournals.clear();
        allJournals.add(new Journal()); // dummy object to fill the spot for a "new journal" button
        presenter.queryJournals(allJournals, datedJournals);
    }

    public void removeJournal(Journal journal) {
        presenter.removeJournal(journal);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.flContainer);
        fragment.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showProgress() {
        // TODO: add progress
    }

    @Override
    public void hideProgress() {
        // TODO: add progress
    }

    @Override
    public void updateJournals() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void refresh() {
        finish();
        startActivity(getIntent());
    }

    @Override
    public void error() {
        Snackbar snackbar = Snackbar
                .make(layout, getResources().getText(R.string.error_home), Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}