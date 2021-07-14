package com.example.museum.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.museum.R;
import com.example.museum.activities.HomeActivity;
import com.example.museum.activities.LandingActivity;
import com.parse.ParseUser;

public class LoginFragment extends Fragment {

    private static final String TAG = "LoginFragment";

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LandingActivity landingActivity = (LandingActivity) getContext();
        TextView tvRegister = view.findViewById(R.id.tvRegister);
        Button btnLogin = view.findViewById(R.id.btnLogin);
        EditText etUsername = view.findViewById(R.id.etUsername);
        EditText etPassword = view.findViewById(R.id.etPassword);

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                landingActivity.fragmentManager.beginTransaction().replace(R.id.flContainer, landingActivity.register).commit();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                loginUser(username, password);
            }
        });
    }

    private void loginUser(String username, String password) {
        ParseUser.logInInBackground(username, password, (user, e) -> {
            if (e != null) {
                // TODO: error handling
                ParseUser.logOut();
                Log.e(TAG, "issue with login", e);
                return;
            }
            Intent i = new Intent(getContext(), HomeActivity.class);
            startActivity(i);
        });
    }
}