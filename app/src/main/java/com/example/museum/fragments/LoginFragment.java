package com.example.museum.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.museum.contracts.LoginContract;
import com.example.museum.models.User;
import com.example.museum.presenters.LoginPresenter;
import com.google.android.material.snackbar.Snackbar;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

public class LoginFragment extends Fragment implements LoginContract.View {

    private static final String TAG = "LoginFragment";

    private LoginContract.Presenter presenter;
    private Context context;
    private CircularProgressButton btnLogin;
    private View view;

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

        this.view = view;
        context = getContext();
        new LoginPresenter(this);

        LoginActivity activity = (LoginActivity) getContext();
        Button btnRegister = view.findViewById(R.id.btnRegister);
        EditText etUsername = view.findViewById(R.id.etUsername);
        EditText etPassword = view.findViewById(R.id.etPassword);
        btnLogin = view.findViewById(R.id.btnLogin);

        // remembers current user
        User currentUser = presenter.getCurrentUser();
        if (currentUser != null) {
            Log.i(TAG, "resuming session as " + currentUser.getUsername());
            Intent i = new Intent(context, HomeActivity.class);
            startActivity(i);
        }

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.fragmentManager.beginTransaction().replace(R.id.flContainer, activity.register).commit();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnLogin.startAnimation();
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                presenter.loginUser(username, password);
            }
        });
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showProgress() {
        btnLogin.startAnimation();
    }

    @Override
    public void hideProgress() {
        btnLogin.revertAnimation();
    }

    @Override
    public void success() {
        Intent i = new Intent(context, HomeActivity.class);
        context.startActivity(i);
    }

    @Override
    public void error() {
        Snackbar snackbar = Snackbar
                .make(view, "Error logging in. Want to sign up?", Snackbar.LENGTH_LONG);
        snackbar.show();
    }

}