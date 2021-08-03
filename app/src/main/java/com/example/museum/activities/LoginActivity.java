package com.example.museum.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.museum.ParseApplication;
import com.example.museum.R;
import com.example.museum.TRApplication;
import com.example.museum.fragments.LoginFragment;
import com.example.museum.fragments.RegisterFragment;

public class LoginActivity extends AppCompatActivity {

    public FragmentManager fragmentManager;
    public LoginFragment login;
    public RegisterFragment register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // must be initialized here
        // if using onCreate, they do not get initialized every time
        TRApplication.initialize(this);
        ParseApplication.initialize(this);

        fragmentManager = getSupportFragmentManager();
        login = new LoginFragment();
        register = new RegisterFragment();
    }

}