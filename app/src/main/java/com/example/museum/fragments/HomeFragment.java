package com.example.museum.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.museum.ParseApplication;
import com.example.museum.R;
import com.example.museum.activities.HomeActivity;
import com.example.museum.adapters.JournalAdapter;
import com.example.museum.models.Journal;
import com.example.museum.models.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private JournalAdapter adapter;
    private Context context;
    private List<Journal> allJournals;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        allJournals = new ArrayList<>();

        TextView tvName = view.findViewById(R.id.tvName);
        User user = (User) ParseUser.getCurrentUser();
        tvName.setText("Hi " + user.getFirstName() + "! \uD83D\uDC4B");
        ImageView ivProfile = view.findViewById(R.id.ivProfile);
        HomeActivity homeActivity = (HomeActivity) getActivity();

        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeActivity.goProfile();
            }
        });

        context = getContext();
        adapter = new JournalAdapter(context, allJournals);

        RecyclerView rvJournals = view.findViewById(R.id.rvJournals);
        rvJournals.setAdapter(adapter);
        rvJournals.setLayoutManager(new GridLayoutManager(context, 2));

        queryJournals();
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == HomeActivity.REQUEST_CODE && resultCode == getActivity().RESULT_OK) queryJournals();
        super.onActivityResult(requestCode, resultCode, data);
    }
}