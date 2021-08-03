package com.example.museum.contracts;

import com.example.museum.base.MvpPresenter;
import com.example.museum.base.MvpView;

public interface RegisterContract {

    interface View extends MvpView<Presenter> {
        void success();
        void error();
    }

    interface Presenter extends MvpPresenter {
        void registerUser(String firstName, String username, String password);
    }

}