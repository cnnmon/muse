package com.example.museum.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.museum.R;
import com.example.museum.activities.HomeActivity;
import com.example.museum.activities.LoginActivity;
import com.example.museum.contracts.RegisterContract;
import com.example.museum.presenters.RegisterPresenter;
import com.google.android.material.snackbar.Snackbar;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

public class RegisterFragment extends Fragment implements RegisterContract.View {

    private RegisterContract.Presenter presenter;
    private Context context;
    private CircularProgressButton btnRegister;
    private View view;

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

        this.view = view;
        context = getContext();
        new RegisterPresenter(this, view, context);

        LoginActivity activity = (LoginActivity) getContext();
        Button btnLogin = view.findViewById(R.id.btnLogin);
        EditText etName = view.findViewById(R.id.etName);
        EditText etUsername = view.findViewById(R.id.etUsername);
        EditText etPassword = view.findViewById(R.id.etPassword);
        btnRegister = view.findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.fragmentManager.beginTransaction().replace(R.id.flContainer, activity.login).commit();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = etName.getText().toString();
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                presenter.registerUser(firstName, username, password);
            }
        });
    }

    @Override
    public void setPresenter(RegisterContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showProgress() {
        btnRegister.startAnimation();
    }

    @Override
    public void hideProgress() {
        btnRegister.revertAnimation();
    }

    @Override
    public void success() {
        Intent i = new Intent(context, HomeActivity.class);
        context.startActivity(i);
    }

    @Override
    public void error() {
        Snackbar snackbar = Snackbar
                .make(view, getResources().getString(R.string.register), Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}