package com.example.museum.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.museum.ParseApplication;
import com.example.museum.R;
import com.example.museum.activities.HomeActivity;
import com.example.museum.activities.LoginActivity;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

public class LoginFragment extends Fragment {

    private static final String TAG = "LoginFragment";
    private Context context;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LoginActivity landingActivity = (LoginActivity) getContext();
        TextView tvRegister = view.findViewById(R.id.tvRegister);
        EditText etUsername = view.findViewById(R.id.etUsername);
        EditText etPassword = view.findViewById(R.id.etPassword);

        context = getContext();
        CircularProgressButton btnLogin = view.findViewById(R.id.btnLogin);

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                landingActivity.fragmentManager.beginTransaction().replace(R.id.flContainer, landingActivity.register).commit();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnLogin.startAnimation();
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                ParseApplication.loginUser(username, password, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (e != null) {
                            btnLogin.revertAnimation();
                            ParseUser.logOut();
                            Toast.makeText(context, "Wrong username or password", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Intent i = new Intent(context, HomeActivity.class);
                        startActivity(i);
                        btnLogin.revertAnimation();
                    }
                });
            }
        });
    }
}