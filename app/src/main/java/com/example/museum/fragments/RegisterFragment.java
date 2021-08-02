package com.example.museum.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.museum.ParseApplication;
import com.example.museum.R;
import com.example.museum.activities.HomeActivity;
import com.example.museum.activities.LoginActivity;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

public class RegisterFragment extends Fragment {

    private static final String TAG = "RegisterFragment";
    private Context context;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LoginActivity landingActivity = (LoginActivity) getContext();
        Button btnLogin = view.findViewById(R.id.btnLogin);
        CircularProgressButton btnRegister = view.findViewById(R.id.btnRegister);
        EditText etName = view.findViewById(R.id.etName);
        EditText etUsername = view.findViewById(R.id.etUsername);
        EditText etPassword = view.findViewById(R.id.etPassword);
        context = getContext();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                landingActivity.fragmentManager.beginTransaction().replace(R.id.flContainer, landingActivity.login).commit();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRegister.startAnimation();
                String firstName = etName.getText().toString();
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                ParseApplication.get().registerUser(firstName, username, password, new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            btnRegister.revertAnimation();
                            ParseUser.logOut();
                            Toast.makeText(context, "Error signing up", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Intent i = new Intent(context, HomeActivity.class);
                        startActivity(i);
                        btnRegister.revertAnimation();
                    }
                });
            }
        });
    }
}