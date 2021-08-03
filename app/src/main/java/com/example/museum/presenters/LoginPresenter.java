package com.example.museum.presenters;

import com.example.museum.ParseApplication;
import com.example.museum.contracts.LoginContract;
import com.example.museum.models.User;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginPresenter implements LoginContract.Presenter {

    private final LoginContract.View view;

    public LoginPresenter(LoginContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public User getCurrentUser() {
        User currentUser = (User) ParseUser.getCurrentUser();
        return currentUser;
    }

    @Override
    public void loginUser(String username, String password) {
        view.showProgress();
        ParseApplication.get().loginUser(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    ParseUser.logOut();
                    view.error();
                    view.hideProgress();
                    return;
                }
                view.success();
            }
        });
    }

}
