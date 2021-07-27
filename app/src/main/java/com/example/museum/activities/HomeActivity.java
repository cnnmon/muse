package com.example.museum.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.museum.ParseApplication;
import com.example.museum.R;
import com.example.museum.adapters.JournalAdapter;
import com.example.museum.models.Journal;
import com.example.museum.models.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    public static final String TAG = "HomeActivity";
    public static int REQUEST_CODE = 42;

    private JournalAdapter adapter;
    private Context context;

    public List<Journal> allJournals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TextView tvName = findViewById(R.id.tvName);
        User user = (User) ParseUser.getCurrentUser();
        tvName.setText("Hi " + user.getFirstName() + "! \uD83D\uDC4B");
        ImageView ivProfile = findViewById(R.id.ivProfile);

        context = this;
        allJournals = new ArrayList<>();
        adapter = new JournalAdapter(this, allJournals);

        RecyclerView rvJournals = findViewById(R.id.rvJournals);
        rvJournals.setAdapter(adapter);
        rvJournals.setLayoutManager(new GridLayoutManager(this, 2));

        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ProfileActivity.class);
                startActivity(i);
            }
        });

        queryJournals();
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
        if (requestCode == HomeActivity.REQUEST_CODE && resultCode == RESULT_OK) queryJournals();
        super.onActivityResult(requestCode, resultCode, data);
    }
}