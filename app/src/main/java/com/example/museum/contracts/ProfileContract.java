package com.example.museum.contracts;

import com.example.museum.base.MvpPresenter;
import com.example.museum.base.MvpView;
import com.example.museum.models.User;

public interface ProfileContract {

    interface View extends MvpView<ProfileContract.Presenter> {
        void goHome();
    }

    interface Presenter extends MvpPresenter {
        User getCurrentUser();
        void logoutUser();
    }

}
