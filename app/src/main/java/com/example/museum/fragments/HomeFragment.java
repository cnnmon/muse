package com.example.museum.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import com.example.museum.R;
import com.example.museum.activities.HomeActivity;
import com.example.museum.activities.ProfileActivity;
import com.example.museum.models.User;
import com.parse.ParseUser;

public class HomeFragment extends Fragment {

    private HomeActivity activity;
    private Context context;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvName = view.findViewById(R.id.tvName);
        User user = (User) ParseUser.getCurrentUser();
        tvName.setText("Hi " + user.getFirstName() + "! \uD83D\uDC4B");
        ImageView ivProfile = view.findViewById(R.id.ivProfile);

        activity = (HomeActivity) getActivity();
        context = getContext();

        RecyclerView rvJournals = view.findViewById(R.id.rvJournals);
        rvJournals.setAdapter(activity.adapter);
        rvJournals.setLayoutManager(new GridLayoutManager(context, 2));

        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ProfileActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == activity.REQUEST_CODE && resultCode == activity.RESULT_OK) activity.queryJournals();
    }

}