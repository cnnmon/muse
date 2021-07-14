package com.example.museum.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.museum.R;
import com.example.museum.fragments.LoginFragment;
import com.example.museum.fragments.RegisterFragment;

public class LandingActivity extends AppCompatActivity {

    public FragmentManager fragmentManager;
    public LoginFragment login;
    public RegisterFragment register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        fragmentManager = getSupportFragmentManager();
        login = new LoginFragment();
        register = new RegisterFragment();
    }
}