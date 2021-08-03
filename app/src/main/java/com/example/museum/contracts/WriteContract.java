package com.example.museum.contracts;

import com.example.museum.base.MvpPresenter;
import com.example.museum.base.MvpView;

public interface WriteContract {

    interface View extends MvpView<WriteContract.Presenter> {
        void error();
        void success();
    }

    interface Presenter extends MvpPresenter {
        void saveJournal(String title, String contents);
    }

}
