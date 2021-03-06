package com.example.museum.contracts;

import com.example.museum.base.MvpPresenter;
import com.example.museum.base.MvpView;
import com.example.museum.models.User;

public interface LoginContract {

    interface View extends MvpView<Presenter> {
        void success();
        void error();
    }

    interface Presenter extends MvpPresenter {
        User getCurrentUser();
        void loginUser(String username, String password);
    }

}
