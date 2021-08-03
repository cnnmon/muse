package com.example.museum.presenters;

import android.content.Context;
import android.view.View;

import com.example.museum.ParseApplication;
import com.example.museum.contracts.RegisterContract;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class RegisterPresenter implements RegisterContract.Presenter {

    private final RegisterContract.View view;
    private View layout;
    private Context context;

    public RegisterPresenter(RegisterContract.View view, View layout, Context context) {
        this.view = view;
        this.view.setPresenter(this);
        this.layout = layout;
        this.context = context;
    }

    @Override
    public void registerUser(String firstName, String username, String password) {
        view.showProgress();
        ParseApplication.get().registerUser(firstName, username, password, new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    ParseUser.logOut();
                    view.hideProgress();
                    return;
                }
                view.success();
            }
        });
    }

}
