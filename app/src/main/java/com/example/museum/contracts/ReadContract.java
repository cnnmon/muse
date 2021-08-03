package com.example.museum.contracts;

import com.example.museum.base.MvpPresenter;
import com.example.museum.base.MvpView;
import com.example.museum.models.Journal;

public interface ReadContract {

    interface View extends MvpView<Presenter> {
        void error();
        void successUpdateTitle();
        void successUpdateJournal();
    }

    interface Presenter extends MvpPresenter {
        void updateTitle(String title, Journal journal);
        void updateJournal(String title, String content, Journal journal);
    }

}