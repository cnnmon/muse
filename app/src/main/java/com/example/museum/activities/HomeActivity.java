package com.example.museum.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.museum.R;
import com.example.museum.TRApplication;
import com.example.museum.adapters.JournalAdapter;
import com.example.museum.models.Journal;
import com.example.museum.models.User;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    public static final String TAG = "HomeActivity";
    public static int REQUEST_CODE = 42;

    private Context context;
    private JournalAdapter adapter;
    private List<Journal> allJournals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TextView tvName = findViewById(R.id.tvName);
        User user = (User) ParseUser.getCurrentUser();
        tvName.setText(user.getFirstName());

        context = this;

        allJournals = new ArrayList<>();
        adapter = new JournalAdapter(this, allJournals);
        RecyclerView rvJournals = findViewById(R.id.rvJournals);
        rvJournals.setAdapter(adapter);
        rvJournals.setLayoutManager(new GridLayoutManager(this, 2));

        Button btnNew = findViewById(R.id.btnNew);
        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, JournalActivity.class);
                startActivityForResult(i, REQUEST_CODE);
            }
        });

        queryJournals();
        TRApplication.initialize(this);
    }

    private void queryJournals() {
        ParseQuery<Journal> query = ParseQuery.getQuery(Journal.class);
        query.include(Journal.KEY_AUTHOR);
        query.whereEqualTo(Journal.KEY_AUTHOR, ParseUser.getCurrentUser());
        query.setLimit(20);
        query.addDescendingOrder("createdAt");
        query.findInBackground((journals, e) -> {
            if (e != null) {
                Log.e(TAG, "issue fetching journals");
                return;
            }
            allJournals.addAll(journals);
            adapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            allJournals.clear();
            queryJournals();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}