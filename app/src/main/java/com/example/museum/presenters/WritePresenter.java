package com.example.museum.presenters;

import com.example.museum.ParseApplication;
import com.example.museum.contracts.WriteContract;
import com.parse.ParseException;
import com.parse.SaveCallback;

public class WritePresenter implements WriteContract.Presenter {

    private final WriteContract.View view;

    public WritePresenter(WriteContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void saveJournal(String title, String content) {
        view.showProgress();
        ParseApplication.get().saveJournal(title, content, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                view.hideProgress();
                if (e != null) {
                    view.error();
                    return;
                }
                view.success();
            }
        });
    }

}
