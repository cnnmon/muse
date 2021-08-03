package com.example.museum.presenters;

import com.example.museum.ParseApplication;
import com.example.museum.contracts.OptionsContract;
import com.example.museum.models.Cover;
import com.example.museum.models.Journal;
import com.parse.ParseException;
import com.parse.SaveCallback;

public class OptionsPresenter implements OptionsContract.Presenter {

    private final OptionsContract.View view;

    public OptionsPresenter(OptionsContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void updateActiveCover(Journal journal, Cover cover) {
        view.showProgress();
        ParseApplication.get().updateActiveCover(journal, cover, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                view.hideProgress();
                if (e != null) {
                    view.error();
                    return;
                }
                view.success(cover);
            }
        });
    }
}
