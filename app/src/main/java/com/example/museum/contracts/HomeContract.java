package com.example.museum.contracts;

import com.example.museum.base.MvpPresenter;
import com.example.museum.base.MvpView;
import com.example.museum.models.Journal;

import java.util.List;
import java.util.Map;

public interface HomeContract {

    interface View extends MvpView<Presenter> {
        void updateJournals();
        void refresh();
        void error();
    }

    interface Presenter extends MvpPresenter {
        void queryJournals(List<Journal> allJournals, Map<String, List<Journal>> datedJournals);
        void removeJournal(Journal journal);
    }

}