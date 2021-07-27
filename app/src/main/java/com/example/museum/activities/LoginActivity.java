package com.example.museum.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.museum.R;
import com.example.museum.TRApplication;
import com.example.museum.fragments.LoginFragment;
import com.example.museum.fragments.RegisterFragment;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LandingActivity";

    public FragmentManager fragmentManager;
    public LoginFragment login;
    public RegisterFragment register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TRApplication.initialize(this);

        fragmentManager = getSupportFragmentManager();
        login = new LoginFragment();
        register = new RegisterFragment();

        // remembers current user
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            Log.i(TAG, "resuming session as " + currentUser.getUsername());
            Intent i = new Intent(this, HomeActivity.class);
            startActivity(i);
        }
    }
}