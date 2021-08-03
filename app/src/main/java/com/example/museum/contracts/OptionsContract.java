package com.example.museum.contracts;

import com.example.museum.base.MvpPresenter;
import com.example.museum.base.MvpView;
import com.example.museum.models.Cover;
import com.example.museum.models.Journal;

public interface OptionsContract {

    interface View extends MvpView<OptionsContract.Presenter> {
        void error();
        void success(Cover cover);
    }

    interface Presenter extends MvpPresenter {
        void updateActiveCover(Journal journal, Cover cover);
    }

}
