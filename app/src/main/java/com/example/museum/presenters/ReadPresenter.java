package com.example.museum.presenters;

import android.app.Activity;
import android.view.View;

import com.example.museum.ParseApplication;
import com.example.museum.contracts.ReadContract;
import com.example.museum.models.Journal;
import com.parse.ParseException;
import com.parse.SaveCallback;

public class ReadPresenter implements ReadContract.Presenter {

    private final ReadContract.View view;
    private final View layout;
    private final Activity activity;

    public ReadPresenter(ReadContract.View view, View layout) {
        this.view = view;
        this.view.setPresenter(this);
        this.activity = (Activity) view;
        this.layout = layout;
    }

    @Override
    public void updateTitle(String title, Journal journal) {
        view.showProgress();
        ParseApplication.get().updateTitle(journal, title, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                view.hideProgress();
                if (e != null) {
                    view.error();
                    return;
                }
                view.successUpdateTitle();
            }
        });
    }

    @Override
    public void updateJournal(String title, String content, Journal journal) {
        view.showProgress();
        ParseApplication.get().updateJournal(journal, title, content, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                view.hideProgress();
                if (e != null) {
                    view.error();
                    return;
                }
                view.successUpdateJournal();
            }
        });
    }
}
