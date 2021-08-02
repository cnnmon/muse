package com.example.museum.contracts;

import com.example.museum.ParseApplication;
import com.example.museum.base.MvpPresenter;
import com.example.museum.base.MvpView;

public interface LoginContract {

    interface View extends MvpView<Presenter> {
        void updateUser(ParseApplication parse);
    }

    interface Presenter extends MvpPresenter {

    }

}
