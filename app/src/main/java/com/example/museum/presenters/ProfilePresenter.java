package com.example.museum.presenters;

import com.example.museum.contracts.ProfileContract;
import com.example.museum.models.User;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class ProfilePresenter implements ProfileContract.Presenter {

    private final ProfileContract.View view;

    public ProfilePresenter(ProfileContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public User getCurrentUser() {
        User currentUser = (User) ParseUser.getCurrentUser();
        return currentUser;
    }

    @Override
    public void logoutUser() {
        ParseUser.logOutInBackground(new LogOutCallback() {
            @Override
            public void done(ParseException e) {
                view.goHome();
            }
        });
    }
}
